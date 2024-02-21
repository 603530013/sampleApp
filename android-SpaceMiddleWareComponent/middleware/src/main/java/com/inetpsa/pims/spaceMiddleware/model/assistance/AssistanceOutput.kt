package com.inetpsa.pims.spaceMiddleware.model.assistance

import java.time.OffsetDateTime

internal data class AssistanceOutput(
    val id: String?,
    val caseNumber: String?,
    val status: String?,
    val driver: Driver?,
    val vehicleLocation: VehicleLocation?,
    val licensePlate: String?,
    val patrol: Patrol?,
    val breakdownCategory: String?,
    val estimatedArrivalTime: OffsetDateTime?,
    val lastUpdate: OffsetDateTime?
) {

    data class Driver(
        val firstname: String?,
        val lastname: String?,
        val phoneNumber: String?
    )

    data class VehicleLocation(
        val address: Address?,
        val latitude: Double?,
        val longitude: Double?
    ) {

        data class Address(
            val streetNumber: String?,
            val street: String?,
            val postalCode: String?,
            val city: String?
        )
    }

    data class Patrol(
        val companyName: String?,
        val callCenter: String?,
        val latitude: Double?,
        val longitude: Double?
    )
}
