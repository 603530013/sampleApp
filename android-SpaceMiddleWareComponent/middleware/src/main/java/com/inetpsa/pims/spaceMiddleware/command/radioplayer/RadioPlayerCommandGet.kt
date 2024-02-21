package com.inetpsa.pims.spaceMiddleware.command.radioplayer

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get.GetOnAirPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get.GetRadioStationsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get.GetRecommendationsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

internal class RadioPlayerCommandGet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.STATIONS -> handleStationsPsaActions()
        Constants.Input.ActionType.RECOMMENDATIONS -> GetRecommendationsPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleStationsPsaActions(): BasePsaExecutor<*, *> =
        when (parameters.hasOrNull<String>(Constants.Input.ACTION)) {
            null -> GetRadioStationsPsaExecutor(this)
            Constants.Input.Action.ON_AIR -> GetOnAirPsaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }
}
