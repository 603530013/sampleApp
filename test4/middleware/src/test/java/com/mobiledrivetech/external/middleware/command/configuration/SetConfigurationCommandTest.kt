package com.mobiledrivetech.external.middleware.command.configuration

import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.executor.configuration.SetConfigurationExecutor
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
class SetConfigurationCommandTest {
    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: SetConfigurationCommand

    @Before
    fun setup() {
        command = spyk(SetConfigurationCommand())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for executor then return SetConfigurationExecutor`() = runTest {
        // Act
        val executor = command.getExecutor()

        // Assert
        Assert.assertTrue(executor is SetConfigurationExecutor)
    }
}