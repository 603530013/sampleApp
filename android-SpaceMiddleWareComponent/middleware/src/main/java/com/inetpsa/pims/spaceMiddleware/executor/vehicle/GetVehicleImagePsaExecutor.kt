package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleDetails
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleDetailsPsa
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("This class should be removed")
internal class GetVehicleImagePsaExecutor(command: BaseCommand) : BasePsaExecutor<String, VehicleDetails>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<VehicleDetails> {
        val request = request(
            VehicleDetailsPsa::class.java,
            arrayOf("/car/v1/vehicle/", input)
        )

        val response: NetworkResponse<VehicleDetailsPsa> =
            communicationManager.get(request, MiddlewareCommunicationManager.MymToken)

        return response.map {
            VehicleDetails(
                vin = it.vin,
                imageUrl = it.visual,
                model = it.shortLabel
            )
        }
    }
}
