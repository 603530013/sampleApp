package com.mobiledrivetech.external.sample.domain.usecase

import com.mobiledrivetech.external.sample.domain.repository.TestRepository

interface InitializeMiddlewareUC {
    suspend operator fun invoke(): Map<String, Any?>
}

internal class InitializeMiddlewareUCImpl(
    private val testRepository: TestRepository
) : InitializeMiddlewareUC {
    override suspend fun invoke(): Map<String, Any?> = testRepository.initialize(emptyMap())
}