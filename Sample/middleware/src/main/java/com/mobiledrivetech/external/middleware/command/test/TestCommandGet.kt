package com.mobiledrivetech.external.middleware.command.test

import com.mobiledrivetech.external.middleware.command.BaseBrandCommand
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.executor.test.GetTestExecutor

internal class TestCommandGet : BaseBrandCommand() {
    override suspend fun getCommonExecutor(): BaseLocalExecutor<*, *> =
        GetTestExecutor(command = this)
}