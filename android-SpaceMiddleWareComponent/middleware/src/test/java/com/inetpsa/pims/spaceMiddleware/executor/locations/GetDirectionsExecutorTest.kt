package com.inetpsa.pims.spaceMiddleware.executor.locations

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionParams
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionsResponse
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionsRoute
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionsRoute.Bounds
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionsRoute.DirectionsPolyline
import com.inetpsa.pims.spaceMiddleware.model.locations.DirectionsRoute.LatLngLiteral
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

internal class GetDirectionsExecutorTest : LocationExecutorTestHelper() {

    private lateinit var executor: GetDirectionsExecutor

    private val directionsResponse = spyk(
        DirectionsResponse(
            routes = listOf(
                DirectionsRoute(
                    bounds = Bounds(
                        northeast = LatLngLiteral(
                            lat = 0.0,
                            lng = 0.0
                        ),
                        southwest = LatLngLiteral(
                            lat = 0.0,
                            lng = 0.0
                        )
                    ),
                    copyrights = "",
                    legs = listOf(),
                    overviewPolyline = DirectionsPolyline(
                        points = ""
                    ),
                    summary = "",
                    warnings = listOf(),
                    waypointOrder = listOf(),
                    fare = null
                )
            ),
            status = Constants.STATUS_OK,
            availableTravelModes = listOf(),
            errorMessage = null,
            geocodedWaypoints = listOf()
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetDirectionsExecutor(baseCommand))
    }

    @Test
    fun `when execute then return success result`() {
        val input = DirectionParams(
            DirectionParams.LatLng(10.123, 10.124),
            DirectionParams.LatLng(10.125, 10.126)
        )
        every { executor.params(any()) } returns input

        coEvery { communicationManager.get<DirectionsResponse>(any(), any()) } returns
            NetworkResponse.Success(directionsResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_ORIGIN to input.start.toString(),
            Constants.QUERY_PARAM_KEY_DESTINATION to input.end.toString(),
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(DirectionsResponse::class.java),
                    urls = eq(arrayOf("directions/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<DirectionsResponse>(
                    request = any(),
                    tokenType = eq(TokenType.None)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(directionsResponse.routes?.size, success.response.directions.size)
        }
    }

    @Test
    fun `when execute then return failure empty result`() {
        val input = DirectionParams(
            DirectionParams.LatLng(10.123, 10.124),
            DirectionParams.LatLng(10.125, 10.126)
        )
        every { executor.params(any()) } returns input
        every { directionsResponse.routes } returns null

        coEvery { communicationManager.get<DirectionsResponse>(any(), any()) } returns
            NetworkResponse.Success(directionsResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_ORIGIN to input.start.toString(),
            Constants.QUERY_PARAM_KEY_DESTINATION to input.end.toString(),
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(DirectionsResponse::class.java),
                    urls = eq(arrayOf("directions/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<DirectionsResponse>(
                    request = any(),
                    tokenType = eq(TokenType.None)
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
        val input = DirectionParams(
            DirectionParams.LatLng(10.123, 10.124),
            DirectionParams.LatLng(10.125, 10.126)
        )
        val serverErrorMsg = "testServerErrorMessage"
        every { executor.params(any()) } returns input
        every { directionsResponse.status } returns "Failure"
        every { directionsResponse.errorMessage } returns serverErrorMsg

        coEvery { communicationManager.get<DirectionsResponse>(any(), any()) } returns
            NetworkResponse.Success(directionsResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_ORIGIN to input.start.toString(),
            Constants.QUERY_PARAM_KEY_DESTINATION to input.end.toString(),
            Constants.QUERY_PARAM_KEY_KEY to executor.googleApiKey
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(DirectionsResponse::class.java),
                    urls = eq(arrayOf("directions/json")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<DirectionsResponse>(
                    request = any(),
                    tokenType = eq(TokenType.None)
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
        val startLatitude = 10.123
        val startLongitude = 10.124
        val endLatitude = 10.125
        val endLongitude = 10.126

        val input = mapOf(
            Constants.PARAM_ORIGIN_LATITUDE to startLatitude,
            Constants.PARAM_ORIGIN_LONGITUDE to startLongitude,
            Constants.PARAM_DIRECTION_LATITUDE to endLatitude,
            Constants.PARAM_DIRECTION_LONGITUDE to endLongitude
        )

        val param = executor.params(input)

        Assert.assertEquals(startLatitude, param.start.latitude, 0.0001)
        Assert.assertEquals(startLongitude, param.start.longitude, 0.0001)
        Assert.assertEquals(endLatitude, param.end.latitude, 0.0001)
        Assert.assertEquals(endLongitude, param.end.longitude, 0.0001)
    }

    @Test
    fun `when execute params with missing start latitude then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_ORIGIN_LATITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid start latitude then throw missing parameter`() {
        val inputParam = "123"
        val input = mapOf(Constants.PARAM_ORIGIN_LATITUDE to inputParam)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ORIGIN_LATITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing start longitude then throw missing parameter`() {
        val input = mapOf(Constants.PARAM_ORIGIN_LATITUDE to 10.123)
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_ORIGIN_LONGITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid start longitude then throw missing parameter`() {
        val inputParam = "123"
        val input = mapOf(
            Constants.PARAM_ORIGIN_LATITUDE to 10.123,
            Constants.PARAM_ORIGIN_LONGITUDE to inputParam
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ORIGIN_LONGITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing end latitude then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_ORIGIN_LATITUDE to 10.123,
            Constants.PARAM_ORIGIN_LONGITUDE to 10.123
        )
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_DIRECTION_LATITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid end latitude then throw missing parameter`() {
        val inputParam = "123"
        val input = mapOf(
            Constants.PARAM_ORIGIN_LATITUDE to 10.123,
            Constants.PARAM_ORIGIN_LONGITUDE to 10.123,
            Constants.PARAM_DIRECTION_LATITUDE to inputParam
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_DIRECTION_LATITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing end longitude then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_ORIGIN_LATITUDE to 10.123,
            Constants.PARAM_ORIGIN_LONGITUDE to 10.123,
            Constants.PARAM_DIRECTION_LATITUDE to 10.123
        )
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_DIRECTION_LONGITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid end longitude then throw missing parameter`() {
        val inputParam = "123"
        val input = mapOf(
            Constants.PARAM_ORIGIN_LATITUDE to 10.123,
            Constants.PARAM_ORIGIN_LONGITUDE to 10.124,
            Constants.PARAM_DIRECTION_LATITUDE to 10.125,
            Constants.PARAM_DIRECTION_LONGITUDE to inputParam
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_DIRECTION_LONGITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
