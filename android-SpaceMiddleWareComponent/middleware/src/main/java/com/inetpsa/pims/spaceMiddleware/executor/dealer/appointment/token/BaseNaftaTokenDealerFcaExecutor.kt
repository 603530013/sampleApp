package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.R
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField

internal abstract class BaseNaftaTokenDealerFcaExecutor<P : BookingIdField, out R>(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<P, R>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    override suspend fun execute(input: P): NetworkResponse<R> =
        NaftaTokenManager().withOssToken(middlewareComponent, input) { params, token ->
            execute(params, token)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal fun headers(token: String) = mapOf("dealer-authorization" to token)

    abstract suspend fun execute(input: P, token: String): NetworkResponse<R>
}
