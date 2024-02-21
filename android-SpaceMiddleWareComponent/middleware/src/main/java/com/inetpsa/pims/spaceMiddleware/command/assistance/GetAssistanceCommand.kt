package com.inetpsa.pims.spaceMiddleware.command.assistance

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetInfoForAssistancePsaExecutor

@Deprecated("try to switch to use this class AssistanceCommandGet")
internal class GetAssistanceCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_VEHICLE_DETAILS -> GetInfoForAssistancePsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
