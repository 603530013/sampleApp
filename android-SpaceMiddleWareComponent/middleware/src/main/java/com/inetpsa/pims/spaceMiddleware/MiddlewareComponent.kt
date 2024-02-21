package com.inetpsa.pims.spaceMiddleware

import android.content.Context
import com.inetpsa.mmx.foundation.BuildConfig
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.commandManager.ICommandManager
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.extensions.asMap
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.genericComponent.GenericCoreComponent
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.CommandName
import com.inetpsa.mmx.foundation.tools.CommandStatus
import com.inetpsa.mmx.foundation.tools.CommandType
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.pims.spaceMiddleware.command.account.AccountCommandGet
import com.inetpsa.pims.spaceMiddleware.command.account.AccountCommandSet
import com.inetpsa.pims.spaceMiddleware.command.assistance.AssistanceCommandGet
import com.inetpsa.pims.spaceMiddleware.command.assistance.AssistanceCommandSet
import com.inetpsa.pims.spaceMiddleware.command.assistance.GetAssistanceCommand
import com.inetpsa.pims.spaceMiddleware.command.assistance.SetAssistanceCommand
import com.inetpsa.pims.spaceMiddleware.command.configuration.SetConfigurationCommand
import com.inetpsa.pims.spaceMiddleware.command.contracts.GetContractsCommand
import com.inetpsa.pims.spaceMiddleware.command.dealer.DealerCommandGet
import com.inetpsa.pims.spaceMiddleware.command.dealer.DealerCommandSet
import com.inetpsa.pims.spaceMiddleware.command.dealer.GetDealerCommand
import com.inetpsa.pims.spaceMiddleware.command.dealer.SetDealerCommand
import com.inetpsa.pims.spaceMiddleware.command.destination.SetDestinationCommand
import com.inetpsa.pims.spaceMiddleware.command.eServices.EServicesCommandGet
import com.inetpsa.pims.spaceMiddleware.command.features.FeaturesCommandGet
import com.inetpsa.pims.spaceMiddleware.command.locations.GetLocationsCommand
import com.inetpsa.pims.spaceMiddleware.command.logIncident.SetLogIncidentCommand
import com.inetpsa.pims.spaceMiddleware.command.radioplayer.GetRadioPlayerCommand
import com.inetpsa.pims.spaceMiddleware.command.radioplayer.RadioPlayerCommandGet
import com.inetpsa.pims.spaceMiddleware.command.settings.GetSettingsCommand
import com.inetpsa.pims.spaceMiddleware.command.settings.SettingsCommandGet
import com.inetpsa.pims.spaceMiddleware.command.settings.SettingsCommandSet
import com.inetpsa.pims.spaceMiddleware.command.user.UserCommandGet
import com.inetpsa.pims.spaceMiddleware.command.user.UserCommandSet
import com.inetpsa.pims.spaceMiddleware.command.vehicle.GetVehicleCommand
import com.inetpsa.pims.spaceMiddleware.command.vehicle.SetVehicleCommand
import com.inetpsa.pims.spaceMiddleware.command.vehicle.VehicleCommandGet
import com.inetpsa.pims.spaceMiddleware.command.vehicle.VehicleCommandSet
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManagerImp
import com.inetpsa.pims.spaceMiddleware.manager.LoggerManager
import com.inetpsa.pims.spaceMiddleware.manager.LoggerManagerImp
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup
import com.inetpsa.pims.spaceMiddleware.model.manager.Config
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.applicationName
import com.inetpsa.pims.spaceMiddleware.util.applicationVersion
import com.inetpsa.pims.spaceMiddleware.util.asGroup
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasEnvironment
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import java.util.Locale

@Suppress("TooManyFunctions")
internal class MiddlewareComponent(val context: Context) : GenericCoreComponent(context) {

    override var serviceName: String? = Constants.SERVICE_NAME
    override var name: String? = Constants.COMPONENT_NAME
    override var version: String? = BuildConfig.VERSION_NAME

    internal val applicationName: String by lazy { context.applicationName() }
    internal val applicationVersion: String by lazy { context.applicationVersion() }

    internal val configurationManager: ConfigurationManager = ConfigurationManagerImp()
    internal lateinit var middlewareCommunicationManager: ICommunicationManager

    internal val loggerManager: LoggerManager = LoggerManagerImp()

    init {
        commandManager.apply {
            initDeprecatedApi(this)

            initUserApi(this)
            initVehicleApi(this)
            initAssistanceApi(this)
            initDealerApi(this)
            initSettingsApi(this)
            initRadioApi(this)
            initLocationApi(this)
            initLogIncidentApi(this)
            initDestinationApi(this)
            initConfigureApi(this)
            initEServicesApi(this)
            initFeaturesApi(this)
        }
    }

    @Deprecated("this should be removed when the migration is done on App")
    private fun initDeprecatedApi(commandManager: ICommandManager) {
        val vehicleCommand = CommandName(Constants.SERVICE_NAME, Constants.VEHICLE)
        commandManager.fillCommandMapper(SetVehicleCommand(), vehicleCommand, CommandType.Set)
        commandManager.fillCommandMapper(GetVehicleCommand(), vehicleCommand, CommandType.Get)

        // will be removed in next releases
        val contractsCommand = CommandName(Constants.SERVICE_NAME, Constants.CONTRACTS)
        commandManager.fillCommandMapper(GetContractsCommand(), contractsCommand, CommandType.Get)

        val assistanceCommand = CommandName(Constants.SERVICE_NAME, Constants.ASSISTANCE)
        commandManager.fillCommandMapper(SetAssistanceCommand(), assistanceCommand, CommandType.Set)
        commandManager.fillCommandMapper(GetAssistanceCommand(), assistanceCommand, CommandType.Get)

        val settingsCommand = CommandName(Constants.SERVICE_NAME, Constants.SETTINGS)
        commandManager.fillCommandMapper(GetSettingsCommand(), settingsCommand, CommandType.Get)

        val accountCommand = CommandName(Constants.SERVICE_NAME, Constants.ACCOUNT)
        commandManager.fillCommandMapper(AccountCommandGet(), accountCommand, CommandType.Get)
        commandManager.fillCommandMapper(AccountCommandSet(), accountCommand, CommandType.Set)

        val dealerCommand = CommandName(Constants.SERVICE_NAME, Constants.DEALER)
        commandManager.fillCommandMapper(GetDealerCommand(), dealerCommand, CommandType.Get)
        commandManager.fillCommandMapper(SetDealerCommand(), dealerCommand, CommandType.Set)

        val radioPlayerCommand = CommandName(Constants.SERVICE_NAME, Constants.RADIO_PLAYER)
        commandManager.fillCommandMapper(GetRadioPlayerCommand(), radioPlayerCommand, CommandType.Get)

        val locationsCommand = CommandName(Constants.SERVICE_NAME, Constants.LOCATIONS)
        commandManager.fillCommandMapper(GetLocationsCommand(), locationsCommand, CommandType.Get)

        val logIncidentCommand = CommandName(Constants.SERVICE_NAME, Constants.LOG_INCIDENT)
        commandManager.fillCommandMapper(SetLogIncidentCommand(), logIncidentCommand, CommandType.Set)

        val destinationCommand = CommandName(Constants.SERVICE_NAME, Constants.DESTINATION)
        commandManager.fillCommandMapper(SetDestinationCommand(), destinationCommand, CommandType.Set)

        val configureCommand = CommandName(Constants.SERVICE_NAME, Constants.CONFIGURATION)
        commandManager.fillCommandMapper(SetConfigurationCommand(), configureCommand, CommandType.Set)
    }

    private fun initDestinationApi(commandManager: ICommandManager) {
        val destinationCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.DESTINATION}")
        commandManager.fillCommandMapper(SetDestinationCommand(), destinationCommand, CommandType.Set)
    }

    private fun initConfigureApi(commandManager: ICommandManager) {
        val configureCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.CONFIGURATION}")
        commandManager.fillCommandMapper(SetConfigurationCommand(), configureCommand, CommandType.Set)
    }

    private fun initLocationApi(commandManager: ICommandManager) {
        val locationsCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.LOCATIONS}")
        commandManager.fillCommandMapper(GetLocationsCommand(), locationsCommand, CommandType.Get)
    }

    private fun initLogIncidentApi(commandManager: ICommandManager) {
        val logIncidentCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.LOG_INCIDENT}")
        commandManager.fillCommandMapper(SetLogIncidentCommand(), logIncidentCommand, CommandType.Set)
    }

    private fun initAssistanceApi(commandManager: ICommandManager) {
        val assistanceCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.ASSISTANCE}")
        commandManager.fillCommandMapper(AssistanceCommandGet(), assistanceCommand, CommandType.Get)
        commandManager.fillCommandMapper(AssistanceCommandSet(), assistanceCommand, CommandType.Set)
    }

    private fun initUserApi(commandManager: ICommandManager) {
        val userCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.USER}")
        commandManager.fillCommandMapper(UserCommandGet(), userCommand, CommandType.Get)
        commandManager.fillCommandMapper(UserCommandSet(), userCommand, CommandType.Set)
    }

    private fun initVehicleApi(commandManager: ICommandManager) {
        val vehicleCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.VEHICLE}")
        commandManager.fillCommandMapper(VehicleCommandGet(), vehicleCommand, CommandType.Get)
        commandManager.fillCommandMapper(VehicleCommandSet(), vehicleCommand, CommandType.Set)
    }

    private fun initEServicesApi(commandManager: ICommandManager) {
        val eServicesCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.ESERVICES}")
        commandManager.fillCommandMapper(EServicesCommandGet(), eServicesCommand, CommandType.Get)
    }

    private fun initFeaturesApi(commandManager: ICommandManager) {
        val featuresCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.FEATURES}")
        commandManager.fillCommandMapper(FeaturesCommandGet(), featuresCommand, CommandType.Get)
    }

    private fun initDealerApi(commandManager: ICommandManager) {
        val dealerCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.DEALER}")
        commandManager.fillCommandMapper(DealerCommandGet(), dealerCommand, CommandType.Get)
        commandManager.fillCommandMapper(DealerCommandSet(), dealerCommand, CommandType.Set)
    }

    private fun initRadioApi(commandManager: ICommandManager) {
        val radioCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.RADIO_PLAYER}")
        commandManager.fillCommandMapper(RadioPlayerCommandGet(), radioCommand, CommandType.Get)
    }

    private fun initSettingsApi(commandManager: ICommandManager) {
        val settingsCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.SETTINGS}")
        commandManager.fillCommandMapper(SettingsCommandGet(), settingsCommand, CommandType.Get)
        commandManager.fillCommandMapper(SettingsCommandSet(), settingsCommand, CommandType.Set)
    }

    override fun initialize(context: Context, parameters: Map<String, Any>, callback: (Map<String, Any>) -> Unit) {
        try {
            val credentialMap: Map<String, Any>? = parameters hasOrNull Constants.PARAMS_KEY_CREDENTIAL
            val profileMap: Map<String, Any> = parameters has Constants.PARAMS_KEY_PROFILE

            // fetch credential section
            val googleApiKey: String = credentialMap has Constants.PARAMS_KEY_GOOGLE_API_KEY

            // fetch profile section
            val brand: Brand = profileMap hasEnum CONTEXT_KEY_BRAND

            val locale = profileMap
                .hasOrNull<String>(Constants.Input.Configuration.LOCALE)
                ?.let { Locale.forLanguageTag(it) }

            val environment = parameters hasEnvironment CONTEXT_KEY_ENVIRONMENT

            val languagePath: String? =
                profileMap hasOrNull Constants.Input.Configuration.LANGUAGE_PATH

            var market: Market = Market.NONE
            var siteCode: String? = null

            when (brand.asGroup()) {
                BrandGroup.FCA -> {
                    market = profileMap hasEnum Constants.CONTEXT_KEY_MARKET
                }

                BrandGroup.PSA -> {
                    siteCode = profileMap has Constants.Input.Configuration.SITE_CODE
                }

                else -> {
                    // nothing to do
                }
            }

            clearCaches()

            val config = Config(
                environment = environment,
                brand = brand,
                googleApiKey = googleApiKey,
                locale = locale,
                market = market,
                siteCode = siteCode,
                languagePath = languagePath
            )
            configurationManager.initialize(component = this, config = config)

            loggerManager.configure(monitoringManager, parameters)

            if (configurationManager.brandGroup == BrandGroup.PSA) {
                middlewareCommunicationManager = MiddlewareCommunicationManager(this, environment)
            }
        } catch (ex: PIMSError) {
            PIMSLogger.w(ex)
            callback(failure(ex))
            return // return to prevent calling callback twice time
        }
        super.initialize(context, parameters, callback)
    }

    private fun failure(ex: PIMSError): Map<String, Any> =
        mapOf("status" to CommandStatus.FAILED, "error" to ex.asMap())

    override fun release() {
        clearCaches()
        super.release()
    }

    private fun clearCaches() {
        BookingOnlineCache.clear()
        com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache.clear()
    }
}
