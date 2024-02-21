package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent

internal fun MiddlewareComponent.generateStorageKey(key: String): String {
    val customerID = userSessionManager.getUserSession()?.customerId?.takeIf { it.isNotBlank() }
    val customerIdLabel = customerID?.let { "_$customerID" } ?: ""
    return "${configurationManager.brand}_${configurationManager.environment}${customerIdLabel}_$key"
}
