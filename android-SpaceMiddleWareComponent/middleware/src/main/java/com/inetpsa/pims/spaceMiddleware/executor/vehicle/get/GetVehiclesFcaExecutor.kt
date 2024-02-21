package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehiclesOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleImageFcaInput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.unwrapNullable

internal class GetVehiclesFcaExecutor(command: BaseCommand) : BaseFcaExecutor<UserInput, VehiclesOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = UserInput(
        action = parameters hasEnum Constants.Input.ACTION,
        vin = null
    )

    override suspend fun execute(input: UserInput): NetworkResponse<VehiclesOutput> {
        if (input.action == Action.Get) {
            readFromCache()
                ?.let { vehicles -> readFromJson(vehicles) }
                ?.let { return NetworkResponse.Success(transformToVehicleOutput(it)) }
        }

        if (input.action == Action.Refresh || input.action == Action.Get) {
            return GetVehiclesResponseFcaExecutor(middlewareComponent, params).execute()
                .map { fetchImages(it) }
                .map { transformToVehicleOutput(it) }
        }

        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(): String? =
        middlewareComponent
            .readSync<String>(Constants.Storage.VEHICLES, StoreMode.APPLICATION)
            .takeIf { !it.isNullOrBlank() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromJson(vehicles: String?): VehiclesResponse? =
        try {
            Gson().fromJson(vehicles, VehiclesResponse::class.java)
        } catch (ex: JsonParseException) {
            PIMSLogger.w(ex)
            null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToVehicleOutput(response: VehiclesResponse): VehiclesOutput {
        val vehiclesOutput = response.vehicles.map {
            VehiclesOutput.Vehicle(
                vin = it.vin,
                shortLabel = it.nickname,
                modelDescription = it.modelDescription,
                picture = it.imageUrl
            )
        }
        return VehiclesOutput(vehiclesOutput)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun fetchImages(vehicles: VehiclesResponse): VehiclesResponse {
        vehicles.vehicles.forEach {
            it.imageUrl = GetVehicleImageFcaExecutor(middlewareComponent, emptyMap())
                .execute(VehicleImageFcaInput(it.vin))
                .unwrapNullable()?.imageUrl
        }
        return vehicles
    }
}
