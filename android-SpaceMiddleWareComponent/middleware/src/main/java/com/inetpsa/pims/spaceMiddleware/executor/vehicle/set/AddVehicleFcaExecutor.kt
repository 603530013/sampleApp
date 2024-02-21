package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleVinNormalFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.add.VehicleInput
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class AddVehicleFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<VehicleInput, VehicleOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): VehicleInput = VehicleInput(
        vin = parameters has Constants.PARAM_VIN,
        connected = (parameters hasOrNull Constants.PARAM_CONNECTED) ?: false
    )

    override suspend fun execute(input: VehicleInput): NetworkResponse<VehicleOutput> {
        val executor = when (input.connected) {
            true -> AddVehicleConnectedFcaExecutor(middlewareComponent, params)
            else -> AddVehicleNoConnectedFcaExecutor(middlewareComponent, params)
        }

        return executor.execute().transform {
            val userInput = UserInput(action = Refresh, vin = input.vin)
            GetVehicleVinNormalFcaExecutor(middlewareComponent, params).execute(userInput)
        }
    }
}
