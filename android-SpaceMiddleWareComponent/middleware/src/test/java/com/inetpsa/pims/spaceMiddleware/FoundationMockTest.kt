package com.inetpsa.pims.spaceMiddleware

import android.content.Context
import android.net.Uri
import com.inetpsa.mmx.foundation.data.IDataManager
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.UserSession
import com.inetpsa.mmx.foundation.userSession.IUserSessionManager
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import java.util.TimeZone

internal open class FoundationMockTest {

    internal val nowMilliseconds = 1692455349000L
    internal val fixedClock = Clock.fixed(Instant.ofEpochMilli(nowMilliseconds), ZoneId.of("UTC"))
    internal val formattedClock = "${fixedClock.instant()}[${fixedClock.zone}]"

    protected val uid = "testCustomerId"
    protected val baseCommand: BaseCommand = mockk()
    protected val middlewareComponent: MiddlewareComponent = mockk()
    protected val configurationManager: ConfigurationManager = spyk()
    protected val context: Context = spyk()
    protected val userSessionManager: IUserSessionManager = mockk(relaxed = true)
    protected val userSession: UserSession = mockk(relaxed = true)
    protected val dataManager: IDataManager = mockk(relaxed = true)
    protected val uri: Uri = mockk(relaxed = true)

    open fun setup() {
        mockFoundation()
        mockFoundationLogger()
        mockTimeApi()
    }

    private fun mockTimeApi() {
        mockkStatic(Clock::class)
        // Default system clock
        every { Clock.systemUTC() } returns fixedClock
        every { Clock.system(any()) } returns fixedClock
        every { Clock.systemDefaultZone() } returns fixedClock
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"))
    }

    private fun mockFoundationLogger() {
        mockkObject(PIMSLogger)
        justRun { PIMSLogger.d(message = any()) }
        justRun { PIMSLogger.d(t = any()) }
        justRun { PIMSLogger.d(t = any(), message = any()) }

        justRun { PIMSLogger.i(message = any()) }
        justRun { PIMSLogger.i(t = any()) }
        justRun { PIMSLogger.i(t = any(), message = any()) }

        justRun { PIMSLogger.v(message = any()) }
        justRun { PIMSLogger.v(t = any()) }
        justRun { PIMSLogger.v(t = any(), message = any()) }

        justRun { PIMSLogger.w(message = any()) }
        justRun { PIMSLogger.w(t = any()) }
        justRun { PIMSLogger.w(t = any(), message = any()) }

        justRun { PIMSLogger.e(message = any()) }
        justRun { PIMSLogger.e(t = any()) }
        justRun { PIMSLogger.e(t = any(), message = any()) }
    }

    private fun mockFoundation() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns uri

        every { configurationManager.locale } returns Locale.FRANCE
        every { configurationManager.environment } returns Environment.PREPROD
        every { middlewareComponent.applicationName } returns "testApplication"
        every { middlewareComponent.applicationVersion } returns "1.0.0"
        every { middlewareComponent.configurationManager } returns configurationManager
        every { middlewareComponent.context } returns context

        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns uid
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
