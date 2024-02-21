package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

internal data class AssistanceResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("caseNumber") val caseNumber: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("driverFirstname") val driverFirstname: String?,
    @SerializedName("driverLastname") val driverLastname: String?,
    @SerializedName("driverPhoneNumber") val driverPhoneNumber: String?,
    @SerializedName("vehicleLicensePlate") val vehicleLicensePlate: String?,
    @SerializedName("vehicleLocationAddressStreet") val vehicleLocationAddressStreet: String?,
    @SerializedName("vehicleLocationAddressStreetNumber") val vehicleLocationAddressStreetNumber: String?,
    @SerializedName("vehicleLocationAddressPostalCode") val vehicleLocationAddressPostalCode: String?,
    @SerializedName("vehicleLocationAddressCity") val vehicleLocationAddressCity: String?,
    @SerializedName("vehicleLocationCoordinatesLatitude") val vehicleLocationCoordinatesLatitude: Double?,
    @SerializedName("vehicleLocationCoordinatesLongitude") val vehicleLocationCoordinatesLongitude: Double?,
    @SerializedName("breakdownCategory") val breakdownCategory: String?,
    @SerializedName("patrolCompanyName") val patrolCompanyName: String?,
    @SerializedName("patrolCallCenterPhoneNumber") val patrolCallCenterPhoneNumber: String?,
    @SerializedName("patrolLatitude") val patrolLatitude: Double?,
    @SerializedName("patrolLongitude") val patrolLongitude: Double?,

    // TODO Here to check whether the format needs to be converted later
    @SerializedName("estimatedArrivalTime") val estimatedArrivalTime: Long?,
    @SerializedName("lastUpdate") val lastUpdate: Long?
)
