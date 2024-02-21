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
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history.GetHistoryForEmeaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history.GetHistoryForLatamFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history.GetHistoryForMaseratiFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history.GetHistoryForNaftaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput

internal class GetAppointmentListFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<Map<String, Any?>, HistoryOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = parameters.orEmpty()

    override suspend fun execute(input: Map<String, Any?>): NetworkResponse<HistoryOutput> =
        when {
            configurationManager.brand == MASERATI -> handleMaseratiFlow(input)
            configurationManager.market == EMEA -> handleEmeaFlow(input)
            configurationManager.market == NAFTA -> handleNaftaFlow(input)
            configurationManager.market == LATAM -> handleLatamFlow(input)
            else -> throw PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleNaftaFlow(input: Map<String, Any?>): NetworkResponse<HistoryOutput> =
        GetHistoryForNaftaFcaExecutor(middlewareComponent, input).execute()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleLatamFlow(input: Map<String, Any?>): NetworkResponse<HistoryOutput> =
        GetHistoryForLatamFcaExecutor(middlewareComponent, input).execute()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleEmeaFlow(input: Map<String, Any?>): NetworkResponse<HistoryOutput> =
        GetHistoryForEmeaFcaExecutor(middlewareComponent, input).execute()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleMaseratiFlow(input: Map<String, Any?>): NetworkResponse<HistoryOutput> =
        GetHistoryForMaseratiFcaExecutor(middlewareComponent, input).execute()
}
