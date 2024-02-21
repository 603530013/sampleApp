package com.inetpsa.pims.spaceMiddleware.executor.logIncident

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.model.logincident.CreateLogIncident
import com.inetpsa.pims.spaceMiddleware.model.logincident.CreateLogIncidentPsaParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

internal class CreateLogIncidentPsaExecutor(command: BaseCommand) :
    BaseLogIncidentPsaExecutor<CreateLogIncidentPsaParams, CreateLogIncident>(command) {

    override fun params(parameters: Map<String, Any?>?): CreateLogIncidentPsaParams =
        CreateLogIncidentPsaParams(
            siteCode = parameters has Constants.BODY_PARAM_SITE_CODE,
            vin = parameters has Constants.BODY_PARAM_VIN,
            firstName = parameters has Constants.BODY_PARAM_FIRST_NAME,
            lastName = parameters has Constants.BODY_PARAM_LAST_NAME,
            nationalID = parameters hasOrNull Constants.BODY_PARAM_NATION_ID,
            phone = parameters hasOrNull Constants.BODY_PARAM_PHONE,
            zipCode = parameters hasOrNull Constants.BODY_PARAM_ZIP_CODE,
            city = parameters hasOrNull Constants.BODY_PARAM_CITY,
            email = parameters hasOrNull Constants.BODY_PARAM_EMAIL,
            idClient = parameters has Constants.BODY_PARAM_ID_CLIENT,
            culture = parameters hasOrNull Constants.BODY_PARAM_CULTURE,
            title = parameters has Constants.BODY_PARAM_TITLE,
            comment = parameters has Constants.BODY_PARAM_COMMENT,
            optin1 = parameters has Constants.BODY_PARAM_OPTIN_ONE,
            optin2 = parameters has Constants.BODY_PARAM_OPTIN_TWO
        )

    override suspend fun execute(input: CreateLogIncidentPsaParams): NetworkResponse<CreateLogIncident> {
        val request = request(
            type = CreateLogIncident::class.java,
            urls = arrayOf("v1/logincident"),
            body = input.toJson()
        )

        return communicationManager.post(request, MiddlewareCommunicationManager.MymToken)
    }
}
