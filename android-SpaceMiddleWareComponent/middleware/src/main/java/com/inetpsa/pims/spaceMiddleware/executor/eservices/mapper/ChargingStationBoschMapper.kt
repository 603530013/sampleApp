package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.BoschFilter
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ChargingMode
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.IFilter
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Location
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.PowerType
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargingStationFilters
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.FilterInput
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

internal class ChargingStationBoschMapper : ChargingStationMapper {

    override fun transformParamsToInput(params: Map<String, Any>?) =
        FilterInput(
            connectorTypes = params hasOrNull ChargingStationFilters.KEY_FILTER_CONNECTOR_TYPES,
            powerTypes = params hasOrNull ChargingStationFilters.KEY_FILTER_POWER_TYPES,
            access = params hasOrNull ChargingStationFilters.KEY_FILTER_ACCESS,
            open24Hours = params hasOrNull ChargingStationFilters.KEY_FILTER_OPEN_24_HOURS,
            renewableEnergy = params hasOrNull ChargingStationFilters.Bosch.KEY_FILTER_RENEWABLE_ENERGY,
            openOnly = params hasOrNull ChargingStationFilters.Bosch.KEY_FILTER_OPEN_ONLY
        )

    override fun transformToBodyRequest(input: ChargeStationLocatorInput): ChargeStationLocatorFCARequest =
        ChargeStationLocatorFCARequest(
            location = Location(
                latitude = input.latitude,
                longitude = input.longitude
            ),
            filters = mapOf("bosch" to transformFilter(input.filters))
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformFilter(filters: FilterInput?): IFilter =
        BoschFilter(
            maxCapacity = null,
            minCapacity = null,
            isOpen24Hours = filters?.open24Hours,
            renewableEnergy = filters?.renewableEnergy,
            onlyAvailable = filters?.onlyAvailable,
            plugTypes = filters?.connectorTypes?.map { ConnectorTypes.valueOf(it) },
            rangeInMeters = null,
            powerLevels = filters?.powerTypes?.map { PowerType.valueOf(it) }?.let { mapPowerTypesBosch(it) },
            access = filters?.access?.map { Access.valueOf(it) }

        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun mapPowerTypesBosch(powerTypesBosch: List<PowerType>): List<ChargingMode> {
        return powerTypesBosch.map {
            when (it) {
                PowerType.SLOW_CHARGE -> ChargingMode.SLOW

                PowerType.REGULAR_CHARGE -> ChargingMode.REGULAR

                PowerType.FAST_CHARGE -> ChargingMode.FAST
            }
        }
    }
}
