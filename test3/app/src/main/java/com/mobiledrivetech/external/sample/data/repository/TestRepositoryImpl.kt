package com.mobiledrivetech.external.sample.data.repository

import com.mobiledrivetech.external.sample.data.datasource.TestDataSource
import com.mobiledrivetech.external.sample.domain.models.Commands
import com.mobiledrivetech.external.sample.domain.repository.TestRepository

class TestRepositoryImpl(
    private val reUseDataSource: TestDataSource
) : TestRepository {
    override suspend fun initialize(parameter: Map<String, Any>?): Map<String, Any?> =
        reUseDataSource.initialize(parameter)

    override suspend fun getTestCommandResult(): Result<String> =
        reUseDataSource.getTestCommandResult()

    override suspend fun executeCommand(command: Commands): Result<Map<String, Any?>> =
        reUseDataSource.executeCommand(command = command)
}
