package com.inetpsa.pims.spaceMiddleware.model.dealer

internal data class DealerOutput(
    val id: String?,
    val name: String?,
    val bookingId: String?,
    val bookingLocation: String?,
    val address: String?,
    val emails: Map<String, String>?,
    val latitude: String?,
    val longitude: String?,
    val phones: Map<String, String>?,
    val website: String?,
    val preferred: Boolean,
    val bookable: Boolean,
    val openingHours: Map<String, Map<String, OpeningHour>>?,
    val services: List<Service>?

) {

    data class OpeningHour(val closed: Boolean, val time: List<String>?)
    data class Service(val code: String, val label: String?, val type: String?)
}
