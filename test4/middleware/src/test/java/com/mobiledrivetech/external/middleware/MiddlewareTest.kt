package com.mobiledrivetech.external.middleware

import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
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

class MiddlewareTest {
    private lateinit var middleware: IMiddleware
    private val middlewareComponent: MiddlewareComponent = mockk()

    @Before
    fun setup() {
        middleware = spyk(Middleware())
        every { (middleware as Middleware).core } returns middlewareComponent

        justRun { middlewareComponent.initialize(any(), any()) }
        every { middlewareComponent.get(any(), any(), any()) } returns "test"
        every { middlewareComponent.set(any(), any(), any()) } returns "test"
        justRun { middlewareComponent.release() }
    }

    @After
    fun release() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `when initialize middleware then call MiddlewareComponent initialize function`() {
        // Arrange
        val params = mapOf(
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_MARKET to Market.NONE,
                Constants.CONTEXT_KEY_BRAND to Brand.DEFAULT
            ),
            Constants.CONTEXT_KEY_ENVIRONMENT to Environment.DEV
        )
        val callback: (Map<String, Any>) -> Unit = {}

        // Act
        middleware.initialize(parameters = params, callback = callback)

        // Assert
        verify { middlewareComponent.initialize(params, callback) }
    }

    @Test
    fun `test release() calls MiddlewareComponent release function`() {
        // Act
        middleware.release()

        // Assert
        verify { middlewareComponent.release() }
    }

    @Test
    fun `when calling get middleware then call MiddlewareComponent get function`() {
        // Arrange
        val api = "${Constants.API_PREFIX}.${Constants.API.TEST}"
        val params: Map<String, Any> = mockk()
        val callback: (Map<String, Any?>) -> Unit = {}
        val expect = "test"
        every { middlewareComponent.get(any(), any(), any()) } returns expect

        // Act
        val result = middleware.get(api = api, parameters = params, callback = callback)

        // Assert
        verify { middlewareComponent.get(api, params, callback) }
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when calling set middleware then call MiddlewareComponent set function`() {
        // Arrange
        val api = "${Constants.API_PREFIX}.${Constants.API.TEST}"
        val params: Map<String, Any> = mockk()
        val callback: (Map<String, Any?>) -> Unit = {}
        val expect = "test"
        every { middlewareComponent.set(any(), any(), any()) } returns expect

        // Act
        val result = middleware.set(api = api, parameters = params, callback = callback)

        // Assert
        verify { middlewareComponent.set(api, params, callback) }
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `test core(MiddlewareComponent) initialization by lazy`() {
        // Arrange
        mockkObject(MDLog)
        justRun { MDLog.debug(any(), any(), any()) }

        // Act
        val core = Middleware().core

        // Assert
        Assert.assertNotNull(core)
    }
}