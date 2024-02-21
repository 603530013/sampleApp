package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

data class DeleteDealerAppointmentResponse(

    @field:SerializedName("codRepairOrder")
    val id: String? = null,

    @field:SerializedName("description")
    val description: String? = null
)
