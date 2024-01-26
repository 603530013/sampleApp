package com.mobiledrivetech.external.middleware.manager

import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import java.util.Locale

// Todo: For now we don't use it but we will use it in the future to manage the configuration
internal interface ConfigurationManager {
    val environment: Environment
    val brand: Brand
    val locale: Locale
    val market: Market

    fun initialize(
        component: MiddlewareComponent,
        config: ConfigurationInput
    )

    fun update(
        component: MiddlewareComponent,
        config: ConfigurationInput
    )
}
