package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import androidx.annotation.VisibleForTesting
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.internalGson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TermCondition
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.readJsonFromAssets

// alias type to json model
private typealias JsonModel = Map<String, Map<String, Map<String, Map<String, Map<String, TermCondition>>>>>

internal class ReadAssetsTermConditionFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseLocalExecutor<TermsConditionsInput, TermCondition>(middlewareComponent, params) {

    companion object {

        const val DEFAULT_BRAND = "defaultBrand"
        const val DEFAULT_COUNTRY = "defaultCountry"
    }

    override fun params(parameters: Map<String, Any?>?): TermsConditionsInput =
        TermsConditionsInput(
            type = parameters hasEnum Constants.Input.TYPE,
            country = parameters has Constants.Input.Settings.COUNTRY,
            sdp = parameters has Constants.Input.Settings.SDP
        )

    override suspend fun execute(input: TermsConditionsInput): NetworkResponse<TermCondition> {
        val environment = configurationManager.environment
        val userCountry = configurationManager.locale.country
        val country = when (input.type.mode) {
            TermsConditionsInput.Mode.User -> userCountry
            TermsConditionsInput.Mode.Vehicle -> input.country ?: userCountry
        }
        PIMSLogger.d("environment: $environment, country: $country, input: $input")

        val terms = fetchTermsConditions(
            environment,
            transformSdp(input.sdp?.uppercase()),
            country,
            input.type.value
        )
        return NetworkResponse.Success(terms.copy(market = country))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Throws(PIMSError::class)
    internal fun fetchTermsConditions(
        environment: Environment,
        sdpValue: String,
        country: String,
        type: String
    ): TermCondition {
        PIMSLogger.d("environment: $environment, sdpValue: $sdpValue, country: $country, type: $type")
        val path = when (environment) {
            Environment.DEV -> "fca/termcondition/tnc_int_mock.json"
            Environment.PREPROD -> "fca/termcondition/tnc_preprod_mock.json"
            Environment.PROD -> "fca/termcondition/tnc_prod_mock.json"
            else -> throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)
        }
        return readTermConditionFile(path, sdpValue, country, type)
            ?: throw PIMSFoundationError.invalidReturnParam(type)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformSdp(sdpValue: String?): String =
        when (sdpValue) {
            "IGNITE" -> "GLOBAL"
            else -> sdpValue.orEmpty()
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformBrand(brand: Brand): String =
        when (brand.name) {
            "ALFAROMEO" -> "alfa"
            else -> brand.name.lowercase()
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readTermConditionFile(
        filePath: String,
        sdp: String,
        country: String,
        type: String
    ): TermCondition? {
        PIMSLogger.d("filePath: $filePath, sdp: $sdp, country: $country, type: $type")
        val jsonString = readJsonFromAssets(middlewareComponent.context, filePath)
        val brand = transformBrand(configurationManager.brand)
        val region = configurationManager.market
        PIMSLogger.d("brand: $brand, region: $region, jsonString: $jsonString")

        val jsonType = object : TypeToken<JsonModel>() {}.type
        val cgus = internalGson.fromJson<JsonModel>(jsonString, jsonType)
        PIMSLogger.d("cgus: $cgus")

        val selectedRegion = cgus?.toSortedMap(java.lang.String.CASE_INSENSITIVE_ORDER)
            ?.get(region.value.lowercase())
            ?.toSortedMap(java.lang.String.CASE_INSENSITIVE_ORDER)
        PIMSLogger.d("selectedRegion: $selectedRegion")

        val selectedSdp = selectedRegion?.get(sdp)?.toSortedMap(java.lang.String.CASE_INSENSITIVE_ORDER)
        PIMSLogger.d("selectedSdp: $selectedSdp")

        val selectedBrand = (selectedSdp?.get(brand) ?: selectedSdp?.get(DEFAULT_BRAND.lowercase()))
            ?.toSortedMap(java.lang.String.CASE_INSENSITIVE_ORDER)
        PIMSLogger.d("selectedBrand: $selectedBrand")

        val selectedEndpoint = selectedBrand?.get(country.lowercase())
            ?: selectedBrand?.get(DEFAULT_COUNTRY.lowercase())
        PIMSLogger.d("selectedEndpoint: $selectedEndpoint")

        return selectedEndpoint?.toSortedMap(java.lang.String.CASE_INSENSITIVE_ORDER)?.get(type.lowercase())
            .also { PIMSLogger.i("output: $it") }
    }
}
