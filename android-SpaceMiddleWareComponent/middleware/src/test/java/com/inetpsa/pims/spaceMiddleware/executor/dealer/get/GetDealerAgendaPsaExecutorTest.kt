package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.MONTH
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse.DaysItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse.DaysItem.SlotsItem
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
import java.time.LocalDate

internal class GetDealerAgendaPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetDealerAgendaPsaExecutor
    private val startDate = LocalDate.of(2023, 7, 20)

    private val agendaResponse = AgendaResponse(
        rrdi = "testRrdi",
        period = 2,
        from = "testFrom",
        to = "testTo",
        type = 1,
        days = listOf(
            DaysItem(
                date = "testDate",
                slots = listOf(
                    SlotsItem(
                        receptionTotal = 1,
                        start = "testStart",
                        discount = 1f,
                        end = "testEnd",
                        receptionAvailable = 1
                    )
                )
            )
        )
    )

    private val result = AgendaOutput(
        rrdi = "testRrdi",
        period = 2,
        from = "testFrom",
        to = "testTo",
        type = 1,
        days = listOf(
            AgendaOutput.DaysItem(
                date = "testDate",
                slots = listOf(
                    AgendaOutput.DaysItem.SlotsItem(
                        receptionTotal = 1,
                        start = "testStart",
                        discount = 1f,
                        end = "testEnd",
                        receptionAvailable = 1
                    )
                )
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetDealerAgendaPsaExecutor(baseCommand))
    }

    @Suppress("LongMethod")
    @Test
    fun `when execute then make a get API call`() {
        val input = AgendaInput(
            dealerId = "testId",
            startDate = startDate,
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = ""
        )

        every { executor.params(any()) } returns input

        coEvery { communicationManager.get<AgendaResponse>(any(), any()) } returns Success(agendaResponse)

        runTest {
            val response = executor.execute()

            val queries = mapOf(
                Constants.QUERY_PARAM_KEY_FROM to "20230720",
                Constants.QUERY_PARAM_KEY_TO to "20230820"
            )

            verify {
                executor.request(
                    type = eq(AgendaResponse::class.java),
                    urls = eq(arrayOf("/shop/v1/rdv/agenda/testId")),
                    queries = eq(queries)
                )
            }

            coVerify {
                communicationManager.get<AgendaResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = (response as Success).response
            Assert.assertEquals(result.days?.size, success.days?.size)
            Assert.assertEquals(result.rrdi, success.rrdi)
            Assert.assertEquals(result.period, success.period)
            Assert.assertEquals(result.from, success.from)
            Assert.assertEquals(result.to, success.to)
            Assert.assertEquals(result.type, success.type)
            Assert.assertArrayEquals(
                result.days?.mapNotNull { it.date }?.toTypedArray(),
                success.days?.mapNotNull { it.date }?.toTypedArray()
            )
            Assert.assertArrayEquals(
                result.days?.mapNotNull { it.date }?.toTypedArray(),
                success.days?.mapNotNull { it.date }?.toTypedArray()
            )
            val successSlots = success.days
                ?.mapNotNull { it.slots }
                ?.flatten()
            val expectedSlots = result.days
                ?.mapNotNull { it.slots }
                ?.flatten()
            Assert.assertArrayEquals(
                expectedSlots?.mapNotNull { it.receptionTotal }?.toTypedArray(),
                successSlots?.mapNotNull { it.receptionTotal }?.toTypedArray()
            )
            Assert.assertArrayEquals(
                expectedSlots?.mapNotNull { it.start }?.toTypedArray(),
                successSlots?.mapNotNull { it.start }?.toTypedArray()
            )
            Assert.assertArrayEquals(
                expectedSlots?.mapNotNull { it.discount }?.toTypedArray(),
                successSlots?.mapNotNull { it.discount }?.toTypedArray()
            )
            Assert.assertArrayEquals(
                expectedSlots?.mapNotNull { it.end }?.toTypedArray(),
                successSlots?.mapNotNull { it.end }?.toTypedArray()
            )
            Assert.assertArrayEquals(
                expectedSlots?.mapNotNull { it.receptionAvailable }?.toTypedArray(),
                successSlots?.mapNotNull { it.receptionAvailable }?.toTypedArray()
            )
        }
    }

    @Test
    fun `when execute params with the right input then return DealerAgendaPsaParams`() {
        val params = AgendaInput(
            dealerId = "7019500",
            startDate = startDate,
            timeFence = MONTH,
            vin = "testVin",
            serviceIds = null
        )

        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "7019500",
            Constants.Input.Appointment.START_DATE to "2023-07-20",
            Constants.Input.Appointment.TIME_FENCE to "month",
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.SERVICES to ""
        )
        val output = executor.params(input)
        Assert.assertEquals(params, output)
    }

    @Test
    fun `when execute params with missing sitegeo then throw missing parameter`() {
        val agendaParams = mapOf(Constants.Input.Appointment.START_DATE to "20230724")
        val input = mapOf(
            Constants.Input.Action.AGENDA to agendaParams
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
