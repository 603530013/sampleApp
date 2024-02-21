package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Settings
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.ApplicationTermsOutput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.VehicleTermsConditionsInput

internal class GetConnectedTermsFCAExecutor(command: BaseCommand) :
    BaseFcaExecutor<VehicleTermsConditionsInput, ApplicationTermsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): VehicleTermsConditionsInput =
        VehicleTermsConditionsInput(
            country = parameters has Settings.COUNTRY,
            sdp = parameters has Settings.SDP
        )

    override suspend fun execute(input: VehicleTermsConditionsInput): NetworkResponse<ApplicationTermsOutput> {
        val termsInput = TermsConditionsInput(
            country = input.country,
            sdp = input.sdp,
            type = TermsConditionsInput.Type.ConnectedTC
        )
        return FetchTermConditionFCAExecutor(middlewareComponent, params).execute(termsInput)
    }
}
