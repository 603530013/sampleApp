package com.mobiledrivetech.external.sample.domain.repository

import com.mobiledrivetech.external.sample.domain.models.Commands

interface TestRepository {
    suspend fun initialize(parameter: Map<String, Any>?): Map<String, Any?>
    suspend fun getTestCommandResult(): Result<String>
    suspend fun executeCommand(command: Commands): Result<Map<String, Any?>>
}
