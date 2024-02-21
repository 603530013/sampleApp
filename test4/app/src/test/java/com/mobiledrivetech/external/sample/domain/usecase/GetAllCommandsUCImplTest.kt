package com.mobiledrivetech.external.sample.domain.usecase

import com.mobiledrivetech.external.sample.domain.models.Commands
import io.mockk.clearAllMocks
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAllCommandsUCImplTest {

    private lateinit var getAllCommandsUCImpl: GetAllCommandsUCImpl

    @Before
    fun setUp() {
        getAllCommandsUCImpl = spyk(GetAllCommandsUCImpl())
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when call invoke`() {
        // Arrange
        val expect = listOf(Commands.TestCommand)

        // Act
        val result = getAllCommandsUCImpl()

        // Assert
        Assert.assertEquals(expect, result)
    }
}
