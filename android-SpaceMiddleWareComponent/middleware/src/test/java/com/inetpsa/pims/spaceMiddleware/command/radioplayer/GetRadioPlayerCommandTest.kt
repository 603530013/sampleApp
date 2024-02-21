package com.inetpsa.pims.spaceMiddleware.command.radioplayer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.GetOnAirPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.GetRecommendationsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.GetStationsPsaExecutor
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

@Deprecated("We should use RadioPlayerCommandGetTest instead")
class GetRadioPlayerCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: GetRadioPlayerCommand

    @Before
    fun setup() {
        command = spyk(GetRadioPlayerCommand())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetStationsPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_RP_STATIONS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetStationsPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetRecommendationsPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_RP_RECOMMENDATIONS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetRecommendationsPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetOnAirPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_RP_ON_AIR

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetOnAirPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with no existing actionType then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns "details_test"
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
            every { command.actionType } returns "details_test"
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
