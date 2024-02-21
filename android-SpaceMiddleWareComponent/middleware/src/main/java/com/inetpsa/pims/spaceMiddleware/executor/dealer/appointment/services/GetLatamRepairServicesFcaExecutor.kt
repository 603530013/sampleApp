package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.LatamDealerServiceResponse
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess

internal class GetLatamRepairServicesFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<ServicesNaftaLatamInput, LatamDealerServiceResponse>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?) = ServicesNaftaLatamInput(
        vin = parameters has Input.VIN,
        dealerId = parameters has Appointment.BOOKING_ID,
        mileage = parameters has Appointment.MILEAGE,
        unit = parameters hasEnum Appointment.PARAM_MILEAGE_UNIT
    )

    override suspend fun execute(input: ServicesNaftaLatamInput): NetworkResponse<LatamDealerServiceResponse> {
        val queries = mapOf(
            Constants.PARAMS_KEY_DEALER_ID to input.dealerId,
            Constants.PARAM_MILEAGE to input.mileageMiles.toString()
        )

        val request = request(
            type = LatamDealerServiceResponse::class.java,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/servicescheduler/repairservices"),
            queries = queries
        )

        return communicationManager.get<LatamDealerServiceResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .ifSuccess { BookingOnlineCache.write(ServiceType.Repair, it.services) }
    }
}
