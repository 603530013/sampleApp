package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import androidx.annotation.VisibleForTesting
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.FcaVehicleItem
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.AccountVehicleListFca
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.Vehicles
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.Vehicles.Vehicle
import com.inetpsa.pims.spaceMiddleware.util.map
import java.util.Locale

@Deprecated("We should switch to use the new class GetVehiclesFcaExecutor")
internal class GetVehiclesFcaExecutor(command: BaseCommand) : BaseFcaExecutor<Unit, Vehicles>(command) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getBrandValue(): String {
        val brand = configurationManager.brand
        val market = configurationManager.market

        return when {
            market == Market.LATAM || market == Market.NAFTA -> Constants.AWS_QUERY_VALUE_ALL

            brand == Brand.LANCIA -> Constants.AWS_QUERY_VALUE_ALL

            brand != Brand.ALFAROMEO && brand != Brand.MASERATI && brand != Brand.FIAT ->
                Constants.AWS_QUERY_VALUE_REST

            else -> brand.name.uppercase(Locale.US)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun asVehicleResponse(item: List<FcaVehicleItem>): List<Vehicle> =
        item.mapNotNull { bean ->
            when {
                bean.fuelType.equals("e", true) ->
                    Vehicle.Type.ELECTRIC

                bean.fuelType.equals("h", true) ->
                    Vehicle.Type.HYBRID

                bean.fuelType.equals("d", true) ->
                    Vehicle.Type.THERMIC

                bean.fuelType.equals("g", true) ->
                    Vehicle.Type.THERMIC

                else -> null
            }?.let { type ->
                Vehicle(
                    vin = bean.vin,
                    lcdv = "",
                    attributes = null,
                    eligibility = null,
                    servicesConnected = bean.services.toJson(),
                    preferredDealer = null,
                    type = type,
                    name = bean.model,
                    regTimeStamp = bean.regTimestamp,
                    nickname = bean.nickname,
                    year = bean.year,
                    sdp = bean.sdp,
                    market = bean.market,
                    make = bean.make,
                    connectorType = transformToVehicleOutput(bean.connectorType)
                )
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToVehicleOutput(connector: FcaVehicleItem.ConnectorType?): Vehicle.ConnectorType? =
        connector?.let { Vehicle.ConnectorType.valueOf(it.name) }

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<Vehicles> {
        val request = request(
            type = object : TypeToken<AccountVehicleListFca>() {}.type,
            urls = arrayOf("/v4/accounts/", uid, "/vehicles/"),
            queries = mapOf(
                Constants.QUERY_PARAM_KEY_BRAND to getBrandValue(),
                Constants.QUERY_PARAM_KEY_SDP to Constants.AWS_QUERY_VALUE_ALL,
                Constants.QUERY_PARAM_KEY_STAGE to Constants.AWS_QUERY_VALUE_ALL
            )
        )

        return communicationManager.get<AccountVehicleListFca>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map { asVehicleResponse(it.vehicles) }
            .map { Vehicles(it) }
    }
}
