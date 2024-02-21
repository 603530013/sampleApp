package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesEmeaMaseratiInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.EmeaDealerServiceResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.EmeaDealerServiceResponse.ServiceList
import com.inetpsa.pims.spaceMiddleware.util.filterNotNull
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetEmeaServiceFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetEmeaServiceFcaExecutor

    private val servicesInput = ServicesEmeaMaseratiInput(
        vin = "testVin",
        dealerId = "testDealerID"
    )

    private val emeaDealerServiceResponse = EmeaDealerServiceResponse(
        servicesList = listOf(
            ServiceList(
                code = "testCode",
                description = "testDescription"
            )
        )
    )

    private val servicesOutput = ServicesOutput(
        services = listOf(Services(id = "testDescription"))
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetEmeaServiceFcaExecutor(middlewareComponent))
    }

    @Test
    fun `when execute then make a get API call`() {
        val vin = "testVin"

        every { executor.params(any()) } returns servicesInput

        coEvery {
            communicationManager.get<EmeaDealerServiceResponse>(any(), any())
        } returns Success(emeaDealerServiceResponse)

        val queries = mapOf(
            Constants.PARAMS_KEY_DEALER_ID to servicesInput.dealerId,
            Constants.PARAM_COUNTRY to configurationManager.locale.country,
            Constants.QUERY_PARAM_KEY_LANGUAGE to configurationManager.locale.language,
            Constants.QUERY_PARAM_KEY_LOCATION to servicesInput.dealerLocation
        ).filterNotNull()

        runTest {
            val response = executor.execute()
            verify {
                executor.request(
                    type = eq(EmeaDealerServiceResponse::class.java),
                    urls = eq(arrayOf("/v1/accounts/", uid, "/vehicles/", vin, "/servicescheduler/services")),
                    queries = queries
                )
            }

            coVerify {
                communicationManager.get<EmeaDealerServiceResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(servicesOutput, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return DealerServicesInput`() {
        val input = ServicesEmeaMaseratiInput(
            vin = "testVin",
            dealerId = "testDealerID",
            dealerLocation = "testLocation"
        )

        val params = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "testDealerID",
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
            Constants.Input.VIN to "testVin",
            Constants.PARAM_MILEAGE to 120,
            Constants.Input.Appointment.PARAM_MILEAGE_UNIT to "km"
        )
        val paramsInput = executor.params(params)

        Assert.assertEquals(input, paramsInput)
    }

    @Test
    fun `when execute params with missing dealerId then throw missing parameter`() {
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
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(Constants.Input.Appointment.BOOKING_ID to "testDealerID")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing mileage then throw missing parameter`() {
        val input = mapOf(Constants.Input.Appointment.BOOKING_ID to "testDealerID", Constants.Input.VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.MILEAGE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute transformToDealerServiceCode then return DealerServicesOutput`() {
        runTest {
            every { executor.transformToDealerServiceOutput(any()) } returns servicesOutput
            val result = executor.transformToDealerServiceOutput(emeaDealerServiceResponse)
            Assert.assertEquals("testDescription", result.services.firstOrNull()?.id)
        }
    }
}
