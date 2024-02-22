package com.mobiledrivetech.external.middleware.extensions

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.model.ErrorCode
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import org.junit.Assert
import org.junit.Test

class ParamExtensionsTest {

    @Test
    fun `when hasEnvironment then get the value`() {
        // Arrange
        val params = mapOf(
            Constants.CONTEXT_KEY_ENVIRONMENT to "Development"
        )

        // Act
        val environment = params hasEnvironment Constants.CONTEXT_KEY_ENVIRONMENT

        // Assert
        Assert.assertEquals(Environment.DEV, environment)
    }

    @Test
    fun `when hasEnvironmentOrNull without the info then return null`() {
        // Arrange
        val params = mapOf(
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_MARKET to "none",
                Constants.CONTEXT_KEY_BRAND to Brand.DEFAULT
            )
        )

        // Act
        val environment = params hasEnvironmentOrNull Constants.CONTEXT_KEY_ENVIRONMENT

        // Assert
        Assert.assertEquals(null, environment)
    }

    @Test
    fun `when hasOrNull without the info then return null`() {
        // Arrange
        val params: Map<String, Any?> = mapOf(
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_MARKET to Market.NONE,
                Constants.CONTEXT_KEY_BRAND to Brand.DEFAULT
            )
        )

        // Act
        val environment: Environment? = params hasOrNull Constants.CONTEXT_KEY_ENVIRONMENT

        // Assert
        Assert.assertEquals(null, environment)
    }

    @Test
    fun `when hasOrNull with other error then throw the error`() {
        // Arrange
        val params = mapOf(
            Constants.CONTEXT_KEY_ENVIRONMENT to Brand.DEFAULT
        )

        try {
            // Act
            params hasOrNull Constants.CONTEXT_KEY_ENVIRONMENT
        } catch (e: MiddleWareError) {
            // Assert
            Assert.assertEquals(ErrorCode.invalidParams, e.code)
            Assert.assertEquals("Invalid ${Constants.CONTEXT_KEY_ENVIRONMENT} parameter", e.message)
        }
    }

    @Test
    fun `when has with optional false then throw error`() {
        // Arrange
        val params: Map<String, Any?> = mapOf(
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_MARKET to Market.NONE,
                Constants.CONTEXT_KEY_BRAND to Brand.DEFAULT
            )
        )

        try {
            // Act
            params.has(Constants.CONTEXT_KEY_ENVIRONMENT, false)
        } catch (e: MiddleWareError) {
            // Assert
            Assert.assertEquals(ErrorCode.missingParams, e.code)
            Assert.assertEquals("Missing ${Constants.CONTEXT_KEY_ENVIRONMENT} parameter", e.message)
        }
    }

    @Test
    fun `when has with optional true then throw error`() {
        // Arrange
        val params: Map<String, Any?> = mapOf(
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_MARKET to Market.NONE,
                Constants.CONTEXT_KEY_BRAND to Brand.DEFAULT
            )
        )

        try {
            // Act
            params.has(Constants.CONTEXT_KEY_ENVIRONMENT, true)
        } catch (e: MiddleWareError) {
            // Assert
            Assert.assertEquals(ErrorCode.missingParams, e.code)
            Assert.assertEquals("Missing ${Constants.CONTEXT_KEY_ENVIRONMENT} parameter", e.message)
        }
    }

    @Test
    fun `when has with the key name then return the value`() {
        // Arrange
        val params: Map<String, String> = mapOf(Constants.CONTEXT_KEY_BRAND to "DEFAULT")

        // Act
        val result: String? = params.has(Constants.CONTEXT_KEY_BRAND, false)

        // Assert
        Assert.assertEquals(Brand.DEFAULT.name, result)
    }

    @Test
    fun `when hasEnumNullable with can't find key then return null`() {
        // Arrange
        val params: Map<String, Any?> = mapOf(
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_MARKET to Market.NONE,
                Constants.CONTEXT_KEY_BRAND to Brand.DEFAULT
            )
        )

        // Act
        val brand = params.hasEnumNullable<Brand>(Constants.CONTEXT_KEY_BRAND)

        // Assert
        Assert.assertEquals(null, brand)
    }

    @Test
    fun `when hasEnumNullable with find key then return value`() {
        // Arrange
        val params: Map<String, Any?> = mapOf(
            Constants.CONTEXT_KEY_MARKET to Market.NONE,
            Constants.CONTEXT_KEY_BRAND to Brand.DEFAULT.name
        )

        // Act
        val brand = params.hasEnumNullable<Brand>(Constants.CONTEXT_KEY_BRAND)

        // Assert
        Assert.assertEquals(Brand.DEFAULT, brand)
    }

    @Test
    fun `when hasEnum with no key then return missing error`() {
        // Arrange
        val params: Map<String, Any?> = mapOf(
            Constants.PARAMS_KEY_PROFILE to mapOf(
                Constants.CONTEXT_KEY_MARKET to Market.NONE
            )
        )

        try {
            // Act
            params.hasEnum<Brand>(Constants.CONTEXT_KEY_BRAND)
        } catch (e: MiddleWareError) {
            // Assert
            Assert.assertEquals(ErrorCode.missingParams, e.code)
            Assert.assertEquals("Missing ${Constants.CONTEXT_KEY_BRAND} parameter", e.message)

        }
    }

    @Test
    fun `when hasEnum with incorrect value then return invalidParameter error`() {
        // Arrange
        val params: Map<String, Any?> = mapOf(Constants.CONTEXT_KEY_BRAND to Environment.DEV)

        try {
            // Act
            params.hasEnum<Brand>(Constants.CONTEXT_KEY_BRAND)
        } catch (e: MiddleWareError) {
            // Assert
            Assert.assertEquals(ErrorCode.invalidParams, e.code)
            Assert.assertEquals("Invalid ${Constants.CONTEXT_KEY_BRAND} parameter", e.message)

        }
    }

    @Test
    fun `when hasEnum with fallback then return value`() {
        // Arrange
        val params: Map<String, Any?> = mapOf(Constants.CONTEXT_KEY_BRAND to Brand.DEFAULT.name)

        // Act
        val brand = params.hasEnum(Constants.CONTEXT_KEY_BRAND, Brand.DEFAULT)

        // Assert
        Assert.assertEquals(Brand.DEFAULT, brand)
    }
}