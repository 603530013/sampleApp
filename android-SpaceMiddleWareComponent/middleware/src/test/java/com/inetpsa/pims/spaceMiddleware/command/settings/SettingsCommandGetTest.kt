package com.inetpsa.pims.spaceMiddleware.command.settings

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetAppPrivacyFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetAppTermsFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetAppTermsPSAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetBcallContactFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetConnectedPrivacyFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetConnectedTermsFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetCurrentLanguageExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetEcallContactFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetLanguageListExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetPaymentListPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetRoadSideContactFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetRoadSideContactPSAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetWebTermsFCAExecutor
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

class SettingsCommandGetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: SettingsCommandGet

    @Before
    fun setup() {
        command = spyk(SettingsCommandGet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType contact then return GetRoadSideContactPSAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.CONTACT,
                Constants.Input.ACTION to Constants.Input.Action.ROADSIDE
            )

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetRoadSideContactPSAExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType PAYMENT then return GetPaymentListPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.PAYMENT,
                Constants.Input.ACTION to Constants.Input.Action.LIST
            )

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetPaymentListPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType contact then return GetCGUContentPSAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APP_TERMS
            )

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetAppTermsPSAExecutor)
        }
    }

    @Test
    fun `when look for common executor with actionType language then return GetLanguageListExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.LANGUAGE,
                Constants.Input.ACTION to Constants.Input.Action.LIST
            )

            val executor = command.getCommonExecutor()
            Assert.assertEquals(true, executor is GetLanguageListExecutor)
        }
    }

    @Test
    fun `when look for common executor with no existing actionType then throw null value`() {
        runTest {
            every { command.actionType } returns "details_test"
            val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)

            try {
                command.getCommonExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for common executor with actionType language then return GetCurrentLanguageExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.LANGUAGE,
                Constants.Input.ACTION to Constants.Input.Action.CURRENT
            )

            val executor = command.getCommonExecutor()
            Assert.assertEquals(true, executor is GetCurrentLanguageExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType contact then return GetRoadSideContactFCAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.CONTACT,
                Constants.Input.ACTION to Constants.Input.Action.ROADSIDE
            )

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetRoadSideContactFCAExecutor)
        }
    }

    @Test
    fun `when look for Fca executor with actionType contact then return GetEcallContactFcaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.CONTACT,
                Constants.Input.ACTION to Constants.Input.Action.E_CALL
            )

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetEcallContactFcaExecutor)
        }
    }

    @Test
    fun `when look for Fca executor with actionType contact then return GetBcallContactFcaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.CONTACT,
                Constants.Input.ACTION to Constants.Input.Action.B_CALL
            )

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetBcallContactFcaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with no existing actionType then throw PimsError exception`() {
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
    fun `when look for FCA executor with no existing actionType then throw PimsError exception`() {
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

    @Test
    fun `when look for PSA executor with no existing action then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.CONTACT,
                Constants.Input.ACTION to "testAction"
            )
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
    fun `when look for FCA executor with no existing action then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.CONTACT,
                Constants.Input.ACTION to "testAction"
            )
            val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)

            try {
                command.getFcaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for FCA executor with actionType AppTerms then return GetAppTermsFCAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APP_TERMS
            )

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetAppTermsFCAExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType WebTerms then return GetWebTermsFCAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.WEB_TERMS
            )

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetWebTermsFCAExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType connectedTC then return GetConnectedTermsFCAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.CONNECTED_TERMS
            )

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetConnectedTermsFCAExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType connectedPP then return GetConnectedPrivacyFCAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.CONNECTED_PRIVACY
            )

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetConnectedPrivacyFCAExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType connectedPP then return GetAppPrivacyFCAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APP_PRIVACY
            )

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetAppPrivacyFCAExecutor)
        }
    }

    @Test
    fun `when look for Common executor with actionType invalid then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to "testActionType"
            )
            val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)

            try {
                command.getCommonExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for Common executor with action invalid then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.LANGUAGE,
                Constants.Input.ACTION to "testAction"
            )
            val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)

            try {
                command.getCommonExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for PSA executor with no invalid action then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.PAYMENT,
                Constants.Input.ACTION to "1234$$"
            )
            val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)

            try {
                command.getPsaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }
}
