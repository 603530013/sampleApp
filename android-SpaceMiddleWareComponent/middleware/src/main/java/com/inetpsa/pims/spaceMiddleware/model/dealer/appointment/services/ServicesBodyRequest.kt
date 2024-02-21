package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services

import com.google.gson.annotations.SerializedName

internal data class ServicesBodyRequest(
    @SerializedName("servicesList")
    val servicesList: List<Service>
) {

    data class Service(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String? = null,
        @SerializedName("price") val price: Float? = null,
        @SerializedName("description") val description: String? = null
    )
}
