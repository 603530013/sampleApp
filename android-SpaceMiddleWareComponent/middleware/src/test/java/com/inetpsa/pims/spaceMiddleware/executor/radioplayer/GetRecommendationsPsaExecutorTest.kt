package com.inetpsa.pims.spaceMiddleware.executor.radioplayer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.recommendations.GetRecommendationListPsaRpParams
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

@Deprecated("This will be replaced by GetRadioPlayerRecommendationsPsaExecutorTest")
internal class GetRecommendationsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetRecommendationsPsaExecutor

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
        executor = spyk(GetRecommendationsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val params = GetRecommendationListPsaRpParams(
            country = "FR",
            rpuid = "",
            factors = "TRENDING",
            latitude = null,
            longitude = null
        )
        every { executor.params(any()) } returns params
        coEvery { communicationManager.get<StationListPsaRp>(any(), any()) } returns Success(
            radioStationList
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(StationListPsaRp::class.java),
                    urls = eq(arrayOf("/shop/v1/recommendations")),
                    headers = null,
                    queries = eq(
                        mapOf(
                            Constants.PARAM_COUNTRY to params.country,
                            Constants.PARAM_FACTORS to params.factors
                        )
                    ),
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
            Assert.assertEquals(radioStationList.stations?.size, (response as Success).response.stations?.size)
        }
    }

    @Test
    fun `when execute params with the right country then return country`() {
        val testParams = GetRecommendationListPsaRpParams(
            country = "FR",
            rpuid = "2503",
            factors = "TRENDING",
            latitude = null,
            longitude = null
        )
        val input = mapOf(
            Constants.PARAM_COUNTRY to testParams.country,
            Constants.PARAM_RPUID to testParams.rpuid,
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
            Constants.PARAM_LNG to 10f
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
            Constants.PARAM_LAT to 40f,
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
            Constants.PARAM_LAT to 40f,
            Constants.PARAM_LNG to 10f
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
            Constants.PARAM_LAT to 40f,
            Constants.PARAM_LNG to 10f
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
            Constants.PARAM_LAT to 40f,
            Constants.PARAM_LNG to 10f
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
            Constants.PARAM_LNG to 10f
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
            Constants.PARAM_LAT to 40f,
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
