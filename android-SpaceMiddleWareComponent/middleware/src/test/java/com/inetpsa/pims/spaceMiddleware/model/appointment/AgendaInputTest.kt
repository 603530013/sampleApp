package com.inetpsa.pims.spaceMiddleware.model.appointment

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.MONTH
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.SEMESTER
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaInput.TimeFence.WEEK
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class AgendaInputTest {

    @Test
    fun `when look for endDate with week fence then add a week to startDate`() {
        val startDate = LocalDate.of(2023, 7, 20)
        val input = AgendaInput(
            startDate = startDate,
            timeFence = WEEK,
            dealerId = "testDealerId",
            dealerLocation = null,
            vin = "testVin",
            serviceIds = null
        )
        Assert.assertEquals(startDate.plusWeeks(AgendaInput.WEEK_VALUE), input.endDate)
    }

    @Test
    fun `when look for endDate with month fence then add a month to startDate`() {
        val startDate = LocalDate.of(2023, 7, 20)
        val input = AgendaInput(
            startDate = startDate,
            timeFence = MONTH,
            dealerId = "testDealerId",
            dealerLocation = null,
            vin = "testVin",
            serviceIds = null
        )
        Assert.assertEquals(startDate.plusMonths(AgendaInput.MONTH_VALUE), input.endDate)
    }

    @Test
    fun `when look for endDate with semester fence then add a semester to startDate`() {
        val startDate = LocalDate.of(2023, 7, 20)
        val input = AgendaInput(
            startDate = startDate,
            timeFence = SEMESTER,
            dealerId = "testDealerId",
            dealerLocation = null,
            vin = "testVin",
            serviceIds = null
        )
        Assert.assertEquals(startDate.plusMonths(AgendaInput.SEMESTER_VALUE), input.endDate)
    }

    @Test
    fun `when look for startDateMilliSeconds then return the right value`() {
        val startDate = LocalDate.of(2023, 7, 20)
        val input = AgendaInput(
            startDate = startDate,
            timeFence = SEMESTER,
            dealerId = "testDealerId",
            dealerLocation = null,
            vin = "testVin",
            serviceIds = null
        )
        Assert.assertEquals(1689811200000, input.startDateMilliSeconds)
    }

    @Test
    fun `when look for endDateMilliSeconds then return the right value`() {
        val startDate = LocalDate.of(2023, 7, 20)
        val input = AgendaInput(
            startDate = startDate,
            timeFence = SEMESTER,
            dealerId = "testDealerId",
            dealerLocation = null,
            vin = "testVin",
            serviceIds = null
        )
        Assert.assertEquals(1705708800000, input.endDateMilliSeconds)
    }
}
