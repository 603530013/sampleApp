package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand.FIAT
import com.inetpsa.mmx.foundation.tools.Brand.JEEP
import com.inetpsa.mmx.foundation.tools.Brand.MASERATI
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.LATAM
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.mmx.foundation.tools.Market.NONE
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history.GetHistoryForEmeaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history.GetHistoryForLatamFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history.GetHistoryForMaseratiFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.history.GetHistoryForNaftaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class GetAppointmentListFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAppointmentListFcaExecutor

    private val input = mapOf(
        Constants.Input.Appointment.BOOKING_ID to "7019500",
        Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
        Constants.Input.VIN to "7019500"
    )

    private val historyOutput =
        HistoryOutput(
            appointments = listOf(
                HistoryOutput.Appointment("serviceIdTest", "", Status.Requested)
            )
        )

    override fun setup() {
        super.setup()
        mockkConstructor(GetHistoryForEmeaFcaExecutor::class)
        mockkConstructor(GetHistoryForMaseratiFcaExecutor::class)
        mockkConstructor(GetHistoryForLatamFcaExecutor::class)
        mockkConstructor(GetHistoryForNaftaFcaExecutor::class)
        executor = spyk(GetAppointmentListFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute with emea region then execute the emea executor`() {
        every { configurationManager.brand } returns FIAT
        every { configurationManager.market } returns EMEA
        coEvery {
            anyConstructed<GetHistoryForEmeaFcaExecutor>().execute(any())
        } returns NetworkResponse.Success(historyOutput)
        runTest {
            executor.execute(input)
            coVerify(exactly = 1) { executor.handleEmeaFlow(any()) }
            coVerify(exactly = 1) { anyConstructed<GetHistoryForEmeaFcaExecutor>().execute(any()) }
        }
    }

    @Test
    fun `when execute with Nafta region then execute the Nafta executor`() {
        every { configurationManager.brand } returns JEEP
        every { configurationManager.market } returns NAFTA
        coEvery {
            anyConstructed<GetHistoryForNaftaFcaExecutor>().execute(any())
        } returns NetworkResponse.Success(historyOutput)
        runTest {
            executor.execute(input)
            coVerify(exactly = 1) { executor.handleNaftaFlow(any()) }
            coVerify(exactly = 1) { anyConstructed<GetHistoryForNaftaFcaExecutor>().execute(any()) }
        }
    }

    @Test
    fun `when execute with Latam region then execute the Latam executor`() {
        every { configurationManager.brand } returns JEEP
        every { configurationManager.market } returns LATAM

        coEvery {
            anyConstructed<GetHistoryForLatamFcaExecutor>().execute(any())
        } returns NetworkResponse.Success(historyOutput)
        runTest {
            executor.execute(input)
            coVerify(exactly = 1) { executor.handleLatamFlow(any()) }
            coVerify(exactly = 1) { anyConstructed<GetHistoryForLatamFcaExecutor>().execute(any()) }
        }
    }

    @Test
    fun `when execute with Maserati region then execute the Masersti executor`() {
        every { configurationManager.brand } returns MASERATI
        every { configurationManager.market } returns EMEA
        coEvery {
            anyConstructed<GetHistoryForMaseratiFcaExecutor>().execute(any())
        } returns NetworkResponse.Success(historyOutput)
        runTest {
            executor.execute(input)
            coVerify(exactly = 1) { executor.handleMaseratiFlow(any()) }
            coVerify(exactly = 1) { anyConstructed<GetHistoryForMaseratiFcaExecutor>().execute(any()) }
        }
    }

    @Test
    fun `test when execute with unknown brand or market throw Pims error`() {
        val pimsError = PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        every { configurationManager.market } returns NONE
        runBlocking {
            try {
                executor.execute(input)
            } catch (error: PIMSError) {
                Assert.assertEquals(pimsError.code, error.code)
                Assert.assertEquals(pimsError.message, error.message)
            }
        }
    }

    @Test
    fun `test params() returns empty map when input is null`() {
        val params = executor.params(null)
        Assert.assertEquals(emptyMap<String, Any?>(), params)
    }

    @Test
    fun `test params() returns params`() {
        val params = executor.params(input)
        Assert.assertEquals(input, params)
        Assert.assertEquals(
            input[Constants.Input.Appointment.BOOKING_ID],
            params[Constants.Input.Appointment.BOOKING_ID]
        )
    }
}
