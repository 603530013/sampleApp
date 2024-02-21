package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.user.GetUserPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.mapper.VehicleServicesPsaMapper
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInputVehicleService
import com.inetpsa.pims.spaceMiddleware.model.vehicles.service.ServicesOutput
import com.inetpsa.pims.spaceMiddleware.util.RSAxPSAEncryption
import com.inetpsa.pims.spaceMiddleware.util.getToken
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.transform
import com.inetpsa.pims.spaceMiddleware.util.unwrapNullable

internal class GetVehicleServicesPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<UserInputVehicleService, ServicesOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) =
        UserInputVehicleService(
            action = parameters hasEnum Constants.Input.ACTION,
            vin = parameters has Constants.Input.VIN,
            schema = parameters hasOrNull Constants.Input.SCHEMA
        )

    override suspend fun execute(input: UserInputVehicleService): NetworkResponse<ServicesOutput> {
        val mapper = VehicleServicesPsaMapper()
        val encryptionRsa = RSAxPSAEncryption()
        val token = getToken()
        val encryptedVin = encryptionRsa.invoke(input.vin, false)

        if (input.action == Action.Get) {
            readVehicleFromCache(input.vin)
                ?.let { vehicle ->
                    val result = mapper.transform(vehicle.servicesConnected, input.schema, encryptedVin, token)
                    return NetworkResponse.Success(result)
                }
        }

        if (input.action == Action.Refresh || input.action == Action.Get) {
            return GetUserPsaExecutor(middlewareComponent, params).execute(input.vin).transform {
                readVehicleFromCache(input.vin)?.let { vehicle ->
                    val result = mapper.transform(vehicle.servicesConnected, input.schema, encryptedVin, token)
                    NetworkResponse.Success(result)
                } ?: NetworkResponse.Failure(PIMSFoundationError.invalidReturnParam(Constants.Storage.VEHICLE))
            }
        }

        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun getToken(): String? =
        middlewareComponent.userSessionManager
            .getToken(TokenType.CVSToken)
            .unwrapNullable()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readVehicleFromCache(vin: String?): VehicleDetailsResponse? =
        middlewareComponent.readSync("${Constants.Storage.VEHICLE}_$vin", StoreMode.APPLICATION)
}
