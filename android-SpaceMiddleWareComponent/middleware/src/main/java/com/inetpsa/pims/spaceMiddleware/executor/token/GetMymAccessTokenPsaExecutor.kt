package com.inetpsa.pims.spaceMiddleware.executor.token

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.TokenType.CVSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.MymAccessToken
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ProfileResponse
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess

internal class GetMymAccessTokenPsaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BasePsaExecutor<Unit, MymAccessToken>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<MymAccessToken> {
        val request = request(
            MymAccessToken::class.java,
            arrayOf("/session/v2/accesstoken")
        )

        return middlewareComponent.middlewareCommunicationManager
            .get<MymAccessToken>(request, CVSToken)
            .ifSuccess { mymToken ->
                mymToken.profile?.let { saveProfile(it) }
                mymToken.accessToken
                    .takeIf { it.isNotBlank() }
                    ?.let {
                        middlewareComponent
                            .userSessionManager
                            .setToken(it, MiddlewareCommunicationManager.MymToken)
                    }
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveProfile(profile: ProfileResponse) {
        val phones = mutableMapOf<String, String>()
        profile.phone?.takeIf { it.isNotBlank() }?.let { phones[ProfileOutput.KEY_PHONE_DEFAULT] = it }
        profile.mobile?.takeIf { it.isNotBlank() }?.let { phones[ProfileOutput.KEY_PHONE_MOBILE] = it }
        profile.mobilePro?.takeIf { it.isNotBlank() }?.let { phones[ProfileOutput.KEY_PHONE_MOBILE_PRO] = it }

        val transformedProfile = ProfileOutput(
            uid = profile.idClient,
            email = profile.email,
            firstName = profile.firstName,
            lastName = profile.lastName,
            civility = profile.civility,
            civilityCode = profile.civilityCode,
            locale = null,
            phones = phones,
            address1 = profile.address1,
            address2 = profile.address2,
            zipCode = profile.zipCode,
            city = profile.city,
            country = profile.country
        )
        middlewareComponent.createSync(
            key = Constants.Storage.PROFILE,
            data = transformedProfile,
            mode = StoreMode.APPLICATION
        )
    }
}
