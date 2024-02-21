package com.inetpsa.pims.spaceMiddleware.executor.user

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileInput
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.asJson
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class SetProfilePsaExecutor(command: BaseCommand) : BasePsaExecutor<ProfileInput, Unit>(command) {

    companion object {

        const val RESPONSE_SUCCESSFULLY = "Successfully Updated"
    }

    override fun params(parameters: Map<String, Any?>?): ProfileInput {
        val profile: Map<String, String?> = parameters has Constants.PARAMS_KEY_PROFILE
        if (profile.isEmpty()) {
            throw PIMSFoundationError.missingParameter(Constants.PARAMS_KEY_PROFILE)
        }

        return ProfileInput(
            firstName = profile hasOrNull Constants.Input.Profile.FIRST_NAME,
            lastName = profile hasOrNull Constants.Input.Profile.LAST_NAME,
            civility = profile hasOrNull Constants.Input.Profile.CIVILITY,
            address1 = profile hasOrNull Constants.Input.Profile.ADDRESS_1,
            address2 = profile hasOrNull Constants.Input.Profile.ADDRESS_2,
            zipCode = profile hasOrNull Constants.Input.Profile.ZIP_CODE,
            city = profile hasOrNull Constants.Input.Profile.CITY,
            country = profile hasOrNull Constants.Input.Profile.COUNTRY,
            mobile = profile hasOrNull Constants.Input.Profile.MOBILE,
            phone = profile hasOrNull Constants.Input.Profile.PHONE,
            mobilePro = profile hasOrNull Constants.Input.Profile.MOBILE_PRO
        )
    }

    override suspend fun execute(input: ProfileInput): NetworkResponse<Unit> {
        val request = request(
            type = String::class.java,
            urls = arrayOf("/me/v1/user_data/profile"),
            body = input.asJson()
        )

        return communicationManager.update<String>(request, MiddlewareCommunicationManager.MymToken)
            .transform {
                if (it == RESPONSE_SUCCESSFULLY) {
                    // update saved profile
                    updateCache(input)
                    NetworkResponse.Success(Unit)
                } else {
                    val reason = "error occurred while updating the profile: $it"
                    val failure = PIMSFoundationError.unknownError(reason)
                    NetworkResponse.Failure(failure)
                }
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun updateCache(input: ProfileInput) {
        var profileOutput = readFromCache()
        input.firstName?.let { profileOutput = profileOutput?.copy(firstName = it) }
        input.lastName?.let { profileOutput = profileOutput?.copy(lastName = it) }
        input.civility?.let { profileOutput = profileOutput?.copy(civility = it) }
        input.address1?.let { profileOutput = profileOutput?.copy(address1 = it) }
        input.address2?.let { profileOutput = profileOutput?.copy(address2 = it) }
        input.zipCode?.let { profileOutput = profileOutput?.copy(zipCode = it) }
        input.city?.let { profileOutput = profileOutput?.copy(city = it) }
        input.country?.let { profileOutput = profileOutput?.copy(country = it) }

        input.phone?.let {
            val phones = profileOutput?.phones.orEmpty().toMutableMap()
            phones[ProfileOutput.KEY_PHONE_DEFAULT] = it
            profileOutput = profileOutput?.copy(phones = phones)
        }

        input.mobile?.let {
            val phones = profileOutput?.phones.orEmpty().toMutableMap()
            phones[ProfileOutput.KEY_PHONE_MOBILE] = it
            profileOutput = profileOutput?.copy(phones = phones)
        }

        input.mobilePro?.let {
            val phones = profileOutput?.phones.orEmpty().toMutableMap()
            phones[ProfileOutput.KEY_PHONE_MOBILE_PRO] = it
            profileOutput = profileOutput?.copy(phones = phones)
        }
        profileOutput?.let { saveOnCache(it) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(): ProfileOutput? =
        middlewareComponent.readSync(Constants.Storage.PROFILE, StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveOnCache(profile: ProfileOutput) {
        middlewareComponent.createSync(key = Constants.Storage.PROFILE, data = profile, mode = StoreMode.APPLICATION)
    }
}
