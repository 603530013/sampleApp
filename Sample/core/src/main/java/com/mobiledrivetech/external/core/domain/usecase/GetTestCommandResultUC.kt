package com.mobiledrivetech.external.core.domain.usecase

import com.mobiledrivetech.external.core.domain.repository.TestRepository

interface GetTestCommandResultUC {
    suspend operator fun invoke(): Result<String>
}

internal class GetTestCommandResultUCImpl(
    private val testRepository: TestRepository
) : GetTestCommandResultUC {
    override suspend fun invoke(): Result<String> =
        testRepository.getTestCommandResult()
}