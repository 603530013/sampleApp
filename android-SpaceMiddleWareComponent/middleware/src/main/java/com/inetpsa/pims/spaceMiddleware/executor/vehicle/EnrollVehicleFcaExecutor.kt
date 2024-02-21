package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors

@Deprecated("should replace with AddVehicleFcaExecutor")
internal class EnrollVehicleFcaExecutor(command: BaseCommand) : BaseFcaExecutor<Boolean, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): Boolean = parameters has Constants.PARAM_CONNECTED

    override suspend fun execute(input: Boolean): NetworkResponse<Unit> =
        input.takeIf { !it }?.let { EnrollNonConnectedVehicleFcaExecutor(middlewareComponent, params).execute() }
            ?: throw PimsErrors.apiNotSupported()
}
