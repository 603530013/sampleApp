package com.inetpsa.pims.spaceMiddleware.command.vehicle

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleCheckFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleCheckPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleContractsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleContractsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleDetailsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleDetailsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleManualFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleManualPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleServicesPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesPsaExecutor

internal class VehicleCommandGet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.LIST -> GetVehiclesPsaExecutor(this)
        Constants.Input.ActionType.DETAILS -> GetVehicleDetailsPsaExecutor(this)
        Constants.Input.ActionType.CONTRACTS -> GetVehicleContractsPsaExecutor(this)
        Constants.Input.ActionType.CHECK -> GetVehicleCheckPsaExecutor(this)
        Constants.Input.ActionType.SERVICES -> GetVehicleServicesPsaExecutor(this)
        Constants.Input.ActionType.MANUAL -> GetVehicleManualPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.LIST -> GetVehiclesFcaExecutor(this)
        Constants.Input.ActionType.DETAILS -> GetVehicleDetailsFcaExecutor(this)
        Constants.Input.ActionType.CONTRACTS -> GetVehicleContractsFcaExecutor(this)
        Constants.Input.ActionType.CHECK -> GetVehicleCheckFcaExecutor(this)
        Constants.Input.ActionType.MANUAL -> GetVehicleManualFcaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }
}
