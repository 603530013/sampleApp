package com.inetpsa.pims.spaceMiddleware.executor.user

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.ErrorCode
import com.inetpsa.mmx.foundation.monitoring.ErrorMessage
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.SubError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.util.getToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class DeleteAccountFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: DeleteAccountFcaExecutor

    @Before
    override fun setup() {
        super.setup()
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.SessionManagerExtensionKt")
        executor = spyk(DeleteAccountFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with right input then return a Unit`() {
        val output = executor.params(emptyMap())
        Assert.assertEquals(Unit, output)
    }

    @Test
    fun `when execute then make a delete API call`() {
        val pin = "testPin"

        coEvery { communicationManager.delete<Unit>(any(), any()) } returns NetworkResponse.Success(Unit)
        coEvery { userSessionManager.getToken(TokenType.OTPToken) } returns NetworkResponse.Success(pin)
        val bodyInfoFca = mapOf(Constants.PARAM_PIN_AUTH to pin).toJson()

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(arrayOf("/v1/accounts/", uid, "/deactivateAccount")),
                    body = eq(bodyInfoFca)
                )
            }

            coVerify {
                communicationManager.delete<Unit>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(Unit, success)
        }
    }

    @Test
    fun `when getPinToken get success then return pin token`() {
        val pin = "testPin"
        coEvery { userSessionManager.getToken(TokenType.OTPToken) } returns NetworkResponse.Success(pin)

        runTest {
            val response = executor.getPinToken()
            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(pin, success)
        }
    }

    @Test
    fun `when getPinToken get Failure then return failure`() {
        val error = PIMSFoundationError.serverError(404, "resource not found")
        coEvery { userSessionManager.getToken(TokenType.OTPToken) } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.getPinToken()
            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(404, failure?.subError?.status)
        }
    }

    @Test
    fun `when delete API return 403 error then return needStrongAuth error`() {
        val pin = "testPin"
        val error = PIMSError(2204, "server error", SubError(403, "the Token is expired"))

        coEvery { communicationManager.delete<Unit>(any(), any()) } returns NetworkResponse.Failure(error)
        coEvery { userSessionManager.getToken(TokenType.OTPToken) } returns NetworkResponse.Success(pin)
        val bodyInfoFca = mapOf(Constants.PARAM_PIN_AUTH to pin).toJson()

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(arrayOf("/v1/accounts/", uid, "/deactivateAccount")),
                    body = eq(bodyInfoFca)
                )
            }

            coVerify {
                communicationManager.delete<Unit>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(ErrorCode.needStrongAuth, failure?.code)
            Assert.assertEquals(ErrorMessage.needStrongAuth, failure?.message)
        }
    }
}
