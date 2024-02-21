package com.inetpsa.pims.spaceMiddleware.model.configuration

import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market
import java.util.Locale

internal data class ConfigurationInput(
    val environment: Environment? = null,
    val brand: Brand? = null,
    val googleApiKey: String? = null,
    val locale: Locale? = null,
    val market: Market? = null,
    val siteCode: String? = null,
    val languagePath: String? = null
)
