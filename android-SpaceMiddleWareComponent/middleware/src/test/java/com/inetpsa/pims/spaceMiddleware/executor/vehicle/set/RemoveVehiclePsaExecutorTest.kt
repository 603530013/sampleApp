package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleListResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class RemoveVehiclePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: RemoveVehiclePsaExecutor
    private val vehicleListResponse = listOf(
        VehicleListResponse(
            vin = "testVin",
            lcdv = "testLCDV",
            shortLabel = "testShortLabel",
            visual = "testVisual",
            command = "testCommand"
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.DataManagerExtensionsKt")
        executor = spyk(RemoveVehiclePsaExecutor(baseCommand))
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        userSessionManager.getUserSession()?.customerId
        every { middlewareComponent.dataManager } returns dataManager
    }

    @Test
    fun `test removeFromVehiclesCache removes the given vin response and saves`() {
        val v1 = VehicleListResponse(
            vin = "testVin",
            lcdv = "testLCDV",
            shortLabel = "testShortLabel",
            visual = "testVisual",
            command = "testCommand"
        )
        val v2 = VehicleListResponse(
            vin = "VIN123",
            lcdv = "testLCDV",
            shortLabel = "testShortLabel",
            visual = "testVisual",
            command = "testCommand"
        )
        val vehicleListResponse = listOf(v1, v2)
        coEvery { executor.saveVehicles(any()) } returns true
        coEvery { executor.readVehiclesFromCache() } returns vehicleListResponse.toJson()
        runBlocking {
            val result = executor.removeFromVehiclesCache("testVin")
            assertTrue(result)
            coVerify { executor.readVehiclesFromJson(vehicleListResponse.toJson()) }
            coVerify { executor.saveVehicles(listOf(v2)) }
        }
    }

    @Test
    fun `test removeFromVehiclesCache doesn't remove the response if VIN not matches`() {
        val v1 = VehicleListResponse(
            vin = "testVin",
            lcdv = "testLCDV",
            shortLabel = "testShortLabel",
            visual = "testVisual",
            command = "testCommand"
        )
        val v2 = VehicleListResponse(
            vin = "VIN123",
            lcdv = "testLCDV",
            shortLabel = "testShortLabel",
            visual = "testVisual",
            command = "testCommand"
        )
        val vehicleListResponse = listOf(v1, v2)
        coEvery { executor.saveVehicles(any()) } returns true
        coEvery { executor.readVehiclesFromCache() } returns vehicleListResponse.toJson()
        runBlocking {
            val result = executor.removeFromVehiclesCache("")
            assertTrue(result)
            coVerify { executor.readVehiclesFromJson(vehicleListResponse.toJson()) }
            coVerify { executor.saveVehicles(vehicleListResponse) }
        }
    }

    @Test
    fun `test readVehiclesFromJson returns null when there is JsonParseException`() {
        val sampleJsonString = "testJson"
        val response = executor.readVehiclesFromJson(sampleJsonString)
        assertNull(response)
    }

    @Test
    fun `when execute params with the right vin then return vin`() {
        val vin = "testVin"
        val input = mapOf(Constants.PARAM_VIN to vin)
        val param = executor.params(input)

        Assert.assertEquals(vin, param)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.PARAM_VIN to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute then make a delete API call with the right response`() {
        val vin = "testVin"
        coEvery { communicationManager.delete<Unit>(any(), any()) } returns NetworkResponse.Success(Unit)
        coEvery { executor.removeFromVehicleCache(eq(vin)) } returns true
        coEvery { executor.removeFromVehiclesCache(eq(vin)) } returns true

        runTest {
            val response = executor.execute(vin)

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(arrayOf("/me/v1/vehicle/", vin))
                )
            }

            coVerify {
                communicationManager.delete<Unit>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            coVerify(exactly = 1) { executor.removeFromVehicleCache(eq(vin)) }
            coVerify(exactly = 1) { executor.removeFromVehiclesCache(eq(vin)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute readVehiclesFromCache in available case then return cache`() {
        val json = "{}"
        every { dataManager.read(any(), any()) } returns json
        val cache = executor.readVehiclesFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_vehicles"), eq(StoreMode.APPLICATION)) }
        Assert.assertEquals(json, cache)
    }

    @Test
    fun `when execute readVehiclesFromCache in empty case then return null`() {
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readVehiclesFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_vehicles"), eq(StoreMode.APPLICATION)) }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute readVehiclesFromJson with valid json then return items`() {
        val json = vehicleListResponse.toJson()
        val cache = executor.readVehiclesFromJson(json)
        Assert.assertEquals(vehicleListResponse.size, cache?.size)
        Assert.assertEquals(vehicleListResponse, cache)
    }

    @Test
    fun `when execute readVehiclesFromJson with invalid json then return null`() {
        val cache = executor.readVehiclesFromJson(null)
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when removeFromVehicleCache then we remove from dataManager`() {
        runTest {
            val vin = "testVin"
            coEvery { middlewareComponent.deleteSync(any(), any()) } returns true
            executor.removeFromVehicleCache(vin)
            coVerify {
                middlewareComponent.deleteSync(
                    eq("vehicle_$vin"),
                    eq(APPLICATION)
                )
            }
        }
    }

    @Test
    fun `when execute saveVehicles then save them in cache`() {
        every {
            dataManager.create(
                key = any(),
                data = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }

        runTest {
            executor.saveVehicles(vehicleListResponse)
        }

        verify {
            dataManager.create(
                key = eq("PEUGEOT_PREPROD_testCustomerId_vehicles"),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }
}
