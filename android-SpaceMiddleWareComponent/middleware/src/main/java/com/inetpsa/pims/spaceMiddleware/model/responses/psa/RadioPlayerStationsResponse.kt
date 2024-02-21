package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

internal data class RadioPlayerStationsResponse(

    @field:SerializedName("stations")
    val stations: List<StationsItem>? = null
) {

    internal data class StationsItem(

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("multimedia")
        val multimedia: List<MultimediaItem?>? = null,

        @field:SerializedName("relevance_index")
        val relevanceIndex: Int? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("rpuid")
        val rpuid: String,

        @field:SerializedName("live_streams")
        val liveStreams: List<LiveStreamsItem?>? = null
    ) {

        internal data class LiveStreamsItem(

            @field:SerializedName("stream_source")
            val streamSource: StreamSource? = null,

            @field:SerializedName("bit_rate")
            val bitRate: BitRate? = null
        ) {

            internal data class StreamSource(

                @field:SerializedName("mime_value")
                val mimeValue: String? = null,

                @field:SerializedName("url")
                val url: String? = null
            )

            internal data class BitRate(

                @field:SerializedName("target")
                val target: Int? = null
            )
        }

        internal data class MultimediaItem(

            @field:SerializedName("width")
            val width: Int? = null,

            @field:SerializedName("url")
            val url: String? = null,

            @field:SerializedName("height")
            val height: Int? = null
        )
    }
}
