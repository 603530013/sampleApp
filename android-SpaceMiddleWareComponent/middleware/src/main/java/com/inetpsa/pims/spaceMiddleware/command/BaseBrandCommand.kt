package com.inetpsa.pims.spaceMiddleware.command

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup

internal abstract class BaseBrandCommand : BaseCommand() {

    override suspend fun getExecutor(): BaseLocalExecutor<*, *> =
        when (middlewareComponent.configurationManager.brandGroup) {
            BrandGroup.FCA -> getCommonExecutor() ?: getFcaExecutor()
            BrandGroup.PSA -> getCommonExecutor() ?: getPsaExecutor()

            else -> throw PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal open suspend fun getPsaExecutor(): BasePsaExecutor<*, *> =
        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal open suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> =
        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal open suspend fun getCommonExecutor(): BaseLocalExecutor<*, *>? = null
}
