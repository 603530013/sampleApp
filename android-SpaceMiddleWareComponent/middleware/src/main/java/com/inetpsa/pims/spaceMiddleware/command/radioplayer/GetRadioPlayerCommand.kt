package com.inetpsa.pims.spaceMiddleware.command.radioplayer

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.GetOnAirPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.GetRecommendationsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.radioplayer.GetStationsPsaExecutor

@Deprecated("This should be replaced by RadioPlayerCommandGet")
internal class GetRadioPlayerCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_TYPE_RP_STATIONS -> GetStationsPsaExecutor(this)
        Constants.PARAM_ACTION_TYPE_RP_RECOMMENDATIONS -> GetRecommendationsPsaExecutor(this)
        Constants.PARAM_ACTION_TYPE_RP_ON_AIR -> GetOnAirPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
