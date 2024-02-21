package com.inetpsa.pims.spaceMiddleware.command.features

import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.features.get.GetFeaturesFcaExecutor

internal class FeaturesCommandGet : BaseBrandCommand() {
    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = GetFeaturesFcaExecutor(this)
}
