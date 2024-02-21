package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesEmeaMaseratiInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.EmeaDealerServiceResponse
import com.inetpsa.pims.spaceMiddleware.util.filterNotNull
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetEmeaServiceFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<ServicesEmeaMaseratiInput, ServicesOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?) = ServicesEmeaMaseratiInput(
        vin = parameters has Input.VIN,
        dealerId = parameters has Appointment.BOOKING_ID,
        dealerLocation = parameters hasOrNull Appointment.BOOKING_LOCATION
    )

    override suspend fun execute(input: ServicesEmeaMaseratiInput): NetworkResponse<ServicesOutput> {
        val locale = configurationManager.locale
        val queries = mapOf(
            Constants.PARAMS_KEY_DEALER_ID to input.dealerId,
            Constants.PARAM_COUNTRY to locale.country,
            Constants.QUERY_PARAM_KEY_LANGUAGE to locale.language,
            Constants.QUERY_PARAM_KEY_LOCATION to input.dealerLocation
        ).filterNotNull()

        val request = request(
            type = EmeaDealerServiceResponse::class.java,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/servicescheduler/services"),
            queries = queries
        )
        return communicationManager
            .get<EmeaDealerServiceResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { transformToDealerServiceOutput(it) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDealerServiceOutput(response: EmeaDealerServiceResponse): ServicesOutput {
        val dealersOutput = response.servicesList?.mapNotNull { item ->
            item.description.takeIf { !it.isNullOrBlank() }?.let {
                ServicesOutput.Services(id = it)
            }
        }.orEmpty()
        return ServicesOutput(dealersOutput)
    }
}
