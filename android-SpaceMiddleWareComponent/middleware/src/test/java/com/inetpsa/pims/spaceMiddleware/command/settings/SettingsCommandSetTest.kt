package com.inetpsa.pims.spaceMiddleware.command.settings

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.settings.set.SetLanguageExecutor
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup
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

class SettingsCommandSetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private val configureManager: ConfigurationManager = mockk()
    private lateinit var command: SettingsCommandSet

    @Before
    fun setup() {
        command = spyk(SettingsCommandSet())
        every { command.middlewareComponent } returns middlewareComponent
        every { middlewareComponent.configurationManager } returns configureManager
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for common executor with actionType profile then return SetLanguageExecutor`() {
        runTest {
            every { configureManager.brandGroup } returns BrandGroup.PSA
            every { command.actionType } returns Constants.Input.ActionType.LANGUAGE

            val executor = command.getCommonExecutor()
            Assert.assertEquals(true, executor is SetLanguageExecutor)
        }
    }

    @Test
    fun `when look for common executor with no existing actionType then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns "details_test"
            val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)

            try {
                command.getCommonExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for PSA executor then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns "details_test"
            val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)

            try {
                command.getPsaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for FCA executor then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns "details_test"
            val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)

            try {
                command.getFcaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }
}
