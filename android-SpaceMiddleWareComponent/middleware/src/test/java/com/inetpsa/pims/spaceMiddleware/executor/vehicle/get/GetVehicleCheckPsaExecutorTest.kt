package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.CheckVehicleItemResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetVehicleCheckPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVehicleCheckPsaExecutor

    private val response = listOf(
        CheckVehicleItemResponse(vin = "testVin1"),
        CheckVehicleItemResponse(vin = "testVin2")
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetVehicleCheckPsaExecutor(baseCommand))
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
    fun `when execute with no existing vin then return success response`() {
        val inputVin = "vin"
        every { executor.params(any()) } returns inputVin

        coEvery {
            communicationManager.get<List<CheckVehicleItemResponse>>(any(), any())
        } returns Success(response)

        runTest {
            val response = executor.execute()
            val type = TypeToken.getParameterized(List::class.java, CheckVehicleItemResponse::class.java).type

            verify {
                executor.request(
                    type = eq(type),
                    urls = eq(arrayOf("/me/v1/vehicles"))
                )
            }

            coVerify {
                communicationManager.get<List<CheckVehicleItemResponse>>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute with existing vin then return failure response`() {
        val inputVin = "testVin2"
        every { executor.params(any()) } returns inputVin

        coEvery {
            communicationManager.get<List<CheckVehicleItemResponse>>(any(), any())
        } returns Success(response)

        runTest {
            val response = executor.execute()
            val type = TypeToken.getParameterized(List::class.java, CheckVehicleItemResponse::class.java).type

            verify {
                executor.request(
                    type = eq(type),
                    urls = eq(arrayOf("/me/v1/vehicles"))
                )
            }

            coVerify {
                communicationManager.get<List<CheckVehicleItemResponse>>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val error = PimsErrors.alreadyExist(Constants.PARAM_VIN)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }
}
