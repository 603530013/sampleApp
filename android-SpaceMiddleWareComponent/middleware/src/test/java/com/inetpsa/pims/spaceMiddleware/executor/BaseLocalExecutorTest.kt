package com.inetpsa.pims.spaceMiddleware.executor

import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import java.util.Locale

internal open class BaseLocalExecutorTest {

    protected val configurationManager: ConfigurationManager = spyk()
    protected val middlewareComponent: MiddlewareComponent = mockk()
    protected val baseCommand: BaseCommand = mockk()

    @Before
    open fun setup() {
        every { configurationManager.locale } returns Locale.FRANCE
        every { configurationManager.environment } returns Environment.PREPROD
        every { configurationManager.brand } returns Brand.PEUGEOT
        every { middlewareComponent.applicationName } returns "testApplication"
        every { middlewareComponent.applicationVersion } returns "1.0.0"
        every { middlewareComponent.configurationManager } returns configurationManager
        every { baseCommand.middlewareComponent } returns middlewareComponent
        every { baseCommand.parameters } returns emptyMap()
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }
}
