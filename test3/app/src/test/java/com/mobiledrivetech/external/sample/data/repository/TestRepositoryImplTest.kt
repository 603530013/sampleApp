package com.mobiledrivetech.external.sample.data.repository

import com.mobiledrivetech.external.sample.data.datasource.TestDataSource
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
class TestRepositoryImplTest {
    private val reUseDataSource: TestDataSource = mockk()
    private lateinit var testRepositoryImpl: TestRepositoryImpl

    @Before
    fun setUp() {
        testRepositoryImpl = spyk(TestRepositoryImpl(reUseDataSource))
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when call initialize`() = runTest {
        // Arrange
        val params = mapOf("key" to "value")
        val expect: Map<String, Any?> = mockk()
        coEvery { reUseDataSource.initialize(any()) } returns expect

        // Act
        val result = testRepositoryImpl.initialize(params)

        // Assert
        coVerify { reUseDataSource.initialize(params) }
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when call getTestCommandResult`() = runTest {
        // Arrange
        val expect: Result<String> = mockk()
        coEvery { reUseDataSource.getTestCommandResult() } returns expect

        // Act
        val result = testRepositoryImpl.getTestCommandResult()

        // Assert
        coVerify { reUseDataSource.getTestCommandResult() }
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when call executeCommand`() = runTest {
        // Arrange
        val expect: Result<Map<String, Any?>> = mockk()
        coEvery { reUseDataSource.executeCommand(any()) } returns expect

        // Act
        val result = testRepositoryImpl.executeCommand(mockk())

        // Assert
        coVerify { reUseDataSource.executeCommand(any()) }
        Assert.assertEquals(expect, result)
    }
}
