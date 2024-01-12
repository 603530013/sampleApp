package com.mobiledrivetech.external.middleware.command

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError

internal abstract class BaseBrandCommand : BaseCommand() {

    //setup more executor by judging middlewareComponent.configurationManager.brand
    // if controlled by different brand
    override suspend fun getExecutor(): BaseLocalExecutor<*, *> = getCommonExecutor()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal open suspend fun getCommonExecutor(): BaseLocalExecutor<*, *> =
        throw MiddleWareFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
}
