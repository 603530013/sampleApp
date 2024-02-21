package com.mobiledrivetech.external.middleware.executor.test

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.executor.ExecutorTestHelper
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.Response
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError
import io.mockk.justRun
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetTestExecutorTest : ExecutorTestHelper() {
    private lateinit var getTestExecutor: GetTestExecutor

    override fun setup() {
        super.setup()
        getTestExecutor = GetTestExecutor(baseCommand)
        mockkObject(MDLog)
        justRun { MDLog.inform(any(), any(), any()) }
    }

    @Test
    fun `when params with ACTION_TYPE then return string`() {
        // Arrange
        val params = mapOf(Constants.Input.ACTION_TYPE to "test")

        // Act
        val result = getTestExecutor.params(params)

        // Assert
        Assert.assertEquals("test", result)
    }

    @Test
    fun `when params without ACTION_TYPE then throw missing parameter`() {
        // Arrange
        val params = emptyMap<String, String>()
        val exception = MiddleWareFoundationError.missingParameter(Constants.Input.ACTION_TYPE)

        try {
            // Act
            getTestExecutor.params(params)
        } catch (ex: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute then return response result`() = runTest {
        // Arrange
        val input = "test"
        val testMapInfo = mapOf(Constants.Output.TEST to input)

        // Act
        val result = getTestExecutor.execute(input)

        // Assert
        Assert.assertEquals(Response.Success(testMapInfo), result)
    }
}