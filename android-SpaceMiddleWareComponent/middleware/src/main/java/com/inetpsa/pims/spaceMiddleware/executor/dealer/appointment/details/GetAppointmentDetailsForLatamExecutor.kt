package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryLatamMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsLatamResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetAppointmentDetailsForLatamExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>?
) : BaseFcaExecutor<DetailsInput, DetailsOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): DetailsInput = DetailsInput(
        vin = parameters has Constants.PARAM_VIN,
        id = parameters has Constants.PARAM_ID
    )

    override suspend fun execute(input: DetailsInput): NetworkResponse<DetailsOutput> {
        val missingDealerId = PIMSFoundationError.missingParameter(Appointment.BOOKING_ID)
        val dealerId = BookingOnlineCache.readAppointmentFromLatam(input.id)?.dealerId
            ?: return NetworkResponse.Failure(missingDealerId)
        val request = request(
            type = AppointmentDetailsLatamResponse::class.java,
            urls = arrayOf(
                "/v1/accounts/",
                uid,
                "/vehicles/",
                input.vin,
                "/servicescheduler/department/",
                dealerId,
                "/appointment/",
                input.id
            )
        )

        return communicationManager
            .get<AppointmentDetailsLatamResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { AppointmentHistoryLatamMapper().transformOutput(it, input, dealerId) }
    }
}
