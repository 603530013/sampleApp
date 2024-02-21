package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class VehicleImageResponse(
    @SerializedName("vin", alternate = ["vinLast8"]) val vin: String?,
    @SerializedName("preciseImageURL") val imageUrl: String?,
    @SerializedName("modelDescription") val description: String? = null,
    @SerializedName("make") val make: String? = null,
    @SerializedName("subMake") val subMake: String? = null,
    @SerializedName("year") val year: Int?,
    @SerializedName("tcuType") val tcuType: String? = null,
    @SerializedName("sdp") val sdp: String? = null,
    @SerializedName("userId") val userid: String? = null,
    @SerializedName("tcCountryCode") val tcCountryCode: String? = null
)
