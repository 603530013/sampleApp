package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AppointmentDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AppointmentDetailsResponse.Customer
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AppointmentDetailsResponse.Dealer
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAppointmentDetailsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetAppointmentDetailsPsaExecutor
    private val response = AppointmentDetailsResponse(
        basketId = "1235687",
        amount = 1000f,
        commandNumber = "00001",
        hasBeenExtracted = false,
        creationDate = 202307061725,
        rdvDate = 202307131445,
        customerComment = "testComment",
        reduction = 0,
        rdvId = 1234,
        payStatus = 0,
        isStoredInRussiaEnvironment = false,
        typeDepot = 0,
        dateLimit = 20230711,
        dealer = Dealer(
            geoId = "testGeoId",
            rrdiId = "485290P01F",
            codeContractRA = "485290P",
            brand = "AC",
            culture = "fr-FR"
        ),
        customer =
        Customer(
            customerId = "1235391",
            name = "Sams",
            firstname = "testfirst",
            email = "test@email.com",
            isUsablePersonalInfo = false,
            telephone = "123465",
            isAcceptingLegalMention = false,
            isNeedMobilSolution = false,
            contactMode = "None",
            civilityCode = "testCvivility",
            isOfferCommercialAccepted = false,
            isOfferCompanyAccepted = false,
            isOfferPartnersAccepted = false,
            origin = "testOrigin",
            creationDate = 202307061725,
            carID = "123467",
            energyLabel = "testenergy",
            brandLabel = "testBrand",
            modelLabel = "testModel",
            familyCode = "testFamiliyCode",
            mileage = 20000f,
            serialNumber = "VF70PHNPJKE506932",
            hasServiceContract = false
        )
    )

    private val result = DetailsOutput(
        amount = 1000f,
        id = "00001",
        vin = "test-vin",
        bookingId = "testGeoId",
        bookingLocation = null,
        scheduledTime = "2023-07-13T14:45:01Z",
        comment = "testComment",
        mileage = "20000.0",
        email = "test@email.com",
        phone = null,
        status = Status.Booked,
        services = null
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetAppointmentDetailsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing id then throw missing parameter`() {
        val input = mapOf(Constants.PARAM_VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with right inputs then return an DealerAppointmentInput`() {
        val vin = "testVin"
        val id = "testDealerID"
        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAM_ID to id
        )
        val dealerAppointmentInput = executor.params(input)
        Assert.assertEquals(vin, dealerAppointmentInput.vin)
        Assert.assertEquals(id, dealerAppointmentInput.id)
    }

    @Test
    fun `when execute then make a get API call`() {
        val detailsInput = DetailsInput(
            id = "testBasketId",
            vin = "test-vin"
        )
        coEvery { communicationManager.get<AppointmentDetailsResponse>(any(), any()) } returns Success(response)

        every { executor.params(any()) } returns detailsInput

        runTest {
            val responseExecutor = executor.execute(detailsInput)

            verify {
                executor.request(
                    type = AppointmentDetailsResponse::class.java,
                    urls = arrayOf("/shop/v1/rdv/detail/", detailsInput.id),
                    queries = any()
                )
            }

            coVerify {
                communicationManager.get<AppointmentDetailsResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, responseExecutor is Success)
            val success = responseExecutor as Success
            Assert.assertEquals(result, success.response)
        }
    }

    @Test
    fun testTransformTimeStampPositive() {
        val timestamp = 202307131445
        val expected = "2023-07-13T14:45:01Z"
        val result = executor.transformTimeStamp(timestamp)
        Assert.assertEquals(expected, result)
    }

    @Test
    fun testTransformTimeStampNegative() {
        val timestamp = null
        val result = executor.transformTimeStamp(timestamp)
        Assert.assertNull(result)
    }
}
