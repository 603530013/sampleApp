package com.inetpsa.pims.spaceMiddleware.model.radioplayer.recommendations

/**
 * Created on 17/11/22.
 */@Deprecated("should be replaced by a GetRadioPlayerRecommendationsPsaParams")
internal data class GetRecommendationListPsaRpParams(
    val country: String,
    val rpuid: String?,
    val factors: String, // separated by commas
    val latitude: Float?,
    val longitude: Float?
)
