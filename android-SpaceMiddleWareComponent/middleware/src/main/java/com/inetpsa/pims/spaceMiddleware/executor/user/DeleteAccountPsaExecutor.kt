package com.inetpsa.pims.spaceMiddleware.executor.user

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager

internal class DeleteAccountPsaExecutor(command: BaseCommand) : BasePsaExecutor<Unit, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Unit> {
        val request = request(
            type = Unit::class.java,
            arrayOf("/me/v1/user_data/unsubscribe"),
            body = "{}"
        )
        return communicationManager.update(request, MiddlewareCommunicationManager.MymToken)
    }
}
