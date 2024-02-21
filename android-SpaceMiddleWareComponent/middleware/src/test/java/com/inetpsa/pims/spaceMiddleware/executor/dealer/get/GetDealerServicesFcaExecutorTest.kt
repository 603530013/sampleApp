package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.LATAM
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.mmx.foundation.tools.Market.NONE
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Action
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services.GetEmeaServiceFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services.GetLatamServiceFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services.GetNaftaServiceFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services.Packages
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services.Packages.Validity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetDealerServicesFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetDealerServicesFcaExecutor

    private val testParams = mapOf(
        Input.Appointment.BOOKING_ID to "testBookingID",
        "vin" to "testVin",
        "mileage" to 100,
        "location" to "000"
    )
    private val dealerServicesOutput = ServicesOutput(
        services = listOf(
            Services(
                id = "testCode",
                title = "testTitle",
                type = ServiceType.Factory,
                packages = listOf(
                    Packages(
                        reference = "testReference",
                        title = "testTitle",
                        description = listOf("testDescription"),
                        price = 0F,
                        type = 1,
                        validity = Validity(
                            start = "testStart",
                            end = "testEnd"
                        )
                    )
                )
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetDealerServicesFcaExecutor(baseCommand))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when execute params with invalid services then throw missing parameter`() {
        val input = mapOf(Action.SERVICES to "testServices")
        val exception = PIMSFoundationError.missingParameter(Action.SERVICES)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing services then throw missing parameter`() {
        val input = emptyMap<String, Any>()
        val exception = PIMSFoundationError.missingParameter(Action.SERVICES)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with the right input then return DealerServicesInput`() {
        val input = mapOf(
            Input.ID to "testId",
            Input.VIN to "testVin",
            Constants.PARAM_MILEAGE to 100,
            Constants.LOCATIONS to "000"
        )
        val output = executor.params(input)
        Assert.assertEquals(input, output)
    }

    @Test
    fun `when market is EMEA execute then make a network call with success response`() {
        every { executor.params(any()) } returns testParams
        every { configurationManager.market } returns EMEA
        mockkConstructor(GetEmeaServiceFcaExecutor::class)
        coEvery { GetEmeaServiceFcaExecutor(middlewareComponent, testParams).execute() } returns
            Success(dealerServicesOutput)
        runBlocking {
            val response = executor.execute(testParams)
            coVerify { GetEmeaServiceFcaExecutor(middlewareComponent, testParams).execute() }
            val success = response as Success
            Assert.assertEquals(dealerServicesOutput, success.response)
        }
    }

    @Test
    fun `when market is Maserati execute then make a network call with success response`() {
        every { executor.params(any()) } returns testParams
        every { configurationManager.brand } returns MASERATI
        every { configurationManager.market } returns LATAM
        mockkConstructor(GetEmeaServiceFcaExecutor::class)
        coEvery { GetEmeaServiceFcaExecutor(middlewareComponent, testParams).execute() } returns
            Success(dealerServicesOutput)
        runBlocking {
            val response = executor.execute(testParams)
            coVerify { GetEmeaServiceFcaExecutor(middlewareComponent, testParams).execute() }
            val success = response as Success
            Assert.assertEquals(dealerServicesOutput, success.response)
        }
    }

    @Test
    fun `when market is Latam execute then make a network call with success response`() {
        every { executor.params(any()) } returns testParams
        every { configurationManager.market } returns LATAM
        mockkConstructor(GetLatamServiceFcaExecutor::class)
        coEvery { GetLatamServiceFcaExecutor(middlewareComponent, testParams).execute() } returns
            Success(dealerServicesOutput)
        runBlocking {
            val response = executor.execute(testParams)
            coVerify { GetLatamServiceFcaExecutor(middlewareComponent, testParams).execute() }
            val success = response as Success
            Assert.assertEquals(dealerServicesOutput, success.response)
        }
    }

    @Test
    fun `when market is Nafta execute then make a network call with success response`() {
        every { executor.params(any()) } returns testParams
        every { configurationManager.market } returns NAFTA
        mockkConstructor(GetNaftaServiceFcaExecutor::class)
        coEvery { GetNaftaServiceFcaExecutor(middlewareComponent, testParams).execute() } returns
            Success(dealerServicesOutput)
        runBlocking {
            val response = executor.execute(testParams)
            coVerify { GetNaftaServiceFcaExecutor(middlewareComponent, testParams).execute() }
            val success = response as Success
            Assert.assertEquals(dealerServicesOutput, success.response)
        }
    }

    @Test
    fun `test when market is other than Nafta,Emea,Latam and Maserati throws error`() {
        val error = PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        every { configurationManager.market } returns NONE
        runBlocking {
            try {
                executor.execute(testParams)
            } catch (ex: Throwable) {
                Assert.assertEquals(error.message, ex.message)
            }
        }
    }
}
