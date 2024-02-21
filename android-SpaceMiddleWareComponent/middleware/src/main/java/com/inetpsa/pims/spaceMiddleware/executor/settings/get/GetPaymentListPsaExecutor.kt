package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse
import com.inetpsa.pims.spaceMiddleware.model.settings.PaymentListOutput
import com.inetpsa.pims.spaceMiddleware.util.getToken
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.unwrap

internal class GetPaymentListPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<Unit, PaymentListOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    override suspend fun execute(input: Unit): NetworkResponse<PaymentListOutput> =
        GetSettingsPSAExecutor(middlewareComponent, params)
            .execute()
            .map { transformToPaymentListOutput(it, getToken()) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToPaymentListOutput(
        response: SettingsResponse,
        token: String,
        fallback: PaymentListOutput = PaymentListOutput()
    ): PaymentListOutput =
        when (response.samsPaymentMethodEnable) {
            true ->
                response.samsPaymentMethodUrl
                    .takeIf { !it.isNullOrBlank() }
                    ?.replace("[TOKEN_CVS]", token)
                    ?.let { PaymentListOutput(it) } ?: fallback

            else -> fallback
        }

    @Throws(PIMSError::class)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun getToken(): String =
        middlewareComponent.userSessionManager.getToken(TokenType.IDToken).unwrap()
}
