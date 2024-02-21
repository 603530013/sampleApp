package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.DealerFcaMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetDealersFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<DealersInput, DealersOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): DealersInput = DealersInput(
        vin = parameters has Constants.PARAM_VIN,
        latitude = parameters has Constants.PARAM_LAT,
        longitude = parameters has Constants.PARAM_LNG,
        max = parameters hasOrNull Constants.PARAM_MAX
    )

    override suspend fun execute(input: DealersInput): NetworkResponse<DealersOutput> {
        val queries = mutableMapOf(
            Constants.PARAM_LAT to input.latitude.toString(),
            Constants.PARAM_LNG to input.longitude.toString()
        )
        input.max?.let { queries[Constants.PARAM_LIMIT] = it.toString() }

        val vehicle = CachedVehicles.getOrThrow(middlewareComponent, input.vin)

        val request = request(
            type = object : TypeToken<List<DealerDetailsResponse>>() {}.type,
            urls = arrayOf("/v2/accounts/", uid, "/vehicles/", input.vin, "/mydealer/searchdealer"),
            queries = queries
        )

        val mapper = DealerFcaMapper(vehicle)
        return communicationManager.get<List<DealerDetailsResponse>>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { items -> DealersOutput(items.map { transformDealer(mapper, it) }) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformDealer(mapper: DealerFcaMapper, response: DealerDetailsResponse): DealerOutput =
        mapper.transformDealer(response)
}
