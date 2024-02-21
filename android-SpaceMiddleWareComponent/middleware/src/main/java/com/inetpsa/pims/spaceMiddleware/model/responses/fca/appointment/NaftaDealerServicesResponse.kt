package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

internal data class NaftaDealerServicesResponse(
    @SerializedName("services") val services: List<Service>?
) {

    internal data class Service(
        @SerializedName("id") override val id: String,
        @SerializedName("name") val name: String? = null,
        @SerializedName("price") val price: Float? = null,
        @SerializedName("description") val description: String? = null
    ) : DealerServiceId
}
