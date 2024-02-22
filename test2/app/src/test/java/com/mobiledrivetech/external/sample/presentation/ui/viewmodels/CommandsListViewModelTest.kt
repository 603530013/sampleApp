package com.mobiledrivetech.external.sample.presentation.ui.viewmodels

import com.mobiledrivetech.external.middleware.model.ErrorCode
import com.mobiledrivetech.external.middleware.model.MiddleWareError
import com.mobiledrivetech.external.middleware.util.ErrorMessage
import com.mobiledrivetech.external.sample.domain.models.Commands
import com.mobiledrivetech.external.sample.domain.usecase.ExecuteCommandUC
import com.mobiledrivetech.external.sample.domain.usecase.GetAllCommandsUC
import com.mobiledrivetech.external.sample.domain.usecase.InitializeMiddlewareUC
import com.mobiledrivetech.external.sample.utils.BaseTestKoin
import com.mobiledrivetech.external.sample.utils.getValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@ExperimentalCoroutinesApi
class CommandsListViewModelTest : BaseTestKoin() {

    private val initializeMiddlewareUC: InitializeMiddlewareUC = mockk(relaxed = true)
    private val getAllCommandsUC: GetAllCommandsUC = mockk(relaxed = true)
    private val executeCommandUC: ExecuteCommandUC = mockk(relaxed = true)
    private lateinit var viewModel: CommandsListViewModel

    private val modules = module {
        factory { initializeMiddlewareUC }
        factory { getAllCommandsUC }
        factory { executeCommandUC }
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        koinTestRule.koin.loadModules(listOf(modules))
        viewModel = spyk(CommandsListViewModel())
    }

    @After
    fun tearDown() {
        koinTestRule.koin.unloadModules(listOf(modules))
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when onItemClick with success then update commandResult with success message`() {
        // Arrange
        val command = mockk<Commands>()
        val expect = mapOf("key" to "value")
        coJustRun { initializeMiddlewareUC.invoke() }
        coEvery { executeCommandUC(command = command) } returns Result.success(expect)

        // Act
        viewModel.onItemClick(command)

        // Assert
        coVerify { executeCommandUC(command = command) }
        Assert.assertEquals(expect.toString(), getValue(viewModel.commandResult))
    }

    @Test
    fun `when onItemClick with failure then update commandResult with failure message`() {
        // Arrange
        val command = mockk<Commands>()
        val middleWareError = MiddleWareError(
            ErrorCode.facadeNotInitialized,
            ErrorMessage.facadeNotInitialized
        )
        coJustRun { initializeMiddlewareUC.invoke() }
        coEvery { executeCommandUC(command = command) } returns Result.failure(middleWareError)

        // Act
        viewModel.onItemClick(command)

        // Assert
        coVerify { executeCommandUC(command = command) }
        Assert.assertEquals(middleWareError.message, getValue(viewModel.commandResult))
    }
}
