package com.inetpsa.pims.spaceMiddleware.command.radioplayer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get.GetOnAirPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get.GetRadioStationsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get.GetRecommendationsPsaExecutor
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RadioPlayerCommandGetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: RadioPlayerCommandGet

    @Before
    fun setup() {
        command = spyk(RadioPlayerCommandGet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType stations then return GetRadioStationsPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.STATIONS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetRadioStationsPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetOnAirPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Constants.Input.Action.ON_AIR)
            every { command.actionType } returns Constants.Input.ActionType.STATIONS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetOnAirPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType recommendations then return GetRecommendationsPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.RECOMMENDATIONS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetRecommendationsPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with action different than onAir then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(Constants.Input.ACTION to "test_action")
            every { command.actionType } returns Constants.Input.ActionType.STATIONS

            val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)

            try {
                command.getPsaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for PSA executor with no existing actionType then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns "stations_test"
            val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)

            try {
                command.getPsaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for FCA executor with no existing actionType then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.STATIONS
            val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)

            try {
                command.getFcaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }
}
