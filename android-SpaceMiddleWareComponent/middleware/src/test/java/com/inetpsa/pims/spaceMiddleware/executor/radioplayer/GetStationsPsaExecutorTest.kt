package com.inetpsa.pims.spaceMiddleware.executor.radioplayer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations.StationListPsaRp
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations.StationPsaRp
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations.StationPsaRp.BitRate
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations.StationPsaRp.LiveStream
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations.StationPsaRp.Multimedia
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations.StationPsaRp.StreamSource
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

@Deprecated("We should use GetRadioStationsPsaExecutorTest instead")
internal class GetStationsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetStationsPsaExecutor

    private val radioStationList = StationListPsaRp(
        stations = listOf(
            StationPsaRp(
                name = "France Inter",
                description = "Joyeuse, savante et populaire, France Inter est la radio généraliste de service public",
                liveStreams = listOf(
                    LiveStream(
                        bitRate = BitRate(128000),
                        streamSource = StreamSource(
                            "https://icecast.radiofrance.fr/franceinter-midfi.mp3?id=radioplayer",
                            "application/vnd.apple.mpegurl"
                        )
                    )
                ),
                multimedia = listOf(
                    Multimedia(
                        url = "https://assets.radioplayer.org/250/2503/1600/1200/kyeqs5cu.png",
                        width = 220,
                        height = 1200
                    )
                ),
                country = "FR",
                rpuid = "2503",
                relevanceIndex = 165
            ),
            StationPsaRp(
                name = "franceinfo",
                description = "L'actualité en direct et en continu avec le média global du service public",
                liveStreams = listOf(
                    LiveStream(
                        bitRate = BitRate(128000),
                        streamSource = StreamSource(
                            "https://icecast.radiofrance.fr/franceinfo-midfi.mp3?id=radioplayer",
                            "application/vnd.apple.mpegurl"
                        )
                    )
                ),
                multimedia = listOf(
                    Multimedia(
                        url = "https://assets.radioplayer.org/250/2504/1600/1200/l02a2r46.png",
                        width = 1600,
                        height = 1200
                    )
                ),
                country = "FR",
                rpuid = "2504",
                relevanceIndex = 58
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetStationsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val country = "FR"
        every { executor.params(any()) } returns country
        coEvery { communicationManager.get<StationListPsaRp>(any(), any()) } returns Success(
            radioStationList
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(StationListPsaRp::class.java),
                    urls = eq(arrayOf("/shop/v1/stations")),
                    headers = null,
                    queries = mapOf(Constants.PARAM_COUNTRY to country),
                    body = null
                )
            }

            coVerify {
                communicationManager.get<StationListPsaRp>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = (response as Success).response
            Assert.assertEquals(radioStationList.stations?.size, success.stations?.size)
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
