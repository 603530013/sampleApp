package com.inetpsa.pims.spaceMiddleware.command.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleCheckFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleCheckPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleContractsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleContractsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleDetailsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleDetailsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleManualFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleManualPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleServicesPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesPsaExecutor
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

class VehicleCommandGetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: VehicleCommandGet

    @Before
    fun setup() {
        command = spyk(VehicleCommandGet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for PSA executor with actionType list then return GetVehiclesPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.LIST

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetVehiclesPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType details then return GetVehicleDetailsPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.DETAILS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetVehicleDetailsPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType contracts then return GetVehicleContractsPSAExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.CONTRACTS

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetVehicleContractsPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType services then return GetVehicleServicesPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.SERVICES

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetVehicleServicesPsaExecutor)
        }
    }

    @Test
    fun `when look for PSA executor with actionType check then return GetVehicleCheckPSAExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.CHECK

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetVehicleCheckPsaExecutor)
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
    fun `when look for PSA executor with actionType manual then return GetVehicleManualPsaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.MANUAL

            val executor = command.getPsaExecutor()
            Assert.assertEquals(true, executor is GetVehicleManualPsaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType list then return GetVehiclesFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.LIST

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetVehiclesFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType details then return GetVehicleDetailsFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.DETAILS

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetVehicleDetailsFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType contracts then return GetVehicleContractsFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.CONTRACTS

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetVehicleContractsFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType check then return GetVehicleCheckFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.CHECK

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetVehicleCheckFcaExecutor)
        }
    }

    @Test
    fun `when look for FCA executor with actionType manual then return GetVehicleManualFcaExecutor`() {
        runTest {
            every { command.actionType } returns Constants.Input.ActionType.MANUAL

            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetVehicleManualFcaExecutor)
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
