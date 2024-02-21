package com.inetpsa.pims.spaceMiddleware.model.vehicles.list

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.AccountVehicleInfoPsa.Dealers

@Deprecated("We should switch to use VehiclesOutput")
internal data class Vehicles(val vehiclesList: List<Vehicle>) {

    internal data class Vehicle(
        val vin: String,
        val lcdv: String,
        val eligibility: List<String>?,
        val attributes: List<String>?,
        val servicesConnected: String,
        val preferredDealer: Dealers?,
        val type: Type,
        val name: String,
        val regTimeStamp: Long,
        val nickname: String,
        val year: Int,
        val settingsUpdate: Long? = 0,
        val sdp: String? = "",
        val market: String? = "",
        val make: String? = "",
        val connectorType: ConnectorType? = null
    ) {

        internal enum class Type { THERMIC, ELECTRIC, HYBRID }

        @Keep
        internal enum class ConnectorType {

            @SerializedName("TYPE_1_YAZAKI")
            TYPE_1_YAZAKI,

            @SerializedName("TYPE_2_MENNEKES")
            TYPE_2_MENNEKES,

            @SerializedName("GBT_PART_2")
            GBT_PART_2,

            @SerializedName("TYPE_1_CCS")
            TYPE_1_CCS,

            @SerializedName("TYPE_2_CCS")
            TYPE_2_CCS,

            @SerializedName("TYPE_3")
            TYPE_3,

            @SerializedName("CHADEMO")
            CHADEMO,

            @SerializedName("GBT_PART_3")
            GBT_PART_3,

            @SerializedName("DOMESTIC_PLUG_GENERIC")
            DOMESTIC_PLUG_GENERIC,

            @SerializedName("NEMA_5_20")
            NEMA_5_20,

            @SerializedName("INDUSTRIAL_BLUE")
            INDUSTRIAL_BLUE,

            @SerializedName("INDUSTRIAL_RED")
            INDUSTRIAL_RED,

            @SerializedName("INDUSTRIAL_WHITE")
            INDUSTRIAL_WHITE,

            @SerializedName("UNKNOWN")
            UNKNOWN
        }
    }
}
