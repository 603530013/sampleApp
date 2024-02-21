package com.inetpsa.pims.spaceMiddleware.command.settings

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.settings.GetCallCenterSettingsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.GetSettingsDetailsPsaExecutor
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
@Deprecated("This should be replaced by GetSettingsCommandTest")
class GetSettingsCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: GetSettingsCommand

    @Before
    fun setup() {
        command = spyk(GetSettingsCommand())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetSettingsDetailsPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_SETTINGS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetSettingsDetailsPsaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType details then return GetCallCenterSettingsFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_SETTINGS

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetCallCenterSettingsFcaExecutor)
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
