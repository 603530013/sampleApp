package com.inetpsa.pims.spaceMiddleware.executor.user

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.asJson
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.user.GigyaAccountResponse
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.util.fromJson

internal class GetProfileFcaExecutor(command: BaseCommand) : BaseFcaExecutor<Unit, ProfileOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<ProfileOutput> =
        when (val response = readFromCache()?.let { transformToProfileOutput(it) }) {
            null -> NetworkResponse.Failure(PIMSFoundationError.invalidReturnParam(Constants.Storage.PROFILE))

            else -> NetworkResponse.Success(response)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(): GigyaAccountResponse? =
        middlewareComponent.userSessionManager.getFCAUserProfile()?.asJson()?.fromJson()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToProfileOutput(response: GigyaAccountResponse): ProfileOutput {
        val userId = response.uid.takeIf { !it.isNullOrBlank() }
            ?: throw PIMSFoundationError.invalidReturnParam("uid")
        return ProfileOutput(
            uid = userId,
            email = response.profile?.email,
            firstName = response.profile?.firstName,
            lastName = response.profile?.lastName,
            civility = null,
            civilityCode = null,
            locale = response.profile?.locale,
            phones = response.profile?.phones
                ?.filter { !it.type.isNullOrBlank() && !it.number.isNullOrBlank() }
                ?.associate { it.type!! to it.number!! },
            address1 = response.profile?.address,
            address2 = null,
            zipCode = response.profile?.zip,
            city = response.profile?.city,
            country = response.profile?.country,
            image = response.profile?.photoURL
        )
    }
}
