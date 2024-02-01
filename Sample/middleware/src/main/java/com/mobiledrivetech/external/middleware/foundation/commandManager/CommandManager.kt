package com.mobiledrivetech.external.middleware.foundation.commandManager

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.Constants.KEY_ERROR
import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericComponentInterface
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandStatus
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.util.ErrorCode
import com.mobiledrivetech.external.middleware.util.ErrorMessage
import com.mobiledrivetech.external.middleware.util.MiddleWareErrorFactory

/**
 * Manage the execution of commands
 */
class CommandManager(
    private var commandMapper: CommandMapper = CommandMapper(),
) : ICommandManager {

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        const val DEFAULT_MAX_PARALLEL_COMMANDS = 1

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        const val MAX_PARALLEL_COMMANDS_VALUE = 3
    }

    override var maxParallelCommands: Int = DEFAULT_MAX_PARALLEL_COMMANDS
        set(value) {
            field = when {
                value < DEFAULT_MAX_PARALLEL_COMMANDS -> {
                    MDLog.warning("supported Range [$DEFAULT_MAX_PARALLEL_COMMANDS, $MAX_PARALLEL_COMMANDS_VALUE], passed value: $DEFAULT_MAX_PARALLEL_COMMANDS")
                    DEFAULT_MAX_PARALLEL_COMMANDS
                }

                value > MAX_PARALLEL_COMMANDS_VALUE -> {
                    MDLog.warning("supported Range [$DEFAULT_MAX_PARALLEL_COMMANDS, $MAX_PARALLEL_COMMANDS_VALUE], passed value: $DEFAULT_MAX_PARALLEL_COMMANDS")
                    MAX_PARALLEL_COMMANDS_VALUE
                }

                else -> value
            }

            MDLog.debug("maxParallelCommands value: $value")
        }

    /**
     * Configure the number of commands that can execute in parallel
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var commandExecutor: CommandExecutor = CommandExecutor(maxParallelCommands)

    /**
     * Initialize the commandManager
     *
     * @param configuration contains the configuration's params
     * @param componentReference component that implement [GenericComponentInterface]
     */
    override fun initialize(
        configuration: Map<String, Any>,
        componentReference: GenericComponentInterface?
    ) {
        MDLog.debug("configuration: $configuration")
        commandMapper.initialize(configuration, componentReference)
    }

    /**
     * Add commandSubClass in the commandMapper
     *
     * @param commandSubClass commandSubClass
     * @param name command name [CommandName]
     * @param type command type [CommandType]
     */
    override fun fillCommandMapper(commandSubClass: Command, name: CommandName, type: CommandType) {
        MDLog.debug("name: $name, type: $type")
        commandMapper.fill(
            command = commandSubClass,
            name = name,
            type = type
        )
    }

    /**
     * Executes the command
     * @param name command name [CommandName]
     * @param type command type [CommandType]
     * @param parameters parameters to use to execute the command
     * @param callback callback to be invoked on response
     *
     */
    override fun executeCommand(
        name: CommandName,
        type: CommandType,
        parameters: Map<String, Any>?,
        callback: ((Map<String, Any?>) -> Unit)?
    ): String {
        MDLog.debug("name: $name, type: $type, parameters: $parameters")

        var command = commandMapper.command(name = name, type = type, parameters = parameters)

        command?.run {

            register(callback)
            commandExecutor.send(this)
            return transactionId
        }

        command = Command(name = name, type = type, parameters = parameters)
        command.error = mapOf(
            Pair(
                KEY_ERROR,
                MiddleWareErrorFactory.create(
                    ErrorCode.invalidParams,
                    ErrorMessage.invalidParams("API")
                )
            )
        )
        command.status = CommandStatus.FAILED

        val result = command.toMap()

        MDLog.warning("<-- result: $result")
        callback?.invoke(result)
        return command.transactionId
    }

    override fun supportedApis(): List<String> {
        val result = commandMapper.supportedApis()
        MDLog.debug("supportedApis: $result")
        return result
    }
}
