package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Settings
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.ApplicationTermsOutput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput

internal class GetAppTermsFCAExecutor(command: BaseCommand) :
    BaseFcaExecutor<String, ApplicationTermsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Settings.SDP

    override suspend fun execute(input: String): NetworkResponse<ApplicationTermsOutput> {
        val termsInput = TermsConditionsInput(
            sdp = input,
            type = TermsConditionsInput.Type.AppTerms,
            country = configurationManager.locale.country
        )
        return FetchTermConditionFCAExecutor(middlewareComponent, params).execute(termsInput)
    }
}
