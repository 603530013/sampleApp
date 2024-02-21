package com.mobiledrivetech.external.sample.data.datasource

import com.mobiledrivetech.external.sample.domain.models.Commands

interface TestDataSource {
    /**
     * Initialize the middleware before executing other commands
     * @param parameter
     * @return
     */
    suspend fun initialize(parameter: Map<String, Any>?): Map<String, Any?>

    /**
     * Get the result of the test command
     * @return
     */
    suspend fun getTestCommandResult(): Result<String>

    /**
     * Execute the command
     * @param command
     * @return
     */
    suspend fun executeCommand(command: Commands): Result<Map<String, Any?>>
}
