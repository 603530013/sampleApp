package com.inetpsa.pims.spaceMiddleware.model.vehicles

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
internal data class VehicleVinOutput(
    override val vin: String,
    val description: String? = null,
    val picture: String? = null,
    val make: String? = null,
    val subMake: String? = null,
    val year: Int? = null,
    val sdp: String? = null,
    val tcuType: String? = null,
    val userid: String? = null,
    val tcCountryCode: String? = null
) : Parcelable, VinField
