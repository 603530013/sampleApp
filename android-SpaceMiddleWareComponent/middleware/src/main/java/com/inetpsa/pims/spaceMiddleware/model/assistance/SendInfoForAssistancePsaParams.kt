package com.inetpsa.pims.spaceMiddleware.model.assistance

import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.Constants

internal data class SendInfoForAssistancePsaParams(
    @SerializedName(value = Constants.BODY_PARAM_VIN) val vin: String,
    @SerializedName(value = Constants.BODY_PARAM_CATEGORY) val category: String,
    @SerializedName(value = Constants.BODY_PARAM_LATITUDE) val latitude: Double,
    @SerializedName(value = Constants.BODY_PARAM_LONGITUDE) val longitude: Double,
    @SerializedName(value = Constants.BODY_PARAM_PHONE_NUMBER) val phoneNumber: String,
    @SerializedName(value = Constants.BODY_PARAM_COUNTRY) val country: String?
)
