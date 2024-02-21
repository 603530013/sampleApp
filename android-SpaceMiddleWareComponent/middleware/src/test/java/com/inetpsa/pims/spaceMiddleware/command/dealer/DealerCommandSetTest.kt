package com.inetpsa.pims.spaceMiddleware.command.dealer

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddDealerAppointmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddDealerAppointmentPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddFavoriteDealerFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddFavoriteDealerPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.DeleteAppointmentsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.RemoveFavoriteDealerPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
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

class DealerCommandSetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: DealerCommandSet

    @Before
    fun setup() {
        command = spyk(DealerCommandSet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType favorite, action add then return AddFavoriteDealerPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Action.Add.name)
            every { command.actionType } returns Constants.Input.ActionType.FAVORITE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is AddFavoriteDealerPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionT favorite action remove then return RemoveFavoriteDealerPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Action.Remove.name)
            every { command.actionType } returns Constants.Input.ActionType.FAVORITE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is RemoveFavoriteDealerPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType favorite without action then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.FAVORITE
            val exception = PIMSFoundationError.missingParameter(Constants.Input.ACTION)

            try {
                command.getPsaExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for PSA executor with actionType Appointment then return AddDealerAppointmentPsaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Action.Add.name
            )
            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is AddDealerAppointmentPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with no existing  action then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.APPOINTMENT,
                Constants.Input.ACTION to Action.Get.name
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
    fun `when look for FCA executor with actionType favorite, action add then return AddFavoriteDealerFcaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Action.Add.name)
            every { command.actionType } returns Constants.Input.ActionType.FAVORITE

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is AddFavoriteDealerFcaExecutor)
        }
    }

    @Test
    fun `when look FCA executor with actionType appointment,action delete then return DeleteAppointmentsFcaExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Action.Delete.name)
            every { command.actionType } returns Constants.Input.ActionType.APPOINTMENT

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is DeleteAppointmentsFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType appointment, action GET then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.APPOINTMENT
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Action.Get.name)
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
    fun `when look for PSA executor with actionType favorite, action GET then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.FAVORITE
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Action.Get.name)
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

    @Test
    fun `when look for FCA executor with actionType favorite, action GET then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.FAVORITE
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Action.Get.name)
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
    fun `when look for FCA executor with actionType appointment then return AddDealerAppointmentFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.APPOINTMENT
            every { command.parameters } returns mapOf(Constants.Input.ACTION to Action.Add.name)
            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is AddDealerAppointmentFcaExecutor)
        }
    }
}
