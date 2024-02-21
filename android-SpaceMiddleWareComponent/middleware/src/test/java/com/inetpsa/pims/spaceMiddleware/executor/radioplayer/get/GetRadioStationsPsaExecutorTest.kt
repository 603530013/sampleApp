package com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
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

internal class GetRadioStationsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetRadioStationsPsaExecutor

    private val response = RadioPlayerStationsResponse(
        stations = listOf(
            RadioPlayerStationsResponse.StationsItem(
                country = "testCountry",
                multimedia = listOf(
                    RadioPlayerStationsResponse.StationsItem.MultimediaItem(
                        width = 100,
                        url = "testUrlLarge",
                        height = 200
                    ),
                    RadioPlayerStationsResponse.StationsItem.MultimediaItem(
                        width = 50,
                        url = "testUrlSmall",
                        height = 100
                    )
                ),
                relevanceIndex = 1,
                name = "testName",
                description = "testDescription",
                rpuid = "testRpuid",
                liveStreams = listOf(
                    RadioPlayerStationsResponse.StationsItem.LiveStreamsItem(
                        streamSource = RadioPlayerStationsResponse.StationsItem.LiveStreamsItem.StreamSource(
                            mimeValue = "testMimevalue",
                            url = "testUrlLowQuality"
                        ),
                        bitRate = RadioPlayerStationsResponse.StationsItem.LiveStreamsItem.BitRate(target = 1200)
                    ),
                    RadioPlayerStationsResponse.StationsItem.LiveStreamsItem(
                        streamSource = RadioPlayerStationsResponse.StationsItem.LiveStreamsItem.StreamSource(
                            mimeValue = "testMimevalue",
                            url = "testUrlHighQuality"
                        ),
                        bitRate = RadioPlayerStationsResponse.StationsItem.LiveStreamsItem.BitRate(target = 10200)
                    )
                )
            )
        )
    )

    private val result = StationsOutput(
        stations = listOf(
            StationsOutput.Station(
                id = "testRpuid",
                name = "testName",
                description = "testDescription",
                country = "testCountry",
                relevanceIndex = 1,
                multimedia = listOf(
                    StationsOutput.Station.Multimedia(
                        width = 50,
                        url = "testUrlSmall",
                        height = 100
                    ),
                    StationsOutput.Station.Multimedia(
                        width = 100,
                        url = "testUrlLarge",
                        height = 200
                    )
                ),
                liveStreams = listOf(
                    StationsOutput.Station.LiveStream(
                        mime = "testMimevalue",
                        url = "testUrlHighQuality",
                        bitRate = 10200
                    ),
                    StationsOutput.Station.LiveStream(
                        mime = "testMimevalue",
                        url = "testUrlLowQuality",
                        bitRate = 1200
                    )
                )
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetRadioStationsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val country = "FR"
        every { executor.params(any()) } returns country
        coEvery { communicationManager.get<RadioPlayerStationsResponse>(any(), any()) } returns Success(response)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(RadioPlayerStationsResponse::class.java),
                    urls = eq(arrayOf("/shop/v1/stations")),
                    headers = null,
                    queries = mapOf(Constants.PARAM_COUNTRY to country),
                    body = null
                )
            }

            coVerify {
                communicationManager.get<RadioPlayerStationsResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            Assert.assertEquals(true, response is Success)
            val success = (response as Success).response
            Assert.assertEquals(result.stations?.size, success.stations?.size)
            Assert.assertEquals(result.stations, success.stations)
        }
    }

    @Test
    fun `when execute params with the right country then return country`() {
        val country = "FR"
        val input = mapOf(Constants.PARAM_COUNTRY to country)
        val param = executor.params(input)

        Assert.assertEquals(country, param)
    }

    @Test
    fun `when execute params with missing country then throw an exception`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_COUNTRY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val country = 123
        val input = mapOf(Constants.PARAM_COUNTRY to country)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_COUNTRY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
