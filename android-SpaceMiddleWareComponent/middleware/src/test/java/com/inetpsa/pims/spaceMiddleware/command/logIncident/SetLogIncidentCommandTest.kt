package com.inetpsa.pims.spaceMiddleware.command.logIncident

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.logIncident.CreateLogIncidentPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.logIncident.UploadLogIncidentPsaExecutor
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SetLogIncidentCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: SetLogIncidentCommand

    @Before
    fun setup() {
        command = spyk(SetLogIncidentCommand())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType enroll then return CreateLogIncidentPsaExecutor`() {
        runBlockingTest {
            every { command.actionType } returns Constants.PARAM_ACTION_VEHICLE_ENROLL

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is CreateLogIncidentPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType enroll then return UploadLogIncidentPsaExecutor`() {
        runBlockingTest {
            every { command.actionType } returns Constants.PARAM_ACTION_VEHICLE_UPLOAD

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is UploadLogIncidentPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with no existing actionType then throw PimsError exception`() {
        runBlockingTest {
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
        runBlockingTest {
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
