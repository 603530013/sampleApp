package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.VehiclePsaParam
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.Vehicles
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.unwrap

@Deprecated("try to switch to use this class GetVehiclesPsaExecutor")
internal class GetVehiclesPsaExecutor(command: BaseCommand) : BasePsaExecutor<Unit, Vehicles>(command) {

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Vehicles> =
        GetVinsPsaExecutor(middlewareComponent, params).execute()
            .map { vehicles ->
                vehicles.vehicleList.mapNotNull { it.vin }
                    .mapIndexed { index, vin ->
                        val param = VehiclePsaParam(vin, index == 0)
                        GetVehicleDetailsPsaExecutor(middlewareComponent, params).execute(param).unwrap()
                    }.let { Vehicles(it) }
            }
}
