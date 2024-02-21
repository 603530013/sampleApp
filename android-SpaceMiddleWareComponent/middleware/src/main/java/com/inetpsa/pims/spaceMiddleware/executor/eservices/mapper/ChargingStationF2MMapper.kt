package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.F2mFilter
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.IFilter
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Location
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.PowerType
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargingStationFilters
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.FilterInput
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

internal class ChargingStationF2MMapper : ChargingStationMapper {

    override fun transformParamsToInput(params: Map<String, Any>?) =
        FilterInput(
            connectorTypes = params hasOrNull ChargingStationFilters.KEY_FILTER_CONNECTOR_TYPES,
            powerTypes = params hasOrNull ChargingStationFilters.KEY_FILTER_POWER_TYPES,
            access = params hasOrNull ChargingStationFilters.KEY_FILTER_ACCESS,
            open24Hours = params hasOrNull ChargingStationFilters.KEY_FILTER_OPEN_24_HOURS,
            chargingCableAttached = params hasOrNull ChargingStationFilters.F2M.KEY_FILTER_CHARGING_CABLE_ATTACHED,
            free = params hasOrNull ChargingStationFilters.F2M.KEY_FILTER_FREE,
            indoor = params hasOrNull ChargingStationFilters.F2M.KEY_FILTER_INDOOR
        )

    override fun transformToBodyRequest(input: ChargeStationLocatorInput): ChargeStationLocatorFCARequest =
        ChargeStationLocatorFCARequest(
            location = Location(
                latitude = input.latitude,
                longitude = input.longitude
            ),
            filters = mapOf("f2m" to transformFilterFromInput(input.filters))
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformFilterFromInput(filters: FilterInput?): IFilter =
        F2mFilter(
            radiusInMeters = null,
            plugTypes = filters?.connectorTypes?.map { ConnectorTypes.valueOf(it) },
            powerLevels = filters?.powerTypes?.map { PowerType.valueOf(it) },
            access = filters?.access?.map { Access.valueOf(it) },
            chargingCableAttached = filters?.chargingCableAttached,
            free = filters?.free,
            indoor = filters?.indoor,
            open24Hours = filters?.open24Hours
        )
}
