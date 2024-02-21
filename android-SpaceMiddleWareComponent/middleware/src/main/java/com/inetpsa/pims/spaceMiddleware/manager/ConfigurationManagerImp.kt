package com.inetpsa.pims.spaceMiddleware.manager

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup
import com.inetpsa.pims.spaceMiddleware.model.manager.Config
import com.inetpsa.pims.spaceMiddleware.util.asGroup
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.readSync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.Locale

internal class ConfigurationManagerImp : ConfigurationManager {

    private var _environment: Environment? = null
    private var _brandGroup: BrandGroup? = null
    private var _brandCode: String? = null
    private var _brand: Brand? = null
        set(value) {
            field = value
            _brandGroup = field.asGroup()
            _brandCode = if (_brandGroup == BrandGroup.PSA) {
                toBrandCode(field)
            } else {
                null
            }
        }

    private var _googleApiKey: String? = null
    private var _market: Market? = null
    private var _siteCode: String? = null
    private var _languagePath: String? = null
    private var _locale: Locale? = null

    override val environment: Environment
        get() = _environment ?: throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)

    override val brand: Brand
        get() = _brand ?: throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)

    override val brandGroup: BrandGroup
        get() = _brandGroup ?: throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)

    override val brandCode: String
        get() = _brandCode ?: throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)

    override val googleApiKey: String
        get() = _googleApiKey ?: throw PIMSFoundationError.invalidParameter(Constants.PARAMS_KEY_GOOGLE_API_KEY)

    override val market: Market
        get() = _market ?: throw PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)

    override val siteCode: String
        get() = _siteCode
            ?: throw PIMSFoundationError.invalidParameter(Constants.Input.Configuration.SITE_CODE)
    override val languagePath: String
        get() = _languagePath
            ?: throw PIMSFoundationError.invalidParameter(Constants.Input.Configuration.LANGUAGE_PATH)

    override val locale: Locale
        get() = _locale ?: Locale.ENGLISH

    override fun initialize(
        component: MiddlewareComponent,
        config: Config
    ) {
        _environment = config.environment
        _brand = config.brand

        _googleApiKey = config.googleApiKey
        when (config.locale) {
            null -> {
                val locale = read<String>(component, Constants.Storage.LANGUAGE, StoreMode.APPLICATION)
                _locale = Locale.forLanguageTag(locale)
            }

            else -> {
                _locale = config.locale
                save(component, Constants.Storage.LANGUAGE, config.locale.toLanguageTag(), StoreMode.APPLICATION)
            }
        }

        _market = config.market
        _siteCode = config.siteCode

        _languagePath = config.languagePath
    }

    override fun update(
        component: MiddlewareComponent,
        config: Config
    ) {
        config.environment?.let { _environment = it }
        config.brand?.let { _brand = it }

        config.googleApiKey?.let { _googleApiKey = it }

        config.locale?.let {
            _locale = it
            save(component, Constants.Storage.LANGUAGE, it.toLanguageTag(), StoreMode.APPLICATION)
        }
        config.market?.let { _market = it }
        config.siteCode?.let { _siteCode = it }

        config.languagePath?.let { _languagePath = it }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun toBrandCode(brand: Brand?): String = when (brand) {
        Brand.PEUGEOT -> "AP"
        Brand.DS -> "DS"
        Brand.CITROEN -> "AC"
        Brand.OPEL -> "OP"
        Brand.VAUXHALL -> "VX"
        else -> throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun save(component: MiddlewareComponent, key: String, data: Any, mode: StoreMode) {
        runBlocking(Dispatchers.IO) {
            component.createSync(key, data, mode)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal inline fun <reified T> read(component: MiddlewareComponent, key: String, mode: StoreMode): T? =
        runBlocking(Dispatchers.IO) {
            component.readSync<T>(key, mode)
        }
}
