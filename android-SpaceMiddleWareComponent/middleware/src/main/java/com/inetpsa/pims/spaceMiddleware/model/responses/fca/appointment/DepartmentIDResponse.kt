package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

internal data class DepartmentIDResponse(
//    This id type is Integer as a server response
    @SerializedName("id") val id: Int
)
