package com.inetpsa.pims.spaceMiddleware.executor.vehicle.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.service.ServicesOutput

internal class VehicleServicesPsaMapper {

    companion object {

        internal const val KEY_VIN: String = "[VIN]"
        internal const val KEY_TOKEN: String = "[TOKEN_CVS]"
        internal const val KEY_SCHEMA: String = "[Mymark]"
    }

    fun transform(
        services: List<VehicleDetailsResponse.ServicesConnected>?,
        schema: String?,
        encryptedVin: String?,
        token: String?
    ): ServicesOutput =
        services.orEmpty()
            .map { transformService(it, schema, encryptedVin, token) }
            .let { ServicesOutput(it) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformService(
        service: VehicleDetailsResponse.ServicesConnected,
        schema: String?,
        encryptedVin: String?,
        token: String?
    ): ServicesOutput.Service =
        ServicesOutput.Service(
            id = service.id,
            title = service.title,
            url = transformUrl(service.urlCvs, schema, encryptedVin, token),
            category = service.category,
            description = service.description,
            price = service.price,
            currency = service.currency,
            offer = service.offer?.let { offer ->
                ServicesOutput.Service.Offer(
                    pricingModel = offer.pricingModel,
                    fromPrice = offer.fromPrice,
                    price = offer.price?.let { price ->
                        ServicesOutput.Service.Offer.Price(
                            periodType = price.periodType,
                            price = price.price,
                            currency = price.currency,
                            typeDiscount = price.typeDiscount
                        )
                    },
                    isFreeTrial = offer.isFreeTrial
                )
            }
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformUrl(
        urlCvs: String?,
        schema: String?,
        encryptedVin: String?,
        token: String?
    ): String? =
        urlCvs?.replace(KEY_VIN, encryptedVin.orEmpty(), true)
            ?.replace(KEY_TOKEN, token.orEmpty(), true)
            ?.replace(KEY_SCHEMA, schema.orEmpty(), true)
    /* https://services-store.peugeot.fr/login-redirect
        ?xcsrf=[VIN]&jwt=[TOKEN_CVS]&inboundApplication=[Mymark]
        &redirect-url=https://services-store.peugeot.fr/store/pack-navigation-connectee-0" */
}
