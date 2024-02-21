package com.inetpsa.pims.spaceMiddleware.command.user

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.user.DeleteAccountFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.DeleteAccountPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.SetProfilePsaExecutor
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

class UserCommandSetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: UserCommandSet

    @Before
    fun setup() {
        command = spyk(UserCommandSet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType profile then return SetProfilePsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.PROFILE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is SetProfilePsaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType profile then return DeleteAccountPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.DELETE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is DeleteAccountPsaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType profile then return DeleteAccountFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.DELETE

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is DeleteAccountFcaExecutor)
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
}
