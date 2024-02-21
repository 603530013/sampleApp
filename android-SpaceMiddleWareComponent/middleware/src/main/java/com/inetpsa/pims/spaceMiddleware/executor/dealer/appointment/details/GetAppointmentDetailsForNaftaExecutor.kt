package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department.BaseNaftaDepartmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryNaftaMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsNaftaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsNaftaResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetAppointmentDetailsForNaftaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseNaftaDepartmentFcaExecutor<DetailsNaftaInput, DetailsOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): DetailsNaftaInput {
        val vin: String = parameters has Constants.PARAM_VIN
        val id: String = parameters has Constants.PARAM_ID
        return DetailsNaftaInput(
            vin = vin,
            id = id,
            dealerId = BookingOnlineCache.readAppointmentFromNafta(id)?.dealerId
                ?: throw PIMSFoundationError.missingParameter(Appointment.BOOKING_ID)
        )
    }

    override suspend fun execute(
        input: DetailsNaftaInput,
        token: String,
        departmentId: String
    ): NetworkResponse<DetailsOutput> {
        val request = request(
            type = AppointmentDetailsNaftaResponse::class.java,
            urls = arrayOf(
                "/v1/accounts/",
                uid,
                "/vehicles/",
                input.vin,
                "/servicescheduler/department/",
                departmentId,
                "/appointment/",
                input.id
            ),
            headers = headers(token)
        )
        return communicationManager
            .get<AppointmentDetailsNaftaResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { AppointmentHistoryNaftaMapper().transformOutput(it, input) }
    }
}
