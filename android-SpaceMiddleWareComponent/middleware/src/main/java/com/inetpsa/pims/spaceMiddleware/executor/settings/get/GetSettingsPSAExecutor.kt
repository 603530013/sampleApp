package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync

internal class GetSettingsPSAExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BasePsaExecutor<Unit, SettingsResponse>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse<SettingsResponse> =
        readFromCache()?.let { NetworkResponse.Success(it) } ?: getFromNetwork()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun getFromNetwork(): NetworkResponse<SettingsResponse> {
        val queries = mapOf(Constants.QUERY_PARAM_KEY_CULTURE to configurationManager.locale.toString())

        val request = request(
            SettingsResponse::class.java,
            arrayOf("/settings/v1/settings"),
            queries = queries
        )

        return communicationManager.get<SettingsResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { it.apply { saveOnCache(this) } }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveOnCache(data: SettingsResponse): Boolean =
        middlewareComponent.createSync(key = Constants.Storage.SETTINGS, data = data, mode = StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(): SettingsResponse? =
        middlewareComponent.readSync(Constants.Storage.SETTINGS, StoreMode.APPLICATION)
}
