package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

internal data class MymResponse<T>(
    @SerializedName("success") val success: T?,
    @SerializedName("error") val error: Error?,
    @SerializedName("errors") val errors: Map<String, Any>?
) {

    val isSuccess: Boolean
        get() = success != null

    internal data class Error(
        @SerializedName("code") val code: Int,
        @SerializedName("message") val message: String
    )
}
