package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class AddFavoriteDealerPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<String, Unit>(command) {

    companion object {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal const val RESPONSE_SUCCESSFULLY = "Favourite dealer successfully added!"

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal const val PARAM_SITE_GEO = "site_geo"
    }

    override fun params(parameters: Map<String, Any?>?): String =
        parameters has Constants.Input.ID

    override suspend fun execute(input: String): NetworkResponse<Unit> {
        val request = request(
            String::class.java,
            arrayOf("/me/v1/user_data/favorite_dealer"),
            body = "{\"${PARAM_SITE_GEO}\":\"$input\"}"
        )
        return communicationManager.post<String>(request, MiddlewareCommunicationManager.MymToken)
            .transform {
                if (it == RESPONSE_SUCCESSFULLY) {
                    removeDealerFromCache()
                    // save favorite dealer
                    saveDealerId(input)

                    NetworkResponse.Success(Unit)
                } else {
                    val reason = "Error occurred when adding favorite dealer: $it"
                    val failure = PIMSFoundationError.unknownError(reason)
                    NetworkResponse.Failure(failure)
                }
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun removeDealerFromCache() {
        middlewareComponent.deleteSync(Constants.Storage.PREFERRED_DEALER_ID, StoreMode.APPLICATION)
        middlewareComponent.deleteSync(Constants.Storage.PREFERRED_DEALER, StoreMode.APPLICATION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveDealerId(id: String): Boolean {
        return middlewareComponent.createSync(Constants.Storage.PREFERRED_DEALER_ID, id, StoreMode.APPLICATION)
    }
}
