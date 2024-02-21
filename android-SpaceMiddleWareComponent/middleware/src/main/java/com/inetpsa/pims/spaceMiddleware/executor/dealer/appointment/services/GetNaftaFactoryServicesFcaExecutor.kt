package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.BaseNaftaTokenDealerFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.NaftaDealerServicesResponse
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess

internal class GetNaftaFactoryServicesFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseNaftaTokenDealerFcaExecutor<ServicesNaftaLatamInput, NaftaDealerServicesResponse>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?) = ServicesNaftaLatamInput(
        dealerId = parameters has Appointment.BOOKING_ID,
        vin = parameters has Input.VIN,
        mileage = parameters has Appointment.MILEAGE,
        unit = parameters hasEnum Appointment.PARAM_MILEAGE_UNIT
    )

    override suspend fun execute(
        input: ServicesNaftaLatamInput,
        token: String
    ): NetworkResponse<NaftaDealerServicesResponse> {
        val queries = mapOf(Constants.PARAM_MILEAGE to input.mileageMiles.toString())
        val request = request(
            type = NaftaDealerServicesResponse::class.java,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/servicescheduler/factoryservices"),
            headers = headers(token),
            queries = queries
        )

        return communicationManager.get<NaftaDealerServicesResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .ifSuccess { BookingOnlineCache.write(ServiceType.Factory, it.services) }
    }
}
