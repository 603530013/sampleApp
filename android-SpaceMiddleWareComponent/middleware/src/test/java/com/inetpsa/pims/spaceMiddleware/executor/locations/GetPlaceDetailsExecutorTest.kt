package com.inetpsa.pims.spaceMiddleware.executor.locations

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.TokenType.None
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.helper.emptyPlaceModel
import com.inetpsa.pims.spaceMiddleware.model.locations.PlaceDetailsResponse
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

internal class GetPlaceDetailsExecutorTest : LocationExecutorTestHelper() {

    private lateinit var executor: GetPlaceDetailsExecutor

    private val placeResponse = spyk(
        PlaceDetailsResponse(
            result = emptyPlaceModel(),
            status = "OK",
            errorMessage = null,
            nextPageToken = null
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetPlaceDetailsExecutor(baseCommand))
    }

    @Test
    fun `when execute then return success result`() {
        val input = "testPlaceId"
        every { executor.params(any()) } returns input

        coEvery { communicationManager.get<PlaceDetailsResponse>(any(), any()) } returns
            NetworkResponse.Success(placeResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_PLACE_ID to input,
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(PlaceDetailsResponse::class.java),
                    urls = eq(arrayOf("place/details/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<PlaceDetailsResponse>(
                    request = any(),
                    tokenType = eq(None)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(placeResponse.result, success.response)
        }
    }

    @Test
    fun `when execute then return failure empty result`() {
        val input = "testPlaceId"
        every { executor.params(any()) } returns input
        every { placeResponse.result } returns null

        coEvery { communicationManager.get<PlaceDetailsResponse>(any(), any()) } returns
            NetworkResponse.Success(placeResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_PLACE_ID to input,
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(PlaceDetailsResponse::class.java),
                    urls = eq(arrayOf("place/details/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<PlaceDetailsResponse>(
                    request = any(),
                    tokenType = eq(None)
                )
            }

            Assert.assertEquals(true, response is Failure)
            val failure = response as Failure
            val exception = PimsErrors.zeroResults()
            Assert.assertEquals(exception.code, failure.error?.code)
            Assert.assertEquals(exception.message, failure.error?.message)
        }
    }

    @Test
    fun `when execute then return failure server error result`() {
        val input = "testPlaceId"
        every { executor.params(any()) } returns input
        every { placeResponse.result } returns null

        val serverErrorMsg = "testServerErrorMessage"
        every { executor.params(any()) } returns input
        every { placeResponse.status } returns "Failure"
        every { placeResponse.errorMessage } returns serverErrorMsg

        coEvery { communicationManager.get<PlaceDetailsResponse>(any(), any()) } returns
            NetworkResponse.Success(placeResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_PLACE_ID to input,
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(PlaceDetailsResponse::class.java),
                    urls = eq(arrayOf("place/details/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<PlaceDetailsResponse>(
                    request = any(),
                    tokenType = eq(None)
                )
            }

            Assert.assertEquals(true, response is Failure)
            val failure = response as Failure
            val exception = PimsErrors.locationServerError(serverErrorMsg)
            Assert.assertEquals(exception.code, failure.error?.code)
            Assert.assertEquals(exception.message, failure.error?.message)
        }
    }

    @Test
    fun `when execute params with the right config then return test response`() {
        val placeId = "testPlaceId"

        val input = mapOf(Constants.PARAM_PLACE_ID to placeId)

        val param = executor.params(input)

        Assert.assertEquals(placeId, param)
    }

    @Test
    fun `when execute params with missing place ID then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_PLACE_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid place ID then throw missing parameter`() {
        val inputParam = 123
        val input = mapOf(Constants.PARAM_PLACE_ID to inputParam)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_PLACE_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
