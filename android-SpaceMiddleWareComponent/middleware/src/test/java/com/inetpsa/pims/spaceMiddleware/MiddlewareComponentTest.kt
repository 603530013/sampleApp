package com.inetpsa.pims.spaceMiddleware

import android.content.Context
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.genericComponent.GenericCoreComponent
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class MiddlewareComponentTest {

    private lateinit var middlewareComponent: MiddlewareComponent
    private val context: Context = mockk()
    private val configurationManager: ConfigurationManager = mockk()

    @Before
    fun setUp() {
        middlewareComponent = spyk(MiddlewareComponent(context))
        every { middlewareComponent.configurationManager } returns configurationManager
        justRun { configurationManager.initialize(any(), any()) }
    }

    @Test
    fun `test initialize()`() {
        val parameters = mapOf(
            Constants.PARAMS_KEY_CREDENTIAL to mapOf(Constants.PARAMS_KEY_GOOGLE_API_KEY to "test"),
            Constants.PARAMS_KEY_PROFILE to mapOf(
                CONTEXT_KEY_BRAND to "JEEP", /*Constants.Input.Configuration.LOCALE to "en-US",*/
                Constants.Input.Configuration.LANGUAGE_PATH to "test",
                Constants.CONTEXT_KEY_MARKET to EMEA
            ),
            CONTEXT_KEY_ENVIRONMENT to "PreProduction"
        )
        middlewareComponent.initialize(context, parameters) {}
//        verify { middlewareComponent.configurationManager.initialize(any(), any()) }
        verify(exactly = 1) { (middlewareComponent as GenericCoreComponent).initialize(context, parameters, any()) }
    }

    @Test
    fun `test release() calls clearCache()`() {
        mockkObject(BookingOnlineCache)
        mockkObject(com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache)
        every { BookingOnlineCache.clear() } returns Unit
        every { com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache.clear() } returns Unit
        middlewareComponent.release()
        verify { (middlewareComponent as GenericCoreComponent).release() }
        verify { BookingOnlineCache.clear() }
        verify { com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache.clear() }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
