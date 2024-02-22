package com.mobiledrivetech.external.sample.domain.usecase

import com.mobiledrivetech.external.sample.domain.repository.TestRepository

/**
 * This use case is used as an example to execute a command
 */
interface GetTestCommandResultUC {
    suspend operator fun invoke(): Result<String>
}

internal class GetTestCommandResultUCImpl(
    private val testRepository: TestRepository
) : GetTestCommandResultUC {
    override suspend fun invoke(): Result<String> =
        testRepository.getTestCommandResult()
}
