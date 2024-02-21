package com.inetpsa.pims.spaceMiddleware.command.features

import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.features.get.GetFeaturesFcaExecutor
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FeaturesCommandGetTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: FeaturesCommandGet

    @Before
    fun setup() {
        command = spyk(FeaturesCommandGet())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for FCA executor then return GetFeaturesFcaExecutor`() {
        runTest {
            val executor = command.getFcaExecutor()
            Assert.assertEquals(true, executor is GetFeaturesFcaExecutor)
        }
    }
}
