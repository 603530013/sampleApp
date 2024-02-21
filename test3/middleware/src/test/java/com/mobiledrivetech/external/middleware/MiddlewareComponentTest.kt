package com.mobiledrivetech.external.middleware

import com.mobiledrivetech.external.middleware.extensions.asMap
import com.mobiledrivetech.external.middleware.foundation.commandManager.CommandManager
import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericCoreComponent
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandStatus
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.manager.ConfigurationManager
import com.mobiledrivetech.external.middleware.model.ErrorCode
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.util.ErrorMessage
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

class MiddlewareComponentTest {
    private lateinit var middlewareComponent: MiddlewareComponent
    private val configurationManager: ConfigurationManager = mockk(relaxed = true)
    private val commandManager: CommandManager = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkObject(MDLog)
        justRun { MDLog.debug(any(), any(), any()) }
        middlewareComponent = spyk(MiddlewareComponent())
        every { middlewareComponent.commandManager } returns commandManager
        justRun { commandManager.fillCommandMapper(any(), any(), any()) }

    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when initialize() then configurationManager initialize`() {
        // Arrange
        val params: Map<String, Any> = mapOf()
        val callback: (Map<String, Any>) -> Unit = {}
        justRun { MDLog.debug(any(), any()) }
        justRun { configurationManager.initialize(any(), any()) }

        // Act
        middlewareComponent.initialize(params, callback)

        // Assert
        verify(exactly = 1) {
            (middlewareComponent as GenericCoreComponent).initialize(
                eq(params),
                eq(callback)
            )
        }
        Assert.assertEquals(Constants.SERVICE_NAME, middlewareComponent.serviceName)
        Assert.assertEquals(Constants.COMPONENT_NAME, middlewareComponent.name)
    }

    @Test
    fun `when initConfigureApi then fillCommandMapper with configureCommand`() {
        // Arrange
        val configureCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.CONFIGURATION}")
        // Act
        middlewareComponent.initConfigureApi(commandManager)

        // Assert
        verify(exactly = 1) {
            commandManager.fillCommandMapper(
                any(),
                configureCommand,
                CommandType.Set
            )
        }
    }

    @Test
    fun `when initTestApi then fillCommandMapper with testCommand`() {
        // Arrange
        val testCommand = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
        // Act
        middlewareComponent.initTestApi(commandManager)

        // Assert
        verify(exactly = 1) {
            commandManager.fillCommandMapper(
                any(),
                testCommand,
                CommandType.Get
            )
        }
    }

    @Test
    fun `when failure return the map to show failure info`() {
        // Arrange
        val middleWareError = MiddleWareError(
            ErrorCode.facadeNotInitialized,
            ErrorMessage.facadeNotInitialized
        )
        val expect = mapOf(
            Constants.KEY_STATUS to CommandStatus.FAILED,
            Constants.KEY_ERROR to middleWareError.asMap()
        )

        // Act
        val result = middlewareComponent.failure(middleWareError)

        // Assert
        Assert.assertEquals(expect, result)
    }
}