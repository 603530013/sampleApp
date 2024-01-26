package com.mobiledrivetech.external.middleware

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
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.manager.ConfigurationManager
import com.mobiledrivetech.external.middleware.manager.ConfigurationManagerImp
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
            //set configuration if needed
            val config = ConfigurationInput()
            configurationManager.initialize(component = this, config = config)

        } catch (ex: MiddleWareError) {
            MDLog.warning(ex.toString())
            callback(failure(ex))
            return
        }
        super.initialize(parameters, callback)
    }

    private fun failure(ex: MiddleWareError): Map<String, Any> =
        mapOf(KEY_STATUS to CommandStatus.FAILED, KEY_ERROR to ex.asMap())
}
