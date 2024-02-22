package com.mobiledrivetech.external.middleware.foundation.genericComponent

import com.mobiledrivetech.external.middleware.Constants.APIS
import com.mobiledrivetech.external.middleware.foundation.commandManager.CommandManager
import com.mobiledrivetech.external.middleware.foundation.commandManager.ICommandManager
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog

open class GenericCoreComponent : GenericComponentInterface {

    open var name: String? = "GenericCoreComponent"
    open var version: String? = ""
    open var serviceName: String? = ""

    var commandManager: ICommandManager = CommandManager()

    override fun initialize(
        parameters: Map<String, Any>,
        callback: (Map<String, Any>) -> Unit
    ) {
        MDLog.debug("component: $name $version")
        MDLog.debug("parameters: $parameters")

        commandManager.initialize(configuration = parameters, componentReference = this)
        callback.invoke(mapOf(APIS to commandManager.supportedApis()))
    }

    override fun release() {
        MDLog.debug("component: $name, version: $version")
    }

    override fun get(
        api: String,
        parameters: Map<String, Any>?,
        callback: (Map<String, Any?>) -> Unit
    ): String {
        MDLog.debug("api: $api, parameters: $parameters")

        val name = CommandName(api)
        return commandManager.executeCommand(
            name = name,
            type = CommandType.Get,
            parameters = parameters,
            callback = callback
        )
    }

    override fun set(
        api: String,
        parameters: Map<String, Any>,
        callback: (Map<String, Any?>) -> Unit
    ): String {
        MDLog.debug("api: $api, parameters: $parameters")

        val name = CommandName(api)
        return commandManager.executeCommand(
            name = name,
            type = CommandType.Set,
            parameters = parameters,
            callback = callback
        )
    }

    override fun subscribe(
        api: String,
        parameters: Map<String, Any>?,
        callback: (Map<String, Any?>) -> Unit
    ): String {
        MDLog.debug("api: $api, parameters: $parameters")

        val name = CommandName(api)
        return commandManager.executeCommand(
            name = name,
            type = CommandType.Subscribe,
            parameters = parameters,
            callback = callback
        )
    }

    override fun unsubscribe(api: String, callback: (Map<String, Any?>) -> Unit): String {
        MDLog.debug("api: $api")

        val name = CommandName(api)
        return commandManager.executeCommand(
            name = name,
            type = CommandType.Unsubscribe,
            parameters = null,
            callback = callback
        )
    }
}
