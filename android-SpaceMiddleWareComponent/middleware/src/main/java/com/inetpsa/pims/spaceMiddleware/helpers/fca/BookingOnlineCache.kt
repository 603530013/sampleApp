package com.inetpsa.pims.spaceMiddleware.helpers.fca

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenCache
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TransportationOptionsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaLATAMResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerServiceId
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryLATAMResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.history.AppointmentHistoryNAFTAResponse

@Suppress("TooManyFunctions")
internal object BookingOnlineCache {

    private var tokens: HashMap<String, OssTokenCache> = hashMapOf()
    private var departmentIds: HashMap<String, Int> = hashMapOf()

    private var dealerId: String? = null

    private var servicesFactory: List<DealerServiceId>? = null
    private var servicesDealer: List<DealerServiceId>? = null
    private var servicesRepair: List<DealerServiceId>? = null
    private var servicesRecall: List<DealerServiceId>? = null

    private var agendaLatamSlots: Map<Long, DealerAgendaLATAMResponse.Segment.Slot>? = null
    private var transportationOptions: List<TransportationOptionsResponse.TransportationOption>? = null
    private var advisors: List<DealerAdvisorResponse.ServiceAdvisors>? = null

    private var appointmentsLatam: List<AppointmentHistoryLATAMResponse.Appointment>? = null
    private var appointmentsNafta: List<AppointmentHistoryNAFTAResponse.Appointment>? = null

    fun readToken(key: BookingIdField): OssTokenCache? = tokens[generateKey(key)]

    fun readDepartmentId(key: BookingIdField): Int? = departmentIds[generateKey(key)]

    fun write(key: BookingIdField, data: OssTokenCache) {
        tokens[generateKey(key)] = data
    }

    fun write(key: BookingIdField, id: Int) {
        departmentIds[generateKey(key)] = id
    }

    fun removeToken(key: BookingIdField) {
        tokens.remove(generateKey(key))
    }

    fun removeDepartmentId(key: BookingIdField) {
        departmentIds.remove(generateKey(key))
    }

    fun clear() {
        tokens.clear()
        departmentIds.clear()
        transportationOptions = null
        advisors = null
        appointmentsLatam = null
        appointmentsNafta = null
    }

    fun clearAfterCreation() {
        servicesFactory = null
        servicesDealer = null
        servicesRepair = null
        servicesRecall = null
        agendaLatamSlots = null
        transportationOptions = null
        advisors = null
        appointmentsLatam = null
        appointmentsNafta = null
        dealerId = null
    }

    fun checkIfSameDealer(dealerId: String) {
        if (this.dealerId != dealerId) {
            clearAfterCreation()
            this.dealerId = dealerId
        }
    }

    fun read(type: ServiceType): List<DealerServiceId>? =
        when (type) {
            ServiceType.Factory -> servicesFactory
            ServiceType.Dealer -> servicesDealer
            ServiceType.Repair -> servicesRepair
            ServiceType.Recall -> servicesRecall
            else -> null
        }

    fun write(type: ServiceType, services: List<DealerServiceId>?) {
        when (type) {
            ServiceType.Factory -> servicesFactory = services
            ServiceType.Dealer -> servicesDealer = services
            ServiceType.Repair -> servicesRepair = services
            ServiceType.Recall -> servicesRecall = services

            else -> {
                // ignore
            }
        }
    }

    fun readLatamSlot(time: Long): DealerAgendaLATAMResponse.Segment.Slot? =
        agendaLatamSlots?.get(time)

    fun write(response: DealerAgendaLATAMResponse) {
        agendaLatamSlots = response.segments
            ?.asSequence()
            ?.mapNotNull { segment -> segment.slots?.filter { it.time != null } }
            ?.flatten()
            ?.associateBy { it.time!! }
    }

    fun readTransportationOption(code: String): TransportationOptionsResponse.TransportationOption? =
        transportationOptions?.firstOrNull { it.code == code }

    fun readTransportationOptions(): List<TransportationOptionsResponse.TransportationOption>? =
        transportationOptions

    fun write(response: TransportationOptionsResponse) {
        transportationOptions = response.transportations
    }

    fun readAdvisor(id: Int): DealerAdvisorResponse.ServiceAdvisors? =
        advisors?.firstOrNull { it.id == id }

    fun readAdvisors(): List<DealerAdvisorResponse.ServiceAdvisors>? =
        advisors

    fun write(response: DealerAdvisorResponse) {
        advisors = response.advisors
    }

    fun readAppointmentFromLatam(id: String): AppointmentHistoryLATAMResponse.Appointment? =
        appointmentsLatam?.firstOrNull { it.appointmentId == id }

    fun readAppointmentsFromLatam(): List<AppointmentHistoryLATAMResponse.Appointment>? =
        appointmentsLatam

    fun write(response: AppointmentHistoryLATAMResponse) {
        appointmentsLatam = response.appointments
    }

    fun readAppointmentFromNafta(id: String): AppointmentHistoryNAFTAResponse.Appointment? =
        appointmentsNafta?.firstOrNull { it.appointmentId == id }

    fun readAppointmentsFromNafta(): List<AppointmentHistoryNAFTAResponse.Appointment>? =
        appointmentsNafta

    fun write(response: AppointmentHistoryNAFTAResponse) {
        appointmentsNafta = response.appointments
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateKey(input: BookingIdField): String = input.dealerId
}
