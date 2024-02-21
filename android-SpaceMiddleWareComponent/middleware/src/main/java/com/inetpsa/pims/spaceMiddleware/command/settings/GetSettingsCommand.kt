package com.inetpsa.pims.spaceMiddleware.command.settings

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.GetCallCenterSettingsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.GetSettingsDetailsPsaExecutor
@Deprecated("This should be replaced by SettingsCommandGet")
internal class GetSettingsCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_TYPE_SETTINGS -> GetSettingsDetailsPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_TYPE_SETTINGS -> GetCallCenterSettingsFcaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
