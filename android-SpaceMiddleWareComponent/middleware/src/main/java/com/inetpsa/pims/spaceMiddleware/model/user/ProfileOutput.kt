package com.inetpsa.pims.spaceMiddleware.model.user

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
internal data class ProfileOutput(
    val uid: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val civility: String? = null,
    val civilityCode: String? = null,
    val locale: String? = null,
    val phones: Map<String, String>? = null,
    val address1: String? = null,
    val address2: String? = null,
    val zipCode: String? = null,
    val city: String? = null,
    val country: String? = null,
    val image: String? = null
) : Parcelable {

    companion object {

        const val KEY_PHONE_DEFAULT = "DEFAULT"
        const val KEY_PHONE_MOBILE = "MOBILE"
        const val KEY_PHONE_MOBILE_PRO = "MOBILE_PRO"
    }
}
