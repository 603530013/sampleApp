package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import android.net.Uri
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
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

internal class EnrollNonConnectedVehicleFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: EnrollNonConnectedVehicleFcaExecutor

    @Before
    override fun setup() {
        super.setup()
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        every { baseCommand.parameters } returns
            mapOf(Constants.PARAM_VIN to "testVin")

        executor = spyk(EnrollNonConnectedVehicleFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute then make a set API call`() {
        val vin = "testVin"
        every { executor.params(any()) } returns vin

        coEvery { communicationManager.post<Unit>(any(), any()) } returns
            Success(Unit)

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
                            "/associate/"
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

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute params with right input then return AddNonConnectedVehicleFcaParams`() {
        val params = "testVin"
        val input = mapOf(
            Constants.BODY_PARAM_VIN to params
        )
        val output = executor.params(input)

        Assert.assertEquals(params, output)
    }
}
