package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.Driver
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.Patrol
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.VehicleLocation
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.VehicleLocation.Address
import com.inetpsa.pims.spaceMiddleware.model.assistance.SendInfoForAssistancePsaParams
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AssistanceResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset

internal class SetAssistancePSAExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: SetAssistancePSAExecutor
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

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(AssistanceMapper::class)
        executor = spyk(SetAssistancePSAExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a post API call`() {
        val input = SendInfoForAssistancePsaParams(
            vin = "testVin",
            category = "testCategory",
            latitude = 123.0,
            longitude = 123.0,
            phoneNumber = "testPhoneNumber",
            country = "testCountry"
        )
        every { executor.params(any()) } returns input
        every { anyConstructed<AssistanceMapper>().transformToAssistanceOutput(any()) } returns assistanceOutput
        coEvery { communicationManager.post<AssistanceResponse>(any(), any()) } returns Success(assistanceResponse)

        runTest {
            val response = executor.execute()
            val body = input.toJson()
            verify {
                executor.request(
                    type = eq(AssistanceResponse::class.java),
                    urls = eq(arrayOf("/car/v1/rsa/assistance")),
                    headers = any(),
                    queries = any(),
                    body = eq(body)
                )
            }

            coVerify {
                communicationManager.post<AssistanceResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            verify(exactly = 1) {
                anyConstructed<AssistanceMapper>().transformToAssistanceOutput(eq(assistanceResponse))
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(assistanceOutput, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return SendInfoForAssistancePsaParams`() {
        val vin = "testVin"
        val category = "testCategory"
        val latitude = 123.0
        val longitude = 123.0
        val phoneNumber = "testPhoneNumber"
        val country = "testCountry"

        val params = SendInfoForAssistancePsaParams(
            vin = vin,
            category = category,
            latitude = latitude,
            longitude = longitude,
            phoneNumber = phoneNumber,
            country = country
        )

        val input = mapOf(
            Constants.BODY_PARAM_VIN to vin,
            Constants.BODY_PARAM_CATEGORY to category,
            Constants.BODY_PARAM_LATITUDE to latitude,
            Constants.BODY_PARAM_LONGITUDE to longitude,
            Constants.BODY_PARAM_PHONE_NUMBER to phoneNumber,
            Constants.BODY_PARAM_COUNTRY to country
        )
        val output = executor.params(input)

        Assert.assertEquals(params, output)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val vin = 123
        val input = mapOf(Constants.BODY_PARAM_VIN to vin)
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing category then throw missing parameter`() {
        val input = mapOf(Constants.BODY_PARAM_VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_CATEGORY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid category then throw missing parameter`() {
        val category = 123
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to category
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_CATEGORY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing latitude then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to "testCategory"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_LATITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid latitude then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to "testCategory",
            Constants.BODY_PARAM_LATITUDE to "testLatitude"
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_LATITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing longitude then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to "testCategory",
            Constants.BODY_PARAM_LATITUDE to 123.0
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_LONGITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid longitude then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to "testCategory",
            Constants.BODY_PARAM_LATITUDE to 123.0,
            Constants.BODY_PARAM_LONGITUDE to "testLongitude"
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_LONGITUDE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing phoneNumber then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to "testCategory",
            Constants.BODY_PARAM_LATITUDE to 123.0,
            Constants.BODY_PARAM_LONGITUDE to 123.0
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_PHONE_NUMBER)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid phoneNumber then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to "testCategory",
            Constants.BODY_PARAM_LATITUDE to 123.0,
            Constants.BODY_PARAM_LONGITUDE to 123.0,
            Constants.BODY_PARAM_PHONE_NUMBER to 123
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_PHONE_NUMBER)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing country then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to "testCategory",
            Constants.BODY_PARAM_LATITUDE to 123.0,
            Constants.BODY_PARAM_LONGITUDE to 123.0,
            Constants.BODY_PARAM_PHONE_NUMBER to "testPhoneNumber"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_COUNTRY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid country then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_CATEGORY to "testCategory",
            Constants.BODY_PARAM_LATITUDE to 123.0,
            Constants.BODY_PARAM_LONGITUDE to 123.0,
            Constants.BODY_PARAM_PHONE_NUMBER to "testPhoneNumber",
            Constants.BODY_PARAM_COUNTRY to 123
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.BODY_PARAM_COUNTRY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
