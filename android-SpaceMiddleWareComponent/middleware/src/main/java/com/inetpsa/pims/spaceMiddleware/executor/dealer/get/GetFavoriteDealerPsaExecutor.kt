package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.DealerPSAMapper
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.PreferredDealerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.toLocale
import java.net.HttpURLConnection

internal class GetFavoriteDealerPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<UserInput, DealersOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) =
        UserInput(
            action = parameters hasEnum Constants.Input.ACTION,
            vin = parameters has Constants.Input.VIN
        )

    override suspend fun execute(input: UserInput): NetworkResponse<DealersOutput> {
        if (input.action == Get) {
            readFromCache()?.let { response ->
                return NetworkResponse.Success(transformToDealers(response))
            }
        }

        val request = request(
            PreferredDealerResponse::class.java,
            arrayOf("/me/v1/user_data/favorite_dealer")
        )

        return communicationManager
            .get<PreferredDealerResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map(
                transformSuccess = { items ->
                    saveOnCache(items.dealer)
                    transformToDealers(items.dealer)
                },
                transformFailure = { responseError ->
                    when (responseError?.subError?.status) {
                        HttpURLConnection.HTTP_NOT_FOUND -> NetworkResponse.Success(DealersOutput(emptyList()))
                        else -> NetworkResponse.Failure(responseError)
                    }
                }
            )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDealers(response: DetailsResponse): DealersOutput {
        val locale = response.culture
            .takeIf { !it.isNullOrBlank() }
            ?.let { it.toLocale() }
            ?: middlewareComponent.configurationManager.locale
        return DealersOutput(listOfNotNull(DealerPSAMapper().transformDealer(response, locale, true)))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(): DetailsResponse? =
        middlewareComponent.readSync(
            Constants.Storage.PREFERRED_DEALER,
            StoreMode.APPLICATION
        )

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveOnCache(dealer: DetailsResponse) {
        middlewareComponent.createSync(
            key = Constants.Storage.PREFERRED_DEALER,
            data = dealer,
            mode = StoreMode.APPLICATION
        )
        middlewareComponent.createSync(
            key = Constants.Storage.PREFERRED_DEALER_ID,
            data = dealer.siteGeo,
            mode = StoreMode.APPLICATION
        )
    }
}
