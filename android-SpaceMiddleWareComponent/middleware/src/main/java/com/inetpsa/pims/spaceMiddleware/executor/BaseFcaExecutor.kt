package com.inetpsa.pims.spaceMiddleware.executor

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.util.asFcaEnvironmentLink
import com.inetpsa.pims.spaceMiddleware.util.asRegionLink

internal abstract class BaseFcaExecutor<P, out R>(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseExecutor<P, R>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    final override val communicationManager: ICommunicationManager
        get() = middlewareComponent.communicationManager

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val uid: String
        @Throws(PIMSError::class)
        get() = middlewareComponent.userSessionManager.getUserSession()?.customerId
            ?: throw PIMSFoundationError.missingParameter("uid")

    override fun baseUrl(args: Array<String>): String {
        val regionLink = configurationManager.market.asRegionLink().takeIf { it.isNotBlank() }?.plus(".")
            ?: throw PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)
        val environmentLink = configurationManager.environment.asFcaEnvironmentLink()
        return buildString {
            append("https://channels.sdpr")
            append(regionLink)
            append(environmentLink)
            append("fcagcv.com")
            args.forEach { append(it) }
        }
    }

    final override fun baseHeaders(): Map<String, String> = mapOf(
        // Constants.AWS_HEADER_KEY_SERVICE_NAME to Constants.AWS_HEADER_VALUE_SERVICE_NAME,
        Constants.LOCALE to getLocale()
    )

    final override fun baseQueries(): Map<String, String> = emptyMap()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getLocale(): String = configurationManager.locale.let { "${it.language}_${it.country}" }
}
