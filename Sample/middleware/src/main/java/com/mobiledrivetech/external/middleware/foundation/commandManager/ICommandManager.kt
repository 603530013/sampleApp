package com.mobiledrivetech.external.middleware.foundation.commandManager

import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericComponentInterface
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandType

interface ICommandManager {

    /**
     * Configure the number of commands that can execute in parallel
     */
    var maxParallelCommands: Int

    /**
     * Initialize the commandManager
     *
     * @param configuration contains the configuration's params
     * @param componentReference component that implement [GenericComponentInterface]
     */
    fun initialize(
        configuration: Map<String, Any>,
        componentReference: GenericComponentInterface?
    )

    /**
     * Add commandSubClass in the commandMapper
     *
     * @param commandSubClass commandSubClass
     * @param name command name [CommandName]
     * @param type command type [CommandType]
     */
    fun fillCommandMapper(commandSubClass: Command, name: CommandName, type: CommandType)

    /**
     * Executes the command
     * @param name command name [CommandName]
     * @param type command type [CommandType]
     * @param parameters parameters to use to execute the command
     * @param callback callback to be invoked on response
     *
     */
    fun executeCommand(
        name: CommandName,
        type: CommandType,
        parameters: Map<String, Any>?,
        callback: ((Map<String, Any?>) -> Unit)?
    ): String

    /**
     * return a list of supported apis
     */
    fun supportedApis(): List<String>
}
