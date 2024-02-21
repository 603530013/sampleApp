package com.inetpsa.pims.spaceMiddleware.executor.partners

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess
import com.inetpsa.pims.spaceMiddleware.util.readSync

internal class GetMarketPlacePartnersFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<UserInput, List<MarketPlacePartnerResponse>>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    override fun params(parameters: Map<String, Any?>?) = UserInput(
        action = parameters hasEnum Constants.Input.ACTION,
        vin = parameters has Constants.Input.VIN
    )

    override suspend fun execute(input: UserInput): NetworkResponse<List<MarketPlacePartnerResponse>> {
        require(!input.vin.isNullOrBlank())
        if (input.action == Action.Get) {
            readFromCache(input.vin)
                ?.let { partners -> readFromJson(partners) }
                ?.let { return NetworkResponse.Success(it) }
        }

        if (input.action == Action.Refresh || input.action == Action.Get) {
            val request = request(
                type = object : TypeToken<List<MarketPlacePartnerResponse>>() {}.type,
                urls = arrayOf("/v2/accounts/", uid, "/vehicles/", input.vin, "/mp-partners")
            )
            return communicationManager
                .get<List<MarketPlacePartnerResponse>>(request, TokenType.AWSToken(SDP))
                .ifSuccess { writeToCache(input.vin, it) }
        }

        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(vin: String): String? =
        middlewareComponent
            .readSync<String>("${vin}_${Constants.Storage.MP_PARTNERS}", StoreMode.APPLICATION)
            .takeIf { !it.isNullOrBlank() }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromJson(response: String?): List<MarketPlacePartnerResponse>? =
        try {
            val type = object : TypeToken<List<MarketPlacePartnerResponse>>() {}.type
            Gson().fromJson(response, type)
        } catch (ex: JsonParseException) {
            PIMSLogger.w(ex)
            null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun writeToCache(vin: String, response: List<MarketPlacePartnerResponse>) {
        middlewareComponent.createSync(
            "${vin}_${Constants.Storage.MP_PARTNERS}",
            response.toJson(),
            StoreMode.APPLICATION
        )
        val partnersVin = middlewareComponent.readSync<Set<String>>(
            Constants.Storage.MP_PARTNERS,
            StoreMode.APPLICATION
        )
        val newPartnersVin = partnersVin.orEmpty().toMutableSet()
        newPartnersVin.add(vin)
        middlewareComponent.createSync(
            Constants.Storage.MP_PARTNERS,
            newPartnersVin.toSet(),
            StoreMode.APPLICATION
        )
    }
}
