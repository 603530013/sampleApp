package com.inetpsa.pims.spaceMiddleware.executor.logIncident

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor

internal abstract class BaseLogIncidentPsaExecutor<P, out R>(command: BaseCommand) : BasePsaExecutor<P, R>(command) {

    override fun baseUrl(args: Array<String>): String {
        val url: String = params has Constants.PARAM_URL
        return buildString {
            append(url)
            args.forEach { append(it) }
        }
    }
}
