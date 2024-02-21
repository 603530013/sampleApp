package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.transform

@Deprecated("should be replaced with RemoveFavoriteDealerPsaExecutor")
internal class RemovePreferredDealerPsaExecutor(command: BaseCommand) : BasePsaExecutor<Unit, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Unit> {
        val request = request(
            String::class.java,
            arrayOf("/me/v1/user_data/favorite_dealer")
        )

        return communicationManager.delete<String>(request, MiddlewareCommunicationManager.MymToken).transform {
            when (it == "successfully deleted!") {
                true -> Success(Unit)

                else -> {
                    val failure = PimsErrors.serverError(
                        null,
                        "error occurred when add preferred dealer $it"
                    )
                    NetworkResponse.Failure(failure)
                }
            }
        }
    }
}
