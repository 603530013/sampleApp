package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.user.GetUserPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleListResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehiclesOutput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetVehiclesPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVehiclesPsaExecutor
    private val vehiclesResponse = listOf(
        VehicleListResponse(
            vin = "testVin1",
            lcdv = "testLCDV1",
            shortLabel = "testShortLabel1",
            visual = "testLinkVisual1",
            // warrantyStartDate = today,
            command = "testCommand1"
        ),
        VehicleListResponse(
            vin = "testVin2",
            lcdv = "testLCDV2",
            shortLabel = "testShortLabel2",
            visual = null,
            // warrantyStartDate = null,
            command = null
        )
    )
    private val vehiclesOutput = VehiclesOutput(
        listOf(
            VehiclesOutput.Vehicle(vin = "testVin1", shortLabel = "testShortLabel1", picture = "testLinkVisual1"),
            VehiclesOutput.Vehicle(vin = "testVin2", shortLabel = "testShortLabel2", picture = null)
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetUserPsaExecutor::class)
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) }
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager

        executor = spyk(GetVehiclesPsaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with missing action then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid action then throw invalid parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.Input.ACTION to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then continue execution`() {
        val input = mapOf(Constants.Input.ACTION to Constants.Input.Action.GET)
        val output = executor.params(input)
        Assert.assertEquals(Action.Get, output.action)
        Assert.assertEquals(null, output.vin)
    }

    @Test
    fun `when execute params with invalid vin then throw invalid parameter`() {
        val vin = 123
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute transformToVehicleOutput then return VehiclesOutput`() {
        val result = executor.transformToVehicleOutput(vehiclesResponse)
        Assert.assertEquals(2, result.vehicles.size)
        val firstElement = result.vehicles.firstOrNull()
        Assert.assertEquals("testVin1", firstElement?.vin)
        Assert.assertEquals("testShortLabel1", firstElement?.shortLabel)
        Assert.assertEquals("testLinkVisual1", firstElement?.picture)
        val secondElement = result.vehicles.getOrNull(1)
        Assert.assertEquals("testVin2", secondElement?.vin)
        Assert.assertEquals("testShortLabel2", secondElement?.shortLabel)
        Assert.assertEquals(null, secondElement?.picture)
    }

    @Test
    fun `when execute readFromCache in available case then return cache`() {
        val json = vehiclesResponse.toJson()
        every { dataManager.read(any(), any()) } returns json
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_vehicles"), eq(StoreMode.APPLICATION)) }
        Assert.assertEquals(json, cache)
    }

    @Test
    fun `when execute readFromCache in empty case then return null`() {
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_vehicles"), eq(StoreMode.APPLICATION)) }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute readFromJson with valid json then return items`() {
        val json = vehiclesResponse.toJson()
        val cache = executor.readFromJson(json)
        Assert.assertEquals(vehiclesResponse.size, cache?.size)
        Assert.assertEquals(vehiclesResponse, cache)
    }

    @Test
    fun `when execute readFromJson with invalid json then return null`() {
        var model = executor.readFromJson(null)
        Assert.assertEquals(null, model)

        model = executor.readFromJson("{ test vehicles }")
        Assert.assertEquals(null, model)
    }

    @Test
    fun `when execute with action get and there is not a valid cache then make a network call`() {
        every { executor.readFromCache() } returns null
        every { executor.readFromJson(any()) } returns vehiclesResponse
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            executor.execute(UserInput(Action.Get, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 2) { executor.readFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }
        }
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        val cache = "{testCache}"
        every { executor.readFromCache() } returns cache
        every { executor.readFromJson(any()) } returns vehiclesResponse

        runTest {
            val response = executor.execute(UserInput(Action.Get, null))
            verify(exactly = 1) { executor.readFromCache() }
            verify(exactly = 1) { executor.readFromJson(any()) }
            coVerify(exactly = 0) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehiclesOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response`() {
        every { executor.readFromCache() } returns "{ test vehicles from cache }"
        every { executor.readFromJson(any()) } returns vehiclesResponse
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            val response = executor.execute(UserInput(Action.Refresh, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readFromCache() }
            verify(exactly = 1) { executor.readFromJson(any()) }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehiclesOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with failure response`() {
        every { executor.readFromCache() } returns "{ test vehicles from cache }"
        every { executor.readFromJson(any()) } returns vehiclesResponse
        val error = PimsErrors.serverError(null, "test-errors")
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute(UserInput(Action.Refresh, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readFromCache() }
            verify(exactly = 0) { executor.readFromJson(any()) }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response but empty cache`() {
        every { executor.readFromCache() } returns "{ test vehicles from cache }"
        every { executor.readFromJson(any()) } returns null
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            val response = executor.execute(UserInput(Action.Refresh, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readFromCache() }
            verify(exactly = 1) { executor.readFromJson(any()) }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            val error = PIMSFoundationError.invalidReturnParam(Constants.Storage.VEHICLES)
            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }
}
