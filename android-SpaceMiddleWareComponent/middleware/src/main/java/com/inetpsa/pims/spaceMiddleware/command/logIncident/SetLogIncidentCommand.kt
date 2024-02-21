package com.inetpsa.pims.spaceMiddleware.command.logIncident

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.logIncident.CreateLogIncidentPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.logIncident.UploadLogIncidentPsaExecutor

internal class SetLogIncidentCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_VEHICLE_ENROLL -> CreateLogIncidentPsaExecutor(this)
        Constants.PARAM_ACTION_VEHICLE_UPLOAD -> UploadLogIncidentPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
