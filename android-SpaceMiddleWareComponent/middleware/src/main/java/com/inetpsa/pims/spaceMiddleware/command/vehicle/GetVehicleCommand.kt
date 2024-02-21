package com.inetpsa.pims.spaceMiddleware.command.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.GetVehicleImageFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.GetVehicleImagePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.GetVehiclesFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.GetVehiclesPsaExecutor

@Deprecated("this should be replaced with VehicleCommandGet")
internal class GetVehicleCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_TYPE_VEHICLE_IMAGE -> GetVehicleImagePsaExecutor(this)
        Constants.PARAM_ACTION_TYPE_VEHICLE_LIST -> GetVehiclesPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_TYPE_VEHICLE_IMAGE -> GetVehicleImageFcaExecutor(this)
        Constants.PARAM_ACTION_TYPE_VEHICLE_LIST -> GetVehiclesFcaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
