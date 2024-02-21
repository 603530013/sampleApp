package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market
import org.junit.Assert
import org.junit.Test

class EnvironmentExtensionsKtTest {

    @Test
    fun testAsFcaEnvironmentLink() {
        val preprodEnvironment: Environment = Environment.PREPROD
        val devEnvironment: Environment = Environment.DEV
        val prodEnvironment: Environment = Environment.PROD
        val invalidEnvironment: Environment? = null

        // Test valid environments
        Assert.assertEquals(preprodEnvironment.asFcaEnvironmentLink(), "prep.")
        Assert.assertEquals(devEnvironment.asFcaEnvironmentLink(), "intg.")
        Assert.assertEquals(prodEnvironment.asFcaEnvironmentLink(), "")

        // Test invalid environment
        Assert.assertThrows(PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)::class.java) {
            invalidEnvironment.asFcaEnvironmentLink()
        }
    }

    @Test
    fun testAsPsaEnvironmentLink() {
        val devEnvironment: Environment = Environment.DEV
        val preprodEnvironment: Environment = Environment.PREPROD
        val prodEnvironment: Environment = Environment.PROD
        val invalidEnvironment: Environment? = null

        // Test valid environments
        Assert.assertEquals(devEnvironment.asPsaEnvironmentLink(), "-rfrec")
        Assert.assertEquals(preprodEnvironment.asPsaEnvironmentLink(), "-preprod")
        Assert.assertEquals(prodEnvironment.asPsaEnvironmentLink(), "")

        // Test invalid environment
        Assert.assertThrows(PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)::class.java) {
            invalidEnvironment.asPsaEnvironmentLink()
        }
    }

    @Test
    fun testAsRegionLink() {
        val emeaMarket: Market = Market.EMEA
        val latamMarket: Market = Market.LATAM
        val naftaMarket: Market = Market.NAFTA
        val invalidMarket: Market? = null

        Assert.assertEquals(emeaMarket.asRegionLink(), "-01")
        Assert.assertEquals(latamMarket.asRegionLink(), "-02")
        Assert.assertEquals(naftaMarket.asRegionLink(), "-02")

        Assert.assertThrows(PIMSFoundationError.invalidParameter("market")::class.java) {
            invalidMarket.asRegionLink()
        }
    }
}
