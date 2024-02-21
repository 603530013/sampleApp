package com.inetpsa.pims.spaceMiddleware

import android.content.Context
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment.DEV
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
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
    private val context: Context = mockk()

    @Before
    fun setup() {
        every { context.applicationContext } returns mockk()

        middleware = spyk(Middleware(context))
        every { (middleware as Middleware).core } returns middlewareComponent

        justRun { middlewareComponent.initialize(any(), any(), any()) }
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
        val params = mapOf(
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_MARKET to "nafta",
                CONTEXT_KEY_BRAND to Brand.JEEP
            ),
            CONTEXT_KEY_ENVIRONMENT to DEV
        )
        middleware.initialize(params) {}
        verify { middlewareComponent.initialize(any(), eq(params), any()) }
    }

    @Test
    fun `when calling get middleware then call MiddlewareComponent get function`() {
        middleware.get("pims.middleware.vehicle", mapOf()) { }
        verify { middlewareComponent.get(eq("pims.middleware.vehicle"), any(), any()) }
    }

    @Test
    fun `when calling set middleware then call MiddlewareComponent set function`() {
        middleware.set("pims.middleware.vehicle", mapOf()) {}
        verify { middlewareComponent.set(eq("pims.middleware.vehicle"), any(), any()) }
    }

    @Test
    fun `test release() calls MiddlewareComponent release function`() {
        middleware.release()
        verify { middlewareComponent.release() }
    }

    @Test
    fun `test core(MiddlewareComponent) initialization by lazy with application context`() {
        val core = Middleware(context).core
        Assert.assertNotNull(core)
        Assert.assertEquals(context.applicationContext, core.context)
    }
}
