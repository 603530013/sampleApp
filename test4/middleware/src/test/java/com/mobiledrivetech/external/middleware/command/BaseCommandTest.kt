package com.mobiledrivetech.external.middleware.command

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericComponentInterface
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.model.ErrorCode
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.model.Response
import com.mobiledrivetech.external.middleware.util.ErrorMessage
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError
import com.mobiledrivetech.external.middleware.utils.MockWeakReference
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference

@OptIn(ExperimentalCoroutinesApi::class)
class BaseCommandTest {
    private val command = spyk(TestCommand())
    private val command2 = spyk(TestCommand2())
    private val command3 = spyk(TestCommand3())
    private val middlewareComponent: MiddlewareComponent = mockk(relaxed = true)

    @Before
    fun setup() {
        mockkObject(MDLog)
        justRun { MDLog.debug(any()) }
        justRun { MDLog.warning(any()) }
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when actionType is missing then throw MiddleWareError missing parameter`() {
        // Arrange
        every { command.parameters } returns mapOf()
        val exception = MiddleWareFoundationError.missingParameter(Constants.Input.ACTION_TYPE)

        try {
            // Act
            command.actionType
        } catch (ex: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when actionType is invalid then throw MiddleWareError invalid parameter`() {
        // Arrange
        every { command.parameters } returns mapOf(Constants.Input.ACTION_TYPE to 123)
        val exception = MiddleWareFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)

        try {
            // Act
            command.actionType
        } catch (ex: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when middlewareComponent is null then throw MiddleWareError component not configured`() {
        // Arrange
        every { command.componentReference } returns null
        val exception = MiddleWareFoundationError.componentNotConfigured(Constants.COMPONENT_NAME)

        try {
            // Act
            command.middlewareComponent

        } catch (ex: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when component is different from MiddlewareComponent then throw MiddleWareError component not configured`() {
        // Arrange
        val weakRef: WeakReference<GenericComponentInterface?> = MockWeakReference(mockk())
        every { command.componentReference } returns weakRef
        val exception = MiddleWareFoundationError.componentNotConfigured(Constants.COMPONENT_NAME)

        try {
            // Act
            command.middlewareComponent
        } catch (ex: MiddleWareError) {
            // Assert
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with success result then success execute`() = runTest {
        // Arrange
        val callback: () -> Unit = mockk(relaxed = true)
        every { command.middlewareComponent } returns middlewareComponent

        // Act
        command.execute { callback() }

        // Assert
        coVerify { command.getExecutor() }
        verify { command.success(emptyMap()) }
        verify(exactly = 0) { command.failure(any()) }
        verify { callback() }
    }

    @Test
    fun `when execute with special result then success execute`() = runTest {
        // Arrange
        val callback: () -> Unit = mockk(relaxed = true)
        every { command2.middlewareComponent } returns middlewareComponent

        // Act
        command2.execute { callback() }

        // Assert
        coVerify { command2.getExecutor() }
        verify { command2.success(mapOf(Constants.Output.TEST to "Test")) }
        verify { callback() }
    }

    @Test
    fun `when execute with failure result then failure execute`() = runTest {
        // Arrange
        val callback: () -> Unit = mockk(relaxed = true)
        every { command3.middlewareComponent } returns middlewareComponent

        // Act
        command3.execute { callback() }

        // Assert
        coVerify { command3.getExecutor() }
        verify(exactly = 0) { command3.success(any()) }
        verify { command3.failure(any()) }
        verify { callback() }
    }

    @Test
    fun `when execute with MiddleWareError then failure execute`() = runTest {
        // Arrange
        val callback: () -> Unit = mockk(relaxed = true)

        // Act
        command.execute { callback() }

        // Assert
        coVerify { command.getExecutor() }
        verify(exactly = 0) { command.success(any()) }
        verify { command.failure(any()) }
        verify { callback() }
    }

    private class TestCommand : BaseCommand() {

        override suspend fun getExecutor(): BaseLocalExecutor<*, *> = TestExecutor()

        private inner class TestExecutor : BaseLocalExecutor<Unit, Unit>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): Response<Unit> = Response.Success(Unit)
        }
    }

    private class TestCommand2 : BaseCommand() {

        override suspend fun getExecutor(): BaseLocalExecutor<*, *> = TestExecutor2()

        private inner class TestExecutor2 : BaseLocalExecutor<String, Map<String, String>>(this) {

            override fun params(parameters: Map<String, Any?>?) = "Test"

            override suspend fun execute(input: String): Response<Map<String, String>> {
                val testMapInfo = mapOf(Constants.Output.TEST to input)

                return Response.Success(testMapInfo)
            }
        }
    }

    private class TestCommand3 : BaseCommand() {

        override suspend fun getExecutor(): BaseLocalExecutor<*, *> = TestExecutor3()

        private inner class TestExecutor3 : BaseLocalExecutor<Unit, Unit>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): Response<Unit> = Response.Failure(
                error = MiddleWareError(
                    ErrorCode.facadeNotInitialized,
                    ErrorMessage.facadeNotInitialized
                )
            )
        }
    }
}