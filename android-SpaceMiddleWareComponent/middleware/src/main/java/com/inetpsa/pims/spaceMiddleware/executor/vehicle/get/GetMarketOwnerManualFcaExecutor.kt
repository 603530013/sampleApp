package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.eservices.get.MarketPlacePartnerManager
import com.inetpsa.pims.spaceMiddleware.executor.partners.GetMarketPlacePartnersFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetMarketOwnerManualFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<String, String>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<String> =
        GetMarketPlacePartnersFcaExecutor(middlewareComponent, params)
            .execute(UserInput(action = Action.Get, vin = input))
            .map { getMarketOwnerManualData(it, input) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getMarketOwnerManualData(response: List<MarketPlacePartnerResponse>, vin: String): String {
        val language = configurationManager.locale.language
        return MarketPlacePartnerManager().fetchOwnerManual(response)
            ?.mapNotNull { item -> item.customExtension?.deepLinks?.takeIf { it.isNotEmpty() } }
            ?.flatten()
            ?.firstOrNull { !it.androidLink.isNullOrBlank() }
            ?.androidLink
            ?.let { generateLink(it, vin, language) }
            .orEmpty()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateLink(link: String, vin: String, language: String) =
        Uri.parse(link)
            .buildUpon()
            .appendQueryParameter("vin", vin)
            .appendQueryParameter("lang", language)
            .appendQueryParameter("defLang", "en")
            .appendQueryParameter("source", "APP")
            .build().toString()
}
