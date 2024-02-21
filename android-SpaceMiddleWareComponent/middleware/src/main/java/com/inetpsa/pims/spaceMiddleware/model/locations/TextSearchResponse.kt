package com.inetpsa.pims.spaceMiddleware.model.locations

import com.google.gson.annotations.SerializedName

internal data class TextSearchResponse(
    @SerializedName("results") val results: List<Place>?,
    @SerializedName("status") val status: String,
    @SerializedName("error_message") val errorMessage: String?,
    @SerializedName("next_page_token") val nextPageToken: String?
)
