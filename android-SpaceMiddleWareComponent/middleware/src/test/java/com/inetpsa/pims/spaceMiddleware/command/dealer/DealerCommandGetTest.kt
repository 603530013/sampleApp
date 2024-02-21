package com.inetpsa.pims.spaceMiddleware.command.dealer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetAppointmentDetailsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetAppointmentDetailsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetAppointmentListFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetAppointmentListPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerAgendaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerAgendaPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerReviewPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerServicesFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerServicesPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealersFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealersPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetFavoriteDealerFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetFavoriteDealerPsaExecutor
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

class DealerCommandGetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: DealerCommandGet

    @Before
    fun setup() {
        command = spyk(DealerCommandGet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType dealerListDetails then return GetDealerPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.LIST

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetDealersPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType favoriteDealerDetails then return GetFavoriteDealerPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.FAVORITE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetFavoriteDealerPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType dealerReview then return GetDealerReviewPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.REVIEW

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetDealerReviewPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA with actionType appointment and action Services then return GetDealerPackagesPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Constants.Input.Action.SERVICES
            )

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetDealerServicesPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with AT appointment and A List then return GetAppointmentListPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Constants.Input.Action.LIST
            )
            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetAppointmentListPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA with actionType appointment and action Details then return GetAptDetailsPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Constants.Input.Action.DETAILS
            )

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetAppointmentDetailsPsaExecutor)
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
    fun `when look for FCA executor with actionType dealerListDetails then return GetDealerFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.LIST

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetDealersFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType dealerListDetails then return GetFavoriteDealerFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.FAVORITE

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetFavoriteDealerFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType Appointment then return GetAppointmentListFcaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Constants.Input.Action.LIST
            )
            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetAppointmentListFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType Appointment then return GetDealerAgendaFcaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Constants.Input.Action.AGENDA
            )
            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetDealerAgendaFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType Appointment then return GetDealerServicesFcaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Constants.Input.Action.SERVICES
            )
            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetDealerServicesFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType Appointment then return GetAppointmentDetailFcaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Constants.Input.Action.DETAILS
            )
            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetAppointmentDetailsFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with no existing  action then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
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

    @Test
    fun `when look for PSA ex with actionType appointment and action Agenda then return GetDealerAgendaPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Constants.Input.Action.AGENDA
            )

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetDealerAgendaPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA ex with actionType appointment and action invalid then throw exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
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
    fun `when look for PSA ex with actionType appointment without action then throw exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT
            )
            val exception = PIMSFoundationError.missingParameter(Constants.Input.ACTION)
            try {
                command.getPsaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }
}
