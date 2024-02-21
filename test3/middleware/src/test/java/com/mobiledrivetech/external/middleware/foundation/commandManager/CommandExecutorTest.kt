package com.mobiledrivetech.external.middleware.foundation.commandManager

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.util.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommandExecutorTest {

    private val workerCount = 3
    private lateinit var commandExecutor: CommandExecutor
    private val onGoingCommands: MutableCollection<Command> = mockk(relaxed = true)
    private val dispatcher: DispatcherProvider = mockk(relaxed = true)
    private val producerScope: ProducerScope<Command> = mockk(relaxed = true)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        commandExecutor = spyk(CommandExecutor(workerCount, dispatcher))
        commandExecutor.onGoingCommands = onGoingCommands
        commandExecutor.producerScope = producerScope
        mockkObject(MDLog)
        justRun { MDLog.debug(any(), any()) }
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when send then send the command`() = runTest {
        // Arrange
        val command = Command().apply {
            name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
            type = CommandType.Get
        }
        commandExecutor.onGoingCommands = mutableListOf()

        // Act
        commandExecutor.send(command)

        // Assert
        coVerify { producerScope.send(any()) }
    }

    @Test
    fun `when executeCommand then execute the command`() = runTest {
        // Arrange
        val command = Command().apply {
            name = CommandName("${Constants.API_PREFIX}.${Constants.API.TEST}")
            type = CommandType.Get
        }
        justRun { command.execute() }

        // Act
        commandExecutor.executeCommand(command)

        // Assert
        // Todo: here it's better to test more if possible
        verify { onGoingCommands.add(command) }
    }
}
