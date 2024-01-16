package com.mobiledrivetech.external.core.domain.usecase

import com.mobiledrivetech.external.core.domain.repository.TestRepository

interface InitializeMiddlewareUC {
    suspend operator fun invoke(): Map<String, Any?>
}

internal class InitializeMiddlewareUCImpl(
    private val testRepository: TestRepository
) : InitializeMiddlewareUC {
    override suspend fun invoke(): Map<String, Any?> = testRepository.initialize(emptyMap())
}