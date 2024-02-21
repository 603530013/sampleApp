package com.inetpsa.pims.spaceMiddleware.executor

import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.util.asPsaEnvironmentLink

internal abstract class BasePsaExecutor<P, out R>(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseExecutor<P, R>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    final override val communicationManager: ICommunicationManager
        get() = middlewareComponent.middlewareCommunicationManager

    override fun baseUrl(args: Array<String>): String {
        val environmentLink = configurationManager.environment.asPsaEnvironmentLink()
        return buildString {
            append("https://microservices")
            append(environmentLink)
            append(".mym.awsmpsa.com")
            args.forEach { append(it) }
        }
    }

    final override fun baseQueries(): Map<String, String> {
        val locale = configurationManager.locale
        return mapOf(
            Constants.QUERY_PARAM_KEY_BRAND to configurationManager.brandCode,
            Constants.QUERY_PARAM_KEY_SOURCE to Constants.QUERY_PARAM_KEY_APP,
            Constants.QUERY_PARAM_KEY_VERSION to middlewareComponent.applicationVersion,
            Constants.QUERY_PARAM_KEY_SITE_CODE to configurationManager.siteCode,
            Constants.QUERY_PARAM_KEY_CULTURE to locale.toString(),
            Constants.QUERY_PARAM_KEY_LANGUAGE to locale.language,
            Constants.QUERY_PARAM_KEY_OS to Constants.QUERY_PARAM_VALUE_OS
        )
    }

    final override fun baseHeaders(): Map<String, String> = emptyMap()
}
