package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.ApplicationTermsOutput
import com.inetpsa.pims.spaceMiddleware.util.formatToQuery
import com.inetpsa.pims.spaceMiddleware.util.map
import java.time.Instant
import java.time.ZoneOffset

internal class GetAppTermsPSAExecutor(command: BaseCommand) :
    BasePsaExecutor<Unit, ApplicationTermsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse<ApplicationTermsOutput> {
        return GetSettingsPSAExecutor(middlewareComponent, params)
            .execute()
            .map { settings ->
                val cgu = settings.cguWebView

                val timestamp = settings.updateCGU

                when (cgu.isNullOrBlank() || timestamp?.takeIf { it > 0 } == null) {
                    true -> throw PIMSFoundationError.invalidReturnParam(Constants.Input.ActionType.APP_TERMS)

                    else -> {
                        val locale = configurationManager.locale
                        ApplicationTermsOutput(
                            url = generateFullPath(cgu),
                            update = formatTimeStamp(timestamp!!),
                            country = locale.country,
                            language = locale.language
                        )
                    }
                }
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateFullPath(cgu: String): String =
        with(StringBuilder(baseUrl(arrayOf("/cms", cgu)))) {
            append(baseQueries().formatToQuery())
            toString()
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun formatTimeStamp(timestamp: Long): String =
        Instant.ofEpochSecond(timestamp)
            .atZone(ZoneOffset.UTC)
            .toOffsetDateTime()
            .toString()
}
