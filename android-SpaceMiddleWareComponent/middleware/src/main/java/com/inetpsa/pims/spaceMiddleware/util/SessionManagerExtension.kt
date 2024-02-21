package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.userSession.IUserSessionManager
import com.inetpsa.mmx.foundation.userSession.model.TokenResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal suspend fun IUserSessionManager.getToken(tokenType: TokenType): NetworkResponse<String> {
    val tokenResponse = suspendCancellableCoroutine<TokenResponse> { cont ->
        getToken(tokenType) { response -> cont.resume(response) }
    }

    return when (tokenResponse) {
        is TokenResponse.Failure -> NetworkResponse.Failure(tokenResponse.error)
        is TokenResponse.Success -> NetworkResponse.Success(tokenResponse.token)
    }
}
