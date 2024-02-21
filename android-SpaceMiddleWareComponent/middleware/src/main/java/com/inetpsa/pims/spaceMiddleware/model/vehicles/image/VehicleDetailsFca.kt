package com.inetpsa.pims.spaceMiddleware.model.vehicles.image

import com.google.gson.annotations.SerializedName

internal data class VehicleDetailsFca(
    @SerializedName("preciseImageURL") val preciseImageURL: String?,
    @SerializedName("modelDescription") val modelDescription: String?,
    @SerializedName("make") val make: String?,
    @SerializedName("subMake") val subMake: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("vin") var vin: String?,
    @SerializedName("vinLast8") val vinLast8: String?,
    @SerializedName("tcuType") val tcuType: String?,
    @SerializedName("userId") val userId: String?,
    @SerializedName("sdp") val sdp: String?,
    @SerializedName("tcCountryCode") val tcCountryCode: String?
)
