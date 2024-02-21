package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market

internal fun Environment?.asFcaEnvironmentLink(): String = when (this) {
    Environment.PREPROD -> "prep."
    Environment.DEV -> "intg."
    Environment.PROD -> ""
    else -> throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)
}

internal fun Environment?.asPsaEnvironmentLink(): String = when (this) {
    Environment.DEV -> "-rfrec"
    Environment.PREPROD -> "-preprod"
    Environment.PROD -> ""
    else -> throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)
}

internal fun Market?.asRegionLink(): String = when (this) {
    Market.EMEA -> "-01"
    Market.LATAM, Market.NAFTA -> "-02"
    else -> throw PIMSFoundationError.invalidParameter("market")
}
