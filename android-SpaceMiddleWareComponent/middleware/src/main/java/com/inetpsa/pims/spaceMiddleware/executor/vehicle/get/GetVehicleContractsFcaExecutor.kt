package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.contracts.ContractsOutput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum

internal class GetVehicleContractsFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<UserInput, ContractsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = UserInput(
        action = parameters hasEnum Constants.Input.ACTION,
        vin = parameters has Constants.Input.VIN
    )

    override suspend fun execute(input: UserInput): NetworkResponse<ContractsOutput> {
        require(!input.vin.isNullOrBlank())
        if (input.action != Action.Get && input.action != Action.Refresh) {
            throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

        val vehicle = CachedVehicles.getOrThrow(middlewareComponent, input.vin, input.action)
        val contract = transformToContractsOutput(vehicle)
        return NetworkResponse.Success(contract)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToContractsOutput(vehicle: VehicleResponse): ContractsOutput =
        vehicle.services
            .orEmpty()
            .filter { it.vehicleCapable }
            .map {
                val status = if (it.serviceEnabled) {
                    ContractsOutput.Status.Active
                } else {
                    ContractsOutput.Status.Deactivated
                }
                ContractsOutput.BaseItem(code = it.service, status = status, title = it.service)
            }
            .let { ContractsOutput(it) }
}
