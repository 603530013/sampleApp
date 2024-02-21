package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment

internal data class DepartmentIdInput(
    override var dealerId: String,
    val services: List<String>? = null
) : BookingIdField
