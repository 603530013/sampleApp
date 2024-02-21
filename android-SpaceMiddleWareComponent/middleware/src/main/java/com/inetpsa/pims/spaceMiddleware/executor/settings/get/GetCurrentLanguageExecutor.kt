package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor

internal class GetCurrentLanguageExecutor(command: BaseCommand) :
    BaseLocalExecutor<Unit, Map<String, String>?>(command) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse.Success<Map<String, String>> {
        val currentCountryInfo = mapOf(Constants.Output.LANGUAGE to configurationManager.locale.toLanguageTag())
        return NetworkResponse.Success(currentCountryInfo)
    }
}
