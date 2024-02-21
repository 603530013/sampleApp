package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync
import java.util.Locale

internal class GetVehiclesResponseFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    parameters: Map<String, Any?>?
) : BaseFcaExecutor<String?, VehiclesResponse>(middlewareComponent, parameters) {

    override fun params(parameters: Map<String, Any?>?): String? = null

    override suspend fun execute(input: String?): NetworkResponse<VehiclesResponse> {
        val request = request(
            type = object : TypeToken<VehiclesResponse>() {}.type,
            urls = arrayOf("/v4/accounts/", uid, "/vehicles/"),
            queries = mapOf(
                Constants.QUERY_PARAM_KEY_BRAND to getBrandValue(),
                Constants.QUERY_PARAM_KEY_SDP to Constants.AWS_QUERY_VALUE_ALL,
                Constants.QUERY_PARAM_KEY_STAGE to Constants.AWS_QUERY_VALUE_ALL
            )
        )

        return communicationManager.get<VehiclesResponse>(
            request = request,
            tokenType = TokenType.AWSToken(FCAApiKey.SDP)
        ).map {
            clearPartnersCache()
            saveVehicles(it)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveVehicles(vehicles: VehiclesResponse): VehiclesResponse =
        vehicles.also {
            middlewareComponent.createSync(
                key = Constants.Storage.VEHICLES,
                data = vehicles.toJson(),
                mode = StoreMode.APPLICATION
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getBrandValue(): String {
        val brand = configurationManager.brand
        val market = configurationManager.market

        return when {
            market == Market.LATAM || market == Market.NAFTA -> Constants.AWS_QUERY_VALUE_ALL

            brand == Brand.LANCIA -> Constants.AWS_QUERY_VALUE_ALL

            brand != Brand.ALFAROMEO && brand != Brand.MASERATI && brand != Brand.FIAT -> Constants.AWS_QUERY_VALUE_REST

            else -> brand.name.uppercase(Locale.US)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun clearPartnersCache() {
        middlewareComponent.readSync<Set<String>>(
            Constants.Storage.MP_PARTNERS,
            StoreMode.APPLICATION
        )?.forEach { vin ->
            middlewareComponent.deleteSync("${vin}_${Constants.Storage.MP_PARTNERS}", StoreMode.APPLICATION)
        }
    }
}
