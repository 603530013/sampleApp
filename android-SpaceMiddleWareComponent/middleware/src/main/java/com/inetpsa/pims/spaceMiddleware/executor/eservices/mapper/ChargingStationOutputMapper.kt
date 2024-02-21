package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.executor.eservices.get.MarketPlacePartnerManager
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse.ChargeStations.CslProviderData
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorOutput.ChargeStation.ProviderInfo
import com.inetpsa.pims.spaceMiddleware.util.filterNotNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("TooManyFunctions")
internal class ChargingStationOutputMapper {

    companion object {

        const val QUERY_KEY_CHARGE_POINT_ID = "{chargePointId}"
    }

    fun transformToOutput(response: ChargeStationLocatorResponse, partner: MarketPlacePartnerResponse?):
        ChargeStationLocatorOutput =
        ChargeStationLocatorOutput(response.chargeStations?.map { transformChargeStation(it, partner) })

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformChargeStation(
        item: ChargeStationLocatorResponse.ChargeStations,
        partner: MarketPlacePartnerResponse?
    ): ChargeStationLocatorOutput.ChargeStation {
        val ids: Pair<String?, String?> = extractIdRelatedToPartner(item.id, partner != null)

        return ChargeStationLocatorOutput.ChargeStation(
            id = ids.first,
            link = extractLinkFromPartner(ids.second, partner),
            locationId = item.locationId,
            name = item.poi?.name,
            openHours = transformToOpenHours(item.poi?.openHours),
            accessType = item.poi?.accessType,
            acceptablePayments = item.poi?.acceptablePayments,
            address = item.address?.freeformAddress,
            position = item.position?.let { location ->
                ChargeStationLocatorOutput.ChargeStation.Position(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            },
            connectors = transformConnectors(item.connectors),
            providers = transformProviders(item.cslProviderData)
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformConnectors(it: List<ChargeStationLocatorResponse.ChargeStations.Connectors>?):
        List<ChargeStationLocatorOutput.ChargeStation.Connectors>? =
        it?.map { connectors ->
            ChargeStationLocatorOutput.ChargeStation.Connectors(
                type = connectors.type,
                compatible = connectors.compatible,
                total = connectors.total,
                powerLevel = connectors.powerLevel?.let { powerLevel ->
                    ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel(
                        chargeTypeAvailability = transformChargeAvailable(powerLevel.chargeTypeAvailability),
                        chargingCapacities = transformChargeCapabilities(powerLevel.chargingCapacities)
                    )
                },
                availability = transformAvailability(connectors.availability)
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformAvailability(
        availability: ChargeStationLocatorResponse.ChargeStations.Connectors.Availability?
    ): ChargeStationLocatorOutput.ChargeStation.Connectors.Availability =
        ChargeStationLocatorOutput.ChargeStation.Connectors.Availability(
            available = availability?.available,
            occupied = availability?.occupied,
            reserved = availability?.reserved,
            unknown = availability?.unknown,
            outOfService = availability?.outOfService
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformChargeCapabilities(
        capabilities: List<PowerLevel.ChargingCapacities>?
    ): List<ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargingCapacities>? =
        capabilities?.map {
            ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargingCapacities(
                type = it.type?.name,
                powerKw = it.powerKw,
                chargingMode = it.chargingMode?.name
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformChargeAvailable(
        availability: PowerLevel.ChargeTypeAvailability?
    ): ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargeTypeAvailability =
        ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargeTypeAvailability(
            fastCharge = availability?.fastCharge,
            slowCharge = availability?.slowCharge,
            regularCharge = availability?.regularCharge,
            unknown = availability?.unknown
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToOpenHours(
        openHours: List<ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours>?
    ): List<ChargeStationLocatorOutput.ChargeStation.OpeningHours>? =
        openHours?.map { opening ->
            ChargeStationLocatorOutput.ChargeStation.OpeningHours(
                startTime = transformOfTime(opening.startTime)?.toString(),
                endTime = transformOfTime(opening.endTime)?.toString()
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformOfTime(
        time: ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours.Time?
    ): LocalDateTime? =
        time?.let {
            LocalDateTime.of(
                LocalDate.parse(time.date),
                LocalTime.of(time.hour ?: 0, time.minute ?: 0)
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformProviders(providers: Map<String, CslProviderData>?): Map<String, ProviderInfo>? =
        providers?.mapValues { entry -> transformToProviderInfo(entry.value) }
            ?.filterNotNull()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToProviderInfo(provider: CslProviderData): ProviderInfo =
        ProviderInfo(
            open24Hours = provider.isOpen24Hours,
            renewableEnergy = provider.renewableEnergy,
            indoor = provider.indoor,
            floor = provider.floor,
            hotline = provider.hotline,
            status = transformProviderStatus(provider.status),
            access = provider.access?.mapNotNull { transformProviderAccess(it) },
            canBeReserved = provider.canBeReserved
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformProviderStatus(
        status: CslProviderData.Status?
    ): ProviderInfo.Status? =
        when (status) {
            CslProviderData.Status.Unknown -> ProviderInfo.Status.Unknown
            CslProviderData.Status.Available -> ProviderInfo.Status.Available
            CslProviderData.Status.Charging -> ProviderInfo.Status.Charging
            CslProviderData.Status.OutOfService -> ProviderInfo.Status.OutOfService
            CslProviderData.Status.Reserved -> ProviderInfo.Status.Reserved
            CslProviderData.Status.Removed -> ProviderInfo.Status.Removed
            else -> null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformProviderAccess(access: CslProviderData.Access?): ProviderInfo.Access? =
        when (access) {
            CslProviderData.Access.ChargingCard -> ProviderInfo.Access.ChargingCard
            CslProviderData.Access.App -> ProviderInfo.Access.App
            CslProviderData.Access.NoAuthentication -> ProviderInfo.Access.NoAuthentication
            else -> null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun extractIdRelatedToPartner(id: String?, supportedPartner: Boolean): Pair<String?, String?> {
        val index = id?.lastIndexOf("-") ?: -1
        return when {
            !supportedPartner -> Pair(id, null)
            index < 0 -> Pair(id, null)
            else -> Pair(id?.substring(0, index), id?.substring(index + 1, id.length))
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun extractLinkFromPartner(deepLinkId: String?, partner: MarketPlacePartnerResponse?): String? =
        deepLinkId
            .takeIf { !it.isNullOrBlank() }
            ?.let { linkId ->
                partner?.customExtension
                    ?.deepLinks
                    ?.firstOrNull {
                        it.key.equals(MarketPlacePartnerManager.KEY_DEFAULT_DEEPLINK, ignoreCase = true) ||
                            it.key.equals(MarketPlacePartnerManager.KEY_MASERATI_DEEPLINK, ignoreCase = true)
                    }
                    ?.androidLink
                    ?.replace(QUERY_KEY_CHARGE_POINT_ID, linkId)
            }
}
