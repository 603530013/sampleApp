package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

data class AddVehicleResponse(

    @field:SerializedName("success")
    val success: Boolean?
)
