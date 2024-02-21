package com.mobiledrivetech.external.middleware.foundation.commandManager

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandStatus
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.model.ErrorCode
import com.mobiledrivetech.external.middleware.util.ErrorMessage
import com.mobiledrivetech.external.middleware.util.MiddleWareErrorFactory
import io.mockk.clearAllMocks
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CommandTest {

    @Before
    fun setup() {
        mockkObject(MDLog)
        justRun { MDLog.debug(any()) }
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when CommandName then set the correct name of command `() {
        // Arrange
        val commandName = CommandName("test")
        val commandType = CommandType.Get

        // Act
        val command = Command(name = commandName, type = commandType, parameters = null)

        // Assert
        Assert.assertEquals(commandName, command.name)
        Assert.assertEquals(commandType, command.type)
        Assert.assertEquals(null, command.parameters)
        Assert.assertEquals(null, command.configuration)
        Assert.assertEquals("", command.sdkVersion)
    }

    @Test
    fun `when register with WAITING then don't execute`() {
        // Arrange
        val callback: ((Map<String, Any?>) -> Unit) = mockk(relaxed = true)
        val command = Command("transactionId").apply {
            name = CommandName("test")
            type = CommandType.Get
            parameters = mapOf(Pair("param1", "paramValue"))
            configuration = mapOf(Pair("config1", "config1Value"))
            result = mapOf(Pair("success", "true"))
            error = mapOf(Pair("error", "null"))
            sdkVersion = "12"
            status = CommandStatus.WAITING
        }

        // Act
        command.register(callback)

        // Assert
        Assert.assertEquals(callback, command.callback)
        verify(exactly = 0) { command.execute() }
    }

    @Test
    fun `when register with SUCCEEDED then execute`() {
        // Arrange
        val callback: ((Map<String, Any?>) -> Unit) = mockk(relaxed = true)
        val command = Command("transactionId").apply {
            name = CommandName("test")
            type = CommandType.Get
            parameters = mapOf(Pair("param1", "paramValue"))
            configuration = mapOf(Pair("config1", "config1Value"))
            result = mapOf(Pair("success", "true"))
            error = mapOf(Pair("error", "null"))
            sdkVersion = "12"
            status = CommandStatus.SUCCEEDED
        }

        // Act
        command.register(callback)

        // Assert
        Assert.assertEquals(callback, command.callback)
        verify { command.execute() }
    }

    @Test
    fun `when cancel then callback is null`() {
        // Arrange
        val command = Command("transactionId").apply {
            name = CommandName("test")
            type = CommandType.Get
        }

        // Act
        command.cancel()

        // Assert
        Assert.assertEquals(null, command.callback)
        Assert.assertEquals(CommandStatus.CANCELED, command.status)
    }

    @Test
    fun `when success then get command result`() {
        // Arrange
        val result = mapOf(Pair("response", true))
        val command = Command("test3").apply {
            name = CommandName("test")
            type = CommandType.Get
            parameters = mapOf(Pair("param1", "paramValue"))
            configuration = mapOf(Pair("config1", "config1Value"))
            sdkVersion = "12"
            status = CommandStatus.WAITING
        }

        // Act
        command.success(result)

        // Assert
        Assert.assertEquals(result, command.result)
        Assert.assertEquals(CommandStatus.SUCCEEDED, command.status)
        Assert.assertEquals(null, command.error)
    }

    @Test
    fun `when failure then get failure result`() {
        // Arrange
        val errorResult = mapOf(
            Pair(
                "error",
                MiddleWareErrorFactory.create(
                    ErrorCode.invalidParams,
                    ErrorMessage.invalidParams("count down")
                )
            )
        )

        val command = Command("test3").apply {
            name = CommandName("test")
            type = CommandType.Get
            parameters = mapOf(Pair("param1", "paramValue"))
            configuration = mapOf(Pair("config1", "config1Value"))
            result = mapOf(Pair("success", "true"))
            error = mapOf(Pair("error", "null"))
            sdkVersion = "12"
            status = CommandStatus.WAITING
        }

        // Act
        command.failure(errorResult)

        // Assert
        Assert.assertEquals(null, command.result)
        Assert.assertEquals(CommandStatus.FAILED, command.status)
        Assert.assertEquals(errorResult, command.error)
    }

    @Test
    fun `when init then get the commands with correct elements`() {
        // Arrange
        val commandName = CommandName("Test")
        val commandType = CommandType.Get
        val params1 = mapOf(
            "abc" to 1,
            "listMap" to listOf(
                mapOf("a" to "a", "d" to "d"),
                mapOf("z" to "z", "t" to "t"),
                mapOf("childSimpleList" to listOf("b1c", "l3c", "l1c"))
            ),
            "bd" to 4,
            "bc" to "2b",
            "simpleMap" to mapOf("mm" to 11, "ss" to 9),
            "simpleList" to listOf("l3", "l1", "b1")
        )
        val params2 = mapOf(
            "listMap" to listOf(
                mapOf("z" to "z", "t" to "t"),
                mapOf("childSimpleList" to listOf("l3c", "l1c", "b1c")),
                mapOf("d" to "d", "a" to "a")
            ),
            "abc" to 1,
            "simpleList" to listOf("b1", "l1", "l3"),
            "simpleMap" to mapOf("ss" to 9, "mm" to 11),
            "bd" to 4,
            "bc" to "2b"
        )

        // Act
        val command = Command().init(
            name = commandName,
            type = commandType,
            parameters = params1,
            configuration = null
        )
        val command2 = Command().init(
            name = commandName,
            type = commandType,
            parameters = params2,
            configuration = null
        )

        // Arrange
        val params3 = mapOf(
            "abc" to 1,
            "simpleList" to listOf("b1", "l1", "l3"),
        )
        val params4 = mapOf(
            "listMap" to listOf<String>(),
            "abc" to 1
        )

        // Act
        val command3 = Command().init(
            name = commandName,
            type = commandType,
            parameters = params3,
            configuration = null
        )
        val command4 = Command().init(
            name = commandName,
            type = commandType,
            parameters = params4,
            configuration = null
        )

        // Assert
        Assert.assertNotEquals(command.transactionId, command2.transactionId)
        Assert.assertEquals(command.name, command2.name)
        Assert.assertEquals(command.type, command2.type)
        Assert.assertEquals(command.identifier, command2.identifier)
        Assert.assertNotEquals(command3.identifier, command4.identifier)
    }

    @Test
    fun `when toMap then get a map`() {
        // Arrange
        val command = Command().apply {
            name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
            type = CommandType.Get
            parameters = mapOf(Pair("param1", "paramValue"))
            configuration = mapOf(Pair("config1", "config1Value"))
            result = mapOf(Pair("success", "true"))
            error = mapOf(Pair("error", "null"))
            sdkVersion = "12"
            status = CommandStatus.WAITING
        }

        // Act
        val map = command.toMap()

        // Assert
        Assert.assertEquals(command.name?.name, map["name"])
        Assert.assertEquals(command.type?.type, map["type"])
        Assert.assertEquals(command.parameters, map["parameters"])
        Assert.assertEquals(command.result, map["result"])
        Assert.assertEquals(command.error, map["error"])
        Assert.assertEquals(command.sdkVersion, map["sdkVersion"])
        Assert.assertEquals(command.status.name, map["status"])
    }

}
