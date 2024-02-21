package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Storage
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.DealerPSAMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.toLocale

internal class GetDealersPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<DealersInput, DealersOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): DealersInput = DealersInput(
        vin = parameters has Constants.PARAM_VIN,
        latitude = parameters has Constants.PARAM_LAT,
        longitude = parameters has Constants.PARAM_LNG,
        max = parameters hasOrNull Constants.PARAM_MAX
    )

    override suspend fun execute(input: DealersInput): NetworkResponse<DealersOutput> {
        val queries = mutableMapOf(
            Constants.PARAM_LAT to input.latitude.toString(),
            Constants.PARAM_LNG to input.longitude.toString()
        )

        input.max?.let { max -> queries[Constants.PARAM_RESULT_MAX] = max.toString() }

        val request = request(
            type = object : TypeToken<List<DetailsResponse>>() {}.type,
            urls = arrayOf("/shop/v1/dealers"),
            queries = queries
        )

        val preferredDealerCache = preferredDealerCache()

        return communicationManager
            .get<List<DetailsResponse>>(request, MiddlewareCommunicationManager.MymToken)
            .map { items -> DealersOutput(items.map { transformToDealerOutput(it, preferredDealerCache) }) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDealerOutput(response: DetailsResponse, preferredDealer: String?): DealerOutput {
        val locale = response.culture
            .takeIf { !it.isNullOrBlank() }
            ?.let { it.toLocale() }
            ?: middlewareComponent.configurationManager.locale

        val siteGeo = response.siteGeo
        PIMSLogger.d("siteGeo: $siteGeo")
        val favoriteDealer = preferredDealerCache()
        PIMSLogger.d("favoriteDealer: $favoriteDealer")

        return DealerPSAMapper().transformDealer(response, locale, response.siteGeo.equals(preferredDealer, true))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun preferredDealerCache(): String? =
        middlewareComponent.readSync(Storage.PREFERRED_DEALER_ID, APPLICATION)
}
