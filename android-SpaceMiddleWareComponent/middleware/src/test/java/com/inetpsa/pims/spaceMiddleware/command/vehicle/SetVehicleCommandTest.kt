package com.inetpsa.pims.spaceMiddleware.command.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.EnrollVehicleFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.EnrollVehiclePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.RemoveVehicleFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.RemoveVehiclePsaExecutor
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

class SetVehicleCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: SetVehicleCommand

    @Before
    fun setup() {
        command = spyk(SetVehicleCommand())
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
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_VEHICLE_REMOVE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is RemoveVehiclePsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType enrollment then return EnrollVehiclePsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is EnrollVehiclePsaExecutor)
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
    fun `when look for FCA executor with actionType remove then return RemoveVehicleFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_VEHICLE_REMOVE

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is RemoveVehicleFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType enrollment then return EnrollVehicleFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is EnrollVehicleFcaExecutor)
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
