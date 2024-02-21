package com.mobiledrivetech.external.middleware.command.test

import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.executor.test.GetTestExecutor
import io.mockk.clearAllMocks
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
class TestCommandGetTest {
    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: TestCommandGet

    @Before
    fun setup() {
        command = spyk(TestCommandGet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for executor then return TestCommandGet`() = runTest {
        // Act
        val executor = command.getExecutor()

        // Assert
        Assert.assertEquals(true, executor is GetTestExecutor)
    }
}