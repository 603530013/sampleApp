package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.R
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.BaseNaftaTokenDealerFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.NaftaTokenManager
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.DepartmentIdInput
import com.inetpsa.pims.spaceMiddleware.util.transform

internal abstract class BaseNaftaDepartmentFcaExecutor<P : BookingIdField, out R>(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseNaftaTokenDealerFcaExecutor<P, R>(middlewareComponent, params) {

    override suspend fun execute(input: P): NetworkResponse<R> =
        NaftaTokenManager().withOssToken(middlewareComponent, input) { params, token ->
            execute(params, token)
        }

    abstract suspend fun execute(input: P, token: String, departmentId: String): NetworkResponse<R>
    override suspend fun execute(input: P, token: String): NetworkResponse<R> =
        GetNaftaDepartmentIdExecutor(middlewareComponent, params)
            .execute(DepartmentIdInput(input.dealerId))
            .transform { execute(input, token, it.toString()) }
}
