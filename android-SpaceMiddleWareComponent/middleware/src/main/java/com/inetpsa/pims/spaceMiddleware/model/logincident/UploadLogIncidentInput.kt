package com.inetpsa.pims.spaceMiddleware.model.logincident

internal data class UploadLogIncidentInput(
    val incidentId: String,
    val path: String,
    val filename: String,
    val type: Type,
    val quality: Int
) {

    internal var data: String = path
    internal var title: String = filename

    fun asBody(): String = "{\"filename\":\"$filename\",\"title\":\"$title\",\"data\":\"$data\"}"

    internal enum class Type { FILE, IMAGE }
}
