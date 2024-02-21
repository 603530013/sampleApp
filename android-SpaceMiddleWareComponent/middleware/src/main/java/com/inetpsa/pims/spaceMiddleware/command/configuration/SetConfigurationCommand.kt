package com.inetpsa.pims.spaceMiddleware.command.configuration

import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.executor.configuration.SetConfigurationExecutor
import com.inetpsa.pims.spaceMiddleware.model.configuration.ConfigurationInput

internal class SetConfigurationCommand : BaseCommand() {

    override suspend fun getExecutor(): BaseLocalExecutor<ConfigurationInput, Unit> =
        SetConfigurationExecutor(this)
}
