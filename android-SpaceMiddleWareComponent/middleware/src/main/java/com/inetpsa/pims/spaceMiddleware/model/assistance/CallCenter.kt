package com.inetpsa.pims.spaceMiddleware.model.assistance

import com.google.gson.annotations.SerializedName

data class CallCenter(
    @SerializedName("primary")
    val primary: String?,
    @SerializedName("secondary")
    val secondary: String? = null
)
