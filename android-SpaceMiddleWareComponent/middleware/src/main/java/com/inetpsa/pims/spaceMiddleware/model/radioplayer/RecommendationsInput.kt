package com.inetpsa.pims.spaceMiddleware.model.radioplayer

internal data class RecommendationsInput(
    val country: String,
    val id: String?,
    val factors: String,
    val latitude: Double?,
    val longitude: Double?
)
