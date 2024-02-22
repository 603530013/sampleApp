package com.mobiledrivetech.external.middleware.extensions

import com.mobiledrivetech.external.middleware.model.ErrorCode
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.Response
import com.mobiledrivetech.external.middleware.util.ErrorMessage
import org.junit.Assert
import org.junit.Test

class ResponseExtensionsTest {

    @Test
    fun `when handleResult with success then actionSuccess`() {
        // Arrange
        var testSting = "test"
        val actionSuccess: (Any?) -> Unit = { testSting = "success" }
        val actionFailure: (MiddleWareError?) -> Unit = { testSting = "failure" }
        val response = Response.Success("test")

        // Act
        response.handleResult(actionSuccess = actionSuccess, actionFailure = actionFailure)

        // Assert
        Assert.assertEquals("success", testSting)
    }

    @Test
    fun `when handleResult with Failure then actionFailure`() {
        // Arrange
        var testSting = "test"
        val actionSuccess: (Any?) -> Unit = { testSting = "success" }
        val actionFailure: (MiddleWareError?) -> Unit = { testSting = "failure" }
        val response = Response.Failure(
            error = MiddleWareError(
                ErrorCode.facadeNotInitialized,
                ErrorMessage.facadeNotInitialized
            )
        )

        // Act
        response.handleResult(actionSuccess = actionSuccess, actionFailure = actionFailure)

        // Assert
        Assert.assertEquals("failure", testSting)
    }

    @Test
    fun `when unwrap with success then return response`() {
        // Arrange
        val expect = "test"
        val response = Response.Success(expect)

        // Act
        val result = response.unwrap()

        // Assert
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when unwrap with Failure then throw error`() {
        // Arrange
        val middleWareError = MiddleWareError(
            ErrorCode.facadeNotInitialized,
            ErrorMessage.facadeNotInitialized
        )
        val response = Response.Failure(
            error = middleWareError
        )

        try {
            // Act
            response.unwrap()
        } catch (error: MiddleWareError) {
            // Assert
            Assert.assertEquals(middleWareError, error)
        }
    }

    @Test
    fun `when unwrapNullable with success then return response`() {
        // Arrange
        val expect = "test"
        val response = Response.Success(expect)

        // Act
        val result = response.unwrapNullable()
        // Assert
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when unwrapNullable with Failure then return null`() {
        // Arrange
        val middleWareError = MiddleWareError(
            ErrorCode.facadeNotInitialized,
            ErrorMessage.facadeNotInitialized
        )
        val response = Response.Failure(
            error = middleWareError
        )

        // Act
        val result = response.unwrapNullable()

        // Assert
        Assert.assertEquals(null, result)
    }

    @Test
    fun `when map with success then return transformed response`() {
        // Arrange
        val expect = "test"
        val response = Response.Success(expect)
        val transform: (String) -> Int = { it.length }

        // Act
        val result = response.map { transform(it) }

        // Assert
        Assert.assertEquals(Response.Success(expect.length), result)
    }

    @Test
    fun `when map with Failure then return response`() {
        // Arrange
        val middleWareError = MiddleWareError(
            ErrorCode.facadeNotInitialized,
            ErrorMessage.facadeNotInitialized
        )
        val response = Response.Failure(
            error = middleWareError
        )
        val transform: (String) -> Int = { it.length }

        // Act
        val result = response.map(transform)

        // Assert
        Assert.assertEquals(response, result)
    }

    @Test
    fun `when ifSuccess success then return response`() {
        // Arrange
        var expect = "test"
        val response = Response.Success(expect)
        val transform: (String) -> Unit = { expect = "success" }

        // Act
        val result = response.ifSuccess(transform = transform)

        // Assert
        Assert.assertEquals("success", expect)
        Assert.assertEquals(response, result)
    }

    @Test
    fun `when ifSuccess with Failure then return response`() {
        // Arrange
        var expect = "test"
        val middleWareError = MiddleWareError(
            ErrorCode.unknownError, ErrorMessage.unknownError
        )
        val response = Response.Failure(
            error = middleWareError
        )
        val transform: (String) -> Unit = { expect = "success" }

        // Act
        val result = response.ifSuccess(transform)

        // Assert
        Assert.assertEquals("test", expect)
        Assert.assertEquals(response, result)
    }

    @Test
    fun `when map with transformSuccess and then return transformed response`() {
        // Arrange
        val expect = "test"
        val response = Response.Success(expect)
        val transformSuccess: (String) -> Int = { it.length }
        val transformFailure: (MiddleWareError?) -> Response<Int>? = { null }

        // Act
        val result = response.map(transformSuccess, transformFailure)

        // Assert
        Assert.assertEquals(Response.Success(expect.length), result)
    }

    @Test
    fun `when map with transformFailure and then return transformed response`() {
        // Arrange
        var testString = "test"
        val transformSuccess: (String) -> Int = {
            testString = "success"
            testString.length
        }
        val transformFailure: (MiddleWareError?) -> Response<MiddleWareError> = {
            testString = "failure"
            Response.Failure(it)
        }
        val error = MiddleWareError(
            ErrorCode.facadeNotInitialized,
            ErrorMessage.facadeNotInitialized
        )
        val response = Response.Failure(error = error)
        // Act
        val result =
            response.map(transformSuccess = transformSuccess, transformFailure = transformFailure)

        // Assert
        Assert.assertEquals(Response.Failure(error), result)
    }
}