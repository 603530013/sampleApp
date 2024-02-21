package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.VehicleListPsa
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.VehicleListPsa.VehiclePsa
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("we should switch to use the new class GetVehiclesPsaExecutor")
internal class GetVinsPsaExecutor(middlewareComponent: MiddlewareComponent, params: Map<String, Any?>?) :
    BasePsaExecutor<Unit, VehicleListPsa>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<VehicleListPsa> {
        val request = request(
            type = object : TypeToken<List<VehiclePsa>>() {}.type,
            urls = arrayOf("/me/v1/vehicles")
        )

        return communicationManager.get<List<VehiclePsa>>(request, MiddlewareCommunicationManager.MymToken)
            .map { VehicleListPsa(it) }
    }
}
