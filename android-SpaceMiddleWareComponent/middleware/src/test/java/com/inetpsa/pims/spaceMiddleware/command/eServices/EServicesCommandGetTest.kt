package com.inetpsa.pims.spaceMiddleware.command.eServices

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.eservices.get.GetChargingStationFiltersExecutor
import com.inetpsa.pims.spaceMiddleware.executor.eservices.get.NearestChargingStationFcaExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class EServicesCommandGetTest {

    private lateinit var eServicesCommandGet: EServicesCommandGet
    private val middlewareComponent: MiddlewareComponent = mockk()

    @Before
    fun setUp() {
        eServicesCommandGet = spyk(EServicesCommandGet())
        every { eServicesCommandGet.middlewareComponent } returns middlewareComponent
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getFcaExecutor() with ACTION as LIST returns NearestChargingStationFcaExecutor`() {
        val params = mapOf(
            Constants.Input.ACTION_TYPE to Constants.Input
                .ActionType.CHARGING_STATION,
            Constants.Input.ACTION to Constants.Input.Action.LIST
        )
        every { eServicesCommandGet.parameters } returns params
        runBlocking {
            val result = eServicesCommandGet.getFcaExecutor()
            assert(result is NearestChargingStationFcaExecutor)
        }
    }

    @Test
    fun `test getFcaExecutor() throws PIMS error when action type doesn't match`() {
        val params = mapOf(
            Constants.Input.ACTION_TYPE to Constants.Input
                .ActionType.SERVICES,
            Constants.Input.ACTION to Constants.Input.Action.LIST
        )
        every { eServicesCommandGet.parameters } returns params
        assertThrows(PIMSError::class.java) {
            runBlocking {
                eServicesCommandGet.getFcaExecutor()
            }
        }
    }

    @Test
    fun `test handleFcaEServicesExecutor() throws PIMS error when ACTION doesn't match`() {
        val params = mapOf(
            Constants.Input.ACTION_TYPE to Constants.Input
                .ActionType.SERVICES,
            Constants.Input.ACTION to Constants.Input.Action.SERVICES
        )
        every { eServicesCommandGet.parameters } returns params
        assertThrows(PIMSError::class.java) {
            runBlocking {
                eServicesCommandGet.handleFcaEServicesExecutor()
            }
        }
    }

    @Test
    fun `test getFcaExecutor() with ACTION as FILTER returns GetChargingStationFiltersExecutor`() {
        val params = mapOf(
            Constants.Input.ACTION_TYPE to Constants.Input
                .ActionType.CHARGING_STATION,
            Constants.Input.ACTION to Constants.Input.Action.FILTERS
        )
        eServicesCommandGet.parameters = params
        every { eServicesCommandGet.actionType } returns Constants.Input
            .ActionType.CHARGING_STATION
        every { eServicesCommandGet.parameters } returns params
        runBlocking {
            val result = eServicesCommandGet.getFcaExecutor()
            assert(result is GetChargingStationFiltersExecutor)
        }
    }
}
