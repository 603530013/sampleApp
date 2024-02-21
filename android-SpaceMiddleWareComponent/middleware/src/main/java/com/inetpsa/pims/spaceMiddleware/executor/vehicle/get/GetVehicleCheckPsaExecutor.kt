package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.CheckVehicleItemResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetVehicleCheckPsaExecutor(command: BaseCommand) : BasePsaExecutor<String, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<Unit> {
        val type = TypeToken.getParameterized(List::class.java, CheckVehicleItemResponse::class.java).type
        val request = request(type, arrayOf("/me/v1/vehicles"))
        return communicationManager.get<List<CheckVehicleItemResponse>>(
            request,
            MiddlewareCommunicationManager.MymToken
        ).transform { response ->
            when (response.firstOrNull { it.vin.equals(input, true) }) {
                null -> NetworkResponse.Success(Unit)
                else -> NetworkResponse.Failure(PimsErrors.alreadyExist(Constants.PARAM_VIN))
            }
        }
    }
}
