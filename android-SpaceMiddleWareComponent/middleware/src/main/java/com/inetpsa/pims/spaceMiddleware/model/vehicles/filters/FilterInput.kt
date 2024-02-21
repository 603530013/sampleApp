package com.inetpsa.pims.spaceMiddleware.model.vehicles.filters

internal data class FilterInput(

    val onlyAvailable: Boolean? = null,
    val partnerOnly: Boolean? = null,
    val minimumAvailable: Boolean? = null,
    val compatibleOnly: Boolean? = null,
    val accessTypes: List<String>? = null,
    val connectorTypes: List<String>? = null,
    val acceptablePayments: List<String>? = null,
    val specialRestrictions: List<String>? = null,
    val powerTypes: List<String>? = null,
    val access: List<String>? = null,
    val open24Hours: Boolean? = null,
    val chargingCableAttached: Boolean? = null,
    val free: Boolean? = null,
    val indoor: Boolean? = null,
    val renewableEnergy: Boolean? = null,
    val openOnly: Boolean? = null
)
