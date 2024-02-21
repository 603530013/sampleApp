package com.mobiledrivetech.external.sample.presentation.ui.mapper

import com.mobiledrivetech.external.sample.domain.models.Commands
import org.junit.Assert
import org.junit.Test

class DisplayedCommandMapperTest {

    @Test
    fun `when toDisplayedCommand then return a DisplayedCommand`() {
        // Arrange
        val command = Commands.TestCommand

        // Act
        val displayedCommand = command.toDisplayedCommand()

        // Assert
        Assert.assertEquals(command.commandName.name, displayedCommand.name)
        Assert.assertEquals(command.commandType.name, displayedCommand.type)
        Assert.assertEquals(command.commandParams.toString(), displayedCommand.params)
        Assert.assertEquals(command, displayedCommand.command)
    }
}
