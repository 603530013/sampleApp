package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toInvalidParamError
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.extensions.toMissingParamError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesResponseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.update.NicknameInput
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess

internal class NicknameUpdateFCAExecutor(command: BaseCommand) : BaseFcaExecutor<NicknameInput, Unit>(command) {

    companion object {

        const val NICKNAME_DELETE_VALUE = "  "
    }

    override fun params(parameters: Map<String, Any?>?): NicknameInput =
        NicknameInput(
            vin = parameters has Constants.PARAM_VIN,
            name = checkNameParameter(parameters, Constants.PARAMS_KEY_NAME)
        )

    override suspend fun execute(input: NicknameInput): NetworkResponse<Unit> {
        val bodyJson = mapOf(
            Constants.PARAMS_KEY_NICKNAME to input.name
        )

        val request = request(
            type = Unit::class.java,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/", "nickname/"),
            body = bodyJson.toJson()
        )
        return communicationManager
            .post<Unit>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .ifSuccess { fetchVehicles() }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun fetchVehicles() {
        GetVehiclesResponseFcaExecutor(middlewareComponent, params).execute()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun checkNameParameter(parameters: Map<String, Any?>?, name: String): String =
        when {
            parameters == null -> throw name.toMissingParamError()
            parameters.isEmpty() -> throw name.toMissingParamError()
            !parameters.containsKey(name) -> throw name.toMissingParamError()
            parameters[name] == null -> NICKNAME_DELETE_VALUE
            parameters[name] !is String -> throw name.toInvalidParamError()
            (parameters[name] is String) && (parameters[name] as String).isBlank() -> NICKNAME_DELETE_VALUE
            parameters[name] is String -> parameters[name] as String
            else -> throw name.toMissingParamError()
        }
}
