package com.inetpsa.pims.spaceMiddleware.network

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.userSession.UserSessionManager
import com.inetpsa.mmx.foundation.userSession.model.TokenResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.token.GetMymAccessTokenPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.MymAccessToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol.HTTP_2
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthenticatorExecutorTest {

    private lateinit var authenticatorExecutor: AuthenticatorExecutor
    private val middlewareComponent: MiddlewareComponent = mockk()
    private val userSessionManager: UserSessionManager = mockk()

    @Before
    fun setUp() {
        authenticatorExecutor = AuthenticatorExecutor(middlewareComponent)
        every { middlewareComponent.userSessionManager } returns userSessionManager
    }

    @Test
    fun `test authenticate with authenticateWithMymToken returns null`() {
        val request = getCVSSampleRequest("CVSToken")
        val authenticatorExecutor = spyk(AuthenticatorExecutor(middlewareComponent))
        every { authenticatorExecutor.authenticateWithMymToken(any()) } returns null
        every { authenticatorExecutor.authenticateWithCVSToken(any()) } returns request

        val response = getMockedResponse(request)
        val result = authenticatorExecutor.authenticate(null, response)
        assertNotNull(result)
        assertEquals(request, result)
        assertEquals(
            request.header(Constants.HEADER_PARAM_CVS_TOKEN),
            result?.header(Constants.HEADER_PARAM_CVS_TOKEN)
        )
    }

    @Test
    fun `test authenticate with authenticateWithMymToken returns success`() {
        val mymRequest = getMymSampleRequest("MymToken")
        val cvsRequest = getCVSSampleRequest("CVSToken")
        val authenticatorExecutor = spyk(AuthenticatorExecutor(middlewareComponent))
        every { authenticatorExecutor.authenticateWithMymToken(any()) } returns mymRequest
        every { authenticatorExecutor.authenticateWithCVSToken(any()) } returns cvsRequest

        val response = getMockedResponse(mymRequest)
        val result = authenticatorExecutor.authenticate(null, response)
        assertNotNull(result)
        assertEquals(mymRequest, result)
        assertEquals(
            mymRequest.header(Constants.HEADER_PARAM_MYM_TOKEN),
            result?.header(Constants.HEADER_PARAM_MYM_TOKEN)
        )
    }

    @Test
    fun `test extract Mym token with not blank header`() {
        val expectedToken = "MymToken"
        val request = getMymSampleRequest(expectedToken)
        val response = getMockedResponse(request)

        val mymToken = authenticatorExecutor.extractMymToken(response)
        assertEquals(expectedToken, mymToken)
    }

    @Test
    fun `test extract Mym token with blank header`() {
        val request = getMymSampleRequest("")
        val response = getMockedResponse(request)

        val mymToken = authenticatorExecutor.extractMymToken(response)
        assertEquals(null, mymToken)
    }

    @Test
    fun `test extract CVSToken with not blank header`() {
        val expectedToken = "CVSToken"
        val request = getCVSSampleRequest(expectedToken)
        val response = getMockedResponse(request)

        val cvsToken = authenticatorExecutor.extractCVSToken(response)
        assertEquals(expectedToken, cvsToken)
    }

    @Test
    fun `test extract CVSToken with blank header`() {
        val request = getCVSSampleRequest("")
        val response = getMockedResponse(request)

        val cvsToken = authenticatorExecutor.extractCVSToken(response)
        assertEquals(null, cvsToken)
    }

    @Test
    fun `test authenticateWithMymToken when token has not changed`() {
        val mymToken = "mymToken"
        val authenticatorExecutor = spyk(AuthenticatorExecutor(middlewareComponent))
        every { authenticatorExecutor.getMymRefreshedToken() } returns mymToken

        val request = getMymSampleRequest(mymToken)
        val response = getMockedResponse(request)
        val authenticatedRequest = authenticatorExecutor.authenticateWithMymToken(response)
        verify { authenticatorExecutor.getMymRefreshedToken() }
        assertEquals(null, authenticatedRequest?.header(Constants.HEADER_PARAM_MYM_TOKEN))
    }

    @Test
    fun `test authenticateWithMymToken when token has changed`() {
        val mymToken = "mymToken"
        val refreshedMymToken = "newMymToken"
        val authenticatorExecutor = spyk(AuthenticatorExecutor(middlewareComponent))
        every { authenticatorExecutor.getMymRefreshedToken() } returns refreshedMymToken

        val request = getMymSampleRequest(mymToken)
        val response = getMockedResponse(request)

        val authenticatedRequest = authenticatorExecutor.authenticateWithMymToken(response)
        verify { authenticatorExecutor.getMymRefreshedToken() }
        assertEquals(refreshedMymToken, authenticatedRequest?.header(Constants.HEADER_PARAM_MYM_TOKEN))
    }

    @Test
    fun `test authenticateWithCVSToken when token has not changed`() {
        val cvsToken = "CVSToken"
        val authenticatorExecutor = spyk(AuthenticatorExecutor(middlewareComponent))
        every { authenticatorExecutor.getCVSRefreshedToken() } returns cvsToken

        val request = getCVSSampleRequest(cvsToken)
        val response = getMockedResponse(request)

        val authenticatedRequest = authenticatorExecutor.authenticateWithCVSToken(response)
        verify { authenticatorExecutor.getCVSRefreshedToken() }
        assertEquals(null, authenticatedRequest?.header(Constants.HEADER_PARAM_CVS_TOKEN))
    }

    @Test
    fun `test authenticateWithCVSToken when token has changed`() {
        val cvsToken = "CVSToken"
        val refreshedCVSToken = "newCVSToken"
        val authenticatorExecutor = spyk(AuthenticatorExecutor(middlewareComponent))
        every { authenticatorExecutor.getCVSRefreshedToken() } returns refreshedCVSToken

        val request = getCVSSampleRequest(cvsToken)
        val response = getMockedResponse(request)

        val authenticatedRequest = authenticatorExecutor.authenticateWithCVSToken(response)
        verify { authenticatorExecutor.getCVSRefreshedToken() }
        assertEquals(refreshedCVSToken, authenticatedRequest?.header(Constants.HEADER_PARAM_CVS_TOKEN))
    }

    @Test
    fun `test getCVSRefreshedToken returns valid token`() = runBlocking {
        val cvsAccessToken = "CVSToken"
        coEvery { middlewareComponent.userSessionManager.refreshToken(TokenType.CVSToken, any()) } coAnswers {
            val callback = arg<(TokenResponse) -> Unit>(1)
            callback(TokenResponse.Success(cvsAccessToken))
        }

        val token = authenticatorExecutor.getCVSRefreshedToken()
        coVerify { middlewareComponent.userSessionManager.refreshToken(TokenType.CVSToken, any()) }
        assertEquals(cvsAccessToken, token)
    }

    @Test
    fun `test getCVSRefreshedToken returns null when token is empty`() {
        coEvery { middlewareComponent.userSessionManager.refreshToken(TokenType.CVSToken, any()) } coAnswers {
            val callback = arg<(TokenResponse) -> Unit>(1)
            callback(TokenResponse.Success(""))
        }
        val token = authenticatorExecutor.getCVSRefreshedToken()
        coVerify { middlewareComponent.userSessionManager.refreshToken(TokenType.CVSToken, any()) }

        assertEquals(null, token)
    }

    @Test
    fun `test getCVSRefreshedToken returns null when there is error`() {
        coEvery { middlewareComponent.userSessionManager.refreshToken(TokenType.CVSToken, any()) } coAnswers {
            val callback = arg<(TokenResponse) -> Unit>(1)
            callback(TokenResponse.Failure(PIMSError(4001, "Unknown error")))
        }

        val token = authenticatorExecutor.getCVSRefreshedToken()
        coVerify { middlewareComponent.userSessionManager.refreshToken(TokenType.CVSToken, any()) }
        assertEquals(null, token)
    }

    @Test
    fun `test getMymRefreshedToken success`() {
        val mymToken = "MymToken"
        val successResponse = NetworkResponse.Success(MymAccessToken(mymToken, null))
        mockkConstructor(GetMymAccessTokenPsaExecutor::class)
        coEvery {
            GetMymAccessTokenPsaExecutor(middlewareComponent, emptyMap())
                .execute()
        } returns successResponse

        val result = authenticatorExecutor.getMymRefreshedToken()
        assertEquals(mymToken, result)
    }

    @Test
    fun `test getMymRefreshedToken success returns empty token`() {
        mockkConstructor(GetMymAccessTokenPsaExecutor::class)
        coEvery {
            GetMymAccessTokenPsaExecutor(middlewareComponent, emptyMap())
                .execute()
        } returns NetworkResponse.Success(MymAccessToken("", null))
        val result = authenticatorExecutor.getMymRefreshedToken()

        assertNull(result)
    }

    @Test
    fun `test getMymRefreshedToken failure`() {
        mockkConstructor(GetMymAccessTokenPsaExecutor::class)
        coEvery {
            GetMymAccessTokenPsaExecutor(middlewareComponent, emptyMap())
                .execute()
        } returns NetworkResponse.Failure(PIMSError(4001, "Unknown"))
        val result = authenticatorExecutor.getMymRefreshedToken()

        assertNull(result)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    /**
     * A helper method get mocked dummy response for test
     */
    private fun getMockedResponse(request: Request): Response {
        return Response.Builder()
            .request(request)
            .code(200)
            .protocol(HTTP_2)
            .message("A sample response")
            .build()
    }

    /**
     * A helper method to get sample request with mym token as header
     */
    private fun getMymSampleRequest(mymToken: String): Request {
        return Request.Builder()
            .url("https://example.com")
            .addHeader(Constants.HEADER_PARAM_MYM_TOKEN, mymToken)
            .build()
    }

    /**
     * A helper method to get sample request with CVS token as header
     */
    private fun getCVSSampleRequest(cvsToken: String): Request {
        return Request.Builder()
            .url("https://example.com")
            .addHeader(Constants.HEADER_PARAM_CVS_TOKEN, cvsToken)
            .build()
    }
}
