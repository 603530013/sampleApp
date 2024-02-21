package com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation

import com.google.gson.annotations.SerializedName

internal data class ChargeStationLocatorOutput(
    @SerializedName("chargingStations")
    val chargingStations: List<ChargeStation>? = null
) {

    internal data class ChargeStation(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("link")
        val link: String? = null,
        @SerializedName("locationId")
        val locationId: String? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("openHours")
        val openHours: List<OpeningHours>? = null,
        @SerializedName("accessType")
        val accessType: String? = null,
        @SerializedName("acceptablePayments")
        val acceptablePayments: List<String>? = null,
        @SerializedName("address")
        val address: String? = null,
        @SerializedName("position")
        val position: Position? = null,
        @SerializedName("connectors")
        val connectors: List<Connectors>? = null,
        @SerializedName("providers")
        val providers: Map<String, ProviderInfo>? = null
    ) {

        internal data class OpeningHours(
            @SerializedName("startTime") val startTime: String? = null,
            @SerializedName("endTime") val endTime: String? = null
        )

        internal data class Position(
            @SerializedName("latitude") val latitude: Double? = null,
            @SerializedName("longitude") val longitude: Double? = null
        )

        internal data class Connectors(
            @SerializedName("type") val type: String? = null,
            @SerializedName("compatible") val compatible: Boolean? = null,
            @SerializedName("powerLevel") val powerLevel: PowerLevel? = null,
            @SerializedName("total") val total: Int? = null,
            @SerializedName("availability") val availability: Availability? = null
        ) {

            internal data class Availability(
                @SerializedName("available") val available: Int? = null,
                @SerializedName("occupied") val occupied: Int? = null,
                @SerializedName("reserved") val reserved: Int? = null,
                @SerializedName("unknown") val unknown: Int? = null,
                @SerializedName("outOfService") val outOfService: Int? = null
            )

            internal data class PowerLevel(
                @SerializedName("chargeTypeAvailability")
                val chargeTypeAvailability: ChargeTypeAvailability? = null,
                @SerializedName("chargingCapacities")
                val chargingCapacities: List<ChargingCapacities>? = null
            ) {

                internal data class ChargeTypeAvailability(
                    @SerializedName("fastCharge") val fastCharge: Int? = null,
                    @SerializedName("regularCharge") val regularCharge: Int? = null,
                    @SerializedName("slowCharge") val slowCharge: Int? = null,
                    @SerializedName("unknown") val unknown: Int? = null
                )

                internal data class ChargingCapacities(
                    @SerializedName("type") val type: String? = null,
                    @SerializedName("powerKw") val powerKw: Int? = null,
                    @SerializedName("chargingMode") val chargingMode: String? = null
                )
            }
        }

        internal data class Address(
            @SerializedName("streetName") val streetName: String? = null,
            @SerializedName("freeformAddress") val freeformAddress: String? = null
        )

        internal data class ProviderInfo(
            @SerializedName("open24Hours") val open24Hours: Boolean? = null,
            @SerializedName("renewableEnergy") val renewableEnergy: Boolean? = null,
            @SerializedName("indoor") val indoor: Boolean? = null,
            @SerializedName("floor") val floor: Int? = null,
            @SerializedName("hotline") val hotline: String? = null,
            @SerializedName("status") val status: Status? = null,
            @SerializedName("access") val access: List<Access>? = null,
            @SerializedName("canBeReserved") val canBeReserved: Boolean? = null
        ) {

            internal enum class Status {
                @SerializedName("unknown")
                Unknown,

                @SerializedName("available")
                Available,

                @SerializedName("charging")
                Charging,

                @SerializedName("outOfService")
                OutOfService,

                @SerializedName("reserved")
                Reserved,

                @SerializedName("removed")
                Removed
            }

            internal enum class Access {
                @SerializedName("chargingCard")
                ChargingCard,

                @SerializedName("app")
                App,

                @SerializedName("noAuthentication")
                NoAuthentication
            }
        }
    }
}
