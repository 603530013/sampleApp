package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleDetails
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleDetailsFca
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleImageFcaInput
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("This class should be removed")
internal class GetVehicleImageFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<VehicleImageFcaInput, VehicleDetails>(command) {

    companion object {

        const val DEFAULT_IMAGE_WIDTH = 300
        const val DEFAULT_IMAGE_HEIGHT = 300
        const val DEFAULT_IMAGE_FORMAT = "png"
    }

    override fun params(parameters: Map<String, Any?>?): VehicleImageFcaInput = VehicleImageFcaInput(
        parameters has Constants.PARAM_VIN,
        (parameters hasOrNull Constants.PARAM_WIDTH) ?: DEFAULT_IMAGE_WIDTH,
        (parameters hasOrNull Constants.PARAM_HEIGHT) ?: DEFAULT_IMAGE_HEIGHT,
        (parameters hasOrNull Constants.PARAM_IMAGE_FORMAT) ?: DEFAULT_IMAGE_FORMAT
    )

    override suspend fun execute(input: VehicleImageFcaInput): NetworkResponse<VehicleDetails> {
        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_VIN to input.vin,
            Constants.PARAM_WIDTH to input.width.toString(),
            Constants.PARAM_HEIGHT to input.height.toString(),
            Constants.QUERY_PARAM_KEY_IMAGE_FORMAT to input.imageFormat
        )

        val request = request(
            VehicleDetailsFca::class.java,
            arrayOf("/v1/vehicles/details/"),
            queries = queries
        )

        val response: NetworkResponse<VehicleDetailsFca> = communicationManager.get(
            request,
            TokenType.AWSToken(FCAApiKey.SDP)
        )
        return response.map {
            VehicleDetails(
                vin = it.vin,
                imageUrl = it.preciseImageURL,
                model = it.modelDescription
            )
        }
    }
}
