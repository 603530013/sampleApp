package com.inetpsa.pims.spaceMiddleware.executor.radioplayer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.onair.OnAirInfoPsaRp
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.onair.OnAirInfoPsaRp.Show
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.onair.OnAirInfoPsaRp.Song
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.onair.OnAirListPsaRp
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

@Deprecated("We should use GetOnAirPsaExecutorTest instead")
internal class GetOnAirPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetOnAirPsaExecutor

    private val onAirInfo = OnAirListPsaRp(
        data = listOf(
            OnAirInfoPsaRp(
                show = Show("show name 1"),
                song = null
            ),
            OnAirInfoPsaRp(
                show = null,
                song = Song(
                    artist = "song artist 2",
                    name = "song name 2"
                )
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetOnAirPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val params = "2503"
        every { executor.params(any()) } returns params
        coEvery { communicationManager.get<OnAirListPsaRp>(any(), any()) } returns Success(onAirInfo)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(OnAirListPsaRp::class.java),
                    urls = eq(arrayOf("/shop/v1/stations/", params, "/onair")),
                    headers = null,
                    queries = null,
                    body = null
                )
            }

            coVerify {
                communicationManager.get<OnAirListPsaRp>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = (response as Success).response
            Assert.assertEquals(success.data?.size, 2)
        }
    }

    @Test
    fun `when execute params with the right country then return country`() {
        val rpuid = "2503"
        val input = mapOf(Constants.PARAM_RPUID to rpuid)
        val param = executor.params(input)

        Assert.assertEquals(rpuid, param)
    }

    @Test
    fun `when execute params with missing country then throw an exception`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_RPUID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val rpuid = 123
        val input = mapOf(Constants.PARAM_RPUID to rpuid)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_RPUID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
