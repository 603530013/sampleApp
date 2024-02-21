package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleImageResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleImageFcaInput
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

internal class GetVehicleImageFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    parameters: Map<String, Any?>?
) : BaseFcaExecutor<VehicleImageFcaInput, VehicleImageResponse>(middlewareComponent, parameters) {

    override fun params(parameters: Map<String, Any?>?): VehicleImageFcaInput = VehicleImageFcaInput(
        parameters has Constants.PARAM_VIN,
        (parameters hasOrNull Constants.PARAM_WIDTH) ?: Constants.QUERY_DEFAULT_IMAGE_WIDTH,
        (parameters hasOrNull Constants.PARAM_HEIGHT) ?: Constants.QUERY_DEFAULT_IMAGE_HEIGHT,
        (parameters hasOrNull Constants.PARAM_IMAGE_FORMAT) ?: Constants.QUERY_DEFAULT_IMAGE_FORMAT
    )

    override suspend fun execute(input: VehicleImageFcaInput): NetworkResponse<VehicleImageResponse> {
        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_VIN to input.vin,
            Constants.PARAM_WIDTH to input.width.toString(),
            Constants.PARAM_HEIGHT to input.height.toString(),
            Constants.QUERY_PARAM_KEY_IMAGE_FORMAT to input.imageFormat
        )

        val request = request(
            VehicleImageResponse::class.java,
            arrayOf("/v1/vehicles/details/"),
            queries = queries
        )

        return communicationManager.get(request, TokenType.AWSToken(FCAApiKey.SDP))
    }
}
