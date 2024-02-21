package com.inetpsa.pims.spaceMiddleware.executor.features.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.features.mapper.FeaturesFcaOutputMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum

internal class GetFeaturesFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<UserInput, FeaturesOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): UserInput {
        val action: Action = parameters hasEnum Constants.Input.ACTION
        if (action != Action.Get && action != Action.Refresh) {
            throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }
        return UserInput(
            action = action,
            vin = parameters has Constants.Input.VIN
        )
    }

    override suspend fun execute(input: UserInput): NetworkResponse<FeaturesOutput> {
        require(!input.vin.isNullOrBlank())

        val vehicle = CachedVehicles.getOrThrow(middlewareComponent, input.vin, input.action)
        val feature = FeaturesFcaOutputMapper(vehicle).transformFeatureOutput()
        return NetworkResponse.Success(feature)
    }
}
