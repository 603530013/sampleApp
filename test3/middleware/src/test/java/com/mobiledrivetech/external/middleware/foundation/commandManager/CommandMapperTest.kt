package com.mobiledrivetech.external.middleware.foundation.commandManager

import com.mobiledrivetech.external.middleware.Constants
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

class CommandMapperTest {
    private lateinit var commandMapper: CommandMapper

    @Before
    fun setup() {
        commandMapper = spyk()
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
    fun `when command which is in the map then return a command`() {
        // Arrange
        val name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        val type = CommandType.Get
        val parameters = mapOf(Pair("param1", "paramValue"))
        commandMapper.fill(command = FakeCommand(), name = name, type = type)

        // Act
        val command = commandMapper.command(name = name, type = type, parameters = parameters)

        // Assert
        Assert.assertEquals(name, command?.name)
        Assert.assertEquals(type, command?.type)
        verify { MDLog.debug(any()) }
    }

    @Test
    fun `when command which is not in the map then return a command`() {
        // Arrange
        val name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        val type = CommandType.Get
        val parameters = mapOf(Pair("param1", "paramValue"))

        // Act
        val command = commandMapper.command(name = name, type = type, parameters = parameters)

        // Assert
        Assert.assertEquals(null, command)
        verify { MDLog.debug(any()) }
    }

    @Test
    fun `when initialize then initialize`() {
        // Arrange
        val configuration = mapOf(Pair("config1", "config1Value"))
        val componentReference = mockk<GenericComponentInterface>(relaxed = true)

        // Act
        commandMapper.initialize(
            configuration = configuration,
            componentReference = componentReference
        )

        // Assert
        Assert.assertEquals(configuration, commandMapper.configuration)
        Assert.assertEquals(componentReference, commandMapper.componentReference?.get())
        verify { MDLog.debug(any()) }
    }

    @Test
    fun `when fill then fill the map`() {
        // Arrange
        val name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        val type = CommandType.Get
        val command = FakeCommand()

        // Act
        commandMapper.fill(command = command, name = name, type = type)

        // Assert
        Assert.assertEquals(command, commandMapper.map["${type.type}:${name.name}"])
        verify { MDLog.debug(any()) }
    }

    @Test
    fun `when supportedApis then return a list of apis`() {
        // Arrange
        val name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        val type = CommandType.Get
        val command = FakeCommand()
        commandMapper.fill(command = command, name = name, type = type)

        // Act
        val apis = commandMapper.supportedApis()

        // Assert
        Assert.assertEquals(1, apis.size)
        Assert.assertEquals("${type.type}:${name.name}", apis[0])
        verify { MDLog.debug(any()) }
    }

    private class FakeCommand : Command() {
        override suspend fun execute(callback: () -> Unit) {
            success(mapOf(Pair("succeeded", true)))
            callback.invoke()
        }
    }
}