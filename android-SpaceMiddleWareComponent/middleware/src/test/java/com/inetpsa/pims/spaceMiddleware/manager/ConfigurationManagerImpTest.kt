package com.inetpsa.pims.spaceMiddleware.manager

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.data.IDataManager
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup
import com.inetpsa.pims.spaceMiddleware.model.manager.Config
import com.inetpsa.pims.spaceMiddleware.util.createSync
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.util.Locale

internal class ConfigurationManagerImpTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private val manager: ConfigurationManagerImp = spyk(ConfigurationManagerImp(), recordPrivateCalls = true)

    @Test
    fun `when execute save then verify that we save using data manager function`() {
        val dataManager: IDataManager = mockk(relaxed = true)
        mockkStatic(MiddlewareComponent::createSync)
        every { middlewareComponent.dataManager } returns dataManager
        coEvery { middlewareComponent.createSync(any(), any(), any()) } returns true
        manager.save(middlewareComponent, "testKey", "testData", StoreMode.APPLICATION)
        coVerify {
            middlewareComponent.createSync(
                eq("testKey"),
                eq("testData"),
                eq(StoreMode.APPLICATION)
            )
        }
    }

    @Test
    fun `when initialize then save all data in memory`() {
        justRun { manager.save(any(), any(), any(), any()) }

        val environment = Environment.PREPROD
        val brand = Brand.PEUGEOT
        val googleApiKey = "testGoogleApiKey"
        val locale = Locale.FRENCH
        val market = Market.EMEA
        val siteCode = "testSiteCode"
        val languagePath = "testLanguagePath"
        runTest {
            val config = Config(
                environment = environment,
                brand = brand,
                googleApiKey = googleApiKey,
                locale = locale,
                market = market,
                siteCode = siteCode,
                languagePath = languagePath
            )
            manager.initialize(
                component = middlewareComponent,
                config = config
            )
            coVerify(exactly = 1) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }

            Assert.assertEquals(environment, manager.environment)

            Assert.assertEquals(brand, manager.brand)
            Assert.assertEquals(BrandGroup.PSA, manager.brandGroup)
            Assert.assertEquals("AP", manager.brandCode)

            Assert.assertEquals(googleApiKey, manager.googleApiKey)
            Assert.assertEquals(market, manager.market)
            Assert.assertEquals(siteCode, manager.siteCode)
            Assert.assertEquals(languagePath, manager.languagePath)
            Assert.assertEquals(locale, manager.locale)
        }
    }

    @Test
    fun `when update Environment then update only environment`() {
        justRun { manager.save(any(), any(), any(), any()) }

        runTest {
            val environment = Environment.PROD
            val locale = Locale.FRENCH

            // region update Environment
            manager.update(
                component = middlewareComponent,
                config = Config(environment = environment)
            )
            coVerify(exactly = 0) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }
            Assert.assertEquals(environment, manager.environment)
            // endregion update Environment
        }
    }

    @Test
    fun `when update Brand with PSA Group then update brand, BrandGroup and brandCode`() {
        justRun { manager.save(any(), any(), any(), any()) }

        runTest {
            val locale = Locale.FRENCH
            val brand = Brand.PEUGEOT

            // region update Environment
            manager.update(
                component = middlewareComponent,
                config = Config(brand = brand)
            )
            coVerify(exactly = 0) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }
            Assert.assertEquals(brand, manager.brand)
            Assert.assertEquals("AP", manager.brandCode)
            Assert.assertEquals(BrandGroup.PSA, manager.brandGroup)
            // endregion update Environment
        }
    }

    @Test
    fun `when update Brand with FCA Group then update only brand, BrandGroup and brandCode`() {
        justRun { manager.save(any(), any(), any(), any()) }

        runTest {
            val locale = Locale.FRENCH
            val brand = Brand.FIAT

            // region update Environment
            manager.update(
                component = middlewareComponent,
                config = Config(brand = brand)
            )
            coVerify(exactly = 0) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }
            Assert.assertEquals(brand, manager.brand)
            Assert.assertEquals(BrandGroup.FCA, manager.brandGroup)
            Assert.assertThrows(PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)::class.java) {
                manager.brandCode
            }
            // endregion update Environment
        }
    }

    @Test
    fun `when update googleApiKey then update only googleApiKey`() {
        justRun { manager.save(any(), any(), any(), any()) }

        runTest {
            val locale = Locale.FRENCH
            val googleApiKey = "testGoogleApiKey"

            // region update Environment
            manager.update(
                component = middlewareComponent,
                config = Config(googleApiKey = googleApiKey)
            )
            coVerify(exactly = 0) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }
            Assert.assertEquals(googleApiKey, manager.googleApiKey)
            // endregion update Environment
        }
    }

    @Test
    fun `when update locale then update only locale`() {
        justRun { manager.save(any(), any(), any(), any()) }

        runTest {
            val locale = Locale.FRENCH

            // region update Environment
            manager.update(
                component = middlewareComponent,
                config = Config(locale = locale)
            )
            coVerify(exactly = 1) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }
            Assert.assertEquals(locale, manager.locale)
            // endregion update Environment
        }
    }

    @Test
    fun `when update market then update only market`() {
        justRun { manager.save(any(), any(), any(), any()) }

        runTest {
            val locale = Locale.FRENCH
            val market = Market.EMEA

            // region update Environment
            manager.update(
                component = middlewareComponent,
                config = Config(market = market)
            )
            coVerify(exactly = 0) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }
            Assert.assertEquals(market, manager.market)
            // endregion update Environment
        }
    }

    @Test
    fun `when update siteCode then update only siteCode`() {
        justRun { manager.save(any(), any(), any(), any()) }

        runTest {
            val locale = Locale.FRENCH
            val siteCode = "testSiteCode"

            // region update Environment
            manager.update(
                component = middlewareComponent,
                config = Config(siteCode = siteCode)
            )
            coVerify(exactly = 0) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }
            Assert.assertEquals(siteCode, manager.siteCode)
            // endregion update Environment
        }
    }

    @Test
    fun `when update languagePath then update only languagePath`() {
        justRun { manager.save(any(), any(), any(), any()) }

        runTest {
            val locale = Locale.FRENCH
            val languagePath = "testLanguagePath"

            // region update Environment
            manager.update(
                component = middlewareComponent,
                config = Config(languagePath = languagePath)
            )
            coVerify(exactly = 0) {
                manager.save(
                    component = eq(middlewareComponent),
                    eq(Constants.Storage.LANGUAGE),
                    eq(locale.toLanguageTag()),
                    eq(StoreMode.APPLICATION)
                )
            }
            Assert.assertEquals(languagePath, manager.languagePath)
            // endregion update Environment
        }
    }

    @Test
    fun `when execute toBrandCode with PSA brand then return the right brand code`() {
        val values = mapOf(
            Brand.PEUGEOT to "AP",
            Brand.CITROEN to "AC",
            Brand.DS to "DS",
            Brand.OPEL to "OP",
            Brand.VAUXHALL to "VX"
        )
        runTest {
            values.forEach { (brand, brandCode) ->
                Assert.assertEquals(brandCode, manager.toBrandCode(brand))
            }
        }
    }

    @Test
    fun `when execute toBrandCode with FCA brand then throw exception`() {
        runTest {
            val error = PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)
            val gmaBrands = listOf(
                Brand.FIAT,
                Brand.ALFAROMEO,
                Brand.FIAT,
                Brand.MASERATI,
                Brand.RAM,
                Brand.JEEP,
                Brand.WAGONEER,
                Brand.DODGE,
                Brand.CHRYSLER,
                Brand.LANCIA,
                Brand.ABARTH,
                Brand.MOPAR
            )

            gmaBrands.forEach { brand ->
                Assert.assertThrows(error::class.java) {
                    manager.toBrandCode(brand)
                }
            }
        }
    }

    @Test
    fun `when look for environment before initialize then throw exception`() {
        Assert.assertThrows(PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)::class.java) {
            manager.environment
        }
    }

    @Test
    fun `when look for brand before initialize then throw exception`() {
        Assert.assertThrows(PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)::class.java) {
            manager.brand
        }
    }

    @Test
    fun `when look for brandGroup before initialize then throw exception`() {
        Assert.assertThrows(PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)::class.java) {
            manager.brandGroup
        }
    }

    @Test
    fun `when look for brandCode before initialize then throw exception`() {
        Assert.assertThrows(PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)::class.java) {
            manager.brandCode
        }
    }

    @Test
    fun `when look for googleApiKey before initialize then throw exception`() {
        Assert.assertThrows(PIMSFoundationError.invalidParameter(Constants.PARAMS_KEY_GOOGLE_API_KEY)::class.java) {
            manager.googleApiKey
        }
    }

    @Test
    fun `when look for siteCode before initialize then throw exception`() {
        Assert.assertThrows(
            PIMSFoundationError.invalidParameter(Constants.Input.Configuration.SITE_CODE)::class.java
        ) {
            manager.siteCode
        }
    }

    @Test
    fun `when look for languagePath before initialize then throw exception`() {
        Assert.assertThrows(
            PIMSFoundationError.invalidParameter(Constants.Input.Configuration.LANGUAGE_PATH)::class.java
        ) {
            manager.languagePath
        }
    }

    @Test
    fun `when look for locale before initialize then return English`() {
        Assert.assertEquals(Locale.ENGLISH.toLanguageTag(), manager.locale.toLanguageTag())
    }

    @Test
    fun `when look for market before initialize then throw exception`() {
        Assert.assertThrows(PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)::class.java) {
            manager.market
        }
    }
}
