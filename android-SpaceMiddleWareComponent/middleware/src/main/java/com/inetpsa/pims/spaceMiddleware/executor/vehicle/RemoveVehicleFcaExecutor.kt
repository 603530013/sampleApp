package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.remove.RemoveVehicleInput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import java.time.Instant
import java.time.format.DateTimeParseException

@Deprecated("try to switch to use this class SetRemoveVehicleFcaExecutor")
internal class RemoveVehicleFcaExecutor(command: BaseCommand) : BaseFcaExecutor<RemoveVehicleInput, Unit>(command) {

    override suspend fun execute(input: RemoveVehicleInput): NetworkResponse<Unit> {
        val pinAuth: String = getPinToken()
        val reason = mapOf(
            Constants.PARAM_REASON_ID to input.reasonId,
            Constants.PARAM_REASON to input.reason
        ).filterValues { it?.isNotBlank() == true }

        val body = mapOf(
            Constants.PARAM_PIN_AUTH to pinAuth,
            Constants.PARAM_REASON to reason
        )

        val request = request(
            Unit::class.java,
            arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/"),
            body = body.toJson()
        )

        return communicationManager.delete(request, TokenType.AWSToken(FCAApiKey.SDP))
    }

    override fun params(parameters: Map<String, Any?>?): RemoveVehicleInput = RemoveVehicleInput(
        parameters has Constants.PARAM_VIN,
        parameters has Constants.PARAM_REASON,
        parameters has Constants.PARAM_REASON_ID
    )

    @Throws(PIMSError::class)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getPinToken(): String = with(middlewareComponent.dataManager) {
        val pinToken = read(Constants.STORAGE_KEY_PIN_TOKEN, StoreMode.SECURE) as? String

        if (pinToken.isNullOrBlank()) {
            throw PimsErrors.needPinToken()
        }

        val pinTokenExpiry = read(Constants.STORAGE_KEY_PIN_TOKEN_EXPIRY, StoreMode.APPLICATION) as? String
            ?: throw PimsErrors.needPinToken()
        val pinTokenExpiryDate = try {
            Instant.parse(pinTokenExpiry)
        } catch (e: DateTimeParseException) {
            throw PimsErrors.needPinToken()
        }

        val currentDate = now()

        if (currentDate.isAfter(pinTokenExpiryDate)) {
            delete(Constants.STORAGE_KEY_PIN_TOKEN, StoreMode.SECURE)
            delete(Constants.STORAGE_KEY_PIN_TOKEN_EXPIRY, StoreMode.APPLICATION)
            throw PimsErrors.needPinToken()
        }

        pinToken
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun now(): Instant = Instant.now()
}
