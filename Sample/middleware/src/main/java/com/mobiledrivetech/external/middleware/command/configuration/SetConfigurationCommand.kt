package com.mobiledrivetech.external.middleware.command.configuration

import com.mobiledrivetech.external.middleware.command.BaseCommand
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.executor.configuration.SetConfigurationExecutor
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput

internal class SetConfigurationCommand : BaseCommand() {

    override suspend fun getExecutor(): BaseLocalExecutor<ConfigurationInput, Unit> =
        SetConfigurationExecutor(this)
}
