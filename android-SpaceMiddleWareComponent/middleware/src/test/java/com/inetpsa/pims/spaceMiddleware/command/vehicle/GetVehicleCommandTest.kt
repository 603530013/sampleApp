package com.inetpsa.pims.spaceMiddleware.command.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.GetVehicleImagePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.GetVehiclesPsaExecutor
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

class GetVehicleCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: GetVehicleCommand

    @Before
    fun setup() {
        command = spyk(GetVehicleCommand())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType list then return GetVehicleDetailsPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_VEHICLE_LIST

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetVehiclesPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType image then return GetVehicleDetailsPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TYPE_VEHICLE_IMAGE

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetVehicleImagePsaExecutor)
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
