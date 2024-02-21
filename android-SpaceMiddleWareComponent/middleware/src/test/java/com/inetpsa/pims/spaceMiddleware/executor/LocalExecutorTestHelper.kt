package com.inetpsa.pims.spaceMiddleware.executor

import android.net.Uri
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.data.IDataManager
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.UserSession
import com.inetpsa.mmx.foundation.userSession.IUserSessionManager
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

internal open class LocalExecutorTestHelper {

    protected val baseCommand: BaseCommand = mockk()
    protected val communicationManager: ICommunicationManager = mockk()
    protected val middlewareComponent: MiddlewareComponent = mockk()
    protected val configurationManager: ConfigurationManager = spyk()
    protected val userSessionManager: IUserSessionManager = mockk(relaxed = true)
    protected val userSession: UserSession = mockk(relaxed = true)
    protected val dataManager: IDataManager = mockk(relaxed = true)

    @Before
    open fun setup() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        every { configurationManager.locale } returns Locale.FRANCE
        every { configurationManager.environment } returns Environment.PREPROD
        every { configurationManager.brand } returns Brand.ALFAROMEO
        every { configurationManager.market } returns EMEA
        every { middlewareComponent.applicationName } returns "testApplication"
        every { middlewareComponent.applicationVersion } returns "1.0.0"
        every { middlewareComponent.configurationManager } returns configurationManager
        every { middlewareComponent.communicationManager } returns communicationManager

        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager

        every { baseCommand.middlewareComponent } returns middlewareComponent
        every { baseCommand.parameters } returns emptyMap()
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }
}
