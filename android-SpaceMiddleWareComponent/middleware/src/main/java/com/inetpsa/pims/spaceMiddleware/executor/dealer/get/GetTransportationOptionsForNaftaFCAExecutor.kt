package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment.BOOKING_ID
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department.BaseNaftaDepartmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.DepartmentIdInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess

internal class GetTransportationOptionsForNaftaFCAExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseNaftaDepartmentFcaExecutor<DepartmentIdInput, TransportationOptionsResponse>(
    middlewareComponent,
    params
) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    override fun params(parameters: Map<String, Any?>?) =
        DepartmentIdInput(parameters has BOOKING_ID)

    override suspend fun execute(
        input: DepartmentIdInput,
        token: String,
        departmentId: String
    ): NetworkResponse<TransportationOptionsResponse> {
        // try to read from cache if exist
        BookingOnlineCache.readTransportationOptions()
            ?.let { return NetworkResponse.Success(TransportationOptionsResponse(it)) }

        // if there is no cache we request the data from the server
        val request = request(
            TransportationOptionsResponse::class.java,
            urls = arrayOf(
                "/v1/servicescheduler/department/",
                departmentId,
                "/transportationoption"
            ),
            headers = headers(token)
        )
        return communicationManager
            .get<TransportationOptionsResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .ifSuccess { response -> BookingOnlineCache.write(response) }
    }
}
