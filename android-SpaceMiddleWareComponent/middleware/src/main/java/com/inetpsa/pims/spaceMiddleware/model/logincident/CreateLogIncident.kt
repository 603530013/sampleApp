package com.inetpsa.pims.spaceMiddleware.model.logincident

import com.google.gson.annotations.SerializedName

internal data class CreateLogIncident(
    @SerializedName("incidentid") val incidentId: String
)
