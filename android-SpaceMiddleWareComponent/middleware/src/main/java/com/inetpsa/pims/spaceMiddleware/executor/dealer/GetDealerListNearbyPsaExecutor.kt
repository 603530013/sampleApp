package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.Dealers
import com.inetpsa.pims.spaceMiddleware.model.dealer.GetDealerListPsaParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

@Deprecated("should be replaced with GetDealerPsaExecutor")
internal class GetDealerListNearbyPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<GetDealerListPsaParams, Dealers>(command) {

    override fun params(parameters: Map<String, Any?>?): GetDealerListPsaParams = GetDealerListPsaParams(
        parameters has Constants.PARAM_LAT,
        parameters has Constants.PARAM_LNG,
        parameters hasOrNull Constants.PARAM_RESULT_MAX
    )

    override suspend fun execute(input: GetDealerListPsaParams): NetworkResponse<Dealers> {
        val queries = mapOf(
            Constants.PARAM_LAT to input.latitude,
            Constants.PARAM_LNG to input.longitude
        )

        val request = request(
            type = Dealers::class.java,
            urls = arrayOf("/shop/v1/dealers/near"),
            queries = queries
        )
        return communicationManager.get(request, MiddlewareCommunicationManager.MymToken)
    }
}
