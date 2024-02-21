package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

internal data class ServiceSchedulerToken(
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: String
)
