package com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.OnAirOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.RadioPlayerOnAirResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class GetOnAirPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetOnAirPsaExecutor

    private val response = RadioPlayerOnAirResponse(
        stations = listOf(
            RadioPlayerOnAirResponse.OnAirItem(
                show = RadioPlayerOnAirResponse.OnAirItem.Show("show name 1"),
                song = null
            ),
            RadioPlayerOnAirResponse.OnAirItem(
                show = null,
                song = RadioPlayerOnAirResponse.OnAirItem.Song(
                    artist = "song artist 2",
                    name = "song name 2"
                )
            )
        )
    )

    private val result = OnAirOutput(
        stations = listOf(
            OnAirOutput.OnAirItem(
                showName = "show name 1",
                songName = null,
                artist = null
            ),
            OnAirOutput.OnAirItem(
                showName = null,
                songName = "song name 2",
                artist = "song artist 2"
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
        coEvery { communicationManager.get<RadioPlayerOnAirResponse>(any(), any()) } returns Success(response)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(RadioPlayerOnAirResponse::class.java),
                    urls = eq(arrayOf("/shop/v1/stations/", params, "/onair")),
                    headers = null,
                    queries = null,
                    body = null
                )
            }

            coVerify {
                communicationManager.get<RadioPlayerOnAirResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            assertEquals(true, response is Success)
            val success = (response as Success).response
            assertEquals(result.stations?.size, success.stations?.size)
            assertEquals(result.stations, success.stations)
        }
    }

    @Test
    fun `when execute params with the right country then return country`() {
        val rpuid = "2503"
        val input = mapOf(Constants.PARAM_ID to rpuid)
        val param = executor.params(input)

        Assert.assertEquals(rpuid, param)
    }

    @Test
    fun `when execute params with missing country then throw an exception`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_ID)
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
        val input = mapOf(Constants.PARAM_ID to rpuid)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
