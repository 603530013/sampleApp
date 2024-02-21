package com.inetpsa.pims.spaceMiddleware.network

import android.net.Uri
import com.google.gson.JsonObject
import com.inetpsa.mmx.foundation.networkManager.EndPoint
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.mmx.foundation.tools.Constants
import com.inetpsa.mmx.foundation.tools.TokenType
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class OkHttpRequestFactoryTest {

    private lateinit var okHttpRequestFactory: OkHttpRequestFactory
    private lateinit var sampleUri: Uri
    private val sampleUrl = "http://www.example.com/path/to/resource"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk {
            every { Uri.parse(any()).toString() } returns sampleUrl
        }
        sampleUri = Uri.parse(sampleUrl)
        okHttpRequestFactory = spyk(OkHttpRequestFactory())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test generateRequest for GET method`() {
        val sampleNetworkRequest = getSampleNetworkRequest(sampleUri, null)
        every { okHttpRequestFactory.handleEndPoint(sampleNetworkRequest) } returns sampleUri.toString()
            .toHttpUrlOrNull()
            ?.newBuilder()
        val requestBuilder = okHttpRequestFactory.generateRequest("GET", sampleNetworkRequest, TokenType.CVSToken)

        assertNotNull(requestBuilder?.build())
        requestBuilder?.build()?.let {
            assertEquals("GET", it.method)
            assertEquals("application/json", it.header("Content-Type"))
            assertEquals("A sample token", it.header("Authorization"))
            assertEquals("application/json", it.header("accept"))
            assertNull(it.body)
            assertEquals(TokenType.CVSToken.name, it.header(Constants.TOKEN_TYPE))
            assertNotNull(it.url.queryParameterNames)
            assertNotNull(it.url)
        }
    }

    @Test
    fun `test generateRequest for POST method`() {
        val sampleNetworkRequest = getSampleNetworkRequest(sampleUri, "Sample body")
        every { okHttpRequestFactory.handleEndPoint(sampleNetworkRequest) } returns sampleUri.toString()
            .toHttpUrlOrNull()
            ?.newBuilder()

        val requestBuilder = okHttpRequestFactory.generateRequest(
            "POST",
            sampleNetworkRequest,
            MiddlewareCommunicationManager.MymToken
        )

        assertNotNull(requestBuilder?.build())
        requestBuilder?.build()?.let {
            assertEquals("POST", it.method)
            assertEquals("application/json", it.header("Content-Type"))
            assertEquals("A sample token", it.header("Authorization"))
            assertEquals("application/json", it.header("accept"))
            assertNotNull(it.body)
            assertEquals(MiddlewareCommunicationManager.MymToken.name, it.header(Constants.TOKEN_TYPE))
            assertNotNull(it.url.queryParameterNames)
            assertNotNull(it.url)
        }
    }

    @Test
    fun `handleEndPoint with EndPoint Path should return path`() {
        val expectedOutput = sampleUri.toString().toHttpUrlOrNull()?.newBuilder()
        val request = mockkClass(NetworkRequest::class)
        every { request.endPoint } returns EndPoint.Path(sampleUrl)

        val result = okHttpRequestFactory.handleEndPoint(request)
        assertNotNull(result)
        assertNotNull(result?.build()?.toUrl())
        assertEquals(expectedOutput?.build()?.toUrl(), result?.build()?.toUrl())
    }

    @Test
    fun `test handleEndPoint with EndPoint Url should return url`() {
        val request = mockkClass(NetworkRequest::class)
        val expectedOutput = sampleUri.toString().toHttpUrlOrNull()?.newBuilder()
        every { request.endPoint } returns EndPoint.Url(sampleUri)

        val result = okHttpRequestFactory.handleEndPoint(request)
        assertNotNull(result)
        assertNotNull(result?.build()?.toUrl())
        assertEquals(expectedOutput?.build()?.toUrl(), result?.build()?.toUrl())
    }

    private fun getSampleNetworkRequest(sampleUri: Uri, body: String?): NetworkRequest {
        return NetworkRequest(
            type = JsonObject::class.java,
            url = sampleUri,
            queries = mapOf("key1" to "value1", "key2" to 111),
            body = body,
            headers = mapOf("Authorization" to "A sample token", "Content-Type" to "application/json")
        )
    }
}
