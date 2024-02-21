package com.inetpsa.pims.spaceMiddleware.executor.locations

import android.net.Uri
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import java.util.Locale

internal open class LocationExecutorTestHelper {

    protected val baseCommand: BaseCommand = mockk()
    protected val communicationManager: ICommunicationManager = mockk()
    protected val middlewareComponent: MiddlewareComponent = mockk()
    protected val configurationManager: ConfigurationManager = spyk()

    @Before
    open fun setup() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        every { baseCommand.parameters } returns emptyMap()
        every { configurationManager.googleApiKey } returns "testGoogleApiKey"
        every { configurationManager.locale } returns Locale.FRANCE
        every { middlewareComponent.configurationManager } returns configurationManager
        every { middlewareComponent.communicationManager } returns communicationManager
        every { baseCommand.middlewareComponent } returns middlewareComponent
        every { baseCommand.parameters } returns emptyMap()
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }
}
