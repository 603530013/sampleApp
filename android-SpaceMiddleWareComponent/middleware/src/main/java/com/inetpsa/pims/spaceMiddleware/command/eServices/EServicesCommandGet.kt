package com.inetpsa.pims.spaceMiddleware.command.eServices

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.eservices.get.GetChargingStationFiltersExecutor
import com.inetpsa.pims.spaceMiddleware.executor.eservices.get.NearestChargingStationFcaExecutor

internal class EServicesCommandGet : BaseBrandCommand() {

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.CHARGING_STATION -> handleFcaEServicesExecutor()
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleFcaEServicesExecutor(): BaseFcaExecutor<*, *> =
        when (parameters.has<String>(Constants.Input.ACTION)) {
            Constants.Input.Action.LIST -> NearestChargingStationFcaExecutor(this)
            Constants.Input.Action.FILTERS -> GetChargingStationFiltersExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }
}
