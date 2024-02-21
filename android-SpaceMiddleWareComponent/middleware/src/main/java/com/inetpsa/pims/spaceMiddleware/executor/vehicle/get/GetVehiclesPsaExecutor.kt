package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.GetUserPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleListResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehiclesOutput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetVehiclesPsaExecutor(command: BaseCommand) : BasePsaExecutor<UserInput, VehiclesOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) =
        UserInput(
            action = parameters hasEnum Constants.Input.ACTION,
            vin = parameters hasOrNull Constants.Input.VIN
        )

    override suspend fun execute(input: UserInput): NetworkResponse<VehiclesOutput> {
        if (input.action == Action.Get) {
            readFromCache()
                ?.let { vehicles -> readFromJson(vehicles) }
                ?.let { return NetworkResponse.Success(transformToVehicleOutput(it)) }
        }

        if (input.action == Action.Refresh || input.action == Action.Get) {
            return GetUserPsaExecutor(middlewareComponent, params).execute(input.vin).transform {
                readFromCache()
                    ?.let { vehicles -> readFromJson(vehicles) }
                    ?.let { value -> NetworkResponse.Success(transformToVehicleOutput(value)) }
                    ?: NetworkResponse.Failure(PIMSFoundationError.invalidReturnParam(Constants.Storage.VEHICLES))
            }
        }

        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(): String? =
        middlewareComponent
            .readSync<String>(Constants.Storage.VEHICLES, StoreMode.APPLICATION)
            .takeIf { !it.isNullOrBlank() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromJson(vehicles: String?): List<VehicleListResponse>? =
        try {
            val type = TypeToken.getParameterized(List::class.java, VehicleListResponse::class.java).type
            Gson().fromJson<List<VehicleListResponse>>(vehicles, type)
        } catch (ex: JsonParseException) {
            PIMSLogger.w(ex)
            null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToVehicleOutput(vehicles: List<VehicleListResponse>): VehiclesOutput {
        val vehiclesOutput = vehicles.map {
            VehiclesOutput.Vehicle(
                vin = it.vin,
                shortLabel = it.shortLabel,
                modelDescription = null,
                picture = it.visual
            )
        }
        return VehiclesOutput(vehiclesOutput)
    }
}
