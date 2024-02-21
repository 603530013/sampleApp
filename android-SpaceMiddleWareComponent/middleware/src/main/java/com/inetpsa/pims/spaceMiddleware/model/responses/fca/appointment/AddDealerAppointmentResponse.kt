package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

data class AddDealerAppointmentResponse(

    @field:SerializedName("codRepairOrder")
    val codRepairOrder: String? = null,

    @field:SerializedName("description")
    val description: String? = null
)
