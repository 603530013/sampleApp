package com.inetpsa.pims.spaceMiddleware.command.assistance

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetAssistanceFaqPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetAssistancePSAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetAssistancePhonesFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.assistance.GetAssistancePhonesPsaExecutor

internal class AssistanceCommandGet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.DETAILS -> GetAssistancePSAExecutor(this)
        Constants.Input.ActionType.FAQ -> GetAssistanceFaqPsaExecutor(this)
        Constants.Input.ActionType.CALL_CENTERS -> GetAssistancePhonesPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.CALL_CENTERS -> GetAssistancePhonesFcaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }
}
