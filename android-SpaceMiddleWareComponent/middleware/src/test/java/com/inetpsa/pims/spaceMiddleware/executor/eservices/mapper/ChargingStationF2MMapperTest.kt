package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access.APP
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access.CHARGING_CARD
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access.NO_AUTHENTICATION
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes.CHADEMO
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes.GBT_PART_3
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes.TYPE_3
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.F2mFilter
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Location
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.PowerType.FAST_CHARGE
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.PowerType.REGULAR_CHARGE
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.PowerType.SLOW_CHARGE
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargingStationFilters
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.FilterInput
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChargingStationF2MMapperTest {

    private val testParams = mapOf(
        ChargingStationFilters.KEY_FILTER_CONNECTOR_TYPES to listOf("TYPE_3", "CHADEMO", "GBT_PART_3"),
        ChargingStationFilters.KEY_FILTER_POWER_TYPES to listOf("SLOW_CHARGE", "REGULAR_CHARGE", "FAST_CHARGE"),
        ChargingStationFilters.KEY_FILTER_ACCESS to listOf("APP", "CHARGING_CARD", "NO_AUTHENTICATION"),
        ChargingStationFilters.F2M.KEY_FILTER_FREE to true,
        ChargingStationFilters.F2M.KEY_FILTER_INDOOR to true
    )
    private val f2mFilter = F2mFilter(
        radiusInMeters = null,
        plugTypes = listOf(TYPE_3, CHADEMO, GBT_PART_3),
        powerLevels = listOf(SLOW_CHARGE, REGULAR_CHARGE, FAST_CHARGE),
        access = listOf(APP, CHARGING_CARD, NO_AUTHENTICATION),
        chargingCableAttached = null,
        free = true,
        indoor = true
    )
    private val filterInput = FilterInput(
        connectorTypes = listOf("TYPE_3", "CHADEMO", "GBT_PART_3"),
        powerTypes = listOf("SLOW_CHARGE", "REGULAR_CHARGE", "FAST_CHARGE"),
        access = listOf("APP", "CHARGING_CARD", "NO_AUTHENTICATION"),
        free = true,
        indoor = true
    )
    private lateinit var chargingStationF2MMapper: ChargingStationF2MMapper

    @Before
    fun setUp() {
        chargingStationF2MMapper = ChargingStationF2MMapper()
    }

    @Test
    fun `test transformToBodyRequest`() {
        val chargeStationLocatorInput = ChargeStationLocatorInput(
            vin = "vin",
            longitude = 1.0,
            latitude = 2.0,
            filters = filterInput
        )
        val chargeStationLocatorFCARequest = ChargeStationLocatorFCARequest(
            location = Location(
                latitude = 2.0,
                longitude = 1.0
            ),
            filters = mapOf("f2m" to f2mFilter)
        )
        val result = chargingStationF2MMapper.transformToBodyRequest(chargeStationLocatorInput)
        assertEquals(chargeStationLocatorFCARequest, result)
    }

    @Test
    fun `test transformParamsToInput`() {
        val result = chargingStationF2MMapper.transformParamsToInput(testParams)
        assertEquals(filterInput, result)
    }

    @Test
    fun `test transformFilterFromInput`() {
        val result = chargingStationF2MMapper.transformFilterFromInput(filterInput)
        assertEquals(f2mFilter, result)
    }
}
