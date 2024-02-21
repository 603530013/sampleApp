package com.inetpsa.pims.spaceMiddleware.executor.locations

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType.None
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.helper.emptyPlaceModel
import com.inetpsa.pims.spaceMiddleware.model.locations.TextSearchResponse
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetPlacesTextSearchExecutorTest : LocationExecutorTestHelper() {

    private lateinit var executor: GetPlacesTextSearchExecutor

    private val searchResponse = spyk(
        TextSearchResponse(
            results = listOf(emptyPlaceModel()),
            status = "OK",
            errorMessage = null,
            nextPageToken = null

        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetPlacesTextSearchExecutor(baseCommand))
    }

    @Test
    fun `when execute then return success result`() {
        val input = "testKeyword"
        every { executor.params(any()) } returns input

        coEvery { communicationManager.get<TextSearchResponse>(any(), any()) } returns
            NetworkResponse.Success(searchResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_QUERY to input,
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(TextSearchResponse::class.java),
                    urls = eq(arrayOf("place/textsearch/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<TextSearchResponse>(
                    request = any(),
                    tokenType = eq(None)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(searchResponse.results, success.response.places)
        }
    }

    @Test
    fun `when execute then return failure empty result`() {
        val input = "testKeyword"
        every { executor.params(any()) } returns input
        every { searchResponse.results } returns null

        coEvery { communicationManager.get<TextSearchResponse>(any(), any()) } returns
            NetworkResponse.Success(searchResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_QUERY to input,
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(TextSearchResponse::class.java),
                    urls = eq(arrayOf("place/textsearch/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<TextSearchResponse>(
                    request = any(),
                    tokenType = eq(None)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            val exception = PimsErrors.zeroResults()
            Assert.assertEquals(exception.code, failure.error?.code)
            Assert.assertEquals(exception.message, failure.error?.message)
        }
    }

    @Test
    fun `when execute then return failure server error result`() {
        val input = "testKeyword"
        every { executor.params(any()) } returns input
        every { searchResponse.results } returns null

        val serverErrorMsg = "testServerErrorMessage"
        every { executor.params(any()) } returns input
        every { searchResponse.status } returns "Failure"
        every { searchResponse.errorMessage } returns serverErrorMsg

        coEvery { communicationManager.get<TextSearchResponse>(any(), any()) } returns
            NetworkResponse.Success(searchResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_QUERY to input,
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(TextSearchResponse::class.java),
                    urls = eq(arrayOf("place/textsearch/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<TextSearchResponse>(
                    request = any(),
                    tokenType = eq(None)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            val exception = PimsErrors.locationServerError(serverErrorMsg)
            Assert.assertEquals(exception.code, failure.error?.code)
            Assert.assertEquals(exception.message, failure.error?.message)
        }
    }

    @Test
    fun `when execute params with the right config then return test response`() {
        val keyword = "testPlaceId"

        val input = mapOf(Constants.PARAM_KEYWORD to keyword)

        val param = executor.params(input)

        Assert.assertEquals(keyword, param)
    }

    @Test
    fun `when execute params with missing keyword then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_KEYWORD)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid keyword then throw missing parameter`() {
        val inputParam = 123
        val input = mapOf(Constants.PARAM_KEYWORD to inputParam)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_KEYWORD)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
