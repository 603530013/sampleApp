package com.inetpsa.pims.spaceMiddleware.command.contracts

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.contracts.GetServiceListPsaExecutor

@Deprecated("this has been moved inside VehicleCommandGet")
internal class GetContractsCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_VEHICLE_DETAILS -> GetServiceListPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
