package com.inetpsa.pims.spaceMiddleware.command.dealer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.GetAdvisorDealerReviewConfigurationPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.GetDealerListNearbyPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.GetDealerListPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.GetPreferredDealerPsaExecutor
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

@Deprecated("we should use DealerCommandGetTest instead")
class GetDealerCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: GetDealerCommand

    @Before
    fun setup() {
        command = spyk(GetDealerCommand())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType dealerListDetails then return GetDealerListPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_DEALER_LIST_DETAILS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetDealerListPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType dealerListNearbyDetails then GetDealerListNearbyPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_DEALER_LIST_NEARBY_DETAILS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetDealerListNearbyPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType preferred then return GetPreferredDealerPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_PREFERRED_DEALER_DETAILS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetPreferredDealerPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType advisorReview then return GetAdvisorReviewPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_ADVISOR_DEALER_REVIEW_DETAILS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetAdvisorDealerReviewConfigurationPsaExecutor)
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
