package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.pims.spaceMiddleware.Constants.CONTEXT_KEY_MARKET
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services.GetEmeaServiceFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services.GetLatamServiceFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services.GetNaftaServiceFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput

internal class GetDealerServicesFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<Map<String, Any?>?, ServicesOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = parameters

    override suspend fun execute(input: Map<String, Any?>?): NetworkResponse<ServicesOutput> = when {
        (configurationManager.market == EMEA || configurationManager.brand == MASERATI) ->
            GetEmeaServiceFcaExecutor(middlewareComponent, input).execute()

        (configurationManager.market == Market.LATAM) ->
            GetLatamServiceFcaExecutor(middlewareComponent, input).execute()

        (configurationManager.market == Market.NAFTA) ->
            GetNaftaServiceFcaExecutor(middlewareComponent, input).execute()

        else -> throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_MARKET)
    }
}
