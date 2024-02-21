package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.assistance.AssistanceOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AssistanceResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetAssistancePSAExecutor(command: BaseCommand) : BasePsaExecutor<String, AssistanceOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_ID

    override suspend fun execute(input: String): NetworkResponse<AssistanceOutput> {
        val request = request(
            AssistanceResponse::class.java,
            arrayOf("/car/v1/rsa/assistance/", input)
        )

        return communicationManager.get<AssistanceResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { AssistanceMapper().transformToAssistanceOutput(it) }
    }
}
