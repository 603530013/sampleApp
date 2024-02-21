package com.inetpsa.pims.spaceMiddleware.model.logincident

import com.google.gson.annotations.SerializedName

internal data class UploadLogIncidentResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("errors") val errors: List<Any>?, // ToDo get the right type of errors
    @SerializedName("incidentid") val incidentId: String
)
