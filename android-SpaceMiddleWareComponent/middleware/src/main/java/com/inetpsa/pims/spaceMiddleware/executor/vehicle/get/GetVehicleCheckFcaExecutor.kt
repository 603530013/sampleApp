package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetVehicleCheckFcaExecutor(command: BaseCommand) : BaseFcaExecutor<String, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): String =
        parameters has Constants.Input.VIN

    override suspend fun execute(input: String): NetworkResponse<Unit> {
        return GetVehiclesResponseFcaExecutor(middlewareComponent, params).execute(input)
            .transform { response ->
                when (response.vehicles.firstOrNull { it.vin.equals(input, true) }) {
                    null -> NetworkResponse.Success(Unit)
                    else -> NetworkResponse.Failure(PimsErrors.alreadyExist(Constants.PARAM_VIN))
                }
            }
    }
}
