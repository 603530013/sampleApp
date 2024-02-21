package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access.APP
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access.CHARGING_CARD
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access.NO_AUTHENTICATION
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.BoschFilter
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ChargingMode.FAST
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ChargingMode.REGULAR
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ChargingMode.SLOW
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes.CHADEMO
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes.GBT_PART_3
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes.TYPE_3
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

class ChargingStationBoschMapperTest {

    private val testParams = mapOf(
        ChargingStationFilters.KEY_FILTER_CONNECTOR_TYPES to listOf("TYPE_3", "CHADEMO", "GBT_PART_3"),
        ChargingStationFilters.KEY_FILTER_POWER_TYPES to listOf("FAST_CHARGE", "REGULAR_CHARGE", "SLOW_CHARGE"),
        ChargingStationFilters.KEY_FILTER_ACCESS to listOf("APP", "CHARGING_CARD", "NO_AUTHENTICATION"),
        ChargingStationFilters.Bosch.KEY_FILTER_RENEWABLE_ENERGY to true,
        ChargingStationFilters.Bosch.KEY_FILTER_OPEN_ONLY to true
    )
    private val boschFilter = BoschFilter(
        plugTypes = listOf(TYPE_3, CHADEMO, GBT_PART_3),
        powerLevels = listOf(FAST, REGULAR, SLOW),
        access = listOf(APP, CHARGING_CARD, NO_AUTHENTICATION),
        renewableEnergy = true
    )
    private val filterInput = FilterInput(
        connectorTypes = listOf("TYPE_3", "CHADEMO", "GBT_PART_3"),
        powerTypes = listOf("FAST_CHARGE", "REGULAR_CHARGE", "SLOW_CHARGE"),
        access = listOf("APP", "CHARGING_CARD", "NO_AUTHENTICATION"),
        renewableEnergy = true,
        openOnly = true
    )
    private lateinit var chargingStationBoschMapper: ChargingStationBoschMapper

    @Before
    fun setUp() {
        chargingStationBoschMapper = ChargingStationBoschMapper()
    }

    @Test
    fun `test transformParamsToInput()`() {
        val result = chargingStationBoschMapper.transformParamsToInput(testParams)
        assertEquals(filterInput, result)
    }

    @Test
    fun `test transformToBodyRequest()`() {
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
            filters = mapOf("bosch" to boschFilter)
        )
        val result = chargingStationBoschMapper.transformToBodyRequest(chargeStationLocatorInput)
        assertEquals(chargeStationLocatorFCARequest, result)
    }

    @Test
    fun `test transformFilter()`() {
        val result = chargingStationBoschMapper.transformFilter(filterInput)
        assertEquals(boschFilter, result)
    }

    @Test
    fun `test mapPowerTypesBosch()`() {
        val input = listOf(SLOW_CHARGE, REGULAR_CHARGE, FAST_CHARGE)
        val expectedOutput = listOf(SLOW, REGULAR, FAST)
        val result = chargingStationBoschMapper.mapPowerTypesBosch(input)
        assertEquals(expectedOutput, result)
    }
}
