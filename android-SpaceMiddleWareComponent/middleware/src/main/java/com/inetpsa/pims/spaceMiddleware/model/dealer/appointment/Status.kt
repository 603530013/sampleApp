package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal enum class Status {

    @SerializedName("requested")
    Requested,

    @SerializedName("booked")
    Booked,

    @SerializedName("cancelled")
    Cancelled,

    @SerializedName("closed")
    Closed
}
