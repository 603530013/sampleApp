package com.inetpsa.pims.spaceMiddleware.executor.settings.set

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.model.manager.Config
import java.util.Locale

internal class SetLanguageExecutor(command: BaseCommand) : BaseLocalExecutor<Locale, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): Locale =
        parameters.has<String>(Constants.Input.LANGUAGE)
            .let { Locale.forLanguageTag(it) }
            .takeIf { !it.language.isNullOrBlank() && !it.country.isNullOrBlank() }
            ?: throw PIMSFoundationError.invalidParameter(Constants.Input.LANGUAGE)

    override suspend fun execute(input: Locale): NetworkResponse<Unit> {
        configurationManager.update(middlewareComponent, config = Config(locale = input))
        return NetworkResponse.Success(Unit)
    }
}
