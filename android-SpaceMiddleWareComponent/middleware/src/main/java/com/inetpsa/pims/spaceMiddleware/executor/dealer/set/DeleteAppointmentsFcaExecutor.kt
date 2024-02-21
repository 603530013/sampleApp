package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set.SetEmeaDeleteAppointmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set.SetLatamDeleteAppointmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.set.SetNaftaDeleteAppointmentFcaExecutor

internal class DeleteAppointmentsFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<Map<String, Any?>?, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): Map<String, Any?>? = parameters

    override suspend fun execute(input: Map<String, Any?>?): NetworkResponse<Unit> = when {
        (configurationManager.market == EMEA) ->
            SetEmeaDeleteAppointmentFcaExecutor(middlewareComponent, input).execute()

        (configurationManager.market == Market.LATAM) ->
            SetLatamDeleteAppointmentFcaExecutor(middlewareComponent, input).execute()

        (configurationManager.market == Market.NAFTA) ->
            SetNaftaDeleteAppointmentFcaExecutor(middlewareComponent, input).execute()

        else -> throw PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
    }
}
