package com.inetpsa.pims.spaceMiddleware.network

import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.userSession.UserSessionManager
import com.inetpsa.mmx.foundation.userSession.model.TokenResponse
import com.inetpsa.mmx.foundation.userSession.model.TokenResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor.Chain
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TokenInterceptorTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var tokenInterceptor: TokenInterceptor
    private val userSessionManager: UserSessionManager = mockk()

    @Before
    fun setUp() {
        tokenInterceptor = spyk(TokenInterceptor(middlewareComponent))
        every { middlewareComponent.userSessionManager } returns userSessionManager
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getToken() with success response`() = runBlocking {
        val expectedResponse = Success("Mocked token")
        val callbackSlot = slot<(TokenResponse) -> Unit>()
        coEvery { userSessionManager.getToken(any(), capture(callbackSlot)) } coAnswers {
            callbackSlot.captured.invoke(expectedResponse)
        }

        val result = tokenInterceptor.getToken(TokenType.CVSToken)

        coVerify { userSessionManager.getToken(TokenType.CVSToken, any()) }
        assertEquals(expectedResponse, result)
    }

    /*   @Test
       fun `test injectToken with token type as MymToken`() = runBlocking {

           val builder = Request.Builder()
           // Set up the expected response
           val expectedResponse = Success("Mocked Mym token")
           val tokenType = MiddlewareCommunicationManager.MymToken

           val callbackSlot = slot<(TokenResponse) -> Unit>()
           coEvery { userSessionManager.getToken(any(), capture(callbackSlot)) } coAnswers {
               callbackSlot.captured.invoke(expectedResponse)

           }
           val result = builder.injectToken(tokenType)
           assertEquals("Mocked CVS token", result.build().header(Constants.HEADER_PARAM_MYM_TOKEN))
       }

       @Test
       fun `test injectToken with token type as CVSToken`(): Unit = runBlocking {

           val builder = Request.Builder().build()
           // Set up the expected response
           val expectedResponse = Success("Mocked CVS token")
           val tokenType = TokenType.CVSToken

           val callbackSlot = slot<(TokenResponse) -> Unit>()
           coEvery { userSessionManager.getToken(any(), capture(callbackSlot)) } coAnswers {
               callbackSlot.captured.invoke(expectedResponse)

           }

           val result = builder.injectToken(tokenType)
           assertEquals("Mocked CVS token", result.build().header(Constants.HEADER_PARAM_CVS_TOKEN))

       }
   */
    @Test
    fun `test intercept() for CVSToken type`() {
        val chain = spyk<Chain>()
        val captor = CapturingSlot<Request>()
        val originalRequest = getSampleRequest(TokenType.CVSToken.name)

        every { chain.request() } returns originalRequest
        every { chain.proceed(capture(captor)) } answers { mockk() }

        val expectedResponse = Success("Mocked CVS token")
        every { tokenInterceptor.getToken(any()) } returns expectedResponse

        assertEquals(expectedResponse, tokenInterceptor.getToken(TokenType.CVSToken))

        tokenInterceptor.intercept(chain)
        verify(exactly = 1) { chain.request() }
        verify { chain.proceed(capture(captor)) }

        assertEquals(expectedResponse.token, captor.captured.header(Constants.HEADER_PARAM_CVS_TOKEN))
    }

    @Test
    fun `test intercept() for MymToken type`() {
        val chain = spyk<Chain>()
        val captor = CapturingSlot<Request>()
        val originalRequest = getSampleRequest(MiddlewareCommunicationManager.MymToken.name)

        every { chain.request() } returns originalRequest
        every { chain.proceed(capture(captor)) } answers { mockk() }

        val expectedResponse = Success("Mocked Mym token")
        every { tokenInterceptor.getToken(any()) } returns expectedResponse

        assertEquals(expectedResponse, tokenInterceptor.getToken(TokenType.CVSToken))

        tokenInterceptor.intercept(chain)
        verify(exactly = 1) { chain.request() }
        verify { chain.proceed(capture(captor)) }
        assertEquals(expectedResponse.token, captor.captured.header(Constants.HEADER_PARAM_MYM_TOKEN))
    }

    private fun getSampleRequest(token: String): Request {
        return Request.Builder()
            .url("https://example.com")
            .addHeader(com.inetpsa.mmx.foundation.tools.Constants.TOKEN_TYPE, token)
            .build()
    }
}
