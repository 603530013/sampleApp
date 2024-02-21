package com.inetpsa.pims.spaceMiddleware.command.account

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.account.GetProfileFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.account.GetProfilePsaExecutor
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

@Deprecated("this should be replaced with UserCommandGetTest")
class AccountCommandGetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: AccountCommandGet

    @Before
    fun setup() {
        command = spyk(AccountCommandGet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetProfilePsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAMS_KEY_PROFILE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetProfilePsaExecutor)
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
    fun `when look for FCA executor with actionType details then return GetProfileFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAMS_KEY_PROFILE

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetProfileFcaExecutor)
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
