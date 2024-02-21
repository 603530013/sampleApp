package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token

import java.time.LocalDateTime

internal data class OssTokenCache(
    val token: String,
    val expireTime: LocalDateTime
)
