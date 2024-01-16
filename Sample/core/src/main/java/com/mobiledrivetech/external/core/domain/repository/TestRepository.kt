package com.mobiledrivetech.external.core.domain.repository

interface TestRepository {
    suspend fun initialize(parameter: Map<String, Any>?): Map<String, Any?>
    suspend fun getTestCommandResult(): Result<String>
}