package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Deprecated("try to switch to use this class SetRemoveVehiclePsaExecutorTest")
internal class RemoveVehiclePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: RemoveVehiclePsaExecutor

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(RemoveVehiclePsaExecutor(baseCommand))
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
        coEvery { communicationManager.delete<Unit>(any(), any()) } returns NetworkResponse.Success(Unit)
        val vin = "testVin"

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

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }
}
