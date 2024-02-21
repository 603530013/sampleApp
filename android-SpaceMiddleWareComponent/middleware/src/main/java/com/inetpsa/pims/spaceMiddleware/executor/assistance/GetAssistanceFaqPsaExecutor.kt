package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetSettingsPSAExecutor
import com.inetpsa.pims.spaceMiddleware.model.assistance.FaqOutput
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetAssistanceFaqPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<Unit, FaqOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): Unit = Unit

    override suspend fun execute(input: Unit): NetworkResponse<FaqOutput> {
        return GetSettingsPSAExecutor(middlewareComponent, params).execute()
            .map { settings ->
                FaqOutput(
                    url = settings.customerHelpFAQ.takeIf { !it.isNullOrBlank() },
                    phone = settings.customerHelpTel.takeIf { !it.isNullOrBlank() },
                    email = settings.customerHelpEmail.takeIf { !it.isNullOrBlank() }
                )
            }
    }
}
