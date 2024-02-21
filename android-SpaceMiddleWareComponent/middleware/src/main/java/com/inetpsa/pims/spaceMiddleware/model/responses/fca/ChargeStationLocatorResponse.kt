package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class ChargeStationLocatorResponse(
    @SerializedName("chargeStations") val chargeStations: List<ChargeStations>? = null
) {

    internal data class ChargeStations(
        @SerializedName("id") val id: String? = null,
        @SerializedName("locationId") val locationId: String? = null,
        @SerializedName("dist") val dist: Double? = null,
        @SerializedName("poi", alternate = ["POI"]) val poi: PointInfoProvider? = null,
        @SerializedName("address", alternate = ["Address"]) val address: Address? = null,
        @SerializedName("position", alternate = ["Position"]) val position: Position? = null,
        @SerializedName("connectors", alternate = ["Connectors"]) val connectors: List<Connectors>? = null,
        @SerializedName("cslProviderData", alternate = ["CslProviderData"])
        val cslProviderData: Map<String, CslProviderData>? = null,
        @SerializedName("brand") val brand: String? = null // CARREFOUR
    ) {

        internal data class PointInfoProvider(
            @SerializedName("name") val name: String? = null,
            @SerializedName("openingHours") val openHours: List<OpeningHours>? = null,
            @SerializedName("accessType", alternate = ["AccessType"]) val accessType: String? = null,
            @SerializedName("acceptablePayments") val acceptablePayments: List<String>? = null,
            // NO_PAYMENT, CASH, CREDIT_CARD, BANK_OR_DEBIT_CARD, ELECTRONIC_PURSE, ELECTRONIC_TOLL_COLLECTION,
            // COINS_ONLY_OR_EXACT_CHANGE, VARIABLE, SERVICE_PROVIDER_PAYMENT_METHOD, FUEL_CARD

            @SerializedName("specialRestrictions") val specialRestrictions: List<String>? = null,
            // UNSPECIFIED, NO_RESTRICTION, GENERIC_RESTRICTION, RESIDENTS_ONLY, EMPLOYEES_ONLY,
            // AUTHORIZED_PERSONNEL_ONLY, MEMBERS_ONLY

            @SerializedName("partnerIDs") val partnerIDs: Int? = null
        ) {

            internal data class OpeningHours(
                @SerializedName("startTime", alternate = ["StartTime"]) val startTime: Time? = null,
                @SerializedName("endTime", alternate = ["EndTime"]) val endTime: Time? = null
            ) {

                internal data class Time(
                    @SerializedName("date", alternate = ["Date"]) val date: String? = null,
                    @SerializedName("hour", alternate = ["Hour"]) val hour: Int? = null,
                    @SerializedName("minute", alternate = ["Minute"]) val minute: Int? = null
                )
            }
        }

        internal data class Address(
            @SerializedName("streetNumber") val streetNumber: String? = null,
            @SerializedName("streetName") val streetName: String? = null,
            @SerializedName("municipality") val municipality: String? = null,
            @SerializedName("countrySubdivision") val countrySubdivision: String? = null,
            @SerializedName("countrySecondarySubdivision") val countrySecondarySubdivision: String? = null,
            @SerializedName("countryTertiarySubdivision") val countryTertiarySubdivision: String? = null,
            @SerializedName("countrySubdivisionName") val countrySubdivisionName: String? = null,
            @SerializedName("postalCode") val postalCode: String? = null,
            @SerializedName("extendedPostalCode") val extendedPostalCode: String? = null,
            @SerializedName("countryCode") val countryCode: String? = null,
            @SerializedName("country") val country: String? = null,
            @SerializedName("countryCodeISO3") val countryCodeISO3: String? = null,
            @SerializedName("freeformAddress") val freeformAddress: String? = null,
            @SerializedName("localName") val localName: String? = null
        )

        internal data class Position(
            @SerializedName("latitude", alternate = ["Latitude", "lat"]) val latitude: Double? = null,
            @SerializedName("longitude", alternate = ["Longitude", "lon"]) val longitude: Double? = null
        )

        internal data class Connectors(
            @SerializedName("type", alternate = ["Type"]) val type: String? = null,
            // DOMESTIC_PLUG_GENERIC, NEMA_5_20, TYPE_1_YAZAKI, TYPE_1_CCS, TYPE_2_MENNEKES, TYPE_2_CCS, TYPE_3,
            // CHADEMO, GBT_PART_2, GBT_PART_3, INDUSTRIAL_BLUE, INDUSTRIAL_RED, INDUSTRIAL_WHITE

            @SerializedName("compatible") val compatible: Boolean? = null,
            @SerializedName("powerLevel", alternate = ["PowerLevel"]) val powerLevel: PowerLevel? = null,
            @SerializedName("total") val total: Int? = null,
            @SerializedName("availability", alternate = ["Availability"]) val availability: Availability? = null
        ) {

            internal data class Availability(
                @SerializedName("available") val available: Int? = null,
                @SerializedName("occupied") val occupied: Int? = null,
                @SerializedName("reserved") val reserved: Int? = null,
                @SerializedName("unknown") val unknown: Int? = null,
                @SerializedName("outOfService") val outOfService: Int? = null
            )

            internal data class PowerLevel(
                @SerializedName("supported") val supported: List<Supported>? = null,
                // UNSPECIFIED, BATTERY_EXCHANGE, 100_TO_120V_AND_1_PHASE_AT_10A, 100_TO_120V_AND_1_PHASE_AT_12A,
                // 100_TO_120V_AND_1_PHASE_AT_16A, 200_TO_240V_AND_1_PHASE_AT_10A, 200_TO_240V_AND_1_PHASE_AT_12A,
                // 200_TO_240V_AND_1_PHASE_AT_16A, 200_TO_240V_AND_1_PHASE_AT_32A, 200_TO_240V_AND_3_PHASE_AT_16A,
                // 200_TO_240V_AND_3_PHASE_AT_32A, 380_TO_480V_AND_3_PHASE_AT_16A, 380_TO_480V_AND_3_PHASE_AT_32A,
                // 380_TO_480V_AND_3_PHASE_AT_63A, 100_TO_120V_AND_1_PHASE_AT_8A, 200_TO_240V_AND_1_PHASE_AT_8A,
                // 50_TO_500V_DIRECT_CURRENT_62A_25KW, 50_TO_500V_DIRECT_CURRENT_125A_50KW,
                // 200_TO_450V_DIRECT_CURRENT_200A_90KW, 200_TO_480V_DIRECT_CURRENT_255A_120KW

                @SerializedName("chargeTypeAvailability", alternate = ["ChargeTypeAvailable"])
                val chargeTypeAvailability: ChargeTypeAvailability? = null,
                @SerializedName("chargingCapacities", alternate = ["ChargingCapacities"])
                val chargingCapacities: List<ChargingCapacities>? = null
            ) {

                internal enum class Supported {
                    @SerializedName("UNSPECIFIED")
                    UNSPECIFIED,

                    @SerializedName("BATTERY_EXCHANGE")
                    BATTERY_EXCHANGE,

                    @SerializedName("100_TO_120V_AND_1_PHASE_AT_10A")
                    Support100To120vAnd1PhaseAt10a,

                    @SerializedName("100_TO_120V_AND_1_PHASE_AT_12A")
                    Support100To120vAnd1PhaseAtA2a,

                    @SerializedName("100_TO_120V_AND_1_PHASE_AT_16A")
                    Support100To120vAnd1PhaseAt16a,

                    @SerializedName("200_TO_240V_AND_1_PHASE_AT_10A")
                    Support200To240vAnd1PhaseAt10a,

                    @SerializedName("200_TO_240V_AND_1_PHASE_AT_12A")
                    Support200To240vAnd1PhaseAt12a,

                    @SerializedName("200_TO_240V_AND_1_PHASE_AT_16A")
                    Support200To240vAnd1PhaseAt16a,

                    @SerializedName("200_TO_240V_AND_1_PHASE_AT_32A")
                    Support200To240vAnd1PhaseAt32a,

                    @SerializedName("200_TO_240V_AND_3_PHASE_AT_16A")
                    Support200To240vAnd3PhaseAt16a,

                    @SerializedName("200_TO_240V_AND_3_PHASE_AT_32A")
                    Support200To240vAnd3PhaseAt32a,

                    @SerializedName("380_TO_480V_AND_3_PHASE_AT_16A")
                    Support380To480vAnd3PhaseAt16a,

                    @SerializedName("380_TO_480V_AND_3_PHASE_AT_32A")
                    Support380To480vAnd3PhaseAt32a,

                    @SerializedName("380_TO_480V_AND_3_PHASE_AT_63A")
                    Support380To480vAnd3PhaseAt63a,

                    @SerializedName("100_TO_120V_AND_1_PHASE_AT_8A")
                    Support100To120vAnd1PhaseAt8a,

                    @SerializedName("200_TO_240V_AND_1_PHASE_AT_8A")
                    Support200To240vAnd1PhaseAt8a,

                    @SerializedName("50_TO_500V_DIRECT_CURRENT_62A_25KW")
                    Support50To500vDirectCurrent62a25kw,

                    @SerializedName("50_TO_500V_DIRECT_CURRENT_125A_50KW")
                    Support50To500vDirectCurrent125a50kw,

                    @SerializedName("200_TO_450V_DIRECT_CURRENT_200A_90KW")
                    Support200To450vDirectCurrent200a90kw,

                    @SerializedName("200_TO_480V_DIRECT_CURRENT_255A_120KW")
                    Support200To480vDirectCurrent255a120kw
                }

                internal data class ChargeTypeAvailability(
                    @SerializedName("fastCharge") val fastCharge: Int? = null,
                    @SerializedName("regularCharge") val regularCharge: Int? = null,
                    @SerializedName("slowCharge") val slowCharge: Int? = null,
                    @SerializedName("unknown") val unknown: Int? = null
                )

                internal data class ChargingCapacities(
                    @SerializedName("type") val type: Type? = null,
                    @SerializedName("powerKw") val powerKw: Int? = null,
                    @SerializedName("chargingMode") val chargingMode: ChargingMode? = null
                ) {

                    internal enum class Type {
                        @SerializedName("AC")
                        AC,

                        @SerializedName("DC")
                        DC
                    }

                    internal enum class ChargingMode {
                        @SerializedName("SLOW")
                        Slow,

                        @SerializedName("REGULAR")
                        Regular,

                        @SerializedName("FAST")
                        Fast,

                        @SerializedName("UNKNOWN")
                        Unknown
                    }
                }
            }
        }

        internal data class CslProviderData(
            @SerializedName("isOpen24Hours", alternate = ["open24Hours"]) val isOpen24Hours: Boolean? = null,
            @SerializedName("renewableEnergy") val renewableEnergy: Boolean? = null,
            @SerializedName("indoor") val indoor: Boolean? = null,
            @SerializedName("floor") val floor: Int? = null,
            @SerializedName("hotline") val hotline: String? = null,
            @SerializedName("status") val status: Status? = null,
            @SerializedName("access") val access: List<Access>? = null,
            @SerializedName("canBeReserved") val canBeReserved: Boolean? = null
        ) {

            internal enum class Status {
                @SerializedName("UNKNOWN")
                Unknown,

                @SerializedName("AVAILABLE")
                Available,

                @SerializedName("CHARGING")
                Charging,

                @SerializedName("OUT_OF_SERVICE")
                OutOfService,

                @SerializedName("RESERVED")
                Reserved,

                @SerializedName("REMOVED")
                Removed
            }

            internal enum class Access {
                @SerializedName("CHARGING_CARD")
                ChargingCard,

                @SerializedName("APP")
                App,

                @SerializedName("NO_AUTHENTICATION")
                NoAuthentication
            }
        }
    }
}
