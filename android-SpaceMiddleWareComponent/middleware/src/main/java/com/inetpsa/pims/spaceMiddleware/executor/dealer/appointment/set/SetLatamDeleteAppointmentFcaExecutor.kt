package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.delete.DeleteNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DeleteDealerAppointmentResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class SetLatamDeleteAppointmentFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<DeleteNaftaLatamInput, Unit>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?) = DeleteNaftaLatamInput(
        vin = parameters has Input.VIN,
        id = parameters has Input.ID,
        dealerId = parameters has Appointment.BOOKING_ID
    )

    override suspend fun execute(input: DeleteNaftaLatamInput): NetworkResponse<Unit> {
        val request = request(
            type = DeleteDealerAppointmentResponse::class.java,
            urls = arrayOf(
                "/v1/accounts/",
                uid,
                "/vehicles/",
                input.vin,
                "/servicescheduler/department/",
                input.dealerId,
                "/appointment/",
                input.id
            )
        )
        return communicationManager
            .delete<DeleteDealerAppointmentResponse>(request, TokenType.AWSToken(SDP))
            .map { Unit }
    }
}
