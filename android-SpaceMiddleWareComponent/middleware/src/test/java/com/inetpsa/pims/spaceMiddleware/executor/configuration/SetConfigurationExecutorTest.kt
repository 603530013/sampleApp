package com.inetpsa.pims.spaceMiddleware.executor.configuration

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.monitoring.IMonitoringManager
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand.CITROEN
import com.inetpsa.mmx.foundation.tools.Brand.FIAT
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.NONE
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutorTest
import com.inetpsa.pims.spaceMiddleware.manager.LoggerManager
import com.inetpsa.pims.spaceMiddleware.model.configuration.ConfigurationInput
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

internal class SetConfigurationExecutorTest : BaseLocalExecutorTest() {

    private lateinit var executor: SetConfigurationExecutor
    private val monitoringManager: IMonitoringManager = mockk()

    override fun setup() {
        super.setup()
        justRun { monitoringManager.configurePIMSLogger(any(), any(), any(), any()) }
        every { middlewareComponent.monitoringManager } returns monitoringManager
        val loggerManager = mockk<LoggerManager>()
        justRun { loggerManager.configure(any(), any()) }
        every { middlewareComponent.loggerManager } returns loggerManager
        executor = spyk(SetConfigurationExecutor(baseCommand))
    }

    @Test
    fun `when execute then make set call`() {
        val configurationInput = ConfigurationInput(
            environment = Environment.PREPROD,
            brand = CITROEN,
            googleApiKey = "testGoogleApiKey",
            locale = Locale.FRANCE,
            market = EMEA,
            siteCode = "testSiteCode",
            languagePath = "testLanguagePath"
        )

        every { executor.params(any()) } returns configurationInput

        runTest {
            val response = executor.execute(configurationInput)
            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute params with right input then return ConfigurationInput`() {
        val configurationInput = ConfigurationInput(
            environment = Environment.PREPROD,
            brand = CITROEN,
            googleApiKey = "testGoogleApiKey",
            locale = Locale.FRANCE,
            market = NONE,
            siteCode = "testSiteCode",
            languagePath = "testLanguagePath"
        )

        val params = mapOf(
            CONTEXT_KEY_ENVIRONMENT to "PreProduction",
            "credential" to mapOf(
                "googleApiKey" to "testGoogleApiKey"
            ),
            "profile" to mapOf(
                "brand" to "CITROEN",
                "locale" to "fr-FR",
                "market" to NONE,
                "siteCode" to "testSiteCode",
                "languagePath" to "testLanguagePath"
            )
        )

        val result = executor.params(params)
        assertEquals(configurationInput, result)
    }

    @Test
    fun `when execute params with right input and market then return ConfigurationInput`() {
        val configurationInput = ConfigurationInput(
            environment = Environment.PREPROD,
            brand = FIAT,
            googleApiKey = "testGoogleApiKey",
            locale = Locale.FRANCE,
            market = EMEA,
            siteCode = null,
            languagePath = "testLanguagePath"
        )

        val params = mapOf(
            CONTEXT_KEY_ENVIRONMENT to "PreProduction",
            "credential" to mapOf(
                "googleApiKey" to "testGoogleApiKey"
            ),
            "profile" to mapOf(
                "brand" to "FIAT",
                "locale" to "fr-FR",
                Constants.CONTEXT_KEY_MARKET to "EMEA",
                "siteCode" to null,
                "languagePath" to "testLanguagePath"
            )
        )

        val result = executor.params(params)
        assertEquals(configurationInput, result)
    }
}
