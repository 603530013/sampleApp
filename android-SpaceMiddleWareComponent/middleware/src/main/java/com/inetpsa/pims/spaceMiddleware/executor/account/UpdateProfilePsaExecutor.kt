package com.inetpsa.pims.spaceMiddleware.executor.account

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.profile.ProfileParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.asJson
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.transform

@Deprecated("This should be replaced with SetProfilePsaExecutor")
internal class UpdateProfilePsaExecutor(command: BaseCommand) : BasePsaExecutor<ProfileParams, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): ProfileParams {
        val profile: Map<String, String?> = parameters has Constants.PARAMS_KEY_PROFILE
        if (profile.isEmpty()) {
            throw PIMSFoundationError.missingParameter(Constants.PARAMS_KEY_PROFILE)
        }

        return ProfileParams(
            email = profile hasOrNull Constants.PARAMS_KEY_PROFILE_EMAIL,
            firstName = profile hasOrNull Constants.PARAMS_KEY_PROFILE_FIRST_NAME,
            lastName = profile hasOrNull Constants.PARAMS_KEY_PROFILE_LAST_NAME,
            civility = profile hasOrNull Constants.PARAMS_KEY_PROFILE_CIVILITY,
            // civilityCode = profile hasOrNull Constants.PARAMS_KEY_PROFILE_CIVILITY_CODE,
            address1 = profile.getOrDefault(Constants.PARAMS_KEY_PROFILE_ADDRESS_1, ""),
            address2 = profile.getOrDefault(Constants.PARAMS_KEY_PROFILE_ADDRESS_2, ""),
            zipCode = profile.getOrDefault(Constants.PARAMS_KEY_PROFILE_ZIP_CODE, ""),
            city = profile.getOrDefault(Constants.PARAMS_KEY_PROFILE_CITY, ""),
            country = profile.getOrDefault(Constants.PARAMS_KEY_PROFILE_COUNTRY, ""),
            mobile = profile.getOrDefault(Constants.PARAMS_KEY_MOBILE, ""),
            phone = profile.getOrDefault(Constants.PARAMS_KEY_PHONE, ""),
            mobilePro = profile.getOrDefault(Constants.PARAMS_KEY_MOBILE_PRO, "")
        )
    }

    override suspend fun execute(input: ProfileParams): NetworkResponse<Unit> {
        val request = request(
            type = String::class.java,
            urls = arrayOf("/me/v1/user_data/profile"),
            body = input.asJson()
        )

        return communicationManager.update<String>(request, MiddlewareCommunicationManager.MymToken)
            .transform {
                if (it == Constants.RESPONSE_RESULT_PROFILE_UPDATE_SUCCESSFULLY) {
                    NetworkResponse.Success(Unit)
                } else {
                    NetworkResponse.Failure(PimsErrors.updateProfileFailed())
                }
            }
    }
}
