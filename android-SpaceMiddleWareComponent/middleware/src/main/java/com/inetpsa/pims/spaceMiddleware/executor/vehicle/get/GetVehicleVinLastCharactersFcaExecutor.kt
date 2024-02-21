package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleImageResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleVinOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleImageFcaInput
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetVehicleVinLastCharactersFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    parameters: Map<String, Any?>?
) : BaseFcaExecutor<VehicleImageFcaInput, VehicleVinOutput>(middlewareComponent, parameters) {

    override fun params(parameters: Map<String, Any?>?): VehicleImageFcaInput = VehicleImageFcaInput(
        parameters has Constants.PARAM_VIN,
        (parameters hasOrNull Constants.PARAM_WIDTH) ?: Constants.QUERY_DEFAULT_IMAGE_WIDTH,
        (parameters hasOrNull Constants.PARAM_HEIGHT) ?: Constants.QUERY_DEFAULT_IMAGE_HEIGHT,
        (parameters hasOrNull Constants.PARAM_IMAGE_FORMAT) ?: Constants.QUERY_DEFAULT_IMAGE_FORMAT
    )

    override suspend fun execute(input: VehicleImageFcaInput): NetworkResponse<VehicleVinOutput> {
        val request = request(
            VehicleImageResponse::class.java,
            arrayOf("/v1/vehicles/details"),
            queries = mapOf(
                Constants.QUERY_PARAM_KEY_VIN_LAST_EIGHT to input.vin,
                Constants.PARAM_WIDTH to input.width.toString(),
                Constants.PARAM_HEIGHT to input.height.toString(),
                Constants.QUERY_PARAM_KEY_IMAGE_FORMAT to input.imageFormat
            )
        )
        return communicationManager
            .get<VehicleImageResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { transformToVehicleOutput(it) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToVehicleOutput(response: VehicleImageResponse): VehicleVinOutput {
        return VehicleVinOutput(
            vin = response.vin.orEmpty(),
            description = response.description,
            picture = response.imageUrl,
            make = response.make,
            subMake = response.subMake,
            year = response.year,
            sdp = response.sdp,
            tcuType = response.tcuType,
            userid = response.userid,
            tcCountryCode = response.tcCountryCode
        )
    }
}
