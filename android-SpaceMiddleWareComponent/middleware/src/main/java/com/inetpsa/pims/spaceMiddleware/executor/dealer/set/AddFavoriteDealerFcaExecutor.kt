package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerFavoriteInput

internal class AddFavoriteDealerFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<DealerFavoriteInput, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?) = DealerFavoriteInput(
        vin = parameters has Constants.Input.VIN,
        id = parameters has Constants.Input.ID
    )

    override suspend fun execute(input: DealerFavoriteInput): NetworkResponse<Unit> {
        require(!input.vin.isNullOrBlank())

        val bodyJson = mapOf(
            Constants.PARAMS_KEY_DEALER_ID to input.id
        )
        val request = request(
            type = String::class.java,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/mydealer/preferredDealer"),
            body = bodyJson.toJson()

        )
        val response = communicationManager.post<String>(request, TokenType.AWSToken(FCAApiKey.SDP))

        return response.let { NetworkResponse.Success(Unit) }
    }
}
