package com.mobiledrivetech.external.middleware.foundation.commandManager

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.foundation.commandManager.CommandManager.Companion.DEFAULT_MAX_PARALLEL_COMMANDS
import com.mobiledrivetech.external.middleware.foundation.commandManager.CommandManager.Companion.MAX_PARALLEL_COMMANDS_VALUE
import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericComponentInterface
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import io.mockk.clearAllMocks
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class CommandManagerTest {

    private lateinit var commandManager: CommandManager
    private val commandExecutor: CommandExecutor = mockk(relaxed = true)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        commandManager = spyk(CommandManager())
        commandManager.commandExecutor = commandExecutor
        mockkObject(MDLog)
        justRun { MDLog.debug(any()) }
        justRun { MDLog.warning(any()) }
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when initialize then initialize the command manager`() {
        // Arrange
        val configuration = mapOf(Pair("config1", "config1Value"))
        val componentReference = mockk<GenericComponentInterface>(relaxed = true)

        // Act
        commandManager.initialize(
            configuration = configuration,
            componentReference = componentReference
        )

        // Assert
        verify {
            commandManager.initialize(
                configuration = configuration,
                componentReference = componentReference
            )
        }
    }

    @Test
    fun `when fillCommandMapper then call fill`() {
        // Arrange
        val name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        val type = CommandType.Get
        val command = TestCommand()

        // Act
        commandManager.fillCommandMapper(commandSubClass = command, name = name, type = type)

        // Assert
        verify {
            commandManager.fillCommandMapper(
                commandSubClass = command,
                name = name,
                type = type
            )
        }
    }

    @Test
    fun `when executeCommand with a command in mapper then execute the command`() {
        // Arrange
        val name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        val type = CommandType.Get
        val parameters = mapOf(Pair("param1", "paramValue"))
        val callback: ((Map<String, Any?>) -> Unit) = mockk(relaxed = true)

        commandManager.fillCommandMapper(commandSubClass = TestCommand(), name = name, type = type)

        // Act
        commandManager.executeCommand(
            name = name,
            type = type,
            parameters = parameters,
            callback = callback
        )

        // Assert
        verify { commandExecutor.send(any()) }
    }

    @Test
    fun `when executeCommand with a command not in mapper then return error`() {
        // Arrange
        val name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        val type = CommandType.Get
        val parameters = mapOf(Pair("param1", "paramValue"))
        val callback: ((Map<String, Any?>) -> Unit) = mockk(relaxed = true)
        commandManager.fillCommandMapper(
            commandSubClass = TestCommand(),
            name = CommandName("test"),
            type = type
        )

        justRun { callback.invoke(any()) }

        // Act
        commandManager.executeCommand(
            name = name,
            type = type,
            parameters = parameters,
            callback = callback
        )

        // Assert
        verify { MDLog.debug("name: $name, type: $type, parameters: $parameters") }
        verify { callback.invoke(any()) }
        verify(exactly = 0) { commandExecutor.send(any()) }
    }

    @Test
    fun `when supportedApis then return supported apis`() {
        // Arrange
        val name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        val type = CommandType.Get
        val command = TestCommand()
        commandManager.fillCommandMapper(commandSubClass = command, name = name, type = type)

        // Act
        val apis = commandManager.supportedApis()

        // Assert
        Assert.assertEquals(1, apis.size)
        Assert.assertEquals("${type.type}:${name.name}", apis[0])
        verify { MDLog.debug(any()) }
    }

    @Test
    fun `when maxParallelCommands then get correct value`() {
        // Assert
        Assert.assertEquals(DEFAULT_MAX_PARALLEL_COMMANDS, commandManager.maxParallelCommands)

        // Act
        commandManager.maxParallelCommands = 10
        // Assert
        Assert.assertEquals(MAX_PARALLEL_COMMANDS_VALUE, commandManager.maxParallelCommands)

        // Act
        commandManager.maxParallelCommands = 0
        // Assert
        Assert.assertEquals(DEFAULT_MAX_PARALLEL_COMMANDS, commandManager.maxParallelCommands)

        // Act
        commandManager.maxParallelCommands = 2
        // Assert
        Assert.assertEquals(2, commandManager.maxParallelCommands)

        // Act
        commandManager.maxParallelCommands = 3
        // Assert
        Assert.assertEquals(3, commandManager.maxParallelCommands)
    }

    private class TestCommand : Command() {
        override suspend fun execute(callback: () -> Unit) {
            success(mapOf(Pair("succeeded", true)))
            callback.invoke()
        }
    }
}