package com.mobiledrivetech.external.middleware.extensions

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.SubError
import io.mockk.justRun
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Test

class ErrorsExtensionsTest {

    @Test
    fun `when asMap with middleWareError then return result as map`() {
        // Arrange
        val subError = SubError(
            status = 2005,
            body = "body"
        )
        val middleWareError = MiddleWareError(
            code = 2005,
            message = "message",
            subError = subError,
            info = mapOf(Constants.KEY_TRANSACTION_ID to "testId")
        )
        mockkObject(MDLog)
        justRun { MDLog.debug(any()) }

        val expect = mapOf(
            Constants.PARAMS_KEY_CODE to 2005,
            Constants.PARAMS_KEY_LABEL to "message",
            Constants.PARAMS_KEY_SUB_CODE to mapOf(
                Constants.PARAMS_KEY_CODE to 2005,
                Constants.PARAMS_KEY_LABEL to "body"
            ),
            Constants.PARAMS_KEY_INFO to mapOf(Constants.KEY_TRANSACTION_ID to "testId")
        )

        // Act
        val result = middleWareError.asMap()

        // Assert
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when asMap with null then return result empty map`() {
        // Arrange
        val middleWareError: MiddleWareError? = null
        val expect: Map<String, Any> = mapOf()
        mockkObject(MDLog)
        justRun { MDLog.debug(any()) }

        // Act
        val result = middleWareError.asMap()

        // Assert
        Assert.assertEquals(expect, result)
    }
}