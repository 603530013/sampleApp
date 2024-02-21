package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateXPSAInput

internal interface AppointmentInputMapper<T : CreateInput> {

    fun transformBodyRequest(input: CreateXPSAInput): String
}
