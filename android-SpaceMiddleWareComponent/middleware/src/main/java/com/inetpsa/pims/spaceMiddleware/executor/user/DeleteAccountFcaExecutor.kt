package com.inetpsa.pims.spaceMiddleware.executor.user

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.util.getToken
import com.inetpsa.pims.spaceMiddleware.util.mapStrongAuthFailure
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class DeleteAccountFcaExecutor(command: BaseCommand) : BaseFcaExecutor<Unit, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Unit> =
        getPinToken().transform { pinAuth ->
            val body = mapOf(Constants.PARAM_PIN_AUTH to pinAuth)
            val request = request(
                type = Unit::class.java,
                urls = arrayOf("/v1/accounts/", uid, "/deactivateAccount"),
                body = body.toJson()
            )

            communicationManager
                .delete<Unit>(request, TokenType.AWSToken(FCAApiKey.SDP))
                .mapStrongAuthFailure()
        }

    @Throws(PIMSError::class)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend inline fun getPinToken(): NetworkResponse<String> =
        middlewareComponent.userSessionManager.getToken(TokenType.OTPToken)
}
