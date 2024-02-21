package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class RemoveFavoriteDealerPsaExecutor(command: BaseCommand) : BasePsaExecutor<Unit, Unit>(command) {

    companion object {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal const val RESPONSE_SUCCESSFULLY = "successfully deleted!"
    }

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Unit> {
        val request = request(
            String::class.java,
            arrayOf("/me/v1/user_data/favorite_dealer")
        )

        return communicationManager.delete<String>(request, MiddlewareCommunicationManager.MymToken)
            .transform {
                when (it == RESPONSE_SUCCESSFULLY) {
                    true -> {
                        removeDealerFromCache()
                        NetworkResponse.Success(Unit)
                    }

                    else -> {
                        val reason = "Error occurred when delete favorite dealer: $it"
                        val failure = PIMSFoundationError.unknownError(reason)
                        NetworkResponse.Failure(failure)
                    }
                }
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun removeDealerFromCache() {
        middlewareComponent.deleteSync(Constants.Storage.PREFERRED_DEALER, StoreMode.APPLICATION)
        middlewareComponent.deleteSync(Constants.Storage.PREFERRED_DEALER_ID, StoreMode.APPLICATION)
    }
}
