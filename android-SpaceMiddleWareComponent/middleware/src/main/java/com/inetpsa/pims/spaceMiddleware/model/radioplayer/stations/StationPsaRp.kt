package com.inetpsa.pims.spaceMiddleware.model.radioplayer.stations

import com.google.gson.annotations.SerializedName

@Deprecated("should be replaced with RadioPlayerStationsResponse.StationsItem")
internal data class StationPsaRp(
    val name: String?,
    val description: String?,
    @SerializedName("live_streams") val liveStreams: List<LiveStream>?,
    val multimedia: List<Multimedia?>?,
    val country: String?,
    val rpuid: String?,
    @SerializedName("relevance_index") val relevanceIndex: Int?
) {

    internal data class LiveStream(
        @SerializedName("bit_rate") val bitRate: BitRate?,
        @SerializedName("stream_source") val streamSource: StreamSource?
    )

    internal data class BitRate(
        val target: Int?
    )

    internal data class StreamSource(
        val url: String?,
        @SerializedName("mime_value") val mimeValue: String?
    )

    internal data class Multimedia(
        val height: Int?,
        val url: String?,
        val width: Int?
    )
}
