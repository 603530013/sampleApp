package com.mobiledrivetech.external.core.data.repository

import com.mobiledrivetech.external.core.data.datasource.TestDataSource
import com.mobiledrivetech.external.core.domain.repository.TestRepository

class TestRepositoryImpl(
    private val reUseDataSource: TestDataSource
) : TestRepository {
    override suspend fun initialize(parameter: Map<String, Any>?): Map<String, Any?> =
        reUseDataSource.initialize(parameter)

    override suspend fun getTestCommandResult(): Result<String> =
        reUseDataSource.getTestCommandResult()
}