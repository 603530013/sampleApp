package com.inetpsa.pims.spaceMiddleware.executor.radioplayer

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.onair.OnAirListPsaRp
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager

@Deprecated("should be replaced with GetOnAirPsaExecutor")
internal class GetOnAirPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<String, OnAirListPsaRp>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_RPUID

    override suspend fun execute(input: String): NetworkResponse<OnAirListPsaRp> {
        val request = request(
            type = OnAirListPsaRp::class.java,
            urls = arrayOf("/shop/v1/stations/", input, "/onair")
        )
        return communicationManager.get(request, MiddlewareCommunicationManager.MymToken)
    }
}
