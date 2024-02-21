package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleDetailsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AddVehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.add.VehicleInput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class AddVehiclePsaExecutor(command: BaseCommand) :
    BasePsaExecutor<VehicleInput, VehicleOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): VehicleInput = VehicleInput(
        vin = parameters has Constants.PARAM_VIN,
        mileage = parameters hasOrNull Constants.PARAM_MILEAGE
    )

    override suspend fun execute(input: VehicleInput): NetworkResponse<VehicleOutput> {
        val request = request(
            AddVehicleResponse::class.java,
            arrayOf("/me/v1/user/vehicles/add"),
            body = input.toJson()
        )

        return communicationManager.post<AddVehicleResponse>(request, MiddlewareCommunicationManager.MymToken)
            .transform {
                val userInput = UserInput(
                    action = Action.Refresh,
                    vin = input.vin
                )
                GetVehicleDetailsPsaExecutor(middlewareComponent, params).execute(userInput)
            }
    }
}
