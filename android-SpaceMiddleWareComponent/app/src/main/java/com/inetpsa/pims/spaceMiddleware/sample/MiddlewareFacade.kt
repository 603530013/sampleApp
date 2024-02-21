package com.inetpsa.pims.spaceMiddleware.sample

import android.content.Context
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.pims.spaceMiddleware.IMiddleware
import com.inetpsa.pims.uisample.parser.GenericComponentSampleInterface
import com.inetpsa.pims.uisample.utils.Constants

class MiddlewareFacade(private val middleware: IMiddleware) : GenericComponentSampleInterface {

    override val serviceName: String = "middleware"
    override val name: String = "MiddlewareComponent"
    override val version: String = com.inetpsa.pims.spaceMiddleware.BuildConfig.VERSION_NAME

    override fun configure(parameters: Map<String, Any>) {
        PIMSLogger.e("parameters: $parameters")
    }

    override fun get(api: String, parameters: Map<String, Any>?, callback: (Map<String, Any?>) -> Unit): String {
        PIMSLogger.e("parameters: $parameters")
        return middleware.get(api, parameters, callback)
    }

    override fun initialize(context: Context, parameters: Map<String, Any>, callback: (Map<String, Any>) -> Unit) {
        PIMSLogger.e("parameters: $parameters")
        val newParams = parameters.toMutableMap()
            .apply {
                val profile: MutableMap<String, Any>? = (get("profile") as? Map<String, Any>)
                    ?.toMutableMap()
                    ?.apply {
                        val psaBrands = listOf(
                            Brand.CITROEN.name,
                            Brand.OPEL.name,
                            Brand.DS.name,
                            Brand.PEUGEOT.name,
                            Brand.VAUXHALL.name
                        )
                        if (psaBrands.contains(get(Constants.BRAND))) {
                            put("languagePath", "assets://languages/preprod_ac_cultures.json")
                        } else {
                            put("languagePath", "assets://languages/emea-maserati-cultures.json")
                        }
                    }
                put("profile", profile.orEmpty())
            }
        PIMSLogger.e("newParams: $newParams")
        middleware.initialize(newParams, callback)
    }

    override fun release() {
        middleware.release()
        PIMSLogger.e("")
    }

    override fun set(api: String, parameters: Map<String, Any>, callback: (Map<String, Any?>) -> Unit): String {
        PIMSLogger.e("parameters: $parameters")
        return middleware.set(api, parameters, callback)
    }

    override fun subscribe(api: String, parameters: Map<String, Any>?, callback: (Map<String, Any?>) -> Unit): String {
        PIMSLogger.e("parameters: $parameters")
        return ""
    }

    override fun unsubscribe(api: String, callback: (Map<String, Any?>) -> Unit): String {
        PIMSLogger.e("api: $api")
        return ""
    }
}
