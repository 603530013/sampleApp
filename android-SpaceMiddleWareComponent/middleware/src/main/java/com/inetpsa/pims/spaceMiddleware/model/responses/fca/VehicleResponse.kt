package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class VehicleResponse(
    @SerializedName("activationSource") val activationSource: String? = null,
    @SerializedName("authorizedPartner") val authorizedPartner: String? = null,
    @SerializedName("brandCode") val brandCode: String? = null,
    @SerializedName("channelFeatures") val channelFeatures: List<ChannelFeature>? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("company") val company: String? = null,
    @SerializedName("connectorType") val connectorType: ConnectorType? = null,
    @SerializedName("consents") val consents: Consents? = null,
    @SerializedName("cslProvider") val cslProvider: CslProvider? = null,
    @SerializedName("customerRegStatus") val customerRegStatus: String? = null,
    @SerializedName("enrollmentStatus") val enrollmentStatus: String? = null,
    @SerializedName("fuelType") val fuelType: String? = null,
    @SerializedName("isCompanyCar") val isCompanyCar: Boolean? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("make") val make: String? = null,
    @SerializedName("market") val market: String? = null,
    @SerializedName("model") val model: String? = null,
    @SerializedName("modelDescription") val modelDescription: String? = null,
    @SerializedName("navEnabledHU") val navEnabledHU: Boolean? = null,
    @SerializedName("nickname") val nickname: String? = null,
    @SerializedName("pp") val pp: Pp? = null,
    @SerializedName("privacyMode") val privacyMode: String? = null,
    @SerializedName("radio") val radio: String? = null,
    @SerializedName("regStatus") val regStatus: String? = null,
    @SerializedName("regTimestamp") val regTimestamp: Long? = null,
    @SerializedName("sdp") val sdp: String? = null,
    @SerializedName("services") val services: List<Service>? = null,
    @SerializedName("soldRegion") val soldRegion: String? = null,
    @SerializedName("subMake") val subMake: String? = null,
    @SerializedName("svla") val svla: Svla? = null,
    @SerializedName("tc") val tc: Tc? = null,
    @SerializedName("tcuType") val tcuType: String? = null,
    @SerializedName("tsoBodyCode") val tsoBodyCode: String? = null,
    @SerializedName("tsoModelYear") val tsoModelYear: String? = null,
    @SerializedName("vehicleLegalDocuments") val vehicleLegalDocuments: List<VehicleLegalDocuments>? = null,
    @SerializedName("vehicleLegalDocumentsToReview")
    val vehicleLegalDocumentsToReview: List<VehicleLegalDocumentsToReview>? = null,
    @SerializedName("vehicleType") val vehicleType: String? = null,
    @SerializedName("vin") val vin: String,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("preciseImageURL") var imageUrl: String? = null
) {

    companion object {

        const val IGNITE = "IGNITE"
        const val SXM = "SXM"
        const val SPRINT = "SPRINT"
    }

    val sdpEnum: SDP
        get() = when (sdp?.uppercase()) {
            SXM -> SDP.SXM
            SPRINT -> SDP.SPRINT
            else -> SDP.IGNITE
        }

    @Keep
    internal data class ChannelFeature(
        @SerializedName("channels") val channels: List<String>? = null,
        @SerializedName("featureCode") val featureCode: String? = null
    )

    @Keep
    internal data class Pp(@SerializedName("activation") val activation: Activation? = null)

    @Keep
    internal data class Service(
        @SerializedName("service") val service: String,
        @SerializedName("serviceEnabled") val serviceEnabled: Boolean,
        @SerializedName("vehicleCapable") val vehicleCapable: Boolean
    )

    @Keep
    internal data class Svla(
        @SerializedName("status") val status: String? = null,
        @SerializedName("timestamp") val timestamp: Long? = null
    )

    @Keep
    internal data class Tc(
        @SerializedName("activation") val activation: Activation? = null,
        @SerializedName("registration") val registration: Registration? = null
    ) {

        @Keep
        internal data class Registration(
            @SerializedName("status") val status: String? = null,
            @SerializedName("version") val version: String? = null
        )
    }

    @Keep
    internal data class Activation(
        @SerializedName("status") val status: String? = null,
        @SerializedName("version") val version: String? = null
    )

    @Keep
    internal data class Consents(
        @SerializedName("thirdPartyProfiling") val thirdPartyProfiling: ThirdPartyProfiling? = null,
        @SerializedName("fcaProfiling") val fcaProfiling: FCAProfiling? = null
    )

    @Keep
    internal data class ThirdPartyProfiling(
        @SerializedName("status") val status: String? = null,
        @SerializedName("creationTimestamp") val creationTimestamp: Long? = null,
        @SerializedName("updateTimestamp") val updateTimestamp: Long? = null
    )

    @Keep
    internal data class FCAProfiling(
        @SerializedName("status") val status: String? = null,
        @SerializedName("creationTimestamp") val creationTimestamp: Long? = null,
        @SerializedName("updateTimestamp") val updateTimestamp: Long? = null
    )

    @Keep
    internal data class VehicleLegalDocuments(
        @SerializedName("documentType") val documentType: String? = null,
        @SerializedName("countryCode") val countryCode: String? = null,
        @SerializedName("status") val status: String? = null,
        @SerializedName("version") val version: String? = null,
        @SerializedName("updatedAt") val updatedAt: String? = null
    )

    @Keep
    internal data class VehicleLegalDocumentsToReview(
        @SerializedName("documentType") val documentType: String? = null,
        @SerializedName("countryCode") val countryCode: String? = null,
        @SerializedName("status") val status: String? = null,
        @SerializedName("latestVersion") val version: String? = null,
        @SerializedName("acceptanceRequired") val updatedAt: Boolean? = null
    )

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

    @Keep
    internal enum class CslProvider {

        @SerializedName("NONE")
        NONE,

        @SerializedName("F2M")
        F2M,

        @SerializedName("TOMTOM")
        TOMTOM,

        @SerializedName("BOSCH")
        BOSCH
    }

    @Keep
    internal enum class SDP {

        SXM, SPRINT, IGNITE
    }

    internal enum class EngineType { ICE, ELECTRIC, HYBRID, UNKNOWN }

    internal enum class TcuType {
        NON_CONNECTED,
        NON_TBM_VEHICLES,
        TBM_VEHICLES,
        B_PILOT_VEHICLES,
        UNKNOWN
    }

    val engineType: EngineType
        get() = when {
            fuelType.equals("e", true) -> EngineType.ELECTRIC
            fuelType.equals("h", true) -> EngineType.HYBRID
            fuelType.equals("d", true) -> EngineType.ICE
            fuelType.equals("g", true) -> EngineType.ICE
            else -> EngineType.UNKNOWN
        }

    val tcu: TcuType
        get() = when {
            tcuType.equals("0", true) -> TcuType.NON_CONNECTED
            tcuType.equals("1", true) -> TcuType.NON_TBM_VEHICLES
            tcuType.equals("2", true) -> TcuType.TBM_VEHICLES
            tcuType.equals("4", true) -> TcuType.B_PILOT_VEHICLES
            else -> TcuType.UNKNOWN
        }
}
