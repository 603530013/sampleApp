package com.inetpsa.pims.spaceMiddleware.model.radioplayer.onair

@Deprecated("We should use the new class OnAirItem instead")
internal data class OnAirInfoPsaRp(
    val show: Show?,
    val song: Song?
) {

    internal data class Show(
        val name: String?
    )

    internal data class Song(
        val artist: String?,
        val name: String?
    )
}
