package com.inetpsa.pims.spaceMiddleware.executor.account

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.util.readSync

@Deprecated("try to switch to use this class GetProfilePsaExecutor")
internal class GetProfilePsaExecutor(command: BaseCommand) : BasePsaExecutor<Unit, String>(command) {

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<String> {
        val response: ProfileOutput? = middlewareComponent.readSync(Constants.Storage.PROFILE, StoreMode.APPLICATION)
        val json = transformToJson(response)
        return when (json.isNullOrEmpty()) {
            true -> NetworkResponse.Failure(PIMSFoundationError.invalidReturnParam(Constants.Storage.PROFILE))
            else -> NetworkResponse.Success(json)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToJson(input: ProfileOutput?): String? {
        return input?.let {
            com.inetpsa.pims.spaceMiddleware.model.profile.ProfileOutput(
                uid = input.uid,
                email = input.email,
                firstName = input.firstName,
                lastName = input.lastName,
                civility = input.civility,
                civilityCode = input.civilityCode,
                locale = input.locale,
                phones = input.phones?.values?.toSet()?.toList(),
                address1 = input.address1,
                address2 = input.address2,
                zipCode = input.zipCode,
                city = input.city,
                country = input.country
            ).toJson()
        }
    }
}
