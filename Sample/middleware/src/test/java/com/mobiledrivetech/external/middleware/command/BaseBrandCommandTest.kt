package com.mobiledrivetech.external.middleware.command

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.manager.ConfigurationManager
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.Response
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseBrandCommandTest {
    private val middlewareComponent: MiddlewareComponent = mockk()
    private val configurationManager: ConfigurationManager = mockk()
    private lateinit var command: FakeCommand
    private lateinit var baseCommand: FakeBaseCommand

    @Before
    fun setup() {
        command = spyk(FakeCommand())
        baseCommand = spyk(FakeBaseCommand())
        every { middlewareComponent.configurationManager } returns configurationManager
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for executor then return TestCommonExecutor`() = runTest {
        // Act
        val executor = command.getExecutor()

        // Assert
        coVerify { command.getCommonExecutor() }
        Assert.assertTrue(executor is FakeCommand.TestCommonExecutor)
    }

    @Test
    fun `when getCommonExecutor with empty parameter then throw invalidParameter error`() =
        runTest {
            // Arrange
            every { baseCommand.parameters } returns mapOf()

            // Act
            try {
                baseCommand.getCommonExecutor()
            } catch (ex: MiddleWareError) {
                // Assert
                Assert.assertEquals(
                    MiddleWareFoundationError.invalidParameter(Constants.Input.ACTION_TYPE).code,
                    ex.code
                )
                Assert.assertEquals(
                    MiddleWareFoundationError.invalidParameter(Constants.Input.ACTION_TYPE).message,
                    ex.message
                )

            }
        }

    private class FakeCommand : BaseBrandCommand() {
        override suspend fun getCommonExecutor(): BaseLocalExecutor<*, *> = TestCommonExecutor()

        inner class TestCommonExecutor : BaseLocalExecutor<Unit, String>(this) {
            override fun params(parameters: Map<String, Any?>?) = Unit
            override suspend fun execute(input: Unit): Response<String> = Response.Success("Test")
        }
    }

    private class FakeBaseCommand : BaseBrandCommand() {
        override suspend fun getCommonExecutor(): BaseLocalExecutor<*, *> =
            throw MiddleWareFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }
}