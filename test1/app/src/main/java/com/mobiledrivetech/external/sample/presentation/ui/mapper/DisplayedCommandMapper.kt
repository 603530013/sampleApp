package com.mobiledrivetech.external.sample.presentation.ui.mapper

import com.mobiledrivetech.external.sample.domain.models.Commands
import com.mobiledrivetech.external.sample.presentation.ui.models.DisplayedCommand

fun Commands.toDisplayedCommand() = DisplayedCommand(
    name = this.commandName.name,
    type = this.commandType.name,
    params = this.commandParams.toString(),
    command = this
)
