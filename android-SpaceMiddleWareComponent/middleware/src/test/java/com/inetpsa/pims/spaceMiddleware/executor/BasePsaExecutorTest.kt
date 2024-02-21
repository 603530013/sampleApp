package com.inetpsa.pims.spaceMiddleware.executor

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Environment
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
import java.util.Locale

class BasePsaExecutorTest {

    private val configurationManager: ConfigurationManager = spyk()
    private val middlewareComponent: MiddlewareComponent = mockk()
    private val command: BaseCommand = mockk()
    private lateinit var executer: TestBasePsaExecutor

    @Before
    fun setup() {
        every { configurationManager.locale } returns Locale.ENGLISH
        every { middlewareComponent.configurationManager } returns configurationManager
        every { command.middlewareComponent } returns middlewareComponent
        every { command.parameters } returns mapOf()
        executer = spyk(TestBasePsaExecutor(command))
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when baseUrl and PREPROD then build base url String`() {
        val expect = "https://microservices-preprod.mym.awsmpsa.com"
        val args = arrayOf("")
        every { configurationManager.environment } returns Environment.PREPROD

        val actual = executer.baseUrl(args)
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `when baseUrl and DEV then build base url String`() {
        val expect = "https://microservices-rfrec.mym.awsmpsa.com"
        val args = arrayOf("")
        every { configurationManager.environment } returns Environment.DEV

        val actual = executer.baseUrl(args)
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `when baseUrl and PROD then build base url String`() {
        val expect = "https://microservices.mym.awsmpsa.com"
        val args = arrayOf("")
        every { configurationManager.environment } returns Environment.PROD

        val actual = executer.baseUrl(args)
        Assert.assertEquals(expect, actual)
    }

    @Test
    fun `when baseQueries returns base headers Map`() {
        every { configurationManager.brandCode } returns "testBrandCode"
        every { configurationManager.siteCode } returns "testSiteCode"
        every { middlewareComponent.applicationVersion } returns "test_applicationVersion"

        val actual = executer.baseQueries()
        Assert.assertEquals(7, actual.size)
        Assert.assertEquals("testBrandCode", actual[Constants.QUERY_PARAM_KEY_BRAND])
        Assert.assertEquals(Constants.QUERY_PARAM_KEY_APP, actual[Constants.QUERY_PARAM_KEY_SOURCE])
        Assert.assertEquals("test_applicationVersion", actual[Constants.QUERY_PARAM_KEY_VERSION])
        Assert.assertEquals("testSiteCode", actual[Constants.QUERY_PARAM_KEY_SITE_CODE])
        Assert.assertEquals(Locale.ENGLISH.toString(), actual[Constants.QUERY_PARAM_KEY_CULTURE])
        Assert.assertEquals(Locale.ENGLISH.language, actual[Constants.QUERY_PARAM_KEY_LANGUAGE])
        Assert.assertEquals(Constants.QUERY_PARAM_VALUE_OS, actual[Constants.QUERY_PARAM_KEY_OS])
    }

    @Test
    fun `when baseHeaders returns emptyMap`() {
        val actual = executer.baseHeaders()
        Assert.assertEquals(emptyMap<String, String>(), actual)
    }

    private class TestBasePsaExecutor(command: BaseCommand) : BasePsaExecutor<String, Unit>(command) {

        override fun params(parameters: Map<String, Any?>?): String = "test_params"

        override suspend fun execute(input: String): NetworkResponse<Unit> = NetworkResponse.Success(Unit)
    }
}
