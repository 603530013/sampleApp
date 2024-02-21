package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.GetReviewInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.GetReviewOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.ReviewResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetDealerReviewPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<GetReviewInput, GetReviewOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): GetReviewInput =
        GetReviewInput(
            parameters has Constants.PARAM_VIN,
            parameters has Constants.BODY_PARAM_VEHICLE_ID_TYPE,
            parameters has Constants.BODY_PARAM_SERVICE_TYPE
        )

    override suspend fun execute(input: GetReviewInput): NetworkResponse<GetReviewOutput> {
        val queries = mapOf(
            Constants.BODY_PARAM_VEHICLE_ID_TYPE to input.vehicleIdType,
            Constants.BODY_PARAM_SERVICE_TYPE to input.serviceType
        )

        val request = request(
            ReviewResponse::class.java,
            arrayOf("/shop/v1/reviews/service/settings/", input.vin),
            queries = queries
        )
        return communicationManager.get<ReviewResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { response ->
                saveOnCache(response)
                transformToGetReviewOutput(response)
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveOnCache(response: ReviewResponse) =
        middlewareComponent.createSync(
            key = Constants.Storage.PREFERRED_DEALER,
            data = response,
            mode = StoreMode.APPLICATION
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToGetReviewOutput(response: ReviewResponse): GetReviewOutput =
        GetReviewOutput(
            reviewMaxDate = response.reviewMaxDate,
            reviewMaxMonth = response.reviewMaxMonth,
            reviewMinDelta = response.reviewMinDelta,
            reviewMaxChar = response.reviewMaxChar,
            ratingNegativeFloor = response.ratingNegativeFloor,
            cguLink = response.cguLink,
            allowed = response.allowed
        )
}
