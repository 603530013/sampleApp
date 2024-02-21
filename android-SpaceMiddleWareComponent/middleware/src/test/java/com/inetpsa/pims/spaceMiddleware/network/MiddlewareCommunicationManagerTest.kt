package com.inetpsa.pims.spaceMiddleware.network

import android.net.Uri
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Environment.DEV
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MiddlewareCommunicationManagerTest {

    private val sampleURL = "http://www.example.com/path/to/resource"
    private val middlewareComponent: MiddlewareComponent = mockk()
    private val communicationManager: ICommunicationManager = mockk()
    private val middlewareOkHttpClient: OkHttpClient = mockk()
    private lateinit var middlewareCommunicationManager: MiddlewareCommunicationManager

    @Before
    fun setUp() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk {
            every { Uri.parse(any()).toString() } returns sampleURL
        }
        every { middlewareComponent.communicationManager } returns communicationManager
        mockkConstructor(MiddlewareOkHttpClient::class)
        mockkClass(MiddlewareNetworkExecutor::class)
        every {
            anyConstructed<MiddlewareOkHttpClient>().build(
                middlewareComponent,
                DEV
            )
        } returns middlewareOkHttpClient
        middlewareCommunicationManager = spyk(MiddlewareCommunicationManager(middlewareComponent, DEV))
    }

    @Test
    fun `test delete() with CVSToken calls executeWithMymClient`() {
        val tokenType = TokenType.CVSToken
        val networkRequest = getNetworkRequest()
        val expectedMymResponse = NetworkResponse.Success("success mym response")
        val expectedFoundationResponse = NetworkResponse.Success("success foundation response")
        with(middlewareCommunicationManager) {
            every {
                executeWithMymClient<String>(any(), networkRequest, tokenType)
            } returns expectedMymResponse
            coEvery {
                executeWithFoundationClient<String>(any(), networkRequest, tokenType)
            } returns expectedFoundationResponse

            val result = runBlocking {
                delete<String>(getNetworkRequest(), tokenType)
            }
            assertEquals(expectedMymResponse, result)
            io.mockk.verify { executeWithMymClient<String>(any(), networkRequest, tokenType) }
            coVerify(exactly = 0) { executeWithFoundationClient<String>(any(), networkRequest, tokenType) }
        }
    }

    @Test
    fun `test delete() with OTPToken and CVSToken`() {
        val networkRequest = getNetworkRequest()
        val expectedMymResponse = NetworkResponse.Success("success mym response")
        val expectedFoundationResponse = NetworkResponse.Success("success foundation response")
        with(middlewareCommunicationManager) {
            every {
                executeWithMymClient<String>(any(), networkRequest, any())
            } returns expectedMymResponse
            coEvery {
                executeWithFoundationClient<String>(any(), networkRequest, any())
            } returns expectedFoundationResponse

            val tokenType = TokenType.OTPToken
            val result = runBlocking {
                delete<String>(getNetworkRequest(), tokenType)
            }
            assertEquals(expectedFoundationResponse, result)
            io.mockk.verify(exactly = 0) { executeWithMymClient<String>(any(), networkRequest, tokenType) }
            coVerify(exactly = 1) { executeWithFoundationClient<String>(any(), networkRequest, tokenType) }

            val cvsToken = TokenType.CVSToken
            val resultCVS = runBlocking {
                delete<String>(getNetworkRequest(), cvsToken)
            }
            assertEquals(expectedMymResponse, resultCVS)
            io.mockk.verify(exactly = 1) { executeWithMymClient<String>(any(), networkRequest, cvsToken) }
            coVerify(exactly = 0) { executeWithFoundationClient<String>(any(), networkRequest, cvsToken) }
        }
    }

    @Test
    fun `test patch() with OTPToken and CVSToken`() {
        val networkRequest = getNetworkRequest()
        val expectedMymResponse = NetworkResponse.Success("success mym response")
        val expectedFoundationResponse = NetworkResponse.Success("success foundation response")
        with(middlewareCommunicationManager) {
            every {
                executeWithMymClient<String>(any(), networkRequest, any())
            } returns expectedMymResponse
            coEvery {
                executeWithFoundationClient<String>(any(), networkRequest, any())
            } returns expectedFoundationResponse

            val tokenType = TokenType.OTPToken
            val result = runBlocking {
                patch<String>(getNetworkRequest(), tokenType)
            }
            assertEquals(expectedFoundationResponse, result)
            io.mockk.verify(exactly = 0) { executeWithMymClient<String>(any(), networkRequest, tokenType) }
            coVerify(exactly = 1) { executeWithFoundationClient<String>(any(), networkRequest, tokenType) }

            val cvsToken = TokenType.CVSToken
            val resultCVS = runBlocking {
                patch<String>(getNetworkRequest(), cvsToken)
            }
            assertEquals(expectedMymResponse, resultCVS)
            io.mockk.verify(exactly = 1) { executeWithMymClient<String>(any(), networkRequest, cvsToken) }
            coVerify(exactly = 0) { executeWithFoundationClient<String>(any(), networkRequest, cvsToken) }
        }
    }

    @Test
    fun `test update() with OTPToken and CVSToken`() {
        val networkRequest = getNetworkRequest()
        val expectedMymResponse = NetworkResponse.Success("success mym response")
        val expectedFoundationResponse = NetworkResponse.Success("success foundation response")
        with(middlewareCommunicationManager) {
            every {
                executeWithMymClient<String>(any(), networkRequest, any())
            } returns expectedMymResponse
            coEvery {
                executeWithFoundationClient<String>(any(), networkRequest, any())
            } returns expectedFoundationResponse

            val tokenType = TokenType.OTPToken
            val result = runBlocking {
                update<String>(getNetworkRequest(), tokenType)
            }
            assertEquals(expectedFoundationResponse, result)
            io.mockk.verify(exactly = 0) { executeWithMymClient<String>(any(), networkRequest, tokenType) }
            coVerify(exactly = 1) { executeWithFoundationClient<String>(any(), networkRequest, tokenType) }

            val cvsToken = TokenType.CVSToken
            val resultCVS = runBlocking {
                update<String>(getNetworkRequest(), cvsToken)
            }
            assertEquals(expectedMymResponse, resultCVS)
            io.mockk.verify(exactly = 1) { executeWithMymClient<String>(any(), networkRequest, cvsToken) }
            coVerify(exactly = 0) { executeWithFoundationClient<String>(any(), networkRequest, cvsToken) }
        }
    }

    @Test
    fun `test get() with OTPToken and MymToken`() {
        val networkRequest = getNetworkRequest()
        val expectedMymResponse = NetworkResponse.Success("success mym response")
        val expectedFoundationResponse = NetworkResponse.Success("success foundation response")
        with(middlewareCommunicationManager) {
            every {
                executeWithMymClient<String>(any(), networkRequest, any())
            } returns expectedMymResponse
            coEvery {
                executeWithFoundationClient<String>(any(), networkRequest, any())
            } returns expectedFoundationResponse

            val tokenType = TokenType.OTPToken
            val result = runBlocking {
                get<String>(getNetworkRequest(), tokenType)
            }
            assertEquals(expectedFoundationResponse, result)
            io.mockk.verify(exactly = 0) { executeWithMymClient<String>(any(), networkRequest, tokenType) }
            coVerify(exactly = 1) { executeWithFoundationClient<String>(any(), networkRequest, tokenType) }

            val mymToken = MiddlewareCommunicationManager.MymToken
            val resultMym = runBlocking {
                get<String>(getNetworkRequest(), mymToken)
            }
            assertEquals(expectedMymResponse, resultMym)
            io.mockk.verify(exactly = 1) { executeWithMymClient<String>(any(), networkRequest, mymToken) }
            coVerify(exactly = 0) { executeWithFoundationClient<String>(any(), networkRequest, mymToken) }
        }
    }

    @Test
    fun `test post() with CVSToken and OTPToken`() {
        val networkRequest = getNetworkRequest()
        val expectedMymResponse = NetworkResponse.Success("success mym response")
        val expectedFoundationResponse = NetworkResponse.Success("success foundation response")
        with(middlewareCommunicationManager) {
            every {
                executeWithMymClient<String>(any(), networkRequest, any())
            } returns expectedMymResponse
            coEvery {
                executeWithFoundationClient<String>(any(), networkRequest, any())
            } returns expectedFoundationResponse

            val tokenType = TokenType.OTPToken
            val result = runBlocking {
                post<String>(getNetworkRequest(), tokenType)
            }
            assertEquals(expectedFoundationResponse, result)
            io.mockk.verify(exactly = 0) { executeWithMymClient<String>(any(), networkRequest, tokenType) }
            coVerify(exactly = 1) { executeWithFoundationClient<String>(any(), networkRequest, tokenType) }

            val cvsToken = TokenType.CVSToken
            val resultCVSToken = runBlocking {
                post<String>(getNetworkRequest(), cvsToken)
            }
            assertEquals(expectedMymResponse, resultCVSToken)
            io.mockk.verify(exactly = 1) { executeWithMymClient<String>(any(), networkRequest, cvsToken) }
            coVerify(exactly = 0) { executeWithFoundationClient<String>(any(), networkRequest, cvsToken) }
        }
    }

    @Test
    fun `test execute() with CVS token calls executeWithMymClient`() {
        val tokenType = TokenType.CVSToken
        val networkRequest = getNetworkRequest()
        val expectedResponse = NetworkResponse.Success("success response")
        with(middlewareCommunicationManager) {
            every {
                executeWithMymClient<String>(any(), networkRequest, tokenType)
            } returns expectedResponse
            val result = runBlocking {
                execute<String>("GET", networkRequest, tokenType)
            }
            io.mockk.verify {
                executeWithMymClient<String>(any(), networkRequest, tokenType)
            }
            assertEquals(expectedResponse, result)
        }
    }

    @Test
    fun `test execute() with other token calls executeWithFoundationClient`() {
        val tokenType = TokenType.OTPToken
        val networkRequest = getNetworkRequest()
        val expectedResponse = NetworkResponse.Success("success response")
        with(middlewareCommunicationManager) {
            coEvery {
                executeWithFoundationClient<String>(any(), networkRequest, tokenType)
            } returns expectedResponse
            val result = runBlocking {
                execute<String>("GET", networkRequest, tokenType)
            }
            coVerify {
                executeWithFoundationClient<String>(any(), networkRequest, tokenType)
            }
            assertEquals(expectedResponse, result)
        }
    }

    @Test
    fun `test execute() with MymToken calls executeWithMymClient`() {
        val tokenType = MiddlewareCommunicationManager.MymToken
        val networkRequest = getNetworkRequest()
        val expectedResponse = NetworkResponse.Success("success response")
        with(middlewareCommunicationManager) {
            every {
                executeWithMymClient<String>(any(), networkRequest, tokenType)
            } returns expectedResponse
            val result = runBlocking {
                execute<String>("GET", networkRequest, tokenType)
            }
            io.mockk.verify {
                executeWithMymClient<String>(any(), networkRequest, tokenType)
            }
            assertEquals(expectedResponse, result)
        }
    }

    @Test
    fun `test executeWithFoundationClient GET method`() {
        val networkRequest = getNetworkRequest()
        val expectedResponse = NetworkResponse.Success("Success GET response")
        with(middlewareComponent.communicationManager) {
            coEvery { get<String>(networkRequest, TokenType.CVSToken) } returns expectedResponse

            val resultGet = runBlocking {
                middlewareCommunicationManager.executeWithFoundationClient<String>(
                    "GET",
                    networkRequest,
                    TokenType.CVSToken
                )
            }
            assertTrue(resultGet is NetworkResponse.Success)
            assertEquals(expectedResponse, resultGet)
            coVerify { get<String>(networkRequest, TokenType.CVSToken) }
        }
    }

    @Test
    fun `test executeWithFoundationClient POST method`() {
        val networkRequest = getNetworkRequest()
        val expectedResponse = NetworkResponse.Success("Success POST response")
        with(middlewareComponent.communicationManager) {
            coEvery { post<String>(networkRequest, TokenType.CVSToken) } returns expectedResponse

            val result = runBlocking {
                middlewareCommunicationManager.executeWithFoundationClient<String>(
                    "POST",
                    networkRequest,
                    TokenType.CVSToken
                )
            }
            assertTrue(result is NetworkResponse.Success)
            assertEquals(expectedResponse, result)
            coVerify { post<String>(networkRequest, TokenType.CVSToken) }
        }
    }

    @Test
    fun `test executeWithFoundationClient DELETE method`() {
        val networkRequest = getNetworkRequest()
        val expectedResponse = NetworkResponse.Success("Success DELETE response")
        with(middlewareComponent.communicationManager) {
            coEvery { delete<String>(networkRequest, TokenType.CVSToken) } returns expectedResponse
            val result = runBlocking {
                middlewareCommunicationManager.executeWithFoundationClient<String>(
                    "DELETE",
                    networkRequest,
                    TokenType.CVSToken
                )
            }
            assertTrue(result is NetworkResponse.Success)
            assertEquals(expectedResponse, result)
            coVerify { delete<String>(networkRequest, TokenType.CVSToken) }
        }
    }

    @Test
    fun `test executeWithFoundationClient UPDATE method`() {
        val networkRequest = getNetworkRequest()
        val expectedResponse = NetworkResponse.Success("Success PATCH response")
        with(middlewareComponent.communicationManager) {
            coEvery { patch<String>(networkRequest, TokenType.CVSToken) } returns expectedResponse
            val result = runBlocking {
                middlewareCommunicationManager.executeWithFoundationClient<String>(
                    "PATCH",
                    networkRequest,
                    TokenType.CVSToken
                )
            }
            assertTrue(result is NetworkResponse.Success)
            assertEquals(expectedResponse, result)
            coVerify { patch<String>(networkRequest, TokenType.CVSToken) }
        }
    }

    @Test
    fun `test executeWithFoundationClient PUT method`() {
        val networkRequest = getNetworkRequest()
        val expectedResponse = NetworkResponse.Success("Success PUT response")
        with(middlewareComponent.communicationManager) {
            coEvery { update<String>(networkRequest, TokenType.CVSToken) } returns expectedResponse
            val result = runBlocking {
                middlewareCommunicationManager.executeWithFoundationClient<String>(
                    "PUT",
                    networkRequest,
                    TokenType.CVSToken
                )
            }
            assertTrue(result is NetworkResponse.Success)
            assertEquals(expectedResponse, result)
            coVerify { update<String>(networkRequest, TokenType.CVSToken) }
        }
    }

    @Test
    fun `test executeWithFoundationClient goes to else case and returns failure`() {
        val networkRequest = getNetworkRequest()
        val result = runBlocking {
            middlewareCommunicationManager.executeWithFoundationClient<String>(
                "UPDATE",
                networkRequest,
                TokenType.CVSToken
            )
        }
        assertTrue(result is NetworkResponse.Failure)
        assertEquals(NetworkResponse.Failure(null), result)
    }

    @Test
    fun `test execute with executeWithMyClient returns success response`() = runBlocking {
        val method = "GET"
        val request = mockkClass(NetworkRequest::class)
        val tokenType = TokenType.CVSToken
        val expectedResponse = NetworkResponse.Success("Success response")
        with(middlewareCommunicationManager) {
            coEvery { executeWithMymClient<String>(any(), any(), any()) } returns
                expectedResponse

            val result = execute<String>(method, request, tokenType)
            io.mockk.verify {
                executeWithMymClient<String>(method, request, tokenType)
            }
            assertEquals(expectedResponse, result)
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    /**
     * A method to get sample network request
     */
    private fun getNetworkRequest(): NetworkRequest {
        return NetworkRequest(
            type = String::class.java,
            url = Uri.parse(sampleURL),
            headers = emptyMap(),
            body = "A sample response",
            queries = null
        )
    }
}
