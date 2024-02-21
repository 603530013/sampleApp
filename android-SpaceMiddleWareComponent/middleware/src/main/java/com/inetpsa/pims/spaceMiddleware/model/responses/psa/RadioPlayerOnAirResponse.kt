package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

/**
 * Created on 17/11/22.
 */
internal data class RadioPlayerOnAirResponse(
    @field:SerializedName("data")
    val stations: List<OnAirItem>? = null
) {

    internal data class OnAirItem(
        @field:SerializedName("show") val show: Show?,
        @field:SerializedName("song") val song: Song?
    ) {

        internal data class Show(
            @field:SerializedName("name") val name: String?
        )

        internal data class Song(
            @field:SerializedName("artist") val artist: String?,
            @field:SerializedName("name") val name: String?
        )
    }
}
