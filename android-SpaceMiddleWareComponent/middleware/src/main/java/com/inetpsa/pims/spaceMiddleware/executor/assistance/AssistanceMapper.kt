package com.inetpsa.pims.spaceMiddleware.executor.assistance

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AssistanceResponse
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class AssistanceMapper {

    internal fun transformToAssistanceOutput(response: AssistanceResponse): AssistanceOutput {
        val driver = transformToDriver(response)
        val vehicleLocation = transformToVehicleLocation(response)
        val patrol = transformToPatrol(response)
        return AssistanceOutput(
            id = response.id,
            caseNumber = response.caseNumber,
            status = response.status,
            driver = driver,
            vehicleLocation = vehicleLocation,
            licensePlate = response.vehicleLicensePlate,
            patrol = patrol,
            breakdownCategory = response.breakdownCategory,
            estimatedArrivalTime = transformToDateTime(response.estimatedArrivalTime),
            lastUpdate = transformToDateTime(response.lastUpdate)
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDateTime(date: Long?): OffsetDateTime? =
        date?.let { Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToPatrol(response: AssistanceResponse): AssistanceOutput.Patrol? {
        val patrolNullable = response.patrolCompanyName.isNullOrBlank() &&
            response.patrolCallCenterPhoneNumber.isNullOrBlank() &&
            response.patrolLatitude == null &&
            response.patrolLongitude == null

        return when (patrolNullable) {
            true -> null
            else -> AssistanceOutput.Patrol(
                companyName = response.patrolCompanyName,
                callCenter = response.patrolCallCenterPhoneNumber,
                latitude = response.patrolLatitude,
                longitude = response.patrolLongitude
            )
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDriver(response: AssistanceResponse): AssistanceOutput.Driver? {
        val driverNullable = response.driverFirstname.isNullOrBlank() &&
            response.driverLastname.isNullOrBlank() &&
            response.driverPhoneNumber.isNullOrBlank()

        return when (driverNullable) {
            true -> null
            else -> AssistanceOutput.Driver(
                firstname = response.driverFirstname,
                lastname = response.driverLastname,
                phoneNumber = response.driverPhoneNumber
            )
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToVehicleLocation(response: AssistanceResponse): AssistanceOutput.VehicleLocation? {
        val addressNullable = response.vehicleLocationAddressStreetNumber.isNullOrBlank() &&
            response.vehicleLocationAddressStreet.isNullOrBlank() &&
            response.vehicleLocationAddressPostalCode.isNullOrBlank() &&
            response.vehicleLocationAddressCity.isNullOrBlank()

        val address = when (addressNullable) {
            true -> null
            else -> AssistanceOutput.VehicleLocation.Address(
                streetNumber = response.vehicleLocationAddressStreetNumber,
                street = response.vehicleLocationAddressStreet,
                postalCode = response.vehicleLocationAddressPostalCode,
                city = response.vehicleLocationAddressCity
            )
        }

        val noContent = address == null &&
            response.vehicleLocationCoordinatesLatitude == null &&
            response.vehicleLocationCoordinatesLongitude == null
        return when (noContent) {
            true -> null
            else -> AssistanceOutput.VehicleLocation(
                address = address,
                latitude = response.vehicleLocationCoordinatesLatitude,
                longitude = response.vehicleLocationCoordinatesLongitude
            )
        }
    }
}
