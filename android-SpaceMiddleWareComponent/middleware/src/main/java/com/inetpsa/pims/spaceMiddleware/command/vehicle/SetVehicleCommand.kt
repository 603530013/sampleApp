package com.inetpsa.pims.spaceMiddleware.command.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.EnrollVehicleFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.EnrollVehiclePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.RemoveVehicleFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.RemoveVehiclePsaExecutor

@Deprecated("this should be replaced with VehicleCommandSet")
internal class SetVehicleCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_TYPE_VEHICLE_REMOVE -> RemoveVehiclePsaExecutor(this)
        Constants.PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT -> EnrollVehiclePsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_TYPE_VEHICLE_REMOVE -> RemoveVehicleFcaExecutor(this)
        Constants.PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT -> EnrollVehicleFcaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
