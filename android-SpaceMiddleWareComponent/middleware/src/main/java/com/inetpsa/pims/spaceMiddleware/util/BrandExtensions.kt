package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup

internal fun Brand?.asGroup(): BrandGroup = when (this) {
    Brand.ALFAROMEO,
    Brand.FIAT,
    Brand.MASERATI,
    Brand.RAM,
    Brand.JEEP,
    Brand.WAGONEER,
    Brand.DODGE,
    Brand.CHRYSLER,
    Brand.LANCIA,
    Brand.ABARTH,
    Brand.MOPAR -> BrandGroup.FCA

    Brand.PEUGEOT,
    Brand.DS,
    Brand.CITROEN,
    Brand.OPEL,
    Brand.VAUXHALL -> BrandGroup.PSA

    else -> BrandGroup.UNKNOWN
}
