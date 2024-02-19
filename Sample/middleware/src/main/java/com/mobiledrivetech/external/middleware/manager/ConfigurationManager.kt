package com.mobiledrivetech.external.middleware.manager

import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import java.util.Locale
/**
 * Configuration manager
 *
 * @property environment with [Environment]
 * @property brand with [Brand]
 * @property locale with [Locale]
 * @property market with [Market]
 * Todo: For now we don't use it but we will use it in the future to manage the configuration
 */
internal interface ConfigurationManager {
    val environment: Environment
    val brand: Brand
    val locale: Locale
    val market: Market

    /**
     * Initialize configuration
     *
     * @param component with component for initialization. See [MiddlewareComponent]
     * @param config with configuration for initialization
     */
    fun initialize(
        component: MiddlewareComponent,
        config: ConfigurationInput
    )

    /**
     * Update configuration
     *
     * @param component with component for update. See [MiddlewareComponent]
     * @param config with configuration for update
     */
    fun update(
        component: MiddlewareComponent,
        config: ConfigurationInput
    )
}
