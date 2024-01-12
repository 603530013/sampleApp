package com.mobiledrivetech.external.middleware.manager

import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.MiddlewareComponent
import com.mobiledrivetech.external.middleware.foundation.models.Brand
import com.mobiledrivetech.external.middleware.foundation.models.Environment
import com.mobiledrivetech.external.middleware.foundation.models.Market
import com.mobiledrivetech.external.middleware.model.configuration.ConfigurationInput
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError
import java.util.Locale

internal class ConfigurationManagerImp : ConfigurationManager {

    private var _environment: Environment? = null
    private var _brandCode: String? = null
    private var _brand: Brand? = null
    private var _googleApiKey: String? = null
    private var _market: Market? = null
    private var _siteCode: String? = null
    private var _languagePath: String? = null
    private var _locale: Locale? = null

    override val environment: Environment
        get() = _environment ?: throw MiddleWareFoundationError.invalidParameter(
            Constants.CONTEXT_KEY_ENVIRONMENT
        )

    override val brand: Brand
        get() = _brand
            ?: throw MiddleWareFoundationError.invalidParameter(Constants.CONTEXT_KEY_BRAND)

    override val brandCode: String
        get() = _brandCode
            ?: throw MiddleWareFoundationError.invalidParameter(Constants.CONTEXT_KEY_BRAND)

    override val googleApiKey: String
        get() = _googleApiKey
            ?: throw MiddleWareFoundationError.invalidParameter(Constants.PARAMS_KEY_GOOGLE_API_KEY)

    override val market: Market
        get() = _market
            ?: throw MiddleWareFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)

    override val siteCode: String
        get() = _siteCode
            ?: throw MiddleWareFoundationError.invalidParameter(Constants.Input.Configuration.SITE_CODE)
    override val languagePath: String
        get() = _languagePath
            ?: throw MiddleWareFoundationError.invalidParameter(Constants.Input.Configuration.LANGUAGE_PATH)

    override val locale: Locale
        get() = _locale ?: Locale.ENGLISH

    override fun initialize(
        component: MiddlewareComponent,
        config: ConfigurationInput
    ) {
        _environment = config.environment
        _brand = config.brand
        _googleApiKey = config.googleApiKey
        _locale = config.locale ?: Locale.ENGLISH
        _market = config.market
        _siteCode = config.siteCode
        _languagePath = config.languagePath
    }

    override fun update(
        component: MiddlewareComponent,
        config: ConfigurationInput
    ) {
        config.environment?.let { _environment = it }
        config.brand?.let { _brand = it }
        config.googleApiKey?.let { _googleApiKey = it }
        config.locale?.let {
            _locale = it
        }
        config.market?.let { _market = it }
        config.siteCode?.let { _siteCode = it }
        config.languagePath?.let { _languagePath = it }
    }
}
