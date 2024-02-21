package com.mobiledrivetech.external.middleware.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.SubError
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Type

class StringExtensionsTest {

    private val gsonBuilder: GsonBuilder = mockk()
    private val gson = mockk<Gson>()

    @Before
    fun setUp() {
        mockkStatic(GsonBuilder::class)
        every { gsonBuilder.create() } returns gson
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when hide then return hidden string`() {
        // Arrange
        val expect = "12345★★★★★"
        val input = "1234567890"

        // Act
        val result = input.hide()

        // Assert
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when asJson with string then return the same string`() {
        // Arrange
        val input = "1234567890"

        // Act
        val result = input.asJson()

        // Assert
        Assert.assertEquals(input, result)
    }

    @Test
    fun `when asJson with object then return json string`() {
        // Arrange
        val input = SubError(status = 2005, body = "body")
        val expect = "{\"status\":2005,\"body\":\"body\"}"

        // Act
        val result = input.asJson()

        // Assert
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when asJson with an exception then throw error`() {
        // Arrange
        val input = mockk<SubError>(relaxed = true)
        every { gson.toJson(any(), any<Type>()) } answers {
            throw Exception("test")
        }
        try {
            // Act
            input.asJson()
        } catch (error: MiddleWareError) {
            // Assert
            Assert.assertEquals(MiddleWareFoundationError.unknownError, error)
        }
    }

    @Test
    fun `when asMap with null then return empty map`() {
        // Arrange
        val input: String? = null
        val expect = mapOf<String, Any?>()

        // Act
        val result = input.asMap()

        // Assert
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when asMap with empty string then return empty map`() {
        // Arrange
        val input = ""
        val expect = mapOf<String, Any?>()

        // Act
        val result = input.asMap()

        // Assert
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when asMap with invalid json string then return empty map`() {
        // Arrange
        val input = "invalid json string"
        val expect = mapOf<String, Any?>()

        // Act
        val result = input.asMap()

        // Assert
        Assert.assertEquals(expect, result)
    }
}