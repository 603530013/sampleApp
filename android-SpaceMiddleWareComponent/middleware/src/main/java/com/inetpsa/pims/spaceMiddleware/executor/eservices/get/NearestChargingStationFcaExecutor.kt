package com.inetpsa.pims.spaceMiddleware.executor.eservices.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationBoschMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationF2MMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationNoneMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationOutputMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationTomTomMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorOutput
import com.inetpsa.pims.spaceMiddleware.util.asJson
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map

internal class NearestChargingStationFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<ChargeStationLocatorInput, ChargeStationLocatorOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): ChargeStationLocatorInput {
        val vin: String = parameters has Constants.Input.VIN
        val latitude: Double = parameters has Constants.Input.LATITUDE
        val longitude: Double = parameters has Constants.Input.LONGITUDE
        return ChargeStationLocatorInput(
            vin = vin,
            latitude = latitude,
            longitude = longitude
        )
    }

    override suspend fun execute(input: ChargeStationLocatorInput): NetworkResponse<ChargeStationLocatorOutput> {
        val vehicle = CachedVehicles.getOrThrow(middlewareComponent, input.vin)
        val mapper = initializeMapper(vehicle.cslProvider, input.vin)
        input.filters = mapper.transformParamsToInput(params hasOrNull Constants.Input.FILTERS)

        val request = request(
            type = ChargeStationLocatorResponse::class.java,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/chargeStations"),
            body = mapper.transformToBodyRequest(input).asJson()

        )
        return communicationManager
            .post<ChargeStationLocatorResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { response ->
                val partner = getSupportedPartner(input.vin)
                ChargingStationOutputMapper().transformToOutput(response, partner)
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun initializeMapper(provider: CslProvider?, vin: String): ChargingStationMapper =
        when (provider) {
            CslProvider.F2M -> ChargingStationF2MMapper()
            CslProvider.BOSCH -> ChargingStationBoschMapper()

            CslProvider.TOMTOM -> {
                val partnersId = MarketPlacePartnerManager()
                    .fetchChargingStationLocator(middlewareComponent, params, vin)
                    ?.mapNotNull { it.partnerID }
                ChargingStationTomTomMapper(partnersId)
            }

            else -> ChargingStationNoneMapper()
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun getSupportedPartner(vin: String): MarketPlacePartnerResponse? {
        val partnersManager = MarketPlacePartnerManager()
        val partners = partnersManager.fetchChargingStationLocator(middlewareComponent, params, vin)
        return partnersManager.getDeepLinkSupportedPartners(partners)
    }
}
