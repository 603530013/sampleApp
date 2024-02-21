package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class OwnerManualPdfResponse(
    @SerializedName("pdf") val pdf: String? = null
)
