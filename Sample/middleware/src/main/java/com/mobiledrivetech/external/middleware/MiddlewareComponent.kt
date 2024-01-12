package com.mobiledrivetech.external.middleware

import android.util.Log
import com.mobiledrivetech.external.middleware.Constants.CONTEXT_KEY_BRAND
import com.mobiledrivetech.external.middleware.command.configuration.SetConfigurationCommand
import com.mobiledrivetech.external.middleware.command.test.TestCommandGet
import com.mobiledrivetech.external.middleware.extensions.asMap
import com.mobiledrivetech.external.middleware.extensions.has
import com.mobiledrivetech.external.middleware.foundation.commandManager.ICommandManager
import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericCoreComponent
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandStatus
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.manager.ConfigurationManager
import com.mobiledrivetech.external.middleware.manager.ConfigurationManagerImp
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import com.mobiledrivetech.external.middleware.util.hasEnum
import com.mobiledrivetech.external.middleware.util.hasEnvironment
import com.mobiledrivetech.external.middleware.util.hasOrNull
import java.util.Locale

internal class MiddlewareComponent : GenericCoreComponent() {

    override var serviceName: String? = Constants.SERVICE_NAME
    override var name: String? = Constants.COMPONENT_NAME

    val configurationManager: ConfigurationManager = ConfigurationManagerImp()

    init {
        commandManager.apply {
            initConfigureApi(this)
            initTestApi(this)
        }
    }

    private fun initConfigureApi(commandManager: ICommandManager) {
        val configureCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.CONFIGURATION}")
        commandManager.fillCommandMapper(
            SetConfigurationCommand(),
            configureCommand,
            CommandType.Set
        )
    }

    private fun initTestApi(commandManager: ICommandManager) {
        val configureCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        commandManager.fillCommandMapper(
            TestCommandGet(),
            configureCommand,
            CommandType.Get
        )
    }

    override fun initialize(
        parameters: Map<String, Any>,
        callback: (Map<String, Any>) -> Unit
    ) {
        try {
            val credentialMap: Map<String, Any>? =
                parameters hasOrNull Constants.PARAMS_KEY_CREDENTIAL
            val profileMap: Map<String, Any> = parameters has Constants.PARAMS_KEY_PROFILE

            // fetch credential section
            val googleApiKey: String = credentialMap has Constants.PARAMS_KEY_GOOGLE_API_KEY

            // fetch profile section
            val brand: Brand = profileMap hasEnum CONTEXT_KEY_BRAND

            val locale = profileMap
                .hasOrNull<String>(Constants.Input.Configuration.LOCALE)
                ?.let { Locale.forLanguageTag(it) }

            val environment = parameters hasEnvironment Constants.CONTEXT_KEY_ENVIRONMENT

            val languagePath: String? =
                profileMap hasOrNull Constants.Input.Configuration.LANGUAGE_PATH

            val market: Market = Market.NONE
            val siteCode: String? = null

            val config = ConfigurationInput(
                environment = environment,
                brand = brand,
                googleApiKey = googleApiKey,
                locale = locale,
                market = market,
                siteCode = siteCode,
                languagePath = languagePath
            )
            configurationManager.initialize(component = this, config = config)

        } catch (ex: MiddleWareError) {
            Log.w("", ex)
            callback(failure(ex))
            return // return to prevent calling callback twice time
        }
        super.initialize(parameters, callback)
    }

    private fun failure(ex: MiddleWareError): Map<String, Any> =
        mapOf("status" to CommandStatus.FAILED, "error" to ex.asMap())
}
