package com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.RecommendationsInput
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.StationsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.RadioPlayerStationsResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetRecommendationsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetRecommendationsPsaExecutor

    private val response = RadioPlayerStationsResponse(
        stations = listOf(
            RadioPlayerStationsResponse.StationsItem(
                country = "testCountry",
                multimedia = listOf(
                    RadioPlayerStationsResponse.StationsItem.MultimediaItem(
                        width = 100,
                        url = "testUrl",
                        height = 500
                    )
                ),
                relevanceIndex = 1,
                name = "testName",
                description = "testDescription",
                rpuid = "testRpuid",
                liveStreams = listOf(
                    RadioPlayerStationsResponse.StationsItem.LiveStreamsItem(
                        streamSource = RadioPlayerStationsResponse.StationsItem.LiveStreamsItem.StreamSource(
                            mimeValue = "testMimeValue",
                            url = "testUrl"
                        ),
                        bitRate = RadioPlayerStationsResponse.StationsItem.LiveStreamsItem.BitRate(
                            target = 100
                        )
                    )
                )
            )
        )
    )

    private val result = StationsOutput(
        stations = listOf(
            StationsOutput.Station(
                country = "testCountry",
                multimedia = listOf(
                    StationsOutput.Station.Multimedia(
                        width = 100,
                        url = "testUrl",
                        height = 500
                    )
                ),
                relevanceIndex = 1,
                name = "testName",
                description = "testDescription",
                id = "testRpuid",
                liveStreams = listOf(
                    StationsOutput.Station.LiveStream(
                        mime = "testMimeValue",
                        url = "testUrl",
                        bitRate = 100
                    )
                )
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetRecommendationsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a GET API call`() {
        val params = RecommendationsInput(
            country = "FR",
            id = "",
            factors = "TRENDING",
            latitude = null,
            longitude = null
        )

        every { executor.params(any()) } returns params
        coEvery { communicationManager.get<RadioPlayerStationsResponse>(any(), any()) } returns
            NetworkResponse.Success(response)
        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(RadioPlayerStationsResponse::class.java),
                    urls = eq(arrayOf("/shop/v1/recommendations")),
                    queries = eq(
                        mapOf(
                            Constants.PARAM_COUNTRY to params.country,
                            Constants.PARAM_FACTORS to params.factors
                        )
                    )
                )
            }

            coVerify {
                communicationManager.get<RadioPlayerStationsResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(result.stations?.size, success.response.stations?.size)
        }
    }

    @Test
    fun `when execute params with the right country then return country`() {
        val testParams = RecommendationsInput(
            country = "FR",
            id = "2503",
            factors = "TRENDING",
            latitude = 41.9266916,
            longitude = 12.5202162
        )
        val input = mapOf(
            Constants.PARAM_COUNTRY to testParams.country,
            Constants.PARAM_ID to testParams.id,
            Constants.PARAM_FACTORS to testParams.factors,
            Constants.PARAM_LAT to testParams.latitude,
            Constants.PARAM_LNG to testParams.longitude
        )
        val param = executor.params(input)

        Assert.assertEquals(testParams, param)
    }

    @Test
    fun `when execute params with missing required value then throw an exception`() {
        var input = mapOf(
            Constants.PARAM_COUNTRY to null,
            Constants.PARAM_RPUID to "2503",
            Constants.PARAM_FACTORS to "TRENDING"
        )
        var exception = PIMSFoundationError.invalidParameter(Constants.PARAM_COUNTRY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }

        input = mapOf(
            Constants.PARAM_COUNTRY to "FR",
            Constants.PARAM_RPUID to null,
            Constants.PARAM_FACTORS to "TRENDING"
        )
        exception = PIMSFoundationError.invalidParameter(Constants.PARAM_RPUID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }

        input = mapOf(
            Constants.PARAM_COUNTRY to "FR",
            Constants.PARAM_RPUID to "2503",
            Constants.PARAM_FACTORS to null
        )
        exception = PIMSFoundationError.invalidParameter(Constants.PARAM_FACTORS)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with factor GEO without coordinates then throw an exception`() {
        var input = mapOf(
            Constants.PARAM_COUNTRY to "FR",
            Constants.PARAM_RPUID to "2503",
            Constants.PARAM_FACTORS to "GEO",
            Constants.PARAM_LAT to null,
            Constants.PARAM_LNG to 12.5202162
        )
        var exception = PIMSFoundationError.missingParameter(Constants.PARAM_LAT)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }

        input = mapOf(
            Constants.PARAM_COUNTRY to "FR",
            Constants.PARAM_RPUID to "2503",
            Constants.PARAM_FACTORS to "GEO",
            Constants.PARAM_LAT to 41.9266916,
            Constants.PARAM_LNG to null
        )
        exception = PIMSFoundationError.missingParameter(Constants.PARAM_LNG)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid country then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_COUNTRY to 123,
            Constants.PARAM_RPUID to "2503",
            Constants.PARAM_FACTORS to "GEO",
            Constants.PARAM_LAT to 41.9266916,
            Constants.PARAM_LNG to 12.5202162
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_COUNTRY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid rpuid then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_COUNTRY to "FR",
            Constants.PARAM_RPUID to 123,
            Constants.PARAM_FACTORS to "GEO",
            Constants.PARAM_LAT to 41.9266916,
            Constants.PARAM_LNG to 12.5202162
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_RPUID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid factors then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_COUNTRY to "FR",
            Constants.PARAM_RPUID to "2503",
            Constants.PARAM_FACTORS to 123,
            Constants.PARAM_LAT to 41.9266916,
            Constants.PARAM_LNG to 12.5202162
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_FACTORS)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid latitude then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_COUNTRY to "FR",
            Constants.PARAM_RPUID to "2503",
            Constants.PARAM_FACTORS to "GEO",
            Constants.PARAM_LAT to "asd",
            Constants.PARAM_LNG to 12.5202162
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_LAT)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid longitude then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_COUNTRY to "FR",
            Constants.PARAM_RPUID to "2503",
            Constants.PARAM_FACTORS to "GEO",
            Constants.PARAM_LAT to 41.9266916,
            Constants.PARAM_LNG to "asd"
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_LNG)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
