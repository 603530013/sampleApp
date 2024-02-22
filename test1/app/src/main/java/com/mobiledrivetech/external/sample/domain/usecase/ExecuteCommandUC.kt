package com.mobiledrivetech.external.sample.domain.usecase

import com.mobiledrivetech.external.sample.domain.models.Commands
import com.mobiledrivetech.external.sample.domain.repository.TestRepository

/**
 * This use case is just for testing a command
 */
interface ExecuteCommandUC {
    suspend operator fun invoke(command: Commands): Result<Map<String, Any?>>
}

internal class ExecuteCommandUCImpl(
    private val testRepository: TestRepository
) : ExecuteCommandUC {
    override suspend fun invoke(command: Commands): Result<Map<String, Any?>> =
        testRepository.executeCommand(command = command)
}
