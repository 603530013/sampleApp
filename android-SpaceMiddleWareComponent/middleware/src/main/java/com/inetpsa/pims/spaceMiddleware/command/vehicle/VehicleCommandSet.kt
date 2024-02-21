package com.inetpsa.pims.spaceMiddleware.command.vehicle

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.set.AddVehicleFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.set.AddVehiclePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.set.NicknameUpdateFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.set.RemoveVehiclePsaExecutor

internal class VehicleCommandSet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.REMOVE -> RemoveVehiclePsaExecutor(this)
        Constants.Input.ActionType.ADD -> AddVehiclePsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        // Constants.Input.ActionType.REMOVE -> RemoveVehicleFcaExecutor(this)
        Constants.Input.ActionType.ADD -> AddVehicleFcaExecutor(this)
        Constants.Input.ActionType.UPDATE -> handleFcaContactExecutor()
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleFcaContactExecutor(): BaseFcaExecutor<*, *> =
        when (parameters.has<String>(Constants.Input.ACTION)) {
            Constants.Input.Action.NICKNAME -> NicknameUpdateFCAExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }
}
