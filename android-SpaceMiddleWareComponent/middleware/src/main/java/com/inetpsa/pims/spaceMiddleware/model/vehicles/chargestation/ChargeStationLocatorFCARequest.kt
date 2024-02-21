package com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation

import com.google.gson.annotations.SerializedName

internal data class ChargeStationLocatorFCARequest(
    @SerializedName("location")
    val location: Location,
    @SerializedName("limit")
    val limit: Int = 20,
    @SerializedName("connectorTypes")
    val connectorTypes: List<ConnectorTypes>? = null,
    @SerializedName("partnerIDs")
    val partnerIds: List<Long>? = null,
    @SerializedName("acceptablePayments")
    val acceptablePayments: List<AcceptablePayment>? = null,
    @SerializedName("accessTypes")
    val accessTypes: List<AccessTypes>? = null,
    @SerializedName("specialRestrictions")
    val specialRestrictions: List<SpecialRestriction>? = null,
    @SerializedName("powerTypes")
    val powerTypes: List<PowerType>? = null,
    @SerializedName("openStationsOnly")
    val openStationsOnly: Boolean? = null,
    @SerializedName("minimumAvailable")
    val minimumAvailable: Int? = null,
    @SerializedName("filters")
    val filters: Map<String, IFilter>? = null
) {

    internal data class Location(
        @SerializedName("lat")
        val latitude: Double,
        @SerializedName("lon")
        val longitude: Double
    )

    interface IFilter

    data class F2mFilter(
        @SerializedName("radius")
        val radiusInMeters: Int? = null,
        @SerializedName("plugTypes")
        var plugTypes: List<ConnectorTypes>? = null,
        @SerializedName("powerLevels")
        var powerLevels: List<PowerType>? = null,
        @SerializedName("access")
        var access: List<Access>? = null,
        @SerializedName("chargingCableAttached")
        var chargingCableAttached: Boolean? = null,
        @SerializedName("free")
        var free: Boolean? = null,
        @SerializedName("indoor")
        var indoor: Boolean? = null,
        @SerializedName("open24Hours")
        var open24Hours: Boolean? = null
    ) : IFilter {

        init {
            // If value is false, must not sent value,
            if (chargingCableAttached == false) {
                chargingCableAttached = null
            }

            if (free == false) {
                free = null
            }

            if (indoor == false) {
                indoor = null
            }

            if (open24Hours == false) {
                open24Hours = null
            }

            if (plugTypes?.isEmpty() == true) {
                plugTypes = null
            }

            if (powerLevels?.isEmpty() == true) {
                powerLevels = null
            }

            if (access?.isEmpty() == true) {
                access = null
            }
        }
    }

    data class BoschFilter(
        @SerializedName("maxCapacity")
        val maxCapacity: Int? = null,
        @SerializedName("minCapacity")
        val minCapacity: Int? = null,
        @SerializedName("isOpen24Hours")
        val isOpen24Hours: Boolean? = null,
        @SerializedName("renewableEnergy")
        val renewableEnergy: Boolean? = null,
        @SerializedName("onlyAvailable")
        val onlyAvailable: Boolean? = null,
        @SerializedName("plugTypes")
        val plugTypes: List<ConnectorTypes>? = null,
        @SerializedName("rangeInMeters")
        val rangeInMeters: Double? = null,
        @SerializedName("powerLevels")
        val powerLevels: List<ChargingMode>? = null,
        @SerializedName("access")
        val access: List<Access>? = null
    ) : IFilter

    enum class ConnectorTypes {
        @SerializedName("DOMESTIC_PLUG_GENERIC")
        DOMESTIC_PLUG_GENERIC,

        @SerializedName("NEMA_5_20")
        NEMA_5_20,

        @SerializedName("TYPE_1_YAZAKI")
        TYPE_1_YAZAKI,

        @SerializedName("TYPE_1_CCS")
        TYPE_1_CCS,

        @SerializedName("TYPE_2_MENNEKES")
        TYPE_2_MENNEKES,

        @SerializedName("TYPE_2_CCS")
        TYPE_2_CCS,

        @SerializedName("TYPE_3")
        TYPE_3,

        @SerializedName("CHADEMO")
        CHADEMO,

        @SerializedName("GBT_PART_2")
        GBT_PART_2,

        @SerializedName("GBT_PART_3")
        GBT_PART_3,

        @SerializedName("INDUSTRIAL_BLUE")
        INDUSTRIAL_BLUE,

        @SerializedName("INDUSTRIAL_RED")
        INDUSTRIAL_RED,

        @SerializedName("INDUSTRIAL_WHITE")
        INDUSTRIAL_WHITE
    }

    enum class ChargingMode {
        @SerializedName("SLOW")
        SLOW,

        @SerializedName("REGULAR")
        REGULAR,

        @SerializedName("FAST")
        FAST,

        @SerializedName("UNKNOWN")
        UNKNOWN
    }

    enum class Access {
        @SerializedName("APP")
        APP,

        @SerializedName("CHARGING_CARD")
        CHARGING_CARD,

        @SerializedName("NO_AUTHENTICATION")
        NO_AUTHENTICATION,

        @SerializedName("PLUG_AND_CHARGE")
        PLUG_AND_CHARGE
    }

    enum class AcceptablePayment {
        @SerializedName("NO_PAYMENT")
        NO_PAYMENT,

        @SerializedName("CASH")
        CASH,

        @SerializedName("CREDIT_CARD")
        CREDIT_CARD,

        @SerializedName("BANK_OR_DEBIT_CARD")
        BANK_OR_DEBIT_CARD,

        @SerializedName("ELECTRONIC_PURSE")
        ELECTRONIC_PURSE,

        @SerializedName("ELECTRONIC_TOLL_COLLECTION")
        ELECTRONIC_TOLL_COLLECTION,

        @SerializedName("COINS_ONLY_OR_EXACT_CHANGE")
        COINS_ONLY_OR_EXACT_CHANGE,

        @SerializedName("VARIABLE")
        VARIABLE,

        @SerializedName("SERVICE_PROVIDER_PAYMENT_METHOD")
        SERVICE_PROVIDER_PAYMENT_METHOD,

        @SerializedName("FUEL_CARD")
        FUEL_CARD
    }

    enum class AccessTypes {
        @SerializedName("PUBLIC")
        PUBLIC,

        @SerializedName("RESIDENTIAL")
        RESIDENTIAL,

        @SerializedName("PRIVATE")
        PRIVATE
    }

    enum class SpecialRestriction {
        @SerializedName("UNSPECIFIED")
        UNSPECIFIED,

        @SerializedName("NO_RESTRICTION")
        NO_RESTRICTION,

        @SerializedName("GENERIC_RESTRICTION")
        GENERIC_RESTRICTION,

        @SerializedName("RESIDENTS_ONLY")
        RESIDENTS_ONLY,

        @SerializedName("EMPLOYEES_ONLY")
        EMPLOYEES_ONLY,

        @SerializedName("AUTHORIZED_PERSONNEL_ONLY")
        AUTHORIZED_PERSONNEL_ONLY,

        @SerializedName("MEMBERS_ONLY")
        MEMBERS_ONLY
    }

    enum class PowerType {
        @SerializedName("SLOW_CHARGE")
        SLOW_CHARGE,

        @SerializedName("REGULAR_CHARGE")
        REGULAR_CHARGE,

        @SerializedName("FAST_CHARGE")
        FAST_CHARGE
    }
}
