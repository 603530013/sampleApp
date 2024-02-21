package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleVinOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VinField
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.VinType
import com.inetpsa.pims.spaceMiddleware.util.hasEnum

internal class GetVehicleDetailsFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>?
) : BaseFcaExecutor<VinType, VinField>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    override fun params(parameters: Map<String, Any?>?): VinType =
        parameters.hasEnum(Constants.Input.TYPE, VinType.Normal)

    override suspend fun execute(input: VinType): NetworkResponse<VinField> =
        when (input) {
            VinType.Normal -> executeNormal()
            VinType.LastCharacters -> executeLastCharacters()
            VinType.Encrypted -> executeEncrypted()
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun executeNormal(): NetworkResponse<VehicleOutput> =
        GetVehicleVinNormalFcaExecutor(middlewareComponent, params).execute()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun executeLastCharacters(): NetworkResponse<VehicleVinOutput> =
        GetVehicleVinLastCharactersFcaExecutor(middlewareComponent, params).execute()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun executeEncrypted(): NetworkResponse<VehicleVinOutput> =
        GetVehicleVinEncryptedFcaExecutor(middlewareComponent, params).execute()
}
