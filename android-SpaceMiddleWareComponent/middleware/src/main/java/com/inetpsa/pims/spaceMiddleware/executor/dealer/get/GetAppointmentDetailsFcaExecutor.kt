package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.LATAM
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details.GetAppointmentDetailsForEmeaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details.GetAppointmentDetailsForLatamExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details.GetAppointmentDetailsForMaseratiExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details.GetAppointmentDetailsForNaftaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput

internal class GetAppointmentDetailsFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<Map<String, Any?>, DetailsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): Map<String, Any?> = parameters.orEmpty()
    override suspend fun execute(input: Map<String, Any?>): NetworkResponse<DetailsOutput> =
        when {
            configurationManager.brand == MASERATI -> handleMaseratiFlow(input)
            configurationManager.market == EMEA -> handleEmeaFlow(input)
            configurationManager.market == NAFTA -> handleNaftaFlow(input)
            configurationManager.market == LATAM -> handleLatamFlow(input)
            else -> throw PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleEmeaFlow(input: Map<String, Any?>): NetworkResponse<DetailsOutput> =
        GetAppointmentDetailsForEmeaExecutor(middlewareComponent, input).execute()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleNaftaFlow(input: Map<String, Any?>): NetworkResponse<DetailsOutput> {
        // TODO Look for  department ID from history cache and then inject it to the input
        return GetAppointmentDetailsForNaftaExecutor(middlewareComponent, input).execute()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleLatamFlow(input: Map<String, Any?>): NetworkResponse<DetailsOutput> {
        // TODO Look for dealer ID from history cache and then inject it to the input
        return GetAppointmentDetailsForLatamExecutor(middlewareComponent, input).execute()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleMaseratiFlow(input: Map<String, Any?>): NetworkResponse<DetailsOutput> =
        GetAppointmentDetailsForMaseratiExecutor(middlewareComponent, input).execute()
}
