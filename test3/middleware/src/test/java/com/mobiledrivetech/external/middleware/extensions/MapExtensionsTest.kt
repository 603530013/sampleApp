package com.mobiledrivetech.external.middleware.extensions

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError
import org.junit.Assert
import org.junit.Test

class MapExtensionsTest {

    @Test
    fun `when sort then return the sorted map`() {
        // Arrange
        val params = mapOf(
            "abc" to 1,
            "listMap" to listOf(
                mapOf("a" to "a", "d" to "d"),
                mapOf("z" to "z", "t" to "t"),
                mapOf("childSimpleList" to listOf("b1c", "l3c", "l1c"))
            ),
            "bd" to 4,
            "bc" to "2b",
            "simpleMap" to mapOf("mm" to 11, "ss" to 9),
            "simpleList" to listOf("l3", "l1", "b1")
        )
        val expect = mapOf(
            "abc" to 1,
            "bc" to "2b",
            "bd" to 4,
            "listMap" to listOf(
                mapOf("a" to "a", "d" to "d"),
                mapOf("childSimpleList" to listOf("b1c", "l1c", "l3c")),
                mapOf("t" to "t", "z" to "z")
            ),
            "simpleList" to listOf("b1", "l1", "l3"),
            "simpleMap" to mapOf("mm" to 11, "ss" to 9)
        )

        // Act
        val result = params.sort()

        // Assert
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when has with null map then throw toMissingParamError`() {
        // Arrange
        val params: Map<String, Any?>? = null
        val exception = MiddleWareFoundationError.missingParameter(Constants.Input.ACTION_TYPE)


        try {
            // Act
            params has Constants.Input.ACTION_TYPE
        } catch (error: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, error.code)
            Assert.assertEquals(exception.message, error.message)
        }
    }

    @Test
    fun `when has with empty map then throw toMissingParamError`() {
        // Arrange
        val params: Map<String, Any?> = emptyMap()
        val exception = MiddleWareFoundationError.missingParameter(Constants.Input.ACTION_TYPE)


        try {
            // Act
            params has Constants.Input.ACTION_TYPE
        } catch (error: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, error.code)
            Assert.assertEquals(exception.message, error.message)
        }
    }

    @Test
    fun `when has without the key then throw toMissingParamError`() {
        // Arrange
        val params = mapOf("test" to 1)
        val exception = MiddleWareFoundationError.missingParameter(Constants.Input.ACTION_TYPE)


        try {
            // Act
            params has Constants.Input.ACTION_TYPE
        } catch (error: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, error.code)
            Assert.assertEquals(exception.message, error.message)
        }
    }

    @Test
    fun `when has with invalid data then throw toInvalidParamError`() {
        // Arrange
        val exception = MiddleWareFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)

        val params = mapOf(Constants.Input.ACTION_TYPE to exception)

        try {
            // Act
            params has Constants.Input.ACTION_TYPE
        } catch (error: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, error.code)
            Assert.assertEquals(exception.message, error.message)
        }
    }

    @Test
    fun `when has with invalid key then throw toMissingParamError`() {
        // Arrange
        val exception = MiddleWareFoundationError.missingParameter(Constants.Input.ACTION_TYPE)

        val params = mapOf("" to "test")

        try {
            // Act
            params has Constants.Input.ACTION_TYPE
        } catch (error: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, error.code)
            Assert.assertEquals(exception.message, error.message)
        }
    }

    @Test
    fun `when has normal map then return the value`() {
        // Arrange
        val params = mapOf(Constants.Input.ACTION_TYPE to "test")

        // Act
        val result: String = params.has(Constants.Input.ACTION_TYPE)

        // Assert
        Assert.assertEquals("test", result)
    }
}