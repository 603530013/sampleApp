package com.mobiledrivetech.external.middleware.command

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.executor.BaseLocalExecutor
import com.mobiledrivetech.external.middleware.util.MiddleWareFoundationError

/**
 * BaseBrandCommand which inherits from BaseCommand. We can use this class to setup more executor
 * by judging middlewareComponent.configurationManager.brand if controlled by different brand
 */
internal abstract class BaseBrandCommand : BaseCommand() {

    /**
     * Get executor by [getCommonExecutor]
     *
     * @return the executor which is [BaseLocalExecutor]
     */
    override suspend fun getExecutor(): BaseLocalExecutor<*, *> = getCommonExecutor()

    /**
     * Get common executor
     *
     * @return the executor which is [BaseLocalExecutor]
     * @throws MiddleWareFoundationError if action type is not found
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal open suspend fun getCommonExecutor(): BaseLocalExecutor<*, *> =
        throw MiddleWareFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
}
