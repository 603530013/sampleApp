package com.inetpsa.pims.spaceMiddleware.model.dealer

import com.google.gson.annotations.SerializedName

internal data class GetReviewOutput(
    @SerializedName("reviewMaxDate") val reviewMaxDate: String? = null,
    @SerializedName("reviewMaxMonth") val reviewMaxMonth: String? = null,
    @SerializedName("reviewMinDelta") val reviewMinDelta: String? = null,
    @SerializedName("reviewMaxChar") val reviewMaxChar: String? = null,
    @SerializedName("ratingNegativeFloor") val ratingNegativeFloor: String? = null,
    @SerializedName("cguLink") val cguLink: String? = null,
    @SerializedName("allowed") val allowed: Boolean? = null
)
