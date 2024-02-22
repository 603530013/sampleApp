package com.mobiledrivetech.external.middleware.command.test

import com.mobiledrivetech.external.middleware.command.BaseBrandCommand
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.executor.test.GetTestExecutor

/**
 * Test command
 */
internal class TestCommandGet : BaseBrandCommand() {
    /**
     * Get executor for TestCommand
     *
     * @return the specific executor
     */
    override suspend fun getCommonExecutor(): BaseLocalExecutor<*, *> =
        GetTestExecutor(command = this)
}