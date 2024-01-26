package com.mobiledrivetech.external.middleware.manager

import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import java.util.Locale

internal class ConfigurationManagerImp : ConfigurationManager {

    private var _environment: Environment? = null
    private var _brand: Brand? = null
    private var _market: Market? = null
    private var _locale: Locale? = null

    override val environment: Environment
        get() = _environment ?: Environment.DEV

    override val brand: Brand
        get() = _brand ?: Brand.DEFAULT
    override val market: Market
        get() = _market ?: Market.NONE
    override val locale: Locale
        get() = _locale ?: Locale.ENGLISH

    override fun initialize(
        component: MiddlewareComponent,
        config: ConfigurationInput
    ) {
        _environment = config.environment
        _brand = config.brand
        _locale = config.locale ?: Locale.ENGLISH
        _market = config.market
    }

    override fun update(
        component: MiddlewareComponent,
        config: ConfigurationInput
    ) {
        config.environment?.let { _environment = it }
        config.brand?.let { _brand = it }
        config.locale?.let { _locale = it }
        config.market?.let { _market = it }
    }
}
