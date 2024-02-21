package com.inetpsa.pims.spaceMiddleware.command.configuration

import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.configuration.SetConfigurationExecutor
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

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
    fun `when look for executor then return SetConfigurationExecutor`() {
        runTest {
            val executor = command.getExecutor()
            assertEquals(true, executor is SetConfigurationExecutor)
        }
    }
}
