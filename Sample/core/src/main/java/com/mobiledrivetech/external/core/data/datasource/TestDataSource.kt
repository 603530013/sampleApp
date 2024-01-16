package com.mobiledrivetech.external.core.data.datasource

interface TestDataSource {
    /**
     * Initialize the middleware before executing other commands
     * @param parameter
     * @return
     */
    suspend fun initialize(parameter: Map<String, Any>?): Map<String, Any?>
    suspend fun getTestCommandResult(): Result<String>
}