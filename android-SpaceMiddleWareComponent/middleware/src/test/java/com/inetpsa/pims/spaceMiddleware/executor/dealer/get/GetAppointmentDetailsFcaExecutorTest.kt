package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand.FIAT
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.LATAM
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.mmx.foundation.tools.Market.NONE
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details.GetAppointmentDetailsForEmeaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details.GetAppointmentDetailsForLatamExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details.GetAppointmentDetailsForMaseratiExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.details.GetAppointmentDetailsForNaftaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Booked
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAppointmentDetailsFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAppointmentDetailsFcaExecutor

    val input = mapOf(
        Constants.Input.VIN to "testVin",
        Constants.Input.ID to "testAppointmentId",
        Constants.Input.Appointment.BOOKING_ID to "testBookingId"
    )
    private val detailsOutput = DetailsOutput(
        id = "testServiceId",
        vin = "testVin",
        bookingId = "testDealerId",
        bookingLocation = "testLocation",
        scheduledTime = "2023-09-29T12:50:01Z",
        comment = "testFaultDescription",
        mileage = "200000",
        email = "testEmail",
        phone = "testTelephone",
        status = Booked,
        services = listOf(
            Service(
                "testServiceList",
                comment = "testComment",
                type = ServiceType.Dealer,
                name = "testServiceName",
                opCode = "testOpCode",
                price = 1234f
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetAppointmentDetailsForEmeaExecutor::class)
        mockkConstructor(GetAppointmentDetailsForNaftaExecutor::class)
        mockkConstructor(GetAppointmentDetailsForLatamExecutor::class)
        mockkConstructor(GetAppointmentDetailsForMaseratiExecutor::class)
        mockkObject(BookingOnlineCache)
        coEvery {
            anyConstructed<GetAppointmentDetailsForEmeaExecutor>().execute(any())
        } returns Success(detailsOutput)
        coEvery {
            anyConstructed<GetAppointmentDetailsForNaftaExecutor>().execute(any())
        } returns Success(detailsOutput)
        coEvery {
            anyConstructed<GetAppointmentDetailsForLatamExecutor>().execute(any())
        } returns Success(detailsOutput)
        coEvery {
            anyConstructed<GetAppointmentDetailsForMaseratiExecutor>().execute(any())
        } returns Success(detailsOutput)

        executor = spyk(GetAppointmentDetailsFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with the right input then return DeleteAppointmentInput`() {
        val output = executor.params(input)
        Assert.assertEquals(input, output)
    }

    @Test
    fun `when market is EMEA execute then make a network call with success response`() = runTest {
        every { executor.params(any()) } returns input
        every { configurationManager.market } returns EMEA
        val result = executor.execute(input)
        coVerify(exactly = 1) { executor.handleEmeaFlow(input) }
        coVerify(exactly = 1) { anyConstructed<GetAppointmentDetailsForEmeaExecutor>().execute(any()) }
        val success = result as Success
        Assert.assertEquals(detailsOutput, success.response)
    }

    @Test
    fun `when market is NAFTA execute then make a network call with success response`() = runTest {
        mockkStatic(BookingOnlineCache::class)
        every {
            BookingOnlineCache.readAppointmentFromNafta("testAppointmentId")
        } returns AppointmentHistoryNAFTAResponse.Appointment(dealerId = "testDealerId")
        every { executor.params(any()) } returns input
        every { configurationManager.market } returns NAFTA
        coEvery {
            anyConstructed<GetAppointmentDetailsForNaftaExecutor>().execute(any())
        } returns Success(detailsOutput)
        val result = executor.execute(input)
        coVerify(exactly = 1) { executor.handleNaftaFlow(input) }
        coVerify(exactly = 1) { anyConstructed<GetAppointmentDetailsForNaftaExecutor>().execute(any()) }
        val success = result as Success
        Assert.assertEquals(detailsOutput, success.response)
    }

    @Test
    fun `when market is LATAM execute then make a network call with success response`() = runTest {
        every { executor.params(any()) } returns input
        every { configurationManager.market } returns LATAM
        coEvery {
            anyConstructed<GetAppointmentDetailsForLatamExecutor>().execute(any())
        } returns Success(detailsOutput)
        val result = executor.execute(input)
        coVerify(exactly = 1) { executor.handleLatamFlow(input) }
        coVerify(exactly = 1) { anyConstructed<GetAppointmentDetailsForLatamExecutor>().execute(any()) }
        val success = result as Success
        Assert.assertEquals(detailsOutput, success.response)
    }

    @Test
    fun `when market is Maserati execute then make a network call with success response`() = runTest {
        every { executor.params(any()) } returns input
        every { configurationManager.brand } returns MASERATI
        coEvery {
            anyConstructed<GetAppointmentDetailsForMaseratiExecutor>().execute(any())
        } returns Success(detailsOutput)
        val result = executor.execute(input)
        coVerify(exactly = 1) { executor.handleMaseratiFlow(input) }
        coVerify(exactly = 1) { anyConstructed<GetAppointmentDetailsForMaseratiExecutor>().execute(any()) }
        val success = result as Success
        Assert.assertEquals(detailsOutput, success.response)
    }

    @Test
    fun `when execute with wrong market then throw an exception`() {
        every { configurationManager.brand } returns FIAT
        every { configurationManager.market } returns NONE
        val exception = PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        runTest {
            try {
                executor.execute(input)
            } catch (e: PIMSError) {
                Assert.assertEquals(exception.code, e.code)
                Assert.assertEquals(exception.message, e.message)
            }
        }
    }
}
