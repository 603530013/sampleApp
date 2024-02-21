package com.inetpsa.pims.spaceMiddleware.executor.configuration

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup
import com.inetpsa.pims.spaceMiddleware.model.configuration.ConfigurationInput
import com.inetpsa.pims.spaceMiddleware.model.manager.Config
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.asGroup
import com.inetpsa.pims.spaceMiddleware.util.hasEnumNullable
import com.inetpsa.pims.spaceMiddleware.util.hasEnvironmentOrNull
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import java.util.Locale

internal class SetConfigurationExecutor(command: BaseCommand) : BaseLocalExecutor<ConfigurationInput, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): ConfigurationInput {
        val credentialMap: Map<String, Any>? = parameters hasOrNull Constants.PARAMS_KEY_CREDENTIAL
        val profileMap: Map<String, Any>? = parameters hasOrNull Constants.PARAMS_KEY_PROFILE

        // fetch credential section
        val googleApiKey: String? = credentialMap hasOrNull Constants.PARAMS_KEY_GOOGLE_API_KEY

        // fetch profile section
        val brand: Brand? = profileMap.hasEnumNullable<Brand>(CONTEXT_KEY_BRAND)

        val languagePath: String? = profileMap hasOrNull Constants.Input.Configuration.LANGUAGE_PATH

        var locale: Locale? = null

        profileMap.hasOrNull<String>(Constants.Input.Configuration.LOCALE)
            ?.takeIf { it.isNotBlank() }
            ?.let { locale = Locale.forLanguageTag(it) }

        val environment = parameters hasEnvironmentOrNull CONTEXT_KEY_ENVIRONMENT

        var market: Market = Market.NONE
        var siteCode: String? = null
        when (brand.asGroup()) {
            BrandGroup.FCA -> {
                market = profileMap.hasEnumNullable<Market>(Constants.CONTEXT_KEY_MARKET) ?: Market.NONE
            }

            BrandGroup.PSA -> {
                siteCode = profileMap hasOrNull Constants.Input.Configuration.SITE_CODE
            }

            else -> {
                // nothing to do
            }
        }

        return ConfigurationInput(
            environment = environment,
            brand = brand,
            googleApiKey = googleApiKey,
            locale = locale,
            market = market,
            siteCode = siteCode,
            languagePath = languagePath
        )
    }

    override suspend fun execute(input: ConfigurationInput): NetworkResponse<Unit> {
        clearCaches()
        with(configurationManager) {
            val config = Config(
                environment = input.environment,
                brand = input.brand,
                googleApiKey = input.googleApiKey,
                locale = input.locale,
                market = input.market,
                siteCode = input.siteCode,
                languagePath = input.languagePath
            )
            update(component = middlewareComponent, config = config)
            if (brandGroup == BrandGroup.PSA) {
                with(middlewareComponent) {
                    middlewareCommunicationManager =
                        MiddlewareCommunicationManager(this, environment)
                }
            }
        }

        middlewareComponent.loggerManager.configure(middlewareComponent.monitoringManager, params)

        return NetworkResponse.Success(Unit)
    }

    private fun clearCaches() {
        BookingOnlineCache.clear()
        com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache.clear()
    }
}
