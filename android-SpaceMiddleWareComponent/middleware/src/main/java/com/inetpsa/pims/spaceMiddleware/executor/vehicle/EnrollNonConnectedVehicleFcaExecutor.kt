package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor

@Deprecated("should replace with AddVehicleFcaExecutor")
internal class EnrollNonConnectedVehicleFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>?
) : BaseFcaExecutor<String, Unit>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<Unit> {
        val request = request(
            Unit::class.java,
            arrayOf("/v1/accounts/", uid, "/vehicles/", input, "/associate/")
        )

        return communicationManager.post(request, TokenType.AWSToken(FCAApiKey.SDP))
    }
}
