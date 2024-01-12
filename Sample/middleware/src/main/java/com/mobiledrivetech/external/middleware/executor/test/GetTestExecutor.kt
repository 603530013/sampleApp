package com.mobiledrivetech.external.middleware.executor.test

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.command.BaseCommand
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.extensions.has
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.model.Response

internal class GetTestExecutor(command: BaseCommand) : BaseLocalExecutor<String, Unit>(command) {
    override fun params(parameters: Map<String, Any?>?): String =
        parameters has Constants.Input.ACTION_TYPE

    override suspend fun execute(input: String): Response<Unit> {
        MDLog.debug("GetTestExecutor execute input: $input")
        return Response.Success(Unit)
    }

}