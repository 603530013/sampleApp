package com.inetpsa.pims.spaceMiddleware.model.vehicles

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
internal data class VehicleOutput(
    override val vin: String,
    val lcdv: String?,
    val eligibility: List<String>?,
    val attributes: List<String>?,
    val type: Type,
    val name: String,
    val regTimeStamp: Long?,
    val year: Int?,
    val lastUpdate: Long? = 0,
    val sdp: String? = null,
    val market: String? = null,
    val make: String? = null,
    val subMake: String? = null,
    val picture: String?,
    val connectorType: ConnectorType? = null,
    val enrollmentStatus: String? = null
) : Parcelable, VinField {

    internal enum class Type { THERMIC, ELECTRIC, HYBRID, UNKNOWN }

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

    internal enum class TcuType {
        @SerializedName("nonConnected")
        NON_CONNECTED,

        @SerializedName("nonTBMVehicles")
        NON_TBM_VEHICLES,

        @SerializedName("TBMVehicles")
        TBM_VEHICLES,

        @SerializedName("bPilotVehicles")
        B_PILOT_VEHICLES,

        @SerializedName("unknown")
        UNKNOWN
    }
}
