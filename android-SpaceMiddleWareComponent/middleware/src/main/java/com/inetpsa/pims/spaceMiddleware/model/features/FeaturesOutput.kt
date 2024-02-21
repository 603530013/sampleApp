package com.inetpsa.pims.spaceMiddleware.model.features

import com.google.gson.annotations.SerializedName

data class FeaturesOutput(
    @SerializedName("features")
    val features: List<Feature>? = null
) {

    data class Feature(

        @SerializedName("code") val code: String? = null,

        @SerializedName("value") val value: String? = null,

        @SerializedName("config") val config: Config? = null,

        @SerializedName("status") val status: Status
    ) {

        enum class Status {
            @SerializedName("enable")
            ENABLE,

            @SerializedName("capable")
            CAPABLE
        }

        data class Config(

            @SerializedName("schedule") val schedule: Int? = null,

            @SerializedName("shared") val shared: Boolean? = null,

            @SerializedName("repeat") val repeat: Boolean? = null,

            @SerializedName("daysType") val daysType: List<DaysType?>? = null,

            @SerializedName("version") val version: Float? = null,

            @SerializedName("engine") val engine: Engine? = null,

            @SerializedName("type") val type: Type? = null,

            @SerializedName("protocol") val protocol: Protocol? = null
        ) {

            enum class Engine {
                @SerializedName("ICE")
                ICE,

                @SerializedName("BEV")
                BEV,

                @SerializedName("PHEV")
                PHEV,

                @SerializedName("unknown")
                UNKNONW
            }

            enum class Type {
                @SerializedName("internal")
                INTERNAL,

                @SerializedName("external")
                EXTERNAL,

                @SerializedName("partner")
                PARTNER
            }

            enum class Protocol {
                @SerializedName("network")
                NETWORK,

                @SerializedName("BLE")
                BLE
            }

            enum class DaysType {
                @SerializedName("chooseDays")
                CHOOSE_DAYS,

                @SerializedName("workdays")
                WORK_DAYS,

                @SerializedName("weekend")
                WEEKEND,

                @SerializedName("daily")
                DAILY
            }
        }
    }
}
