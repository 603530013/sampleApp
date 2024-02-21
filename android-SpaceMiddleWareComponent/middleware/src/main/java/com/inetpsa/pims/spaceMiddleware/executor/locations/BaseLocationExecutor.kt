package com.inetpsa.pims.spaceMiddleware.executor.locations

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseExecutor

internal abstract class BaseLocationExecutor<P, out R>(command: BaseCommand) : BaseExecutor<P, R>(command) {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal val googleApiKey: String
        @Throws(PIMSError::class)
        get() = configurationManager.googleApiKey

    override val communicationManager: ICommunicationManager
        get() = middlewareComponent.communicationManager

    final override fun baseUrl(args: Array<String>): String = buildString {
        append("https://maps.googleapis.com/maps/api/")
        args.forEach { append(it) }
    }

    override fun baseHeaders(): Map<String, String> = emptyMap()

    override fun baseQueries(): Map<String, String> =
        mapOf("language" to middlewareComponent.configurationManager.locale.toLanguageTag())
}
