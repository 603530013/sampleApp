package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market
import org.junit.Assert
import org.junit.Test

class EnvironmentExtensionsTest {

    @Test
    fun `when asFcaEnvironmentLink and PREPROD then return prep`() {
        val environment = Environment.PREPROD
        val actual = environment.asFcaEnvironmentLink()
        Assert.assertEquals("prep.", actual)
    }

    @Test
    fun `when asFcaEnvironmentLink and DEV then return prep`() {
        val environment = Environment.DEV
        val actual = environment.asFcaEnvironmentLink()
        Assert.assertEquals("intg.", actual)
    }

    @Test
    fun `when asFcaEnvironmentLink and PROD then return prep`() {
        val environment = Environment.PROD
        val actual = environment.asFcaEnvironmentLink()
        Assert.assertEquals("", actual)
    }

    @Test
    fun `when asFcaEnvironmentLink and invalid environment then throw invalid Parameter`() {
        val environment = Environment.PROD
        val expect = PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)
        try {
            environment.asFcaEnvironmentLink()
        } catch (error: PIMSError) {
            Assert.assertEquals(expect.code, error.code)
            Assert.assertEquals(expect.message, error.message)
        }
    }

    @Test
    fun `when asPsaEnvironmentLink and DEV then return prep`() {
        val environment = Environment.DEV
        val actual = environment.asPsaEnvironmentLink()
        Assert.assertEquals("-rfrec", actual)
    }

    @Test
    fun `when asPsaEnvironmentLink and PREPROD then return prep`() {
        val environment = Environment.PREPROD
        val actual = environment.asPsaEnvironmentLink()
        Assert.assertEquals("-preprod", actual)
    }

    @Test
    fun `when asPsaEnvironmentLink and PROD then return prep`() {
        val environment = Environment.PROD
        val actual = environment.asPsaEnvironmentLink()
        Assert.assertEquals("", actual)
    }

    @Test
    fun `when asPsaEnvironmentLink and invalid environment then throw invalid Parameter`() {
        val environment = Environment.PROD
        val expect = PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)
        try {
            environment.asPsaEnvironmentLink()
        } catch (error: PIMSError) {
            Assert.assertEquals(expect.code, error.code)
            Assert.assertEquals(expect.message, error.message)
        }
    }

    @Test
    fun `when asRegionLink and EMEA then return prep`() {
        val market = Market.EMEA
        val actual = market.asRegionLink()
        Assert.assertEquals("-01", actual)
    }

    @Test
    fun `when asRegionLink and LATAM then return prep`() {
        val market = Market.LATAM
        val actual = market.asRegionLink()
        Assert.assertEquals("-02", actual)
    }

    @Test
    fun `when asRegionLink and NAFTA then return prep`() {
        val market = Market.NAFTA
        val actual = market.asRegionLink()
        Assert.assertEquals("-02", actual)
    }

    @Test
    fun `when asRegionLink and invalid Market then throw invalid Parameter`() {
        val market = Market.NONE
        val expect = PIMSFoundationError.invalidParameter("market")
        try {
            market.asRegionLink()
        } catch (error: PIMSError) {
            Assert.assertEquals(expect.code, error.code)
            Assert.assertEquals(expect.message, error.message)
        }
    }
}
