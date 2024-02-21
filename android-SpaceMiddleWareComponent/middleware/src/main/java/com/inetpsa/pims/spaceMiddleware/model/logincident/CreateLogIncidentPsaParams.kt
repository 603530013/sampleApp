package com.inetpsa.pims.spaceMiddleware.model.logincident

internal data class CreateLogIncidentPsaParams(
    val siteCode: String? = null,
    val vin: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val nationalID: String? = null,
    val phone: String? = null,
    val zipCode: String? = null,
    val city: String? = null,
    val email: String? = null,
    val idClient: String? = null,
    val culture: String? = null,
    val title: String,
    val comment: String,
    val optin1: Boolean = false,
    val optin2: Boolean = false
)
