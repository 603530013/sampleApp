package com.inetpsa.pims.spaceMiddleware.command.account

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.account.GetProfileFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.account.GetProfilePsaExecutor

@Deprecated("this should be replaced with UserCommandGet")
internal class AccountCommandGet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAMS_KEY_PROFILE -> GetProfilePsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.PARAMS_KEY_PROFILE -> GetProfileFcaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
