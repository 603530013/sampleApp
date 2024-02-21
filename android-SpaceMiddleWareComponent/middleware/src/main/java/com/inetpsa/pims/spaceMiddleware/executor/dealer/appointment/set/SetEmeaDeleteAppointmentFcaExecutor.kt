package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.delete.DeleteEmeaMaseratiInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DeleteDealerAppointmentResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class SetEmeaDeleteAppointmentFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<DeleteEmeaMaseratiInput, Unit>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?) = DeleteEmeaMaseratiInput(
        vin = parameters has Input.VIN,
        id = parameters has Input.ID
    )

    override suspend fun execute(input: DeleteEmeaMaseratiInput): NetworkResponse<Unit> {
        val request = request(
            type = DeleteDealerAppointmentResponse::class.java,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/servicescheduler/appointment/", input.id)
        )
        return communicationManager
            .delete<DeleteDealerAppointmentResponse>(request, TokenType.AWSToken(SDP))
            .map { Unit }
    }
}
