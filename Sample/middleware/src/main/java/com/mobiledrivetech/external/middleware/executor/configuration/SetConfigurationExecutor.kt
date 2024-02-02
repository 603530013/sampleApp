package com.mobiledrivetech.external.middleware.executor.configuration

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.Constants.CONTEXT_KEY_BRAND
import com.mobiledrivetech.external.middleware.Constants.CONTEXT_KEY_ENVIRONMENT
import com.mobiledrivetech.external.middleware.Constants.CONTEXT_KEY_LOCALE
import com.mobiledrivetech.external.middleware.command.BaseCommand
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.extensions.hasEnumNullable
import com.mobiledrivetech.external.middleware.extensions.hasEnvironmentOrNull
import com.mobiledrivetech.external.middleware.extensions.hasOrNull
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.model.Response
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import java.util.Locale

internal class SetConfigurationExecutor(command: BaseCommand) :
    BaseLocalExecutor<ConfigurationInput, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): ConfigurationInput {
        val profileMap: Map<String, Any>? = parameters hasOrNull Constants.PARAMS_KEY_PROFILE

        // fetch profile section
        val brand: Brand? = profileMap.hasEnumNullable(CONTEXT_KEY_BRAND)
        var locale: Locale? = null

        profileMap.hasOrNull<String>(CONTEXT_KEY_LOCALE)
            ?.takeIf { it.isNotBlank() }
            ?.let { locale = Locale.forLanguageTag(it) }

        val environment = parameters hasEnvironmentOrNull CONTEXT_KEY_ENVIRONMENT
        val market: Market = Market.NONE

        return ConfigurationInput(
            environment = environment,
            brand = brand,
            locale = locale,
            market = market
        )
    }

    override suspend fun execute(input: ConfigurationInput): Response<Unit> {
        // Todo: For now we don't use it but we will use it in the future to manage the configuration
        return Response.Success(Unit)
    }
}
