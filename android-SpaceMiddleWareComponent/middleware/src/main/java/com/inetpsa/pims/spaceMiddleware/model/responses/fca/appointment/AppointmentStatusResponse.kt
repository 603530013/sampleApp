package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal enum class AppointmentStatusResponse {

    @SerializedName("requested")
    Requested,

    @SerializedName("booked")
    Booked,

    @SerializedName("cancelled")
    Cancelled,

    @SerializedName("closed")
    Closed
}
