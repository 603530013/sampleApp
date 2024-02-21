package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import android.util.Base64
import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.LocalApplicationTermsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TermCondition
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.ApplicationTermsOutput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.CheckTermsLinkInput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput
import com.inetpsa.pims.spaceMiddleware.util.compareVersion
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync
import com.inetpsa.pims.spaceMiddleware.util.transform
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class FetchTermConditionFCAExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<TermsConditionsInput, ApplicationTermsOutput>(middlewareComponent, params) {

    override fun baseUrl(args: Array<String>): String =
        args.firstOrNull().orEmpty()

    override fun params(parameters: Map<String, Any?>?): TermsConditionsInput =
        TermsConditionsInput(
            type = parameters hasEnum Constants.Input.TYPE,
            country = parameters has Constants.Input.Settings.COUNTRY,
            sdp = parameters has Constants.Input.Settings.SDP
        )

    override suspend fun execute(input: TermsConditionsInput): NetworkResponse<ApplicationTermsOutput> {
        PIMSLogger.d("input: $input")
        return ReadAssetsTermConditionFcaExecutor(middlewareComponent, params)
            .execute(input)
            .transform {
                when (it.mode) {
                    TermCondition.Mode.BROWSER -> transformFromBrowser(it.url, it.market.orEmpty())
                    TermCondition.Mode.LOCAL -> transformFromLocal(input.type.value, it.url, it.market.orEmpty())
                }
            }.also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun transformFromBrowser(
        url: String,
        market: String
    ): NetworkResponse<ApplicationTermsOutput> {
        PIMSLogger.d("url: $url, market: $market")
        return CheckTermsLinkExecutor.invoke<String>(middlewareComponent, params)
            .execute(CheckTermsLinkInput(market, url))
            .map { link -> ApplicationTermsOutput(url = link) }
            .also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun transformFromLocal(
        storageKey: String,
        url: String,
        market: String
    ): NetworkResponse<ApplicationTermsOutput> {
        PIMSLogger.d("storageKey: $storageKey, url: $url, market: $market")
        return CheckTermsLinkExecutor.invoke<LocalApplicationTermsResponse>(middlewareComponent, params)
            .execute(CheckTermsLinkInput(market, url))
            .map { response -> transformResponse(storageKey, response) }
            .also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun transformResponse(
        storageKey: String,
        response: LocalApplicationTermsResponse
    ): ApplicationTermsOutput {
        PIMSLogger.d("storageKey: $storageKey, response: $response")
        val content = response.copy.takeIf { !it.isNullOrBlank() }?.let { String(Base64.decode(it, Base64.DEFAULT)) }
        val version = response.version.orEmpty()

        return ApplicationTermsOutput(
            content = content,
            version = version,
            update = checkVersion(storageKey, version),
            language = response.language,
            country = response.country
        ).also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun checkVersion(storageKey: String, version: String): String {
        PIMSLogger.d("storageKey: $storageKey, version: $version")
        val lastUpdate = read("${storageKey}_${Constants.Storage.CGU_LAST_UPDATE}")
        val time = ZonedDateTime.now(ZoneOffset.UTC).toString()
        PIMSLogger.d("lastUpdate: $lastUpdate, time: $time")

        return if (lastUpdate.isNullOrBlank()) {
            save("${storageKey}_${Constants.Storage.CGU_LAST_UPDATE}", time)
            save("${storageKey}_${Constants.Storage.CGU_VERSION}", version)
            time
        } else {
            val cachedVersion = read(Constants.Storage.CGU_VERSION)
            val hasNewVersion = cachedVersion compareVersion version < 0
            PIMSLogger.d("hasNewVersion: $hasNewVersion, cachedVersion: $cachedVersion")

            if (hasNewVersion) {
                save("${storageKey}_${Constants.Storage.CGU_LAST_UPDATE}", time)
                save("${storageKey}_${Constants.Storage.CGU_VERSION}", version)
                time
            } else {
                lastUpdate
            }
        }.also { PIMSLogger.i("output: $it") }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun save(key: String, data: Any) {
        middlewareComponent.createSync(key, data, StoreMode.APPLICATION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun read(key: String): String? =
        middlewareComponent.readSync<String>(key, StoreMode.APPLICATION)
            .also { PIMSLogger.i("output: $it") }
}
