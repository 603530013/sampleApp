package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.Driver
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.Patrol
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.VehicleLocation
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.VehicleLocation.Address
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AssistanceResponse
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset

class AssistanceMapperTest {

    private val mapper: AssistanceMapper = spyk()
    private val date = 1679048789343L // Fri Mar 17 2023 10:26:29 UTC
    private val offsetDate = Instant.ofEpochMilli(date).atOffset(ZoneOffset.UTC)

    private val assistanceResponse = AssistanceResponse(
        id = "idTest",
        caseNumber = "caseNumberTest",
        status = "statusTest",
        driverFirstname = "driverFirstNameTest",
        driverLastname = "driverLastnameTest",
        driverPhoneNumber = "driverPhoneNumberTest",
        vehicleLicensePlate = "vehicleLicensePlateTest",
        vehicleLocationAddressStreet = "vehicleLocationAddressStreetTest",
        vehicleLocationAddressStreetNumber = "vehicleLocationAddressStreetNumberTest",
        vehicleLocationAddressPostalCode = "vehicleLocationAddressPostalCodeTest",
        vehicleLocationAddressCity = "vehicleLocationAddressCityTest",
        vehicleLocationCoordinatesLatitude = 12.34,
        vehicleLocationCoordinatesLongitude = 56.78,
        breakdownCategory = "breakdownCategoryTest",
        patrolCompanyName = "patrolCompanyNameTest",
        patrolCallCenterPhoneNumber = "patrolCallCenterPhoneNumberTest",
        patrolLatitude = 12.34,
        patrolLongitude = 56.78,
        estimatedArrivalTime = null,
        lastUpdate = date
    )

    private val assistanceOutput = AssistanceOutput(
        id = "idTest",
        caseNumber = "caseNumberTest",
        status = "statusTest",
        driver = Driver(
            firstname = "driverFirstNameTest",
            lastname = "driverLastnameTest",
            phoneNumber = "driverPhoneNumberTest"
        ),
        vehicleLocation = VehicleLocation(
            address = Address(
                streetNumber = "vehicleLocationAddressStreetNumberTest",
                street = "vehicleLocationAddressStreetTest",
                postalCode = "vehicleLocationAddressPostalCodeTest",
                city = "vehicleLocationAddressCityTest"
            ),
            latitude = 12.34,
            longitude = 56.78
        ),
        licensePlate = "vehicleLicensePlateTest",
        patrol = Patrol(
            companyName = "patrolCompanyNameTest",
            callCenter = "patrolCallCenterPhoneNumberTest",
            latitude = 12.34,
            longitude = 56.78
        ),
        breakdownCategory = "breakdownCategoryTest",
        estimatedArrivalTime = null,
        lastUpdate = offsetDate
    )

    @Test
    fun `when execute transformToFormattedDate then return a readable date`() {
        val output = mapper.transformToDateTime(date)
        Assert.assertEquals(offsetDate, output)
    }

    @Test
    fun `when execute transformToFormattedDate with null value then return a null`() {
        val output = mapper.transformToDateTime(null)
        Assert.assertEquals(null, output)
    }

    @Test
    fun `when execute transformToAssistanceOutput then return a valid AssistanceOutput`() {
        val output = mapper.transformToAssistanceOutput(assistanceResponse)
        Assert.assertEquals(assistanceOutput, output)
    }

    @Test
    fun `when execute transformToPatrol with null value then return a null`() {
        val response = AssistanceResponse(
            id = "idTest",
            caseNumber = "caseNumberTest",
            status = "statusTest",
            driverFirstname = null,
            driverLastname = null,
            driverPhoneNumber = null,
            vehicleLicensePlate = null,
            vehicleLocationAddressStreet = null,
            vehicleLocationAddressStreetNumber = null,
            vehicleLocationAddressPostalCode = null,
            vehicleLocationAddressCity = null,
            vehicleLocationCoordinatesLatitude = null,
            vehicleLocationCoordinatesLongitude = null,
            breakdownCategory = null,
            patrolCompanyName = null,
            patrolCallCenterPhoneNumber = null,
            patrolLatitude = null,
            patrolLongitude = null,
            estimatedArrivalTime = null,
            lastUpdate = null
        )
        val output = mapper.transformToPatrol(response)
        Assert.assertEquals(null, output)
    }

    @Test
    fun `when execute transformToPatrol then return a valid AssistanceOutput`() {
        val output = mapper.transformToPatrol(assistanceResponse)
        Assert.assertEquals(assistanceOutput.patrol, output)
    }

    @Test
    fun `when execute transformToDriver with null value then return a null`() {
        val response = AssistanceResponse(
            id = "idTest",
            caseNumber = "caseNumberTest",
            status = "statusTest",
            driverFirstname = null,
            driverLastname = null,
            driverPhoneNumber = null,
            vehicleLicensePlate = null,
            vehicleLocationAddressStreet = null,
            vehicleLocationAddressStreetNumber = null,
            vehicleLocationAddressPostalCode = null,
            vehicleLocationAddressCity = null,
            vehicleLocationCoordinatesLatitude = null,
            vehicleLocationCoordinatesLongitude = null,
            breakdownCategory = null,
            patrolCompanyName = null,
            patrolCallCenterPhoneNumber = null,
            patrolLatitude = null,
            patrolLongitude = null,
            estimatedArrivalTime = null,
            lastUpdate = null
        )
        val output = mapper.transformToDriver(response)
        Assert.assertEquals(null, output)
    }

    @Test
    fun `when execute transformToDriver then return a valid AssistanceOutput`() {
        val output = mapper.transformToDriver(assistanceResponse)
        Assert.assertEquals(assistanceOutput.driver, output)
    }

    @Test
    fun `when execute transformToVehicleLocation with null value then return a null`() {
        val response = AssistanceResponse(
            id = "idTest",
            caseNumber = "caseNumberTest",
            status = "statusTest",
            driverFirstname = null,
            driverLastname = null,
            driverPhoneNumber = null,
            vehicleLicensePlate = null,
            vehicleLocationAddressStreet = null,
            vehicleLocationAddressStreetNumber = null,
            vehicleLocationAddressPostalCode = null,
            vehicleLocationAddressCity = null,
            vehicleLocationCoordinatesLatitude = null,
            vehicleLocationCoordinatesLongitude = null,
            breakdownCategory = null,
            patrolCompanyName = null,
            patrolCallCenterPhoneNumber = null,
            patrolLatitude = null,
            patrolLongitude = null,
            estimatedArrivalTime = null,
            lastUpdate = null
        )
        val output = mapper.transformToVehicleLocation(response)
        Assert.assertEquals(null, output)
    }

    @Test
    fun `when execute transformToVehicleLocation then return a valid AssistanceOutput-VehicleLocation`() {
        val output = mapper.transformToVehicleLocation(assistanceResponse)
        Assert.assertEquals(assistanceOutput.vehicleLocation, output)
    }
}
