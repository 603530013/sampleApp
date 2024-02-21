package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.model.settings.CountryResponse
import com.inetpsa.pims.spaceMiddleware.util.readAssetsJsonFile
import java.util.Locale

internal class GetLanguagesLocalesListExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseLocalExecutor<Unit, List<Locale>>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse<List<Locale>> {
        readFromJsonFile()
            ?.also { PIMSLogger.i("output: $it") }
            ?.let { data ->
                return NetworkResponse.Success(data)
            }
        return NetworkResponse.Failure(
            PIMSFoundationError.invalidParameter(Constants.Input.Configuration.LANGUAGE_PATH)
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromJsonFile(): List<Locale>? {
        val path = middlewareComponent.configurationManager.languagePath
        return path.readAssetsJsonFile<Map<String, CountryResponse>>(middlewareComponent.context)
            ?.let { parseJson(it) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun parseJson(data: Map<String, CountryResponse>): List<Locale> {
        PIMSLogger.d("data: $data")
        return data.values
            .asSequence()
            .map { transformLanguage(it) }
            .flatten()
            .toHashSet()
            .sortedBy { it.country }
            .toList()
            .also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformLanguage(country: CountryResponse): List<Locale> {
        PIMSLogger.d("country: $country")
        return country.languages
            .mapNotNull { language ->
                language.takeIf { it.isNotBlank() }
                    ?.let {
                        val items = language.split("_", "-", limit = 3, ignoreCase = true)
                        Locale(
                            items.getOrNull(0) ?: "",
                            items.getOrNull(1) ?: "",
                            items.getOrNull(2) ?: ""
                        ).takeIf { !it.country.isNullOrBlank() && !it.language.isNullOrBlank() }
                    }
            }.also { PIMSLogger.i("output: $it") }
    }
}
