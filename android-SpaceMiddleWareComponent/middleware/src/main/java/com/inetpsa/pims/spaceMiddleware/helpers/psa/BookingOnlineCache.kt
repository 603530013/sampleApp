package com.inetpsa.pims.spaceMiddleware.helpers.psa

import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse

internal object BookingOnlineCache {
    var psaAgenda: AgendaResponse? = null
    var psaServices: PackageResponse? = null

    fun clear() {
        psaAgenda = null
        psaServices = null
    }
}
