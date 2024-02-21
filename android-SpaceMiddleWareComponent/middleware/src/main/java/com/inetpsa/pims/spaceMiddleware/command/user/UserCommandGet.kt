package com.inetpsa.pims.spaceMiddleware.command.user

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.GetProfileFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.GetProfilePsaExecutor

internal class UserCommandGet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.PROFILE -> GetProfilePsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.PROFILE -> GetProfileFcaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }
}
