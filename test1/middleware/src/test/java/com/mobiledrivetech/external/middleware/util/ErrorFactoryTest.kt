package com.mobiledrivetech.external.middleware.util

import com.mobiledrivetech.external.middleware.model.ErrorCode
import org.junit.Assert
import org.junit.Test

class ErrorFactoryTest {

    @Test
    fun `when ErrorMessage then get correct string`() {
        // Act
        val result = ErrorMessage.missingParams("test")
        // Assert
        Assert.assertEquals("Missing test parameter", result)

        // Act
        val result2 = ErrorMessage.invalidParams("test")
        // Assert
        Assert.assertEquals("Invalid test parameter", result2)

        // Act
        val result3 = ErrorMessage.paramsNotSet("test")
        // Assert
        Assert.assertEquals("test parameter not set", result3)

        // Act
        val result4 = ErrorMessage.componentNotConfigured("test")
        // Assert
        Assert.assertEquals("test not configured", result4)
    }

    @Test
    fun `when MiddleWareErrorFactory create then return MiddleWareError`() {
        // Arrange
        val code = ErrorCode.paramsNotSet
        val message = ErrorMessage.paramsNotSet("test")
        val subError = null

        // Act
        val result =
            MiddleWareErrorFactory.create(code = code, message = message, subError = subError)

        // Assert
        Assert.assertEquals(code, result.code)
        Assert.assertEquals(message, result.message)
        Assert.assertEquals(null, result.subError)
    }

    @Test
    fun `when MiddleWareErrorFactory create with info then return MiddleWareError`() {
        // Arrange
        val code = ErrorCode.unknownError
        val message = ErrorMessage.paramsNotSet("test")
        val subError = null
        val info = mapOf(Pair("test", "test"))

        // Act
        val result = MiddleWareErrorFactory.create(
            code = code,
            message = message,
            subError = subError,
            info = info
        )

        // Assert
        Assert.assertEquals(code, result.code)
        Assert.assertEquals(message, result.message)
        Assert.assertEquals(null, result.subError)
        Assert.assertEquals(info, result.info)
    }

    @Test
    fun `when MiddleWareFoundationError missingParameter then create error`() {
        //Act
        val result = MiddleWareFoundationError.missingParameter("test")

        //Assert
        Assert.assertEquals(ErrorCode.missingParams, result.code)
        Assert.assertEquals(ErrorMessage.missingParams("test"), result.message)
    }

    @Test
    fun `when MiddleWareFoundationError invalidParameter then create error`() {
        //Act
        val result = MiddleWareFoundationError.invalidParameter("test")

        //Assert
        Assert.assertEquals(ErrorCode.invalidParams, result.code)
        Assert.assertEquals(ErrorMessage.invalidParams("test"), result.message)
    }

    @Test
    fun `when MiddleWareFoundationError paramNotSet then create error`() {
        //Act
        val result = MiddleWareFoundationError.paramNotSet("test")

        //Assert
        Assert.assertEquals(ErrorCode.paramsNotSet, result.code)
        Assert.assertEquals(ErrorMessage.paramsNotSet("test"), result.message)
    }

    @Test
    fun `when MiddleWareFoundationError unknownError then create error`() {
        //Act
        val result = MiddleWareFoundationError.unknownError("test")

        //Assert
        Assert.assertEquals(ErrorCode.unknownError, result.code)
        Assert.assertEquals("test", result.message)
    }

    @Test
    fun `when MiddleWareFoundationError componentNotConfigured then create error`() {
        //Act
        val result = MiddleWareFoundationError.componentNotConfigured("test")

        //Assert
        Assert.assertEquals(ErrorCode.componentNotConfigured, result.code)
        Assert.assertEquals(ErrorMessage.componentNotConfigured("test"), result.message)
    }
}