package com.inetpsa.pims.spaceMiddleware.executor.account

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor

@Deprecated("This class should be replaced with the new GetProfileFcaExecutor")
internal class GetProfileFcaExecutor(command: BaseCommand) : BaseFcaExecutor<Unit, String>(command) {

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<String> {
        val response =
            middlewareComponent.dataManager.read(Constants.STORAGE_KEY_PROFILE, StoreMode.APPLICATION) as? String
        return when (response.isNullOrEmpty()) {
            true -> NetworkResponse.Failure(PIMSFoundationError.invalidReturnParam(Constants.STORAGE_KEY_PROFILE))
            else -> NetworkResponse.Success(response)
        }
    }
}
