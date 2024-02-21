package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.Dealer
import com.inetpsa.pims.spaceMiddleware.model.dealer.PreferredDealer
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("We should use GetFavoriteDealerPsaExecutor instead")
internal class GetPreferredDealerPsaExecutor(command: BaseCommand) : BasePsaExecutor<Unit, Dealer>(command) {

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Dealer> {
        val request = request(
            PreferredDealer::class.java,
            arrayOf("/me/v1/user_data/favorite_dealer")
        )
        return communicationManager.get<PreferredDealer>(request, MiddlewareCommunicationManager.MymToken)
            .map { it.dealer }
    }
}
