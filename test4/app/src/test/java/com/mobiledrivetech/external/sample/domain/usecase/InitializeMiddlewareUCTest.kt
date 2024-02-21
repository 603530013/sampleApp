package com.mobiledrivetech.external.sample.domain.usecase

import com.mobiledrivetech.external.sample.domain.repository.TestRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
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
class InitializeMiddlewareUCTest {

    private val testRepository: TestRepository = mockk()
    private lateinit var initializeMiddlewareUCImpl: InitializeMiddlewareUCImpl

    @Before
    fun setUp() {
        initializeMiddlewareUCImpl = spyk(InitializeMiddlewareUCImpl(testRepository))
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when call invoke`() = runTest {
        // Arrange
        val expect: Map<String, Any?> = mockk()
        coEvery { testRepository.initialize(any()) } returns expect

        // Act
        val result = initializeMiddlewareUCImpl()

        // Assert
        coVerify { testRepository.initialize(emptyMap()) }
        Assert.assertEquals(expect, result)
    }
}
