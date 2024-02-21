package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import android.net.Uri
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class AddVehicleNoConnectedFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: AddVehicleNoConnectedFcaExecutor

    @Before
    override fun setup() {
        super.setup()
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        every { baseCommand.parameters } returns
            mapOf(Constants.PARAM_VIN to "testVin")

        executor = spyk(AddVehicleNoConnectedFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute then make a set API call`() {
        val vin = "testVin"
        every { executor.params(any()) } returns vin

        coEvery { communicationManager.post<Unit>(any(), any()) } returns NetworkResponse.Success(Unit)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            uid,
                            "/vehicles/",
                            vin,
                            "/associate"
                        )
                    )
                )
            }

            coVerify {
                communicationManager.post<Unit>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(Unit, success.response)
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
