package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.ApplicationTermsOutput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.VehicleTermsConditionsInput

internal class GetConnectedPrivacyFCAExecutor(command: BaseCommand) :
    BaseFcaExecutor<VehicleTermsConditionsInput, ApplicationTermsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): VehicleTermsConditionsInput =
        VehicleTermsConditionsInput(
            country = parameters has Constants.Input.Settings.COUNTRY,
            sdp = parameters has Constants.Input.Settings.SDP
        )

    override suspend fun execute(input: VehicleTermsConditionsInput): NetworkResponse<ApplicationTermsOutput> {
        val termsInput = TermsConditionsInput(
            country = input.country,
            sdp = input.sdp,
            type = TermsConditionsInput.Type.ConnectedPP
        )
        return FetchTermConditionFCAExecutor(middlewareComponent, params).execute(termsInput)
    }
}
