package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

internal data class MymAccessToken(
    @SerializedName("mym_access_token") val accessToken: String,
    @SerializedName("profil") val profile: ProfileResponse?
)
