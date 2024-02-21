package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.CheckTermsLinkInput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.unwrapNullable

internal class CheckTermsLinkExecutor<out R : Any>(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null,
    val type: Class<out R>
) : BaseLocalExecutor<CheckTermsLinkInput, R>(middlewareComponent, params) {

    companion object {

        inline operator fun <reified T : Any> invoke(
            middlewareComponent: MiddlewareComponent,
            params: Map<String, Any?>? = null
        ) = CheckTermsLinkExecutor(middlewareComponent, params, T::class.java)
    }

    private val communicationManager = middlewareComponent.communicationManager

    override fun params(parameters: Map<String, Any?>?) = CheckTermsLinkInput("", "")

    override suspend fun execute(input: CheckTermsLinkInput): NetworkResponse<R> {
        val brand = configurationManager.brand.name.lowercase()
        val locale = configurationManager.locale
        val currentLanguage = locale.language.takeIf { input.country.equals(locale.country, true) }
        PIMSLogger.d("brand: $brand, locale: $locale, currentLanguage: $currentLanguage")

        // retrieve supported languages from the file that related to selected country
        val languages = fetchLanguages(input.country, currentLanguage)

        return fetchLinkByLanguage(
            brand = brand,
            country = input.country,
            url = input.url,
            languages = languages
        ).also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun fetchLanguages(
        country: String,
        currentLanguage: String? = null
    ): List<String> {
        PIMSLogger.d("country: $country, currentLanguage: $currentLanguage")
        return GetLanguagesLocalesListExecutor(middlewareComponent, params)
            .execute()
            .unwrapNullable()
            ?.groupBy { it.country }
            ?.get(country)
            ?.map { it.language }
            ?.filter { !it.equals(currentLanguage, true) }
            ?.let { listOfNotNull(currentLanguage) + it }
            ?.also { PIMSLogger.i("output: $it") }
            ?: listOfNotNull(currentLanguage).also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun fetchLinkByLanguage(
        brand: String,
        country: String,
        url: String,
        languages: List<String>
    ): NetworkResponse<R> {
        PIMSLogger.d("brand: $brand, country: $country, url: $url, languages: $languages")
        val error = PimsErrors.invalidServerUrl(url)
        var lastResponse: NetworkResponse<R> = NetworkResponse.Failure(error)
        return languages.firstNotNullOfOrNull { language ->
            val link = generateLink(url, brand, country, language)
            val request = generateRequest(link)

            lastResponse = communicationManager
                .get<R>(request, TokenType.NotConnectedAWSToken(FCAApiKey.SDP))
                .handleResponse(link = link)
                .also { PIMSLogger.i("output: $it") }
            lastResponse.takeIf { it is NetworkResponse.Success }
        } ?: lastResponse.also { PIMSLogger.i("output: $it") }
    }

    private fun NetworkResponse<R>.handleResponse(
        link: String
    ): NetworkResponse<R> {
        PIMSLogger.d("link: $link")
        return when (this) {
            is NetworkResponse.Success -> {
                when (type) {
                    String::class.java -> (NetworkResponse.Success(link) as NetworkResponse.Success<R>)

                    else -> this
                }
            }

            is NetworkResponse.Failure -> this
        }.also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateLink(
        url: String,
        brand: String,
        country: String,
        language: String
    ): String {
        PIMSLogger.d("url: $url, brand: $brand, country: $country, language: $language")
        return url.replace("{brand}", brand)
            .replace("{market}", country.lowercase())
            .replace("{language}", language)
            .also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateRequest(link: String): NetworkRequest = NetworkRequest(
        type = type,
        url = Uri.parse(link),
        headers = null,
        body = null,
        queries = null
    ).also { PIMSLogger.i("output: $it") }
}
