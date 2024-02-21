package com.mobiledrivetech.external.middleware.foundation.genericComponent

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.foundation.commandManager.ICommandManager
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
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

class GenericCoreComponentTest {

    private lateinit var genericCoreComponent: GenericCoreComponent
    private val commandManager: ICommandManager = mockk(relaxed = true)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        genericCoreComponent = spyk(GenericCoreComponent())
        genericCoreComponent.commandManager = commandManager
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
    fun `when initialize then initialize the component`() {
        // Arrange
        val parameters = mapOf(Pair("params1", "testParams"))
        val callback: ((Map<String, Any>) -> Unit) = mockk(relaxed = true)
        every { commandManager.supportedApis() } returns listOf(Constants.API.TEST)

        // Act
        genericCoreComponent.initialize(parameters = parameters, callback = callback)

        // Assert
        verify { commandManager.initialize(any(), any()) }
        verify { callback.invoke(mapOf(Constants.APIS to listOf(Constants.API.TEST))) }
    }

    @Test
    fun `when release then release the component`() {
        // Act
        genericCoreComponent.release()

        // Assert
        verify { MDLog.debug(any()) }
    }

    @Test
    fun `when get then execute the command`() {
        // Arrange
        val api = "${Constants.API_PREFIX}.${Constants.API.TEST}"
        val callback: ((Map<String, Any?>) -> Unit) = mockk(relaxed = true)

        // Act
        val transactionId =
            genericCoreComponent.get(api = api, parameters = null, callback = callback)

        // Assert
        Assert.assertEquals(
            transactionId,
            commandManager.executeCommand(
                name = CommandName(api),
                type = CommandType.Get,
                parameters = null,
                callback = callback
            )
        )
    }

    @Test
    fun `when set then execute the command`() {
        // Arrange
        val api = "${Constants.API_PREFIX}.${Constants.API.TEST}"
        val callback: ((Map<String, Any?>) -> Unit) = mockk(relaxed = true)
        val parameters:Map<String, Any> = mapOf(Pair("param1", "testParam"))

        // Act
        val transactionId =
            genericCoreComponent.set(api = api, parameters = parameters, callback = callback)

        // Assert
        Assert.assertEquals(
            transactionId,
            commandManager.executeCommand(
                name = CommandName(api),
                type = CommandType.Set,
                parameters = parameters,
                callback = callback
            )
        )
    }

    @Test
    fun `when subscribe then subscribe the command`() {
        // Arrange
        val api = "${Constants.API_PREFIX}.${Constants.API.TEST}"
        val callback: ((Map<String, Any?>) -> Unit) = mockk(relaxed = true)

        // Act
        val transactionId =
            genericCoreComponent.subscribe(api = api, parameters = null, callback = callback)

        // Assert
        Assert.assertEquals(
            transactionId,
            commandManager.executeCommand(
                name = CommandName(api),
                type = CommandType.Subscribe,
                parameters = null,
                callback = callback
            )
        )
    }
    @Test
    fun `when unsubscribe then unsubscribe the command`() {
        // Arrange
        val api = "${Constants.API_PREFIX}.${Constants.API.TEST}"
        val callback: ((Map<String, Any?>) -> Unit) = mockk(relaxed = true)

        // Act
        val transactionId =
            genericCoreComponent.unsubscribe(api = api, callback = callback)

        // Assert
        Assert.assertEquals(
            transactionId,
            commandManager.executeCommand(
                name = CommandName(api),
                type = CommandType.Unsubscribe,
                parameters = null,
                callback = callback
            )
        )
    }
}