package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField

internal data class OssTokenInput(
    override var dealerId: String
) : BookingIdField
