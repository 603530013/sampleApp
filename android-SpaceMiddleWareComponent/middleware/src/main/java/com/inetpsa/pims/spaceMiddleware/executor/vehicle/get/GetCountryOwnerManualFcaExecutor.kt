package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.OwnerManualPdfResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetCountryOwnerManualFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<VehicleResponse, String?>(middlewareComponent, params) {

    // this will never be called
    override fun params(parameters: Map<String, Any?>?): VehicleResponse =
        VehicleResponse(vin = parameters has Constants.Input.VIN)

    override suspend fun execute(input: VehicleResponse): NetworkResponse<String?> {
        val brand = input.subMake?.lowercase().orEmpty()
        val model = input.model?.lowercase().orEmpty()
        val year = input.year.toString()

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_BRAND to brand,
            Constants.QUERY_PARAM_KEY_MODEL to model,
            Constants.QUERY_PARAM_KEY_YEAR to year
        )

        val request = request(
            type = OwnerManualPdfResponse::class.java,
            urls = arrayOf("/v1/digitalglovebox/ownermanual/manual"),
            queries = queries
        )
        return communicationManager
            .get<OwnerManualPdfResponse>(request, TokenType.AWSToken(SDP))
            .map { response -> response.pdf?.takeIf { it.isNotBlank() } }
    }
}
