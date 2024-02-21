package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
@Deprecated("try to switch to use this class SetRemoveVehiclePsaExecutor")
internal class RemoveVehiclePsaExecutor(command: BaseCommand) : BasePsaExecutor<String, Unit>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<Unit> {
        val request = request(
            Unit::class.java,
            arrayOf("/me/v1/vehicle/", input)
        )

        return communicationManager.delete(request, MiddlewareCommunicationManager.MymToken)
    }
}
