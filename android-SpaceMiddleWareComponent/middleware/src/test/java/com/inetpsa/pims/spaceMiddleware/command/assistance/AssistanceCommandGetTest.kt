package com.inetpsa.pims.spaceMiddleware.command.assistance

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetAssistanceFaqPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetAssistancePSAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetAssistancePhonesFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetAssistancePhonesPsaExecutor
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

class AssistanceCommandGetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: AssistanceCommandGet

    @Before
    fun setup() {
        command = spyk(AssistanceCommandGet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetAssistancePSAExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_VEHICLE_DETAILS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetAssistancePSAExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetAssistanceFaqPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.FAQ

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetAssistanceFaqPsaExecutor)
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
    fun `when look for PSA executor with  actionType faq then return GetAssistanceFaqPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.FAQ

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetAssistanceFaqPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with  actionType callcenters then return GetAssistancePhonesPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.CALL_CENTERS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetAssistancePhonesPsaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with  actionType callcenters then return GetAssistancePhonesFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.CALL_CENTERS

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetAssistancePhonesFcaExecutor)
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
