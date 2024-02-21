package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("this was moved to GetAssistancePhonesFcaExecutor class")
internal class GetEcallContactFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<String, Map<String, Map<String, String>>?>(command) {

    companion object {

        const val CALL_CENTER_TYPE = "SOS"
        const val SERVICE_TYPE = "ECALL"
    }

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<Map<String, Map<String, String>>?> {
        return if (hasSOSServices(input)) {
            transformToPhones(input)
        } else {
            val failure = PimsErrors.apiNotSupported()
            NetworkResponse.Failure(failure)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun transformToPhones(input: String): NetworkResponse<Map<String, Map<String, String>>?> {
        return readSettings(input).map { items ->
            val settings = items.mapNotNull { it.settings }
                .flatten()
                .firstOrNull { it.callCenterType.equals(CALL_CENTER_TYPE, true) }

            val phones = mutableMapOf<String, String>()
            putPhoneNumber(phones, settings?.primaryNumber, Constants.PRIMARY)
            putPhoneNumber(phones, settings?.secondaryNumber, Constants.SECONDARY)

            mapOf(Constants.Output.Common.PHONES to phones)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun hasSOSServices(input: String): Boolean =
        CachedVehicles.getOrThrow(middlewareComponent, input)
            .services
            ?.firstOrNull { it.service == SERVICE_TYPE && it.serviceEnabled } != null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun putPhoneNumber(data: MutableMap<String, String>, phone: String?, key: String) {
        phone.takeIf { !it.isNullOrBlank() }
            ?.let { data.put(key, it) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun readSettings(input: String) =
        GetSettingsFCAExecutor(middlewareComponent, params).execute(input)
}
