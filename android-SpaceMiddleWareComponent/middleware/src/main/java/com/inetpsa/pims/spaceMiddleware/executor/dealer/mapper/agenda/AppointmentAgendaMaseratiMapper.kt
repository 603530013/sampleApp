package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.agenda

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.agenda.AgendaOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAgendaMaseratiResponse

internal class AppointmentAgendaMaseratiMapper {

    fun transformOutput(response: DealerAgendaMaseratiResponse): AgendaOutput {
        val transform = response.agenda?.map { agenda ->
            AgendaOutput.DaysItem(
                date = agenda.date,
                slots = agenda.slots?.map { slot -> AgendaOutput.DaysItem.SlotsItem(start = slot.time) }
            )
        }
        return AgendaOutput(days = transform)
    }
}
