package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.userSession.IUserSessionManager
import com.inetpsa.mmx.foundation.userSession.model.TokenResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class SessionManagerExtensionKtTest {

    private val sessionManager = mockk<IUserSessionManager>()
    private val tokenResponse = TokenResponse.Success("testTokenResponseSuccess")

    @Test
    fun `when execute getToken then execute IUserSessionManager-getToken function`() {
        runTest {
            every {
                sessionManager.getToken(
                    tokenType = any(),
                    callback = captureLambda()
                )
            } answers {
                lambda<(TokenResponse) -> Unit>().captured.invoke(tokenResponse)
            }

            val response = sessionManager.getToken(TokenType.OTPToken)
            verify { sessionManager.getToken(eq(TokenType.OTPToken), any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals("testTokenResponseSuccess", success)
        }
    }
}
