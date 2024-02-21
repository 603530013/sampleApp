package com.inetpsa.pims.spaceMiddleware.model.vehicles.details

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Deprecated("should switch to use the new class VehicleResponse")
internal data class FcaVehicleItem(
    val activationSource: String,
    val brandCode: String,
    val channelFeatures: List<ChannelFeatures>,
    val color: String,
    val company: String,
    @SerializedName("connectorType") val connectorType: ConnectorType? = null,
    val customerRegStatus: String,
    val enrollmentStatus: String,
    val fuelType: String,
    val language: String,
    val make: String,
    val market: String,
    val model: String,
    val modelDescription: String,
    val navEnabledHU: Boolean,
    val nickname: String,
    val privacyMode: String,
    val radio: String,
    val regStatus: String,
    val regTimestamp: Long,
    val sdp: String,
    val services: List<Service>,
    val soldRegion: String,
    val subMake: String,
    val svla: Svla,
    val tc: Tc,
    val tcuType: String,
    val tsoBodyCode: String,
    val tsoModelYear: String,
    val vin: String,
    val year: Int
) {

    internal data class Service(val service: String, val serviceEnabled: Boolean, val vehicleCapable: Boolean)

    internal data class Svla(val status: String, val timestamp: Long)

    internal data class Tc(val activation: Activation, val registration: Registration) {

        internal data class Activation(val status: String, val version: String)

        internal data class Registration(val status: String, val version: String)
    }

    internal data class ChannelFeatures(val featureCode: String, val channels: List<String>)

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
