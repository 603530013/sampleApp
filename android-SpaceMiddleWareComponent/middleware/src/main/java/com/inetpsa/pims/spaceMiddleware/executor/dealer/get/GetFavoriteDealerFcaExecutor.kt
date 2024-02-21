package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.DealerFcaMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.map
import java.net.HttpURLConnection

internal class GetFavoriteDealerFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<UserInput, DealersOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = UserInput(
        action = parameters hasEnum Input.ACTION,
        vin = parameters has Input.VIN
    )

    override suspend fun execute(input: UserInput): NetworkResponse<DealersOutput> {
        require(!input.vin.isNullOrBlank())

        val vehicle = CachedVehicles.getOrThrow(middlewareComponent, input.vin)

        val request = request(
            type = object : TypeToken<List<DealerDetailsResponse>>() {}.type,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/mydealer/preferredDealer")
        )
        val mapper = DealerFcaMapper(vehicle)
        return communicationManager.get<List<DealerDetailsResponse>>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map(
                transformSuccess = { items -> DealersOutput(items.map { mapper.transformDealer(it) }) },
                transformFailure = { responseError ->
                    when (responseError?.subError?.status) {
                        HttpURLConnection.HTTP_NOT_FOUND -> NetworkResponse.Success(DealersOutput(emptyList()))
                        else -> NetworkResponse.Failure(responseError)
                    }
                }
            )
    }
}
