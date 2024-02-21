package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class SettingsCallCenterItemResponse(
    @SerializedName("settingType")
    val settingType: String?,
    @SerializedName("version")
    val version: String?,
    @SerializedName("settings")
    val settings: List<CallCenterSettingFca>?
) {

    internal data class CallCenterSettingFca(
        @SerializedName("callCenterType")
        val callCenterType: String?,
        @SerializedName("callType")
        val callType: String?,
        @SerializedName("primaryNumber")
        val primaryNumber: String?,
        @SerializedName("secondaryNumber")
        val secondaryNumber: String?,
        @SerializedName("settingCategory")
        val settingCategory: String?
    ) {

        val callCenterTypeEnum: CallCenterType
            get() = when (callCenterType.orEmpty().uppercase()) {
                CallCenterType.Emergency.input -> CallCenterType.Emergency
                CallCenterType.RoadSide.input -> CallCenterType.RoadSide
                CallCenterType.UConnect.input -> CallCenterType.UConnect
                CallCenterType.Brand.input -> CallCenterType.Brand
                CallCenterType.Stolen.input -> CallCenterType.Stolen
                CallCenterType.Help.input -> CallCenterType.Help
                CallCenterType.Guardian.input -> CallCenterType.Guardian
                else -> CallCenterType.None
            }

        val category: Category?
            get() = when (settingCategory.orEmpty().uppercase()) {
                Category.B_CALL.input -> Category.B_CALL
                Category.E_CALL.input -> Category.E_CALL
                Category.SVLA.input -> Category.SVLA
                Category.ASSIST.input -> Category.ASSIST
                Category.IVA.input -> Category.IVA
                Category.BRAND.input -> Category.BRAND
                else -> null
            }

        enum class CallCenterType(val input: String, val output: String) {
            None("NONE", "none"),
            Emergency("SOS", "emergency"),
            RoadSide("ROADSIDE", "roadSide"),
            UConnect("UCONNECT", "uConnect"),
            Brand("BRAND", "brand"),
            Stolen("SVLA", "stolen"),
            Help("HELP", "help"),
            Guardian("GUARDIAN", "guardian")
        }

        enum class Category(val input: String, val output: String) {
            B_CALL("BCALL", "bCall"),
            E_CALL("ECALL", "eCall"),
            SVLA("SVLA", "svla"),
            ASSIST("ASSIST", "assist"),
            IVA("IVA", "iva"),
            BRAND("BRAND", "brand")
        }
    }
}
