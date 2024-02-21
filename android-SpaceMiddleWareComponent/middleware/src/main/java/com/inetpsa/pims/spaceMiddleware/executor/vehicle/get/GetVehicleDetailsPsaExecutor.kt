package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.GetUserPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class GetVehicleDetailsPsaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>?
) : BasePsaExecutor<UserInput, VehicleOutput>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    override fun params(parameters: Map<String, Any?>?) =
        UserInput(
            action = parameters hasEnum Constants.Input.ACTION,
            vin = parameters has Constants.Input.VIN
        )

    override suspend fun execute(input: UserInput): NetworkResponse<VehicleOutput> {
        if (input.action == Action.Get) {
            readVehicleFromCache(input.vin)
                ?.let { vehicles ->
                    val lastUpdate = readLastUpdateFromCache()
                    return NetworkResponse.Success(transformToVehicleOutput(vehicles, lastUpdate))
                }
        }

        if (input.action == Action.Refresh || input.action == Action.Get) {
            return GetUserPsaExecutor(middlewareComponent, params).execute(input.vin).transform {
                readVehicleFromCache(input.vin)?.let { vehicles ->
                    val lastUpdate = readLastUpdateFromCache()
                    NetworkResponse.Success(transformToVehicleOutput(vehicles, lastUpdate))
                } ?: NetworkResponse.Failure(PIMSFoundationError.invalidReturnParam(Constants.Storage.VEHICLE))
            }
        }

        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readVehicleFromCache(vin: String?): VehicleDetailsResponse? =
        middlewareComponent
            .readSync("${Constants.Storage.VEHICLE}_$vin", StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readLastUpdateFromCache(): Long? =
        middlewareComponent
            .readSync<Long>(Constants.Storage.LAST_UPDATE, StoreMode.APPLICATION)
            ?.takeIf { it > 0L }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformType(vehicle: VehicleDetailsResponse): VehicleOutput.Type =
        when (vehicle.typeVehicle) {
            VehicleDetailsResponse.VEHICLE_THERMIC -> VehicleOutput.Type.THERMIC
            VehicleDetailsResponse.VEHICLE_ELECTRIC -> VehicleOutput.Type.ELECTRIC
            VehicleDetailsResponse.VEHICLE_HYBRID_A,
            VehicleDetailsResponse.VEHICLE_HYBRID_B,
            VehicleDetailsResponse.VEHICLE_HYBRID_C -> VehicleOutput.Type.HYBRID

            else -> VehicleOutput.Type.UNKNOWN
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToVehicleOutput(vehicle: VehicleDetailsResponse, lastUpdate: Long?): VehicleOutput =
        VehicleOutput(
            vin = vehicle.vin,
            lcdv = vehicle.lcdv,
            eligibility = vehicle.eligibility,
            attributes = vehicle.attributes,
            type = transformType(vehicle),
            name = vehicle.shortName,
            regTimeStamp = 0, // vehicle.warrantyStartDate,
            year = null,
            lastUpdate = lastUpdate,
            sdp = null,
            market = null,
            make = null,
            picture = vehicle.visual,
            enrollmentStatus = null
        )
}
