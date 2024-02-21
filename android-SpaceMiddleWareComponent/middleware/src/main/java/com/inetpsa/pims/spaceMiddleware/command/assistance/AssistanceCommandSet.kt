package com.inetpsa.pims.spaceMiddleware.command.assistance

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.SetAssistancePSAExecutor

internal class AssistanceCommandSet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.ASK -> SetAssistancePSAExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }
}
