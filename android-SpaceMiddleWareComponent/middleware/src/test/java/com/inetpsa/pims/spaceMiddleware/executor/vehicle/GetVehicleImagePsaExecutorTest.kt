package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleDetailsPsa
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetVehicleImagePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVehicleImagePsaExecutor

    private val vehicle = VehicleDetailsPsa(
        vin = "testVin",
        lcdv = "testLCDV",
        shortLabel = "testShortLabel",
        warrantyStartDate = null,
        typeVehicle = null,
        visual = "https://image.test.com",
        reviewMaxDate = null
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetVehicleImagePsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val vin = "testID"
        every { executor.params(any()) } returns vin
        coEvery { communicationManager.get<VehicleDetailsPsa>(any(), any()) } returns NetworkResponse.Success(vehicle)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(VehicleDetailsPsa::class.java),
                    urls = eq(arrayOf("/car/v1/vehicle/", vin)),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<VehicleDetailsPsa>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = (response as Success).response
            Assert.assertEquals(vehicle.vin, success.vin)
            Assert.assertEquals(vehicle.visual, success.imageUrl)
            Assert.assertEquals(vehicle.shortLabel, success.model)
        }
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
}
