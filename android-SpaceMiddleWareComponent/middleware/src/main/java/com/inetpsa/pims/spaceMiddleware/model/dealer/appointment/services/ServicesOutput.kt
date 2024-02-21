package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services

import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType

internal data class ServicesOutput(
    @field:SerializedName("services") val services: List<Services>
) {

    internal data class Services(
        @field:SerializedName("id")
        val id: String,
        @field:SerializedName("title")
        val title: String? = null,
        @field:SerializedName("type")
        val type: ServiceType? = null,
        @field:SerializedName("description")
        val description: String? = null,
        @field:SerializedName("price")
        val price: Float? = null,
        @field:SerializedName("packages")
        val packages: List<Packages>? = null
    ) {

        internal data class Packages(
            val reference: String? = null,
            val title: String? = null,
            val description: List<String>? = null,
            val price: Float? = null,
            val type: Int? = null,
            val validity: Validity? = null
        ) {

            internal data class Validity(
                val start: String?,
                val end: String?
            )
        }
    }
}
