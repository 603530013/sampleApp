package com.mobiledrivetech.external.middleware.executor

import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.command.BaseCommand
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.manager.ConfigurationManager
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import java.util.Locale

internal open class ExecutorTestHelper {

    protected val configurationManager: ConfigurationManager = spyk()
    protected val middlewareComponent: MiddlewareComponent = mockk()
    protected val baseCommand: BaseCommand = mockk()

    @Before
    open fun setup() {
        every { configurationManager.locale } returns Locale.FRANCE
        every { configurationManager.environment } returns Environment.DEV
        every { configurationManager.brand } returns Brand.DEFAULT
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
