package com.inetpsa.pims.spaceMiddleware.command.settings

import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.set.SetLanguageExecutor

internal class SettingsCommandSet : BaseBrandCommand() {

    override suspend fun getCommonExecutor(): BaseLocalExecutor<*, *>? = when (actionType) {
        Constants.Input.ActionType.LANGUAGE -> SetLanguageExecutor(this)
        else -> null
    }
}
