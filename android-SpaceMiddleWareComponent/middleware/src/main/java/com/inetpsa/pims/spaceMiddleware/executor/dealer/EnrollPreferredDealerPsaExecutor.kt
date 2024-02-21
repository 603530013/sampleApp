package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.transform

@Deprecated("We should use AddFavoriteDealerPsaExecutor instead")
internal class EnrollPreferredDealerPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<String, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): String =
        parameters has Constants.PARAM_SITE_GEO

    override suspend fun execute(input: String): NetworkResponse<Unit> {
        val request = request(
            String::class.java,
            arrayOf("/me/v1/user_data/favorite_dealer"),
            body = "{\"${Constants.PARAM_SITE_GEO}\":\"$input\"}"
        )
        return communicationManager.post<String>(request, MiddlewareCommunicationManager.MymToken).transform {
            when (it == "Favourite dealer successfully added!") {
                true -> Success(Unit)

                else -> {
                    val failure = PimsErrors.serverError(
                        null,
                        "error occurred when adding preferred dealer $it"
                    )
                    NetworkResponse.Failure(failure)
                }
            }
        }
    }
}
