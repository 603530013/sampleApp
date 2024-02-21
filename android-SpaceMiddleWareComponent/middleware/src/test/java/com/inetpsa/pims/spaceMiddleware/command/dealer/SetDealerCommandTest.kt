package com.inetpsa.pims.spaceMiddleware.command.dealer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.EnrollPreferredDealerPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.RemovePreferredDealerPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.SendAdvisorDealerReviewPsaExecutor
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

@Deprecated("should be replaced with DealerCommandSetTest")
class SetDealerCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: SetDealerCommand

    @Before
    fun setup() {
        command = spyk(SetDealerCommand())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType enrollPreferredDealer return EnrollPreferredDealerPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_PREFERRED_DEALER_ENROLL

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is EnrollPreferredDealerPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType remove then return RemovePreferredDealerPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_PREFERRED_DEALER_REMOVE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is RemovePreferredDealerPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType advisorReview then return SendAdvisorDealerReviewPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_SEND_ADVISOR_DEALER_REVIEW

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is SendAdvisorDealerReviewPsaExecutor)
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
