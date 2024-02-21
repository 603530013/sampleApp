package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.NaftaDealerServicesResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.NaftaDealerServicesResponse.Service
import com.inetpsa.pims.spaceMiddleware.util.filterNotNull
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class GetNaftaFactoryServicesFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetNaftaFactoryServicesFcaExecutor
    private val token = "testToken"

    private val dealerServicesInput = ServicesNaftaLatamInput(
        dealerId = "testDealerId",
        vin = "testVin",
        mileage = 120,
        unit = ServicesNaftaLatamInput.MileageUnit.KM
    )

    private val dealerServiceResponse = NaftaDealerServicesResponse(
        services = listOf(
            Service(
                id = "1.0",
                name = "testName",
                price = 10.0f,
                description = "testDescription"
            )
        )
    )

    @Before
    fun setUp() {
        super.setup()
        mockkObject(BookingOnlineCache)
        every { middlewareComponent.dataManager } returns dataManager
        every { middlewareComponent.userSessionManager } returns userSessionManager
        every { userSessionManager.getUserSession() } returns userSession
        every { userSessionManager.getUserSession()?.customerId } returns "testCustomerId"
        executor = spyk(GetNaftaFactoryServicesFcaExecutor(middlewareComponent))
    }

    @Test
    fun `when execute then make a get API call`() {
        every { executor.params(any()) } returns dealerServicesInput

        coEvery {
            communicationManager.get<NaftaDealerServicesResponse>(any(), any())
        } returns Success(dealerServiceResponse)

        coJustRun { BookingOnlineCache.write(any<ServiceType>(), any()) }

        val queries = mapOf(
            Constants.PARAM_MILEAGE to dealerServicesInput.mileageMiles.toString()
        ).filterNotNull()

        runTest {
            val response = executor.execute(dealerServicesInput, token = token)
            verify {
                executor.request(
                    type = eq(NaftaDealerServicesResponse::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            uid,
                            "/vehicles/",
                            dealerServicesInput.vin,
                            "/servicescheduler/factoryservices"
                        )
                    ),
                    headers = eq(mapOf("dealer-authorization" to token)),
                    queries = eq(queries)
                )
            }
            coVerify {
                communicationManager.get<NaftaDealerServicesResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }

            coVerify(exactly = 1) { BookingOnlineCache.write(ServiceType.Factory, dealerServiceResponse.services) }

            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(dealerServiceResponse, success.response)
        }
    }

    @Test
    fun `when pass with right params then return object`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "testDealerId",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.VIN to "testVin",
            Constants.PARAM_MILEAGE to 120,
            Constants.Input.Appointment.PARAM_MILEAGE_UNIT to "km"
        )

        val response = executor.params(input)

        assertEquals(dealerServicesInput, response)
    }

    @Test
    fun `when params with missing all values then throw missing parameter`() {
        val input = mapOf<String, String>(
            Constants.Input.Appointment.BOOKING_ID to "testDealerId"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing vin then return null`() {
        val params = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "testDealerId",
            Constants.Input.Appointment.MILEAGE to 120
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(params)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(Constants.PARAM_VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    /*   @Test
       fun `when params with missing mileage then return null`() {
           val params = mapOf(
               Constants.Input.Appointment.BOOKING_ID to "testDealerId",
               Constants.Input.VIN to "testVin"
           )
           val exception = PIMSFoundationError.missingParameter(Constants.PARAM_MILEAGE)
           try {
               executor.params(params)
           } catch (ex: PIMSError) {
               assertEquals(exception.code, ex.code)
               assertEquals(exception.message, ex.message)
           }
       }*/

    @Test
    fun `when execute params with missing mileage Unit then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "testDealerID",
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.MILEAGE to 12345
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.PARAM_MILEAGE_UNIT)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing BookingId then throw missing parameter`() {
        val input = mapOf(Constants.Input.VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with invalid vin then throw invalid parameter`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "testDealerId",
            Constants.Input.VIN to 123,
            Constants.PARAM_MILEAGE to 120
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    /* @Test
     fun `when params with right params then return object`() {
         val params = mapOf(
             Constants.Input.Appointment.BOOKING_ID to "testDealerId",
             Constants.Input.VIN to "testVin",
             Constants.Input.Appointment.MILEAGE to 120
         )
         val result = executor.params(params)
         assertEquals(dealerServicesInput, result)
     }*/
}
