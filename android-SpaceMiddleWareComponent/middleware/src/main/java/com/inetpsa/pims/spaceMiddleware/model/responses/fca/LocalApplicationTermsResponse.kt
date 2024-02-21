package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class LocalApplicationTermsResponse(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("copyHU")
    val copyHU: String? = null,

    @field:SerializedName("webURL")
    val webURL: String? = null,

    @field:SerializedName("contentId")
    val contentId: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("copy")
    val copy: String? = null,

    @field:SerializedName("contentType")
    val contentType: String? = null,

    @field:SerializedName("version")
    val version: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: String? = null
)
