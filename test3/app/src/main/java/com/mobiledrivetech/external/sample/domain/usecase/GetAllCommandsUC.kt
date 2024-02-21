package com.mobiledrivetech.external.sample.domain.usecase

import com.mobiledrivetech.external.sample.domain.models.Commands
import com.mobiledrivetech.external.sample.domain.models.defaultCommandsList

/**
 * This use case is used to get all commands
 */
interface GetAllCommandsUC {
    operator fun invoke(): List<Commands>
}

internal class GetAllCommandsUCImpl() : GetAllCommandsUC {
    override fun invoke(): List<Commands> = defaultCommandsList
}
