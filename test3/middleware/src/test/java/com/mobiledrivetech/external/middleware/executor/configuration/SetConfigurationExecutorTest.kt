package com.mobiledrivetech.external.middleware.executor.configuration

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.executor.ExecutorTestHelper
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.model.Response
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetConfigurationExecutorTest : ExecutorTestHelper() {
    private lateinit var setConfigurationExecutor: SetConfigurationExecutor

    override fun setup() {
        super.setup()
        setConfigurationExecutor = spyk(SetConfigurationExecutor(baseCommand))
    }

    @Test
    fun `when params then return ConfigurationInput`() {
        // Arrange
        val params = mapOf(
            Constants.CONTEXT_KEY_ENVIRONMENT to "Development",
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_BRAND to "default",
                Constants.CONTEXT_KEY_LOCALE to "fr-FR",
                Constants.CONTEXT_KEY_MARKET to Market.NONE
            )
        )
        val configurationInput = ConfigurationInput(
            environment = Environment.DEV,
            brand = Brand.DEFAULT,
            locale = Locale.FRANCE,
            market = Market.NONE
        )

        // Act
        val result = setConfigurationExecutor.params(params)

        // Assert
        Assert.assertEquals(configurationInput, result)
    }

    @Test
    fun `when params with incorrect environment then return ConfigurationInput`() {
        // Arrange
        val params = mapOf(
            Constants.CONTEXT_KEY_ENVIRONMENT to "test",
        )
        val configurationInput = ConfigurationInput(
            environment = null,
            brand = null,
            locale = null,
            market = Market.NONE
        )

        // Act
        val result = setConfigurationExecutor.params(params)

        // Assert
        Assert.assertEquals(configurationInput, result)
    }

    @Test
    fun `when execute then return response result`() = runTest {
        // Act
        val result = setConfigurationExecutor.execute()

        // Assert
        Assert.assertEquals(Response.Success(Unit), result)
    }
}