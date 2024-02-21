package com.inetpsa.pims.spaceMiddleware.model.radioplayer

import com.google.gson.annotations.SerializedName

internal data class StationsOutput(

    @field:SerializedName("stations")
    val stations: List<Station>? = null
) {

    internal data class Station(

        @field:SerializedName("id")
        val id: String,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("relevanceIndex")
        val relevanceIndex: Int? = null,

        @field:SerializedName("multimedia")
        val multimedia: List<Multimedia>? = null,

        @field:SerializedName("liveStreams")
        val liveStreams: List<LiveStream>? = null
    ) {

        internal data class LiveStream(

            @field:SerializedName("mime")
            val mime: String? = null,

            @field:SerializedName("url")
            val url: String? = null,

            @field:SerializedName("bitRate")
            val bitRate: Int? = null
        )

        internal data class Multimedia(

            @field:SerializedName("url")
            val url: String? = null,

            @field:SerializedName("width")
            val width: Int? = null,

            @field:SerializedName("height")
            val height: Int? = null
        )
    }
}
