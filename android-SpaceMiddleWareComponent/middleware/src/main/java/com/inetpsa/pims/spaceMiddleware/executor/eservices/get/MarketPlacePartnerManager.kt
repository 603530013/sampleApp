package com.inetpsa.pims.spaceMiddleware.executor.eservices.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.partners.GetMarketPlacePartnersFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.util.unwrapNullable

internal class MarketPlacePartnerManager {

    companion object {

        const val STATUS_ACTIVE = "active"
        const val SERVICE_CHARGING_STATION_LOCATOR = "CSL"
        const val KEY_DEFAULT_DEEPLINK = "BOOK_PREFERRED_CHARGING_STATION"
        const val KEY_MASERATI_DEEPLINK = "MASERATI_CHARGING_STATION"
        const val KEY_OWNER_MANUAL = "OWNERS_MANUAL"
    }

    suspend fun fetchChargingStationLocator(
        middlewareComponent: MiddlewareComponent,
        params: Map<String, Any?>? = null,
        vin: String
    ): List<MarketPlacePartnerResponse>? =
        GetMarketPlacePartnersFcaExecutor(middlewareComponent, params)
            .execute(UserInput(action = Get, vin = vin))
            .unwrapNullable()
            ?.asSequence()
            ?.filter { item ->
                val hasCslService = hasCSLServiceID(item)
                val matchDeepLinkKey = supportDeeplink(item)
                val isCSLProvider = hasCslService || matchDeepLinkKey
                val statusActive = partnerIsActive(item)
                statusActive && isCSLProvider
            }?.toList()

    fun getDeepLinkSupportedPartners(partners: List<MarketPlacePartnerResponse>?): MarketPlacePartnerResponse? =
        partners?.firstOrNull { supportDeeplink(it) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun partnerIsActive(item: MarketPlacePartnerResponse): Boolean =
        item.partnerStatus.equals(STATUS_ACTIVE, ignoreCase = true)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun hasCSLServiceID(item: MarketPlacePartnerResponse): Boolean =
        item.serviceID
            .orEmpty()
            .contains(SERVICE_CHARGING_STATION_LOCATOR, ignoreCase = true)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun supportDeeplink(item: MarketPlacePartnerResponse): Boolean =
        item.customExtension
            ?.deepLinks
            .orEmpty()
            .any {
                it.key.equals(KEY_DEFAULT_DEEPLINK, ignoreCase = true) ||
                    it.key.equals(KEY_MASERATI_DEEPLINK, ignoreCase = true)
            }

    fun fetchOwnerManual(
        partners: List<MarketPlacePartnerResponse>?
    ): List<MarketPlacePartnerResponse>? =
        partners?.asSequence()
            ?.filter { item -> partnerIsActive(item) && supportOwnerManual(item) }
            ?.toList()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun supportOwnerManual(item: MarketPlacePartnerResponse): Boolean =
        item.customExtension
            ?.deepLinks
            .orEmpty()
            .any { it.key.equals(KEY_OWNER_MANUAL, ignoreCase = true) }
}
