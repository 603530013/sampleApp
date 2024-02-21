package com.inetpsa.pims.spaceMiddleware.command.user

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.DeleteAccountFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.DeleteAccountPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.SetProfilePsaExecutor

internal class UserCommandSet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.PROFILE -> SetProfilePsaExecutor(this)
        Constants.Input.ActionType.DELETE -> DeleteAccountPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }
    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.DELETE -> DeleteAccountFcaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }
}
