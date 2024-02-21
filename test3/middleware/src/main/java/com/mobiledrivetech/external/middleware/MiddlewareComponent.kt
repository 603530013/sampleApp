package com.mobiledrivetech.external.middleware

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.Constants.KEY_ERROR
import com.mobiledrivetech.external.middleware.Constants.KEY_STATUS
import com.mobiledrivetech.external.middleware.command.configuration.SetConfigurationCommand
import com.mobiledrivetech.external.middleware.command.test.TestCommandGet
import com.mobiledrivetech.external.middleware.extensions.asMap
import com.mobiledrivetech.external.middleware.foundation.commandManager.ICommandManager
import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericCoreComponent
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandStatus
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.manager.ConfigurationManager
import com.mobiledrivetech.external.middleware.manager.ConfigurationManagerImp
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput

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

    /**
     * Init configure api
     *
     * @param commandManager with command manager. We will use it to fill command mapper in [initConfigureApi]
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun initConfigureApi(commandManager: ICommandManager) {
        val configureCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.CONFIGURATION}")
        commandManager.fillCommandMapper(
            SetConfigurationCommand(),
            configureCommand,
            CommandType.Set
        )
    }

    /**
     * Init test api
     *
     * @param commandManager with command manager. We will use it to fill command mapper in [initTestApi]
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun initTestApi(commandManager: ICommandManager) {
        val configureCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        commandManager.fillCommandMapper(
            TestCommandGet(),
            configureCommand,
            CommandType.Get
        )
    }

    /**
     * Initialize
     *
     * @param parameters with parameters for initialization
     * @param callback with callback for initialization
     */
    override fun initialize(
        parameters: Map<String, Any>,
        callback: (Map<String, Any>) -> Unit
    ) {
        //set configuration if needed
        val config = ConfigurationInput()
        configurationManager.initialize(component = this, config = config)
        super.initialize(parameters, callback)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun failure(ex: MiddleWareError): Map<String, Any> =
        mapOf(KEY_STATUS to CommandStatus.FAILED, KEY_ERROR to ex.asMap())
}
