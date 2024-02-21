package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

internal data class EmeaDealerServiceResponse(

    @field:SerializedName("servicesList")
    val servicesList: List<ServiceList>? = null
) {

    internal data class ServiceList(
        @field:SerializedName("code")
        val code: String? = null,
        @field:SerializedName("description")
        val description: String? = null
    )
}
