package com.inetpsa.pims.spaceMiddleware.command.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.set.AddVehicleFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.set.AddVehiclePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.set.NicknameUpdateFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.set.RemoveVehiclePsaExecutor
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

class VehicleCommandSetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: VehicleCommandSet

    @Before
    fun setup() {
        command = spyk(VehicleCommandSet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType remove then return RemoveVehiclePsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.REMOVE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is RemoveVehiclePsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType add then return AddVehiclePsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.ADD

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is AddVehiclePsaExecutor)
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

//    @Test
//    fun `when look for FCA executor with actionType remove then return RemoveVehicleFcaExecutor`() {
//        runTest {
//            every { command.actionType } returns Constants.Input.ActionType.REMOVE
//
//            val executor = command.getFcaExecutor()
//            Assert.assertEquals(true, executor is RemoveVehicleFcaExecutor)
//        }
//    }

    @Test
    fun `when look for FCA executor with actionType add then return AddVehicleFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.ADD

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is AddVehicleFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType update then return NickNameUpdateFCAExecutor`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.UPDATE,
                Constants.Input.ACTION to Constants.Input.Action.NICKNAME
            )
            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is NicknameUpdateFCAExecutor)
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
    fun `when look for FCA executor with no existing action then throw PimsError exception`() {
        runTest {
            every { command.parameters } returns mapOf(
                Constants.Input.ACTION_TYPE to Constants.Input.ActionType.UPDATE,
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
}
