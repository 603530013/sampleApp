package com.inetpsa.pims.spaceMiddleware.manager

import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup
import com.inetpsa.pims.spaceMiddleware.model.manager.Config
import java.util.Locale

internal interface ConfigurationManager {

    val environment: Environment
    val brand: Brand
    val brandGroup: BrandGroup
    val brandCode: String

    val googleApiKey: String
    val locale: Locale

    val market: Market

    val siteCode: String
    val languagePath: String

    fun initialize(
        component: MiddlewareComponent,
        config: Config
    )

    fun update(
        component: MiddlewareComponent,
        config: Config
    )
}
