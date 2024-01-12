package com.mobiledrivetech.external.middleware.executor.configuration

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.Constants.CONTEXT_KEY_BRAND
import com.mobiledrivetech.external.middleware.Constants.CONTEXT_KEY_ENVIRONMENT
import com.mobiledrivetech.external.middleware.command.BaseCommand
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import com.mobiledrivetech.external.middleware.model.Response
import com.mobiledrivetech.external.middleware.util.hasEnumNullable
import com.mobiledrivetech.external.middleware.util.hasEnvironmentOrNull
import com.mobiledrivetech.external.middleware.util.hasOrNull
import java.util.Locale

internal class SetConfigurationExecutor(command: BaseCommand) :
    BaseLocalExecutor<ConfigurationInput, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): ConfigurationInput {
        val credentialMap: Map<String, Any>? = parameters hasOrNull Constants.PARAMS_KEY_CREDENTIAL
        val profileMap: Map<String, Any>? = parameters hasOrNull Constants.PARAMS_KEY_PROFILE

        // fetch credential section
        val googleApiKey: String? = credentialMap hasOrNull Constants.PARAMS_KEY_GOOGLE_API_KEY

        // fetch profile section
        val brand: Brand? = profileMap.hasEnumNullable(CONTEXT_KEY_BRAND)

        val languagePath: String? = profileMap hasOrNull Constants.Input.Configuration.LANGUAGE_PATH

        var locale: Locale? = null

        profileMap.hasOrNull<String>(Constants.Input.Configuration.LOCALE)
            ?.takeIf { it.isNotBlank() }
            ?.let { locale = Locale.forLanguageTag(it) }

        val environment = parameters hasEnvironmentOrNull CONTEXT_KEY_ENVIRONMENT

        return ConfigurationInput(
            environment = environment,
            brand = brand,
            googleApiKey = googleApiKey,
            locale = locale,
            languagePath = languagePath
        )
    }

    override suspend fun execute(input: ConfigurationInput): Response<Unit> {
        // Todo: For now we don't use it but we will use it in the future to manage the configuration
        return Response.Success(Unit)
    }
}
