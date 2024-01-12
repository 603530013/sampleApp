package com.mobiledrivetech.external.middleware.model.configuration

import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
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