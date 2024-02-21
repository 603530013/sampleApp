package com.inetpsa.pims.spaceMiddleware.executor.radioplayer

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations.StationListPsaRp
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
@Deprecated("should be replaced with GetRadioStationsPsaExecutor")
internal class GetStationsPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<String?, StationListPsaRp>(command) {

    override fun params(parameters: Map<String, Any?>?): String? = parameters hasOrNull Constants.PARAM_COUNTRY

    override suspend fun execute(input: String?): NetworkResponse<StationListPsaRp> {
        val queries = input?.let { mapOf(Constants.PARAM_COUNTRY to it) }
        val request = request(
            StationListPsaRp::class.java,
            arrayOf("/shop/v1/stations"),
            queries = queries
        )

        return communicationManager.get(request, MiddlewareCommunicationManager.MymToken)
    }
}
