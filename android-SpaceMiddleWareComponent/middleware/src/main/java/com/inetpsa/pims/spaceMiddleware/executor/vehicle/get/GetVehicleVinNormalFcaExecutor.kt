package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleImageFcaInput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.transform
import com.inetpsa.pims.spaceMiddleware.util.unwrapNullable

internal class GetVehicleVinNormalFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>?
) : BaseFcaExecutor<UserInput, VehicleOutput>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    override fun params(parameters: Map<String, Any?>?) = UserInput(
        action = parameters hasEnum Constants.Input.ACTION,
        vin = parameters has Constants.Input.VIN
    )

    override suspend fun execute(input: UserInput): NetworkResponse<VehicleOutput> {
        require(!input.vin.isNullOrBlank())

        if (input.action == Action.Get) {
            readFromCache()
                ?.let { vehicles -> readFromJson(vehicles) }
                ?.let { transformToVehicleOutput(it, input.vin) }
                ?.let { return NetworkResponse.Success(it) }
        }

        if (input.action == Action.Refresh || input.action == Action.Get) {
            return GetVehiclesResponseFcaExecutor(middlewareComponent, params).execute(input.vin)
                .map { fetchImages(it, input.vin) }
                .transform {
                    when (val response = transformToVehicleOutput(it, input.vin)) {
                        null -> {
                            val error = PIMSFoundationError.invalidReturnParam(Constants.Storage.VEHICLE)
                            NetworkResponse.Failure(error)
                        }

                        else -> NetworkResponse.Success(response)
                    }
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
    internal fun readFromJson(vehicles: String?): VehiclesResponse? =
        try {
            Gson().fromJson(vehicles, VehiclesResponse::class.java)
        } catch (ex: JsonParseException) {
            PIMSLogger.w(ex)
            null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformType(vehicle: VehicleResponse): VehicleOutput.Type =
        when {
            vehicle.fuelType.equals("e", true) -> VehicleOutput.Type.ELECTRIC
            vehicle.fuelType.equals("h", true) -> VehicleOutput.Type.HYBRID
            vehicle.fuelType.equals("d", true) -> VehicleOutput.Type.THERMIC
            vehicle.fuelType.equals("g", true) -> VehicleOutput.Type.THERMIC
            else -> VehicleOutput.Type.UNKNOWN
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToVehicleOutput(response: VehiclesResponse, vin: String): VehicleOutput? =
        response.vehicles
            .firstOrNull { it.vin.equals(vin, true) }
            ?.let { vehicle ->
                VehicleOutput(
                    vin = vehicle.vin,
                    lcdv = null,
                    eligibility = null,
                    attributes = null,
                    type = transformType(vehicle),
                    name = vehicle.nickname.orEmpty(),
                    regTimeStamp = vehicle.regTimestamp,
                    year = vehicle.year,
                    lastUpdate = null,
                    sdp = vehicle.sdp,
                    market = vehicle.market,
                    make = vehicle.make,
                    subMake = vehicle.subMake,
                    picture = vehicle.imageUrl,
                    connectorType = transformToVehicleOutput(vehicle.connectorType),
                    enrollmentStatus = vehicle.enrollmentStatus
                )
            }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToVehicleOutput(connector: VehicleResponse.ConnectorType?): VehicleOutput.ConnectorType? =
        connector?.let { VehicleOutput.ConnectorType.valueOf(it.name) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun fetchImages(vehicles: VehiclesResponse, vin: String): VehiclesResponse {
        val vehicle = vehicles.vehicles.firstOrNull { vin.equals(it.vin, true) }
        if (vin.isNotBlank() && vehicle != null) {
            vehicle.imageUrl = GetVehicleImageFcaExecutor(middlewareComponent, params)
                .execute(VehicleImageFcaInput(vehicle.vin))
                .unwrapNullable()?.imageUrl
        }
        return vehicles
    }
}
