package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.BaseNaftaTokenDealerFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.DepartmentIdInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesBodyRequest
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DepartmentIDResponse
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetNaftaDepartmentIdExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseNaftaTokenDealerFcaExecutor<DepartmentIdInput, Int>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): DepartmentIdInput =
        DepartmentIdInput(
            dealerId = parameters has Appointment.BOOKING_ID,
            services = parameters.hasOrNull(Appointment.SERVICES)
        )

    override suspend fun execute(input: DepartmentIdInput, token: String): NetworkResponse<Int> {
        BookingOnlineCache.readDepartmentId(input)
            ?.let { return NetworkResponse.Success(it) }

        val services = input.services?.sorted()?.map { ServicesBodyRequest.Service(id = it) }.orEmpty()
        val body = ServicesBodyRequest(services).toJson()

        val request = request(
            object : TypeToken<DepartmentIDResponse>() {}.type,
            arrayOf("/v1/servicescheduler/dealerdepartment"),
            body = body,
            headers = headers(token)
        )

        return communicationManager
            .post<DepartmentIDResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .ifSuccess { BookingOnlineCache.write(input, it.id) }
            .map { it.id }
    }
}
