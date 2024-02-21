package com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer

import com.google.gson.annotations.SerializedName

@Suppress("LongParameterList")
internal class ReviewResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("vehicle_id_type") val vehicleIdType: String? = null,
    @SerializedName("VIN_mask") val vinMask: String? = null,
    @SerializedName("review_max_date") val reviewMaxDate: String? = null,
    @SerializedName("review_max_month") val reviewMaxMonth: String? = null,
    @SerializedName("review_min_delta") val reviewMinDelta: String? = null,
    @SerializedName("review_max_char") val reviewMaxChar: String? = null,
    @SerializedName("rating_negative_floor") val ratingNegativeFloor: String? = null,
    @SerializedName("CGU_link") val cguLink: String? = null,
    @SerializedName("allowed") val allowed: Boolean? = null
)
