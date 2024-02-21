package com.mobiledrivetech.external.sample.presentation.ui.models

import com.mobiledrivetech.external.sample.domain.models.Commands
import kotlinx.parcelize.RawValue

data class DisplayedCommand(
    val name: String,
    val type: String,
    val params: String,
    val command: @RawValue Commands
)
