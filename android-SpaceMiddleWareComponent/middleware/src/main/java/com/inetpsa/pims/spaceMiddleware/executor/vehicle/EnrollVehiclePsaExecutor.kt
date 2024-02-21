package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.enrollvehicle.EnrollVehiclePsaParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

@Deprecated("try to switch to use this class AddVehiclePsaExecutor")
internal class EnrollVehiclePsaExecutor(command: BaseCommand) : BasePsaExecutor<EnrollVehiclePsaParams, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): EnrollVehiclePsaParams = EnrollVehiclePsaParams(
        parameters has Constants.PARAM_VIN,
        parameters hasOrNull Constants.PARAM_MILEAGE
    )

    override suspend fun execute(input: EnrollVehiclePsaParams): NetworkResponse<Unit> {
        val body = mutableMapOf<String, Any>()
        body[Constants.PARAM_VIN] = input.vin
        input.mileage?.also { body[Constants.PARAM_MILEAGE] = it }

        val request = request(
            Unit::class.java,
            arrayOf("/me/v1/user/vehicles/add"),
            body = body.toJson()
        )

        return communicationManager.post(request, MiddlewareCommunicationManager.MymToken)
    }
}
