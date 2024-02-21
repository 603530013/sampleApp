package com.inetpsa.pims.spaceMiddleware.model.profile

@Deprecated("Should switch to use ProfileOutput")
internal data class ProfileOutput(
    val uid: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val civility: String? = null,
    val civilityCode: String? = null,
    val locale: String? = null,
    val phones: List<String>? = null,
    val address1: String? = null,
    val address2: String? = null,
    val zipCode: String? = null,
    val city: String? = null,
    val country: String? = null
)
