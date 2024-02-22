package com.mobiledrivetech.external.sample.domain.usecase

import com.mobiledrivetech.external.sample.domain.models.Commands
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
class ExecuteCommandUCImplTest {
    private val testRepository: TestRepository = mockk()
    private lateinit var executeCommandUCImpl: ExecuteCommandUCImpl

    @Before
    fun setUp() {
        executeCommandUCImpl = spyk(ExecuteCommandUCImpl(testRepository))
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when call invoke`() = runTest {
        // Arrange
        val command = Commands.TestCommand
        val expect = Result.success(mapOf<String, Any?>())
        coEvery { testRepository.executeCommand(any()) } returns expect

        // Act
        val result = executeCommandUCImpl(command)

        // Assert
        coVerify { testRepository.executeCommand(command) }
        Assert.assertEquals(expect, result)
    }
}
