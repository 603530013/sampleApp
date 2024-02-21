package com.inetpsa.pims.spaceMiddleware.executor.logIncident

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.logIncident.UploadLogIncidentPsaExecutor.Companion.DEFAULT_QUALITY
import com.inetpsa.pims.spaceMiddleware.model.logincident.UploadLogIncidentInput
import com.inetpsa.pims.spaceMiddleware.model.logincident.UploadLogIncidentOutput
import com.inetpsa.pims.spaceMiddleware.model.logincident.UploadLogIncidentResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.FileSplitter
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
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

internal class UploadLogIncidentPsaExecutorTest : PsaExecutorTestHelper() {

    private val fileSplitter: FileSplitter = mockk(relaxed = true, relaxUnitFun = true)
    private lateinit var executor: UploadLogIncidentPsaExecutor

    @Before
    override fun setup() {
        mockkStatic("kotlin.io.FilesKt")
        mockkStatic("kotlin.io.FilesKt__FileReadWriteKt")
        super.setup()
        every { middlewareComponent.context } returns context
        every { baseCommand.parameters } returns mapOf(
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st"
        )
        executor = spyk(UploadLogIncidentPsaExecutor(baseCommand))
        executor.fileSplitter = fileSplitter
    }

    @Test
    fun `when params, FILE with right input then return maps with all required information`() {
        val incidentId = "test_incidentId"
        val filename = "test_filename"
        val title = "test_title"

        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to incidentId,
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_FILE_NAME to filename,
            Constants.BODY_PARAM_DATA to "testData",
            Constants.BODY_PARAM_TITLE to title,
            Constants.BODY_PARAM_FILE_TYPE to "file"
        )

        val output = executor.params(input)
        Assert.assertEquals(UploadLogIncidentInput.Type.FILE, output.type)
        Assert.assertEquals("testData", output.path)
        Assert.assertEquals(incidentId, output.incidentId)
        Assert.assertEquals(filename, output.filename)
        Assert.assertEquals(DEFAULT_QUALITY, output.quality)
    }

    @Test
    fun `when params, IMAGE and the right input then return maps with all required information`() {
        val incidentId = "test_incidentId"
        val filename = "test_filename"
        val title = "test_title"

        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to incidentId,
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_FILE_NAME to filename,
            Constants.BODY_PARAM_DATA to "testData",
            Constants.BODY_PARAM_TITLE to title,
            Constants.BODY_PARAM_FILE_TYPE to "file",
            Constants.BODY_PARAM_QUALITY to 36
        )

        val output = executor.params(input)
        Assert.assertEquals(UploadLogIncidentInput.Type.FILE, output.type)
        Assert.assertEquals("testData", output.path)
        Assert.assertEquals(incidentId, output.incidentId)
        Assert.assertEquals(filename, output.filename)
        Assert.assertEquals(36, output.quality)
    }

    @Test
    fun `when params with missing data then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to "test_incidentId",
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_FILE_NAME to "test_filename",
            Constants.BODY_PARAM_TITLE to "test_title"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_DATA)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with invalid data then throw missing parameter`() {
        val data = 123
        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to "test_incidentId",
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_FILE_NAME to "test_filename",
            Constants.BODY_PARAM_DATA to data,
            Constants.BODY_PARAM_TITLE to "test_title"
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_DATA)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing type then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to "test_incidentId",
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_FILE_NAME to "filename",
            Constants.BODY_PARAM_DATA to "testData",
            Constants.BODY_PARAM_TITLE to "test_title"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_FILE_TYPE)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with invalid type then throw missing parameter`() {
        val type = 123
        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to "test_incidentId",
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_FILE_NAME to "test_filename",
            Constants.BODY_PARAM_DATA to "test_data",
            Constants.BODY_PARAM_FILE_TYPE to type,
            Constants.BODY_PARAM_TITLE to "test_title"
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_FILE_TYPE)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing incidentId then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_FILE_NAME to "filename",
            Constants.BODY_PARAM_DATA to "testData",
            Constants.BODY_PARAM_TITLE to "title",
            Constants.BODY_PARAM_FILE_TYPE to "image",
            Constants.BODY_PARAM_QUALITY to 20
        )
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_INCIDENT_ID)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with invalid incidentId then throw missing parameter`() {
        val incidentId = 123
        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to incidentId,
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_FILE_NAME to "filename",
            Constants.BODY_PARAM_DATA to "testData",
            Constants.BODY_PARAM_TITLE to "title",
            Constants.BODY_PARAM_FILE_TYPE to "image",
            Constants.BODY_PARAM_QUALITY to 20
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_INCIDENT_ID)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing filename then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to "test_incidentId",
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_DATA to "testData",
            Constants.BODY_PARAM_TITLE to "title",
            Constants.BODY_PARAM_FILE_TYPE to "image",
            Constants.BODY_PARAM_QUALITY to 20
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_FILE_NAME)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with invalid filename then throw missing parameter`() {
        val filename = 123
        val input = mapOf(
            Constants.PARAM_INCIDENT_ID to "test_incidentId",
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st",
            Constants.BODY_PARAM_DATA to "testData",
            Constants.BODY_PARAM_TITLE to "title",
            Constants.BODY_PARAM_FILE_NAME to filename,
            Constants.BODY_PARAM_FILE_TYPE to "image",
            Constants.BODY_PARAM_QUALITY to 20
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_FILE_NAME)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    /* @Test
     fun `when execute, FILE and postRequest Success then return NetworkResponse Success`() {
         val request: NetworkRequest = mockk()
         val input = mapOf(
             PARAMS_KEY_TYPE to "file",
             PARAMS_KEY_PATH to "test_path",
             PARAMS_KEY_INCIDENT_ID to "test_incidentId",
             PARAMS_KEY_FILENAME to "test_filename",
             PARAMS_KEY_QUALITY to "20"
         )

         val splitPaths = listOf("test_path_1", "test_path_2")
         every { fileSplitter.handleUploadFile(any(), any()) } returns splitPaths
         every { fileSplitter.toBase64(any()) } returns "base64_data"
         every { executor.toRequest(any(), any(), any()) } returns request
         coEvery {
             executor.postRequest(any(), any())
         } returns NetworkResponse.Success(UploadLogIncidentOutput("test"))

         runTest {
             val actual = executor.execute(input)
             Assert.assertTrue(actual is NetworkResponse.Success)
             coVerify(exactly = 2) { executor.postRequest("test_filename", request) }
         }
     }

     @Test
     fun `when execute, FILE and postRequest Failure then return NetworkResponse Failure`() {
         val request: NetworkRequest = mockk()
         val input = mapOf(
             PARAMS_KEY_TYPE to "file",
             PARAMS_KEY_PATH to "test_path",
             PARAMS_KEY_INCIDENT_ID to "test_incidentId",
             PARAMS_KEY_FILENAME to "test_filename",
             PARAMS_KEY_QUALITY to "20"
         )

         val splitPaths = listOf("test_path_1", "test_path_2")
         every { fileSplitter.handleUploadFile(any(), any()) } returns splitPaths
         every { fileSplitter.toBase64(any()) } returns "base64_data"
         every { executor.toRequest(any(), any(), any()) } returns request
         coEvery {
             executor.postRequest(any(), any())
         } returns NetworkResponse.Failure(PimsErrors.serverError(null, "server error"))

         runTest {
             val actual = executor.execute(input)
             Assert.assertTrue(actual is NetworkResponse.Failure)
             coVerify(exactly = 1) { executor.postRequest("test_filename", request) }
         }
     }*/

    @Test
    fun `when execute and FILE then go to handleFileRequest`() {
        runTest {
            val input = UploadLogIncidentInput(
                incidentId = "test_incidentId",
                path = "test_path",
                filename = "test_filename",
                type = UploadLogIncidentInput.Type.FILE,
                quality = 20
            )
            val successResponse = UploadLogIncidentOutput("test")
            coEvery { executor.handleFileRequest(any()) } returns NetworkResponse.Success(successResponse)

            executor.execute(input)
            coVerify { executor.handleFileRequest(input) }
        }
    }

    @Test
    fun `when execute and IMAGE then go to postRequest`() {
        runTest {
            val input = UploadLogIncidentInput(
                incidentId = "test_incidentId",
                path = "test_path",
                filename = "test_filename",
                type = UploadLogIncidentInput.Type.IMAGE,
                quality = 20
            )
            coEvery {
                executor.handleImageRequest(any())
            } returns NetworkResponse.Success(UploadLogIncidentOutput("test_filename"))

            executor.execute(input)
            coVerify { executor.handleImageRequest(input) }
        }
    }

    @Test
    fun `when toRequest then return format NetworkRequest`() {
        val input = spyk(
            UploadLogIncidentInput(
                incidentId = "test_incident_id",
                path = "test_path",
                filename = "test_filename",
                type = UploadLogIncidentInput.Type.FILE,
                quality = 0
            )
        )
        input.data = "test_data"
        input.title = "test_title"
        every { input.asBody() } returns "test_body"
        val actual = executor.toRequest(input)
        Assert.assertEquals(UploadLogIncidentResponse::class.java, actual.type)
        Assert.assertEquals("test_body", actual.body)
        verify {
            executor.request(
                type = eq(UploadLogIncidentResponse::class.java),
                urls = eq(arrayOf("v1/logincident/pj/", "test_incident_id")),
                body = eq("test_body")
            )
        }
    }

    @Test
    fun `when handleFileRequest and postRequest Success then return NetworkResponse Success`() {
        val request: NetworkRequest = mockk()
        val splitPaths = listOf("test_path_1", "test_path_2")
        every { fileSplitter.handleUploadFile(any(), any(), any()) } returns splitPaths
        every { fileSplitter.toBase64(any(), any()) } returns "base64_data"
        every { executor.toRequest(any()) } returns request
        coEvery {
            executor.postRequest(any(), any())
        } returns NetworkResponse.Success(UploadLogIncidentOutput("test_filename"))

        val input = UploadLogIncidentInput(
            incidentId = "test_incidentId",
            path = "test_path",
            filename = "test_filename",
            type = UploadLogIncidentInput.Type.IMAGE,
            quality = 20
        )

        runTest {
            val actual = executor.handleFileRequest(input)
            Assert.assertTrue(actual is NetworkResponse.Success)
            verify { fileSplitter.toBase64("test_path_1", context) }
            verify { fileSplitter.toBase64("test_path_2", context) }
            coVerify { fileSplitter.handleUploadFile("test_path", "test_filename", context) }
            coVerify(exactly = 2) { executor.postRequest("test_filename", request) }
        }
    }

    @Test
    fun `when handleFileRequest and postRequest Failure then return NetworkResponse Failure`() {
        val request: NetworkRequest = mockk()
        val splitPaths = listOf("test_path_1", "test_path_2")
        every { fileSplitter.handleUploadFile(any(), any(), any()) } returns splitPaths
        every { executor.toRequest(any()) } returns request
        every { fileSplitter.toBase64(any(), any()) } returns "base64_data"
        coEvery {
            executor.postRequest(any(), any())
        } returns NetworkResponse.Failure(PimsErrors.serverError(listOf(), "error"))

        val input = UploadLogIncidentInput(
            incidentId = "test_incidentId",
            path = "test_path",
            filename = "test_filename",
            type = UploadLogIncidentInput.Type.IMAGE,
            quality = 20
        )

        runTest {
            val actual = executor.handleFileRequest(input)
            Assert.assertTrue(actual is NetworkResponse.Failure)
            verify { fileSplitter.toBase64("test_path_1", context) }
            coVerify { executor.postRequest("test_filename", request) }
        }
    }

    @Test
    fun `when postRequest and communicationManager post Success then return NetworkResponse Success `() {
        val request: NetworkRequest = mockk()
        val incidentId = "testIncidentId"
        val response = UploadLogIncidentResponse(success = true, errors = null, incidentId = incidentId)
        coEvery {
            communicationManager.post<UploadLogIncidentResponse>(any(), any())
        } returns NetworkResponse.Success(response)

        runTest {
            val actual = executor.postRequest("test_file_name", request)
            Assert.assertTrue(actual is NetworkResponse.Success)
            if (actual is NetworkResponse.Success) {
                Assert.assertEquals("test_file_name", actual.response.filename)
            }
            coVerify {
                communicationManager.post<UploadLogIncidentResponse>(request, MiddlewareCommunicationManager.MymToken)
            }
        }
    }

    @Test
    fun `when postRequest and communicationManager post Failure then return NetworkResponse Failure `() {
        val request: NetworkRequest = mockk()
        val errors = listOf("post_error")
        coEvery {
            communicationManager.post<UploadLogIncidentResponse>(any(), any())
        } returns NetworkResponse.Failure(PimsErrors.serverError(errors, "test_failed"))

        runTest {
            val actual = executor.postRequest("test_file_name", request)
            Assert.assertTrue(actual is NetworkResponse.Failure)
            coVerify {
                communicationManager.post<UploadLogIncidentResponse>(request, MiddlewareCommunicationManager.MymToken)
            }
        }
    }

    @Test
    fun `when handleImageRequest then return network response`() {
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.ByteArrayExtensionsKt")
        runTest {
            val response = NetworkResponse.Success(UploadLogIncidentOutput("test_filename"))
            val bytes = byteArrayOf(1, 2)
            val input = UploadLogIncidentInput(
                incidentId = "test_incidentId",
                path = "test_path",
                filename = "test_filename",
                type = UploadLogIncidentInput.Type.IMAGE,
                quality = 20
            )
            every { fileSplitter.handleUploadImage(any(), any(), any()) } returns bytes
            coEvery { executor.postRequest(any(), any()) } returns response

            executor.handleImageRequest(input)

            coVerify { fileSplitter.handleUploadImage(input.path, input.quality, context) }
            coVerify { executor.postRequest(input.filename, any()) }
        }
    }
}
