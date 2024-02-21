package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access
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
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChargingStationTomTomMapperTest {

    private val testParams = mapOf(
        ChargingStationFilters.KEY_FILTER_CONNECTOR_TYPES to listOf("TYPE_3", "CHADEMO", "GBT_PART_3"),
        ChargingStationFilters.KEY_FILTER_POWER_TYPES to listOf("SLOW_CHARGE", "REGULAR_CHARGE", "FAST_CHARGE"),
        ChargingStationFilters.KEY_FILTER_ACCESS to listOf("APP", "CHARGING_CARD", "NO_AUTHENTICATION"),
        ChargingStationFilters.Tomtom.KEY_FILTER_ONLY_AVAILABLE to false,
        ChargingStationFilters.Tomtom.KEY_FILTER_PARTNER_ONLY to true
    )
    private val f2mFilter = F2mFilter(
        radiusInMeters = null,
        plugTypes = listOf(TYPE_3, CHADEMO, GBT_PART_3),
        powerLevels = listOf(SLOW_CHARGE, REGULAR_CHARGE, FAST_CHARGE),
        access = listOf(Access.APP, CHARGING_CARD, NO_AUTHENTICATION),
        chargingCableAttached = null
    )
    private val filterInput = FilterInput(
        connectorTypes = listOf("TYPE_3", "CHADEMO", "GBT_PART_3"),
        powerTypes = listOf("SLOW_CHARGE", "REGULAR_CHARGE", "FAST_CHARGE"),
        access = listOf("APP", "CHARGING_CARD", "NO_AUTHENTICATION"),
        onlyAvailable = false,
        partnerOnly = true
    )
    private val partnerIDs = listOf(1L, 2L, 3L)

    private lateinit var chargingStationTomTomMapper: ChargingStationTomTomMapper

    @Before
    fun setUp() {
        chargingStationTomTomMapper = ChargingStationTomTomMapper(partnerIDs)
    }

    @Test
    fun `test transformParamsToInput`() {
        val result = chargingStationTomTomMapper.transformParamsToInput(testParams)
        Assert.assertEquals(filterInput, result)
    }

    @Test
    fun `test transformToBodyRequest()`() {
        val chargingStationLocatorInput = ChargeStationLocatorInput(
            vin = "testVin",
            latitude = 1.0,
            longitude = 2.0,
            filters = filterInput
        )
        val chargeStationLocatorFCARequest = ChargeStationLocatorFCARequest(
            location = Location(
                latitude = 1.0,
                longitude = 2.0
            ),
            partnerIds = partnerIDs,
            filters = mapOf("tomtom" to f2mFilter)
        )
        val result = chargingStationTomTomMapper.transformToBodyRequest(chargingStationLocatorInput)
        Assert.assertEquals(chargeStationLocatorFCARequest, result)
    }

    @Test
    fun `test transformToBodyRequest() with partnerOnly=false returns empty partnerID's`() {
        val chargingStationLocatorInput = ChargeStationLocatorInput(
            vin = "testVin",
            latitude = 1.0,
            longitude = 2.0,
            filters = FilterInput(partnerOnly = false)
        )
        val chargeStationLocatorFCARequest = ChargeStationLocatorFCARequest(
            location = Location(
                latitude = 1.0,
                longitude = 2.0
            ),
            partnerIds = emptyList(),
            filters = mapOf("tomtom" to F2mFilter())
        )
        val result = chargingStationTomTomMapper.transformToBodyRequest(chargingStationLocatorInput)
        Assert.assertEquals(chargeStationLocatorFCARequest, result)
    }

    @Test
    fun `test transformFilterFromInput`() {
        val result = chargingStationTomTomMapper.transformFilterFromInput(filterInput)
        Assert.assertEquals(f2mFilter, result)
    }

    @Test
    fun `test transformFilterFromInput with input as null`() {
        val result = chargingStationTomTomMapper.transformFilterFromInput(null)
        Assert.assertEquals(F2mFilter(), result)
    }
}
