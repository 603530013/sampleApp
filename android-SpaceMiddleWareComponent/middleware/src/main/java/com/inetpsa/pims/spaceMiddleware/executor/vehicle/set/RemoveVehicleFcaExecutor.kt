package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.remove.RemoveVehicleInput
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import com.inetpsa.pims.spaceMiddleware.util.getToken
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.mapStrongAuthFailure
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class RemoveVehicleFcaExecutor(command: BaseCommand) : BaseFcaExecutor<RemoveVehicleInput, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): RemoveVehicleInput = RemoveVehicleInput(
        parameters has Constants.PARAM_VIN,
        parameters has Constants.PARAM_REASON,
        parameters has Constants.PARAM_REASON_ID
    )

    override suspend fun execute(input: RemoveVehicleInput): NetworkResponse<Unit> {
        val reason = mapOf(
            Constants.PARAM_REASON_ID to input.reasonId,
            Constants.PARAM_REASON to input.reason
        ).filterValues { it?.isNotBlank() == true }

        return getPinToken().transform { pinAuth ->

            val body = mapOf(
                Constants.PARAM_PIN_AUTH to pinAuth,
                Constants.PARAM_REASON to reason
            )

            val request = request(
                type = Unit::class.java,
                urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/"),
                body = body.toJson()
            )

            communicationManager.delete<Unit>(request, TokenType.AWSToken(FCAApiKey.SDP))
                .mapStrongAuthFailure()
                .map {
                    removeFromVehicleCache(input.vin)
                    removeFromVehiclesCache(input.vin)
                    Unit
                }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun removeFromVehicleCache(vin: String): Boolean =
        middlewareComponent
            .deleteSync("${Constants.Storage.VEHICLE}_$vin", StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun removeFromVehiclesCache(vin: String): Boolean =
        CachedVehicles.getAll(middlewareComponent, Action.OnlyCache)
            ?.let { cache ->
                val vehicles = cache.vehicles.toMutableList()
                vehicles.removeIf { it.vin.equals(vin, true) }
                cache.copy(vehicles = vehicles)
            }
            ?.let { saveVehicles(it) } ?: false

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveVehicles(vehicles: VehiclesResponse): Boolean =
        middlewareComponent.createSync(
            key = Constants.Storage.VEHICLES,
            data = vehicles.toJson(),
            mode = StoreMode.APPLICATION
        )

    @Throws(PIMSError::class)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun getPinToken(): NetworkResponse<String> =
        middlewareComponent.userSessionManager.getToken(TokenType.OTPToken)
}
