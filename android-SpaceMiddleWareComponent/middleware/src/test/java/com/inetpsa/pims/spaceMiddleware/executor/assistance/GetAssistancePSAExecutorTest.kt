package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.Driver
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.Patrol
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.VehicleLocation
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput.VehicleLocation.Address
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AssistanceResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
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

internal class GetAssistancePSAExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetAssistancePSAExecutor
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
        executor = spyk(GetAssistancePSAExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call and get Success`() {
        val paramsId = "testID"
        every { executor.params(any()) } returns paramsId
        coEvery {
            communicationManager.get<AssistanceResponse>(any(), any())
        } returns NetworkResponse.Success(assistanceResponse)
        every { anyConstructed<AssistanceMapper>().transformToAssistanceOutput(any()) } returns assistanceOutput

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(AssistanceResponse::class.java),
                    urls = eq(arrayOf("/car/v1/rsa/assistance/", paramsId)),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<AssistanceResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            verify(exactly = 1) {
                anyConstructed<AssistanceMapper>().transformToAssistanceOutput(eq(assistanceResponse))
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(assistanceOutput, success.response)
        }
    }

    @Test
    fun `when execute then make a get API call and get a failure`() {
        val paramsId = "testID"
        val error = PimsErrors.serverError(null, "server is not reachable")
        every { executor.params(any()) } returns paramsId
        coEvery {
            communicationManager.get<AssistanceResponse>(any(), any())
        } returns NetworkResponse.Failure(error)
        every { anyConstructed<AssistanceMapper>().transformToAssistanceOutput(any()) } returns assistanceOutput

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(AssistanceResponse::class.java),
                    urls = eq(arrayOf("/car/v1/rsa/assistance/", paramsId)),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<AssistanceResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            verify(exactly = 0) {
                anyConstructed<AssistanceMapper>().transformToAssistanceOutput(eq(assistanceResponse))
            }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }

    @Test
    fun `when execute params with the right paramID then return paramId`() {
        val paramsId = "testID"
        val input = mapOf(Constants.PARAM_ID to paramsId)
        val param = executor.params(input)

        Assert.assertEquals(paramsId, param)
    }

    @Test
    fun `when execute params with missing paramID then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid paramID then throw invalid parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.PARAM_ID to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
