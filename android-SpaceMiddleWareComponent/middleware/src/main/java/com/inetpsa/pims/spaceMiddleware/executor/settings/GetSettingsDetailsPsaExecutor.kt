package com.inetpsa.pims.spaceMiddleware.executor.settings

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.settings.Settings
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager

@Deprecated("This should be replaced by GetSettingsPSAExecutor")
internal class GetSettingsDetailsPsaExecutor(command: BaseCommand) : BasePsaExecutor<Unit, Settings>(command) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Settings> {
        val queries = mapOf(Constants.QUERY_PARAM_KEY_CULTURE to configurationManager.locale.toString())

        val request = request(
            Settings::class.java,
            arrayOf("/settings/v1/settings"),
            queries = queries
        )

        return communicationManager.get(request, MiddlewareCommunicationManager.MymToken)
    }
}
