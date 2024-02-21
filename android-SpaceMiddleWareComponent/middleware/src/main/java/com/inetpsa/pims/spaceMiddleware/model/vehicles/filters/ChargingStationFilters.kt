package com.inetpsa.pims.spaceMiddleware.model.vehicles.filters

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.AcceptablePayment
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.AccessTypes
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.PowerType
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.SpecialRestriction
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargeStationFiltersOutput.FilterItem

internal sealed class ChargingStationFilters {

    companion object {
        const val LIST_TYPE = "list"
        const val BOOLEAN_TYPE = "boolean"
        const val INTEGER_TYPE = "integer"
        const val KEY_FILTER_POWER_TYPES = "powerTypes"
        const val KEY_FILTER_ACCESS = "access"
        const val KEY_FILTER_OPEN_24_HOURS = "open24Hours"
        const val KEY_FILTER_CONNECTOR_TYPES = "connectorTypes"
    }

    protected fun commonFilters(): List<FilterItem> = listOf(
        FilterItem(
            key = KEY_FILTER_CONNECTOR_TYPES,
            type = LIST_TYPE,
            data = ConnectorTypes.values().map { it.name }
        ),
        FilterItem(
            key = KEY_FILTER_POWER_TYPES,
            type = LIST_TYPE,
            data = getPowerTypesValues(this)
        ),
        FilterItem(
            key = KEY_FILTER_ACCESS,
            type = LIST_TYPE,
            data = getAccessValues(this)
        ),
        FilterItem(
            key = KEY_FILTER_OPEN_24_HOURS,
            type = BOOLEAN_TYPE,
            data = null
        )
    )

    abstract val filters: List<FilterItem>

    object Tomtom : ChargingStationFilters() {

        const val KEY_FILTER_ONLY_AVAILABLE = "onlyAvailable"
        const val KEY_FILTER_PARTNER_ONLY = "partnerOnly"
        const val KEY_FILTER_MINIMUM_AVAILABLE = "minimumAvailable"
        const val KEY_FILTER_COMPATIBLE_ONLY = "compatibleOnly"
        const val KEY_FILTER_ACCESS_TYPES = "accessTypes"
        const val KEY_FILTER_ACCEPTABLE_PAYMENTS = "acceptablePayments"
        const val KEY_FILTER_SPECIAL_RESTRICTIONS = "specialRestrictions"

        override val filters: List<FilterItem> = commonFilters() + listOf(
            FilterItem(
                key = KEY_FILTER_ONLY_AVAILABLE,
                type = BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = KEY_FILTER_PARTNER_ONLY,
                type = BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = KEY_FILTER_MINIMUM_AVAILABLE,
                type = INTEGER_TYPE,
                data = null
            ),
            FilterItem(
                key = KEY_FILTER_COMPATIBLE_ONLY,
                type = BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = KEY_FILTER_ACCESS_TYPES,
                type = LIST_TYPE,
                data = AccessTypes.values().map { it.name }
            ),
            FilterItem(
                key = KEY_FILTER_ACCEPTABLE_PAYMENTS,
                type = LIST_TYPE,
                data = AcceptablePayment.values().map { it.name }
            ),
            FilterItem(
                key = KEY_FILTER_SPECIAL_RESTRICTIONS,
                type = LIST_TYPE,
                data = SpecialRestriction.values().map { it.name }
            )
        )
    }

    object F2M : ChargingStationFilters() {

        const val KEY_FILTER_CHARGING_CABLE_ATTACHED = "chargingCableAttached"
        const val KEY_FILTER_FREE = "free"
        const val KEY_FILTER_INDOOR = "indoor"

        override val filters: List<FilterItem> = commonFilters() + listOf(

            FilterItem(
                key = KEY_FILTER_CHARGING_CABLE_ATTACHED,
                type = BOOLEAN_TYPE,
                data = null
            ),

            FilterItem(
                key = KEY_FILTER_FREE,
                type = BOOLEAN_TYPE,
                data = null
            ),

            FilterItem(
                key = KEY_FILTER_INDOOR,
                type = BOOLEAN_TYPE,
                data = null
            )
        )
    }

    object Bosch : ChargingStationFilters() {

        const val KEY_FILTER_RENEWABLE_ENERGY = "renewableEnergy"
        const val KEY_FILTER_OPEN_ONLY = "openOnly"

        override val filters: List<FilterItem> = commonFilters() + listOf(
            FilterItem(
                key = KEY_FILTER_RENEWABLE_ENERGY,
                type = BOOLEAN_TYPE,
                data = null
            ),

            FilterItem(
                key = KEY_FILTER_OPEN_ONLY,
                type = BOOLEAN_TYPE,
                data = null
            )
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getAccessValues(provider: ChargingStationFilters): List<String> =
        Access.values().mapNotNull { type ->
            when (provider) {
                is Bosch -> type.name
                else -> type.name.takeIf { type != Access.PLUG_AND_CHARGE }
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getPowerTypesValues(provider: ChargingStationFilters): List<String> =
        PowerType.values().mapNotNull { type ->
            when (provider) {
                is Bosch -> type.name
                else -> type.name.takeIf { type != PowerType.SLOW_CHARGE }
            }
        }
}
