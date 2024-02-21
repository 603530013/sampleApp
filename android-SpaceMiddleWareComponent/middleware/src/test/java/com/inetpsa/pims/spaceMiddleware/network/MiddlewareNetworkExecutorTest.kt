package com.inetpsa.pims.spaceMiddleware.network

import android.net.Uri
import androidx.core.text.HtmlCompat
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.asJson
import com.inetpsa.mmx.foundation.monitoring.ErrorCode
import com.inetpsa.mmx.foundation.monitoring.PIMSErrorFactory
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.MymResponse
import com.inetpsa.pims.spaceMiddleware.util.Utils
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.UnknownHostException

class MiddlewareNetworkExecutorTest {

    private val sampleURL = "http://www.example.com/path/to/resource"
    private lateinit var mockWebServer: MockWebServer
    private val okHttpClient: OkHttpClient = mockk()
    private lateinit var middlewareNetworkExecutor: MiddlewareNetworkExecutor

    @Before
    fun setUp() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk {
            every { Uri.parse(any()).toString() } returns sampleURL
        }
        mockWebServer = MockWebServer()
        mockWebServer.start()
        middlewareNetworkExecutor = spyk(MiddlewareNetworkExecutor(okHttpClient))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        unmockkAll()
    }

    @Test
    fun `test handle2xxResponse empty body`() {
        val response = mockk<Response>()
        every { response.body?.string() } returns ""
        val type = object : TypeToken<String>() {}.type
        val result = middlewareNetworkExecutor.handle2xxResponse<String>(response, type)

        assertEquals(NetworkResponse.Success(Unit).toString(), result.toString())
    }

    @Test
    fun handleMyMResponse_success() {
        val responseString = Gson().toJson("A sample response")
        mockkObject(Utils)
        val gson = mockk<Gson>()
        val type = object : TypeToken<String>() {}.type
        every { Utils.getJsonClient() } returns gson
        every {
            gson.fromJson<MymResponse<String>>(
                responseString,
                TypeToken.getParameterized(MymResponse::class.java, type).type
            )
        } returns MymResponse(
            success = "success",
            null,
            null
        )

        val result = middlewareNetworkExecutor.handleMyMResponse<String>(responseString, type)
        assertEquals(NetworkResponse.Success("success"), result)
    }

    @Test
    fun handleMyMResponse_error() {
        val response = "sample response"
        mockkObject(Utils)
        val gson = mockk<Gson>()
        val type = object : TypeToken<String>() {}.type
        every { Utils.getJsonClient() } returns gson
        every {
            gson.fromJson<MymResponse<String>>(
                response,
                TypeToken.getParameterized(MymResponse::class.java, type).type
            )
        } returns MymResponse(
            null,
            error = MymResponse
                .Error(500, "Internal Server Error"),
            null
        )

        val result = middlewareNetworkExecutor.handleMyMResponse<String>(response, type)
        val failureError = PIMSFoundationError.serverError(500, "Internal Server Error")
        val failure = NetworkResponse.Failure(failureError)
        assertEquals(failure.toString(), result.toString())
    }

    @Test
    fun `test handleMyMResponse error list`() {
        val response = "sample response"
        mockkObject(Utils)
        val gson = mockk<Gson>()
        val type = object : TypeToken<String>() {}.type
        every { Utils.getJsonClient() } returns gson
        every {
            gson.fromJson<MymResponse<String>>(
                response,
                TypeToken.getParameterized(MymResponse::class.java, type).type
            )
        } returns MymResponse(
            null,
            error = null,
            errors = mapOf("error1" to "Unknown error")
        )

        val result = middlewareNetworkExecutor.handleMyMResponse<String>(response, type)
        val expectedError = PIMSFoundationError.serverError(
            ErrorCode.serverError,
            mapOf("error1" to "Unknown error")
                .asJson()
        )
        assertEquals(
            NetworkResponse.Failure(expectedError).toString(),
            result.toString()
        )
    }

    @Test
    fun `test handleMyMResponse unknown error`() {
        val response = "sample response"
        mockkObject(Utils)
        val gson = mockk<Gson>()
        val type = object : TypeToken<String>() {}.type
        every { Utils.getJsonClient() } returns gson
        every {
            gson.fromJson<MymResponse<String>>(
                response,
                TypeToken.getParameterized(MymResponse::class.java, type).type
            )
        } returns MymResponse(
            null,
            error = null,
            errors = null
        )

        val result = middlewareNetworkExecutor.handleMyMResponse<String>(response, type)
        assertEquals(
            NetworkResponse.Failure(PIMSFoundationError.unknownError).toString(),
            result.toString()
        )
    }

    @Test
    fun `test handleMyMResponse() with type as MymResponse throws IllegalArgumentException`() {
        val superType = object : TypeToken<MymResponse<String>>() {}.type
        assertThrows(IllegalArgumentException::class.java) {
            middlewareNetworkExecutor.handleMyMResponse<String>("", superType)
        }
    }

    @Test
    fun `test handle204Response() returns success()`() {
        val result = middlewareNetworkExecutor.handle204Response<String>()
        assertEquals(NetworkResponse.Success(Unit).toString(), result.toString())
    }

    @Test
    fun `test handleNot2xxResponse() serverError`() {
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("mockResponseBody")

        mockWebServer.enqueue(mockResponse)

        val baseUrl = mockWebServer.url("/")
        val result = middlewareNetworkExecutor.handleNot2xxResponse<String>(
            createResponse(baseUrl, 500, "Internal Server Error")
        )
        val expectedError = PIMSFoundationError.serverError(500, "Internal Server Error")
        assertEquals(NetworkResponse.Failure(expectedError).toString(), result.toString())
    }

    @Test
    fun `test handleNot2xxResponse html response`() {
        mockkStatic(HtmlCompat::class)
        every { HtmlCompat.fromHtml(any(), any()) } returns mockk {
            every { HtmlCompat.fromHtml(any(), any()).toString() } returns "Not Found"
        }
        val mockResponseBody = "<html><body>Not Found</body></html>"

        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setBody(mockResponseBody)
        mockWebServer.enqueue(mockResponse)

        val baseUrl = mockWebServer.url("/")
        val responseMock = middlewareNetworkExecutor.handleNot2xxResponse<String>(
            createResponse(baseUrl, 404, "")
        )

        val expectedError = PIMSFoundationError.serverError(404, "Not Found")
        assertEquals(NetworkResponse.Failure(expectedError).toString(), responseMock.toString())
    }

    @Test
    fun `test handleNot2xxResponse() plainTextResponse`() {
        val mockResponse = MockResponse()
            .setResponseCode(403)
            .setBody("")
        mockWebServer.enqueue(mockResponse)

        val baseUrl = mockWebServer.url("/")
        val result = middlewareNetworkExecutor.handleNot2xxResponse<String>(
            createResponse(baseUrl, 403, "Forbidden")
        )

        val expectedError = PIMSFoundationError.serverError(403, "Forbidden")
        assertEquals(NetworkResponse.Failure(expectedError).toString(), result.toString())
    }

    @Suppress("ThrowsCount")
    @Test
    fun `test safeNetworkCall cover all exceptions`() {
        with(middlewareNetworkExecutor) {
            val actionSuccess: () -> NetworkResponse<String> = { NetworkResponse.Success("Parsed result") }
            val actionUnknownHostException: () -> NetworkResponse<String> = { throw UnknownHostException() }
            val actionIllegalStateException: () -> NetworkResponse<String> =
                { throw IllegalStateException("error") }
            val actionSocketTimeoutException: () -> NetworkResponse<String> =
                { throw java.net.SocketTimeoutException() }

            val resultSuccess = safeNetworkCall(actionSuccess)
            val resultIllegalStateException = safeNetworkCall(actionIllegalStateException)
            val resultSocketTimeoutException = safeNetworkCall(actionSocketTimeoutException)
            val resultUnknownHostException = safeNetworkCall(actionUnknownHostException)

            val actionConnectionShutdownException: () -> NetworkResponse<String> =
                { throw ConnectionShutdownException() }
            val resultConnectionShutdownException = safeNetworkCall(actionConnectionShutdownException)

            val actionIOException: () -> NetworkResponse<String> = { throw IOException() }
            val resultIOException = safeNetworkCall(actionIOException)

            @Suppress("TooGenericExceptionThrown")
            val actionException: () -> NetworkResponse<String> = { throw Exception() }
            val resultException = safeNetworkCall(actionException)

            assertEquals(NetworkResponse.Success("Parsed result"), resultSuccess)
            val errorIllegalStateException = PIMSErrorFactory.create(
                -1,
                "call has already been executed: error"
            )
            assertEquals(
                NetworkResponse.Failure(errorIllegalStateException).toString(),
                resultIllegalStateException.toString()
            )

            assertEquals(
                NetworkResponse.Failure(PIMSFoundationError.timeoutError).toString(),
                resultSocketTimeoutException.toString()
            )
            assertEquals(
                NetworkResponse.Failure(PIMSFoundationError.networkError).toString(),
                resultUnknownHostException.toString()
            )
            assertEquals(
                NetworkResponse.Failure(PIMSFoundationError.networkError).toString(),
                resultConnectionShutdownException.toString()
            )
            assertEquals(
                NetworkResponse.Failure(PIMSFoundationError.networkError).toString(),
                resultIOException.toString()
            )
            assertEquals(
                NetworkResponse.Failure(PIMSFoundationError.unknownError).toString(),
                resultException.toString()
            )
        }
    }

    @Test
    fun `test safeParsingCall() successful parsing`() {
        val action: () -> NetworkResponse<String> = { NetworkResponse.Success("Parsed result") }

        val result = middlewareNetworkExecutor.safeParsingCall(action)
        assertNotNull(result)
        assertEquals(NetworkResponse.Success("Parsed result"), result)
    }

    @Test
    fun `test safeParsingCall() with JsonSyntaxException`() {
        val error = PIMSErrorFactory.create(
            -3,
            "attempts to read a malformed JSON element"
        )
        val action: () -> NetworkResponse<String> = { throw JsonSyntaxException("syntax error") }

        val result = middlewareNetworkExecutor.safeParsingCall(action)
        assertEquals(NetworkResponse.Failure(error).toString(), result.toString())
    }

    @Test
    fun `test safeParsingCall() with JsonParseException`() {
        val error = PIMSErrorFactory.create(
            -4,
            "there is a serious issue that occurs during parsing of a Json string "
        )
        val action: () -> NetworkResponse<String> = { throw JsonParseException("Parsing issue") }
        val result = middlewareNetworkExecutor.safeParsingCall(action)
        assertEquals(NetworkResponse.Failure(error).toString(), result.toString())
    }

    @Test
    fun `test execute() for success response code`() {
        val successfulResponse = mockk<Response>()
        val mymRequest = Request.Builder()
            .url("https://example.com")
            .addHeader(Constants.HEADER_PARAM_CVS_TOKEN, "MymToken")
            .build()
        every { successfulResponse.code } returns 200
        every { successfulResponse.body?.string() } returns "A success response"
        every { okHttpClient.newCall(any()).execute() } returns successfulResponse

        middlewareNetworkExecutor.execute<String>(mymRequest, String::class.java)

        verify { middlewareNetworkExecutor.handle2xxResponse<String>(successfulResponse, String::class.java) }
    }

    @Test
    fun `test execute() for response code no content`() {
        val successfulResponse = mockk<Response>()
        val mymRequest = getSampleRequest()
        every { successfulResponse.code } returns 204
        every { successfulResponse.body?.string() } returns "A success response"
        every { okHttpClient.newCall(any()).execute() } returns successfulResponse

        with(middlewareNetworkExecutor) {
            every { handle204Response<String>() } returns NetworkResponse.Success("")
            val result = execute<String>(mymRequest, String::class.java)
            verify { handle204Response<String>() }
            assertNotNull(result)
            assert(result is NetworkResponse.Success)
        }
    }

    @Test
    fun `test execute() calls handleNot2xxResponse `() {
        val successfulResponse = mockk<Response>()
        val mymRequest = getSampleRequest()
        val error = PIMSFoundationError.serverError(500, "server error")
        every { successfulResponse.code } returns 500
        every { successfulResponse.message } returns "server error"
        every { okHttpClient.newCall(any()).execute() } returns successfulResponse
        with(middlewareNetworkExecutor) {
            every { handleNot2xxResponse<String>(any()) } returns NetworkResponse.Failure(error)
            val result = execute<String>(mymRequest, String::class.java)
            verify { handleNot2xxResponse<String>(any()) }
            assertNotNull(result)
            assert(result is NetworkResponse.Failure)
            assertEquals(result.toString(), NetworkResponse.Failure(error).toString())
        }
    }

    @Test
    fun testHandleTypeResponse_Success() {
        val responseString = Gson().toJson("A sample response")
        val gson = mockk<Gson>()
        val networkRequest = getSampleNetworkRequest()
        every { gson.fromJson(responseString, String::class.java) } returns responseString

        val result =
            middlewareNetworkExecutor.handleTypeResponse<NetworkResponse<String>>(
                responseString,
                networkRequest.type
            )

        assertNotNull(result)
        assertEquals(NetworkResponse.Success("A sample response").toString(), result.toString())
    }

    @Test
    fun `test HandleTypeResponse failure due to JsonSyntaxException`() {
        val responseString = """ {"name": "temp"} """
        val jsonSyntaxException = PIMSErrorFactory.create(
            -3,
            "attempts to read a malformed JSON element"
        )
        val networkRequest = getSampleNetworkRequest()

        val result =
            middlewareNetworkExecutor.handleTypeResponse<String>(
                responseString,
                networkRequest.type
            )

        assertNotNull(result)
        assertEquals(NetworkResponse.Failure(jsonSyntaxException).toString(), result.toString())
    }

    private fun getSampleNetworkRequest(): NetworkRequest {
        return NetworkRequest(
            type = String::class.java,
            url = Uri.parse(sampleURL),
            queries = emptyMap(),
            body = null,
            headers = emptyMap()
        )
    }

    private fun createResponse(baseUrl: HttpUrl, code: Int, message: String): Response {
        return Response.Builder()
            .request(Request.Builder().url(baseUrl).build())
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(message)
            .body("".toResponseBody())
            .build()
    }

    private fun getSampleRequest(): Request {
        return Request.Builder()
            .url("https://example.com")
            .addHeader(Constants.HEADER_PARAM_MYM_TOKEN, "MymToken")
            .build()
    }
}
