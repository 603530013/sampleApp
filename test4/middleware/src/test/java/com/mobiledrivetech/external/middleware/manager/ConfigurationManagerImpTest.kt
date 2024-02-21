package com.mobiledrivetech.external.middleware.manager

import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

class ConfigurationManagerImpTest {
    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var manager: ConfigurationManagerImp

    @Before
    fun setup() {
        manager = spyk(ConfigurationManagerImp())
        manager.initialize(middlewareComponent, ConfigurationInput())
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when initialize then initialize the members`() {
        // Arrange
        val configurationInput = ConfigurationInput(
            environment = Environment.DEV,
            brand = Brand.DEFAULT,
            locale = Locale.FRANCE,
            market = Market.NONE
        )

        // Act
        manager.initialize(middlewareComponent, configurationInput)

        // Assert
        Assert.assertEquals(Environment.DEV, manager.environment)
        Assert.assertEquals(Brand.DEFAULT, manager.brand)
        Assert.assertEquals(Locale.FRANCE, manager.locale)
        Assert.assertEquals(Market.NONE, manager.market)
    }

    @Test
    fun `when update then update the members`() {
        // Arrange
        val configurationInput = ConfigurationInput()
        manager.initialize(middlewareComponent, configurationInput)

        val newConfigurationInput = ConfigurationInput(
            environment = Environment.DEV,
            brand = Brand.DEFAULT,
            locale = Locale.GERMANY,
            market = Market.NONE
        )

        // Act
        manager.update(middlewareComponent, newConfigurationInput)

        // Assert
        Assert.assertEquals(newConfigurationInput.environment, manager.environment)
        Assert.assertEquals(newConfigurationInput.brand, manager.brand)
        Assert.assertEquals(newConfigurationInput.locale, manager.locale)
        Assert.assertEquals(newConfigurationInput.market, manager.market)
    }
}