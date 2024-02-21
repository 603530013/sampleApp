package com.inetpsa.pims.spaceMiddleware.executor

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.UserSession
import com.inetpsa.mmx.foundation.userSession.UserSessionManager
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BaseFcaExecutorTest {

    private val configurationManager: ConfigurationManager = spyk()
    private val middlewareComponent: MiddlewareComponent = mockk()
    private val command: BaseCommand = mockk()
    private lateinit var executer: TestBaseFcaExecutor

    @Before
    fun setup() {
        every { middlewareComponent.configurationManager } returns configurationManager
        every { command.middlewareComponent } returns middlewareComponent
        every { command.parameters } returns mapOf()
        executer = spyk(TestBaseFcaExecutor(command))
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when uid is missing then return uid value`() {
        val userSessionManager: UserSessionManager = mockk()
        val userSession: UserSession = mockk()
        every { middlewareComponent.userSessionManager } returns userSessionManager
        every { userSessionManager.getUserSession() } returns userSession
        every { userSession.customerId } returns "test_uid"

        val actual = executer.uid
        Assert.assertEquals("test_uid", actual)
    }

    @Test
    fun `when uid is missing then throw missing uid Parameter`() {
        val userSessionManager: UserSessionManager = mockk()
        val userSession: UserSession = mockk()
        every { middlewareComponent.userSessionManager } returns userSessionManager
        every { userSessionManager.getUserSession() } returns userSession
        every { userSession.customerId } returns null

        val exception = PIMSFoundationError.missingParameter("uid")
        try {
            executer.uid
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when baseUrl, EMEA and PREPROD then build base url String`() {
        val expect = "https://channels.sdpr-01.prep.fcagcv.com"
        val args = arrayOf("")
        every { configurationManager.market } returns Market.EMEA
        every { configurationManager.environment } returns Environment.PREPROD

        val actual = executer.baseUrl(args)
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `when baseUrl, EMEA and DEV then build base url String`() {
        val expect = "https://channels.sdpr-01.intg.fcagcv.com"
        val args = arrayOf("")
        every { configurationManager.market } returns Market.EMEA
        every { configurationManager.environment } returns Environment.DEV

        val actual = executer.baseUrl(args)
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `when baseUrl, EMEA and PROD then build base url String`() {
        val expect = "https://channels.sdpr-01.fcagcv.com"
        val args = arrayOf("")
        every { configurationManager.market } returns Market.EMEA
        every { configurationManager.environment } returns Environment.PROD

        val actual = executer.baseUrl(args)
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `when baseUrl, LATAM and PROD then build base url String`() {
        val expect = "https://channels.sdpr-02.fcagcv.com"
        val args = arrayOf("")
        every { configurationManager.market } returns Market.LATAM
        every { configurationManager.environment } returns Environment.PROD

        val actual = executer.baseUrl(args)
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `when baseUrl, NAFTA and PREPROD then build base url String`() {
        val expect = "https://channels.sdpr-02.prep.fcagcv.com"
        val args = arrayOf("")
        every { configurationManager.market } returns Market.NAFTA
        every { configurationManager.environment } returns Environment.PREPROD

        val actual = executer.baseUrl(args)
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `when baseUrl, invalid market and PREPROD then throw invalidParameter`() {
        val expect = PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        val args = arrayOf("")
        every { configurationManager.market } returns Market.NONE
        every { configurationManager.environment } returns Environment.PREPROD
        try {
            executer.baseUrl(args)
        } catch (throwable: PIMSError) {
            Assert.assertEquals(expect.code, throwable.code)
            Assert.assertEquals(expect.message, throwable.message)
        }
    }

    @Test
    fun `when baseUrl, EMEA and Invalid Environment then throw invalidEnvironment`() {
        val expect = PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)
        val args = arrayOf("")
        every { configurationManager.market } returns Market.EMEA
        every { configurationManager.environment } returns Environment.PREPROD
        try {
            executer.baseUrl(args)
        } catch (throwable: PIMSError) {
            Assert.assertEquals(expect.code, throwable.code)
            Assert.assertEquals(expect.message, throwable.message)
        }
    }

    @Test
    fun `when baseHeaders returns base headers Map`() {
        every { middlewareComponent.applicationName } returns "test_applicationName"
        every { middlewareComponent.applicationVersion } returns "test_applicationVersion"
        every { executer.getLocale() } returns "test_locale"

        val actual = executer.baseHeaders()
        Assert.assertEquals(1, actual.size)
        // Assert.assertEquals(Constants.AWS_HEADER_VALUE_SERVICE_NAME, actual[Constants.AWS_HEADER_KEY_SERVICE_NAME])
        Assert.assertEquals("test_locale", actual[Constants.LOCALE])
    }

    @Test
    fun `when baseQueries returns emptyMap`() {
        val actual = executer.baseQueries()
        Assert.assertEquals(emptyMap<String, String>(), actual)
    }

    private class TestBaseFcaExecutor(command: BaseCommand) : BaseFcaExecutor<String, Unit>(command) {

        override fun params(parameters: Map<String, Any?>?): String = "test_params"

        override suspend fun execute(input: String): NetworkResponse<Unit> = NetworkResponse.Success(Unit)
    }
}
