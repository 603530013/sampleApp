package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleListResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess
import com.inetpsa.pims.spaceMiddleware.util.readSync

internal class RemoveVehiclePsaExecutor(command: BaseCommand) : BasePsaExecutor<String, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<Unit> {
        val request = request(
            Unit::class.java,
            arrayOf("/me/v1/vehicle/", input)
        )

        return communicationManager
            .delete<Unit>(request, MiddlewareCommunicationManager.MymToken)
            .ifSuccess {
                removeFromVehicleCache(input)
                removeFromVehiclesCache(input)
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun removeFromVehicleCache(vin: String): Boolean =
        middlewareComponent
            .deleteSync("${Constants.Storage.VEHICLE}_$vin", StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun removeFromVehiclesCache(vin: String): Boolean =
        readVehiclesFromCache()?.let { readVehiclesFromJson(it) }
            ?.toMutableList()
            ?.also { vehicles -> vehicles.removeIf { it.vin.equals(vin, true) } }
            ?.let { saveVehicles(it) } ?: false

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readVehiclesFromCache(): String? =
        middlewareComponent
            .readSync<String>(Constants.Storage.VEHICLES, StoreMode.APPLICATION)
            .takeIf { !it.isNullOrBlank() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readVehiclesFromJson(vehicles: String?): List<VehicleListResponse>? =
        try {
            val type = TypeToken.getParameterized(List::class.java, VehicleListResponse::class.java).type
            Gson().fromJson<List<VehicleListResponse>>(vehicles, type)
        } catch (ex: JsonParseException) {
            PIMSLogger.w(ex)
            null
        }

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveVehicles(vehicles: List<VehicleListResponse>): Boolean =
        middlewareComponent.createSync(
            key = Constants.Storage.VEHICLES,
            data = vehicles.toJson(),
            mode = StoreMode.APPLICATION
        )
}
