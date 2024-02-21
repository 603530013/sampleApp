package com.inetpsa.pims.spaceMiddleware.model.radioplayer

import com.google.gson.annotations.SerializedName

/**
 * Created on 17/11/22.
 */
internal data class OnAirOutput(
    @field:SerializedName("stations")
    val stations: List<OnAirItem>? = null
) {

    internal data class OnAirItem(
        @field:SerializedName("showName") val showName: String?,
        @field:SerializedName("songName") val songName: String?,
        @field:SerializedName("artist") val artist: String?
    )
}
