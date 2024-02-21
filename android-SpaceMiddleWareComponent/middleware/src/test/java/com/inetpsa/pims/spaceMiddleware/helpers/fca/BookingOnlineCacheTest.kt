package com.inetpsa.pims.spaceMiddleware.helpers.fca

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse.TransportationOption
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse.ServiceAdvisors
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse.Segment
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse.Segment.Slot
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.LatamDealerServiceResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.NaftaDealerServicesResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryLATAMResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryLATAMResponse.Appointment
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

internal class BookingOnlineCacheTest {

    val tokenKey1 = OssTokenInput("testDealerId1")
    val tokenData1 = OssTokenCache(
        token = "testToken1",
        expireTime = LocalDateTime.of(2023, 11, 3, 3, 46, 0, 0)
    )
    val tokenKey2 = OssTokenInput("testDealerId2")
    val tokenData2 = OssTokenCache(
        token = "testToken2",
        expireTime = LocalDateTime.of(2023, 11, 3, 3, 46, 2, 0)
    )

    private val departmentIdKey1 = OssTokenInput("testDealerId1")
    private val departmentIdData1 = 123
    private val departmentIdKey2 = OssTokenInput("testDealerId2")
    private val departmentIdData2 = 456

    @Before
    fun setup() {
        mockkObject(BookingOnlineCache)
    }

    @Test
    fun `when write token then we can read value from cache`() {
        BookingOnlineCache.write(tokenKey1, tokenData1)
        val data = BookingOnlineCache.readToken(tokenKey1)
        Assert.assertEquals(tokenData1, data)
    }

    @Test
    fun `when remove a token key then we can read other values from cache`() {
        BookingOnlineCache.write(tokenKey1, tokenData1)
        BookingOnlineCache.write(tokenKey2, tokenData2)
        BookingOnlineCache.removeToken(tokenKey1)
        Assert.assertNull(BookingOnlineCache.readToken(tokenKey1))
        val data = BookingOnlineCache.readToken(tokenKey2)
        Assert.assertEquals(tokenData2, data)
    }

    @Test
    fun `when clear cache we can't read any values from cache`() {
        BookingOnlineCache.write(tokenKey1, tokenData1)
        BookingOnlineCache.write(tokenKey2, tokenData2)
        BookingOnlineCache.write(departmentIdKey1, departmentIdData1)
        BookingOnlineCache.write(departmentIdKey2, departmentIdData2)
        BookingOnlineCache.clear()
        Assert.assertNull(BookingOnlineCache.readToken(tokenKey1))
        Assert.assertNull(BookingOnlineCache.readToken(tokenKey2))
        Assert.assertNull(BookingOnlineCache.readDepartmentId(departmentIdKey1))
        Assert.assertNull(BookingOnlineCache.readDepartmentId(departmentIdKey2))
    }

    @Test
    fun `when generate key then return concatenated value`() {
        val generate1 = BookingOnlineCache.generateKey(tokenKey1)
        Assert.assertEquals("testDealerId1", generate1)
        val generate2 = BookingOnlineCache.generateKey(tokenKey2)
        Assert.assertEquals("testDealerId2", generate2)
    }

    @Test
    fun `when write departmentId then we can read value from cache`() {
        BookingOnlineCache.write(departmentIdKey1, departmentIdData1)
        val data = BookingOnlineCache.readDepartmentId(departmentIdKey1)
        Assert.assertEquals(departmentIdData1, data)
    }

    @Test
    fun `when remove a departmentId key then we can read other values from cache`() {
        BookingOnlineCache.write(departmentIdKey1, departmentIdData1)
        BookingOnlineCache.write(departmentIdKey2, departmentIdData2)
        BookingOnlineCache.removeDepartmentId(departmentIdKey1)
        Assert.assertNull(BookingOnlineCache.readDepartmentId(departmentIdKey1))
        val data = BookingOnlineCache.readDepartmentId(departmentIdKey2)
        Assert.assertEquals(departmentIdData2, data)
    }

    @Test
    fun `when clearAfterCreation then we can't read any values from cache`() {
        BookingOnlineCache.write(
            ServiceType.Factory,
            listOf(NaftaDealerServicesResponse.Service("testDealerId1", "testServiceId1"))
        )
        BookingOnlineCache.write(
            ServiceType.Dealer,
            listOf(NaftaDealerServicesResponse.Service("testDealerId2", "testServiceId2"))
        )
        BookingOnlineCache.write(
            ServiceType.Repair,
            listOf(NaftaDealerServicesResponse.Service("testDealerId3", "testServiceId3"))
        )
        BookingOnlineCache.write(
            ServiceType.Recall,
            listOf(NaftaDealerServicesResponse.Service("testDealerId4", "testServiceId4"))
        )
        BookingOnlineCache.write(
            ServiceType.Recall,
            listOf(LatamDealerServiceResponse.Service("testDealerId5", "testServiceId5"))
        )
        BookingOnlineCache.clearAfterCreation()
        Assert.assertNull(BookingOnlineCache.read(ServiceType.Factory))
        Assert.assertNull(BookingOnlineCache.read(ServiceType.Dealer))
        Assert.assertNull(BookingOnlineCache.read(ServiceType.Repair))
        Assert.assertNull(BookingOnlineCache.read(ServiceType.Recall))
    }

    // add unit test read and write services
    @Test
    fun `when write services then we can read value from cache`() {
        BookingOnlineCache.write(
            ServiceType.Factory,
            listOf(NaftaDealerServicesResponse.Service("testDealerId1", "testServiceId1"))
        )

        BookingOnlineCache.write(
            ServiceType.Dealer,
            listOf(LatamDealerServiceResponse.Service("testDealerId2", "testServiceId2"))
        )
        val data = BookingOnlineCache.read(ServiceType.Factory)
        Assert.assertEquals(
            listOf(NaftaDealerServicesResponse.Service("testDealerId1", "testServiceId1")),
            data
        )
    }

    @Test
    fun `test write and read TransportationOptions `() {
        val transportationOptionResponse = TransportationOptionsResponse(
            transportations = listOf(
                TransportationOption(
                    "testDealerId1",
                    "testServiceId1"
                )
            )
        )
        BookingOnlineCache.write(
            transportationOptionResponse
        )
        Assert.assertEquals(
            transportationOptionResponse.transportations,
            BookingOnlineCache.readTransportationOptions()
        )
        transportationOptionResponse.transportations?.get(0)?.let {
            Assert.assertEquals(it, BookingOnlineCache.readTransportationOption(it.code ?: ""))
        }
    }

    @Test
    fun `test read and write DealerAdvisorResponse ServiceAdvisors`() {
        val dealerAdvisorResponse = DealerAdvisorResponse(
            advisors = listOf(
                ServiceAdvisors(
                    12,
                    "testServiceId1"
                ),
                ServiceAdvisors(
                    10,
                    "testServiceId1"
                )
            )
        )
        BookingOnlineCache.write(
            dealerAdvisorResponse
        )
        Assert.assertEquals(
            dealerAdvisorResponse.advisors,
            BookingOnlineCache.readAdvisors()
        )
        dealerAdvisorResponse.advisors?.get(0)?.let {
            Assert.assertEquals(it, BookingOnlineCache.readAdvisor(it.id ?: 0))
        }
        dealerAdvisorResponse.advisors?.get(0)?.let {
            Assert.assertEquals(it, BookingOnlineCache.readAdvisor(it.id ?: 0))
        }
    }

    @Test
    fun `test read and write AppointmentHistoryNAFTAResponse`() {
        val appointmentDetailsNaftaResponse = AppointmentHistoryNAFTAResponse(
            appointments = listOf(
                AppointmentHistoryNAFTAResponse.Appointment(
                    "testDealerId1",
                    "testServiceId1"
                ),
                AppointmentHistoryNAFTAResponse.Appointment(
                    "testDealerId2",
                    "testServiceId2"
                )
            )
        )
        BookingOnlineCache.write(
            appointmentDetailsNaftaResponse
        )
        Assert.assertEquals(
            appointmentDetailsNaftaResponse.appointments,
            BookingOnlineCache.readAppointmentsFromNafta()
        )
        appointmentDetailsNaftaResponse.appointments?.get(0)?.let {
            Assert.assertEquals(
                it,
                BookingOnlineCache.readAppointmentFromNafta(it.appointmentId ?: "")
            )
        }
        appointmentDetailsNaftaResponse.appointments?.get(1)?.let {
            Assert.assertEquals(
                it,
                BookingOnlineCache.readAppointmentFromNafta(it.appointmentId ?: "")
            )
        }
    }

    @Test
    fun `test read and write AppointmentHistoryLATAMResponse`() {
        val appointmentHistoryLATAMResponse = AppointmentHistoryLATAMResponse(
            appointments = listOf(
                Appointment(
                    "testDealerId1",
                    "testServiceId1"
                ),
                Appointment(
                    "testDealerId2",
                    "testServiceId2"
                )
            )
        )
        BookingOnlineCache.write(
            appointmentHistoryLATAMResponse
        )
        Assert.assertEquals(
            appointmentHistoryLATAMResponse.appointments,
            BookingOnlineCache.readAppointmentsFromLatam()
        )
        appointmentHistoryLATAMResponse.appointments?.get(1)?.let {
            Assert.assertEquals(
                it,
                BookingOnlineCache.readAppointmentFromLatam(it.appointmentId ?: "")
            )
        }
    }

    @Test
    fun `test read and write DealerAgendaLATAMResponse`() {
        val dealerAgendaLATAMResponse = DealerAgendaLATAMResponse(
            segments = listOf(
                Segment(
                    date = 1001L,
                    slots = listOf(
                        Slot(
                            10002L,
                            12
                        )
                    )
                )
            )
        )
        BookingOnlineCache.write(
            dealerAgendaLATAMResponse
        )
        dealerAgendaLATAMResponse.segments?.get(0)?.let {
            Assert.assertEquals(
                it.slots!![0],
                BookingOnlineCache.readLatamSlot(it.slots[0].time ?: 0)
            )
        }
    }
}
