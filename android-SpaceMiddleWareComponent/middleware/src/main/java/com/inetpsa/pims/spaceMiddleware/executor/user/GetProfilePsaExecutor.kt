package com.inetpsa.pims.spaceMiddleware.executor.user

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetProfilePsaExecutor(command: BaseCommand) : BasePsaExecutor<UserInput, ProfileOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) =
        UserInput(
            action = parameters hasEnum Constants.Input.ACTION,
            vin = parameters hasOrNull Constants.Input.VIN
        )

    override suspend fun execute(input: UserInput): NetworkResponse<ProfileOutput> {
        if (input.action == Action.Get) {
            readFromCache()?.let { profile ->
                return NetworkResponse.Success(profile)
            }
        }

        if (input.action == Action.Refresh || input.action == Action.Get) {
            return GetUserPsaExecutor(middlewareComponent, params).execute(input.vin).transform {
                readFromCache()
                    ?.let { NetworkResponse.Success(it) }
                    ?: NetworkResponse.Failure(PIMSFoundationError.invalidReturnParam(Constants.Storage.PROFILE))
            }
        }

        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(): ProfileOutput? =
        middlewareComponent.readSync(Constants.Storage.PROFILE, StoreMode.APPLICATION)
}
