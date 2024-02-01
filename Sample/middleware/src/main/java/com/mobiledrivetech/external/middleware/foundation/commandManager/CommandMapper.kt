package com.mobiledrivetech.external.middleware.foundation.commandManager

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericComponentInterface
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import java.lang.ref.WeakReference


/**
 * Manage the mapping between commandSubClass, commandName and commandType
 */
class CommandMapper {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var map = mutableMapOf<String, Command>()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var configuration: Map<String, Any>? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var componentReference: WeakReference<GenericComponentInterface?>? = null

    /**
     * Returns [Command] if key {name:type} exist
     * or returns null if this key does not exist
     */
    fun command(name: CommandName, type: CommandType, parameters: Map<String, Any>?): Command? {
        MDLog.debug("name: $name, type: $type, parameters: $parameters")

        val key = "${type.type}:${name.name}"

        return map[key]?.init(
            name = name,
            type = type,
            parameters = parameters,
            configuration = configuration
        )?.apply {
            componentReference = this@CommandMapper.componentReference
            MDLog.debug("<-- result: $this")
        }
    }

    /**
     * Initialize
     */
    fun initialize(
        configuration: Map<String, Any>,
        componentReference: GenericComponentInterface?
    ) {
        MDLog.debug("configuration: $configuration")

        this.configuration = configuration
        this.componentReference = WeakReference(componentReference)
    }

    /**
     * Add commandSubClass item
     *
     * @param command commandSubClass
     * @param name command name [CommandName]
     * @param type command type [CommandType]
     */
    fun fill(command: Command, name: CommandName, type: CommandType) {
        MDLog.debug("name: $name, type: ${type.type}")

        val key = "${type.type}:${name.name}"
        map[key] = command
    }

    fun supportedApis(): List<String> {
        MDLog.debug("-->")
        return map.map { it.key }
    }
}