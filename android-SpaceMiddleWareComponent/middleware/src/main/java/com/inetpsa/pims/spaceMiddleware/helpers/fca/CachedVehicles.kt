package com.inetpsa.pims.spaceMiddleware.helpers.fca

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesResponseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.unwrap

internal object CachedVehicles {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Throws(PIMSError::class)
    suspend fun getAll(
        middlewareComponent: MiddlewareComponent,
        action: Action = Action.Get
    ): VehiclesResponse? =
        when (action) {
            Action.Refresh -> readFromRemote(middlewareComponent)
            Action.OnlyCache -> readFromLocal(middlewareComponent)
            Action.Get -> readFromLocal(middlewareComponent) ?: readFromRemote(middlewareComponent)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

    @Throws(PIMSError::class)
    suspend fun get(
        middlewareComponent: MiddlewareComponent,
        vin: String,
        action: Action = Action.Get
    ): VehicleResponse? = getAll(middlewareComponent, action)?.vehicles?.firstOrNull { it.vin == vin }

    @Throws(PIMSError::class)
    suspend fun getOrThrow(
        middlewareComponent: MiddlewareComponent,
        vin: String,
        action: Action = Action.Get
    ): VehicleResponse = getAll(middlewareComponent, action)?.vehicles?.firstOrNull { it.vin == vin }
        ?: throw PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromLocal(middlewareComponent: MiddlewareComponent): VehiclesResponse? =
        readFromCache(middlewareComponent)
            ?.let { vehicles -> readFromJson(vehicles) }
            ?.takeIf { it.vehicles.isNotEmpty() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun readFromRemote(middlewareComponent: MiddlewareComponent): VehiclesResponse =
        GetVehiclesResponseFcaExecutor(middlewareComponent, emptyMap())
            .execute()
            .unwrap()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(middlewareComponent: MiddlewareComponent): String? =
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
}
