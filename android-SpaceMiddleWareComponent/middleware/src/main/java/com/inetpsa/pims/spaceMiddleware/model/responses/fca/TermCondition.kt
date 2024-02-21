package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class TermCondition(

    @field:SerializedName("mode")
    val mode: Mode,

    @field:SerializedName("url")
    val url: String,

    var market: String? = null
) {

    enum class Mode { LOCAL, BROWSER }
}
