package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.enrollment.AddVehicleConnectedFcaInput
import com.inetpsa.pims.spaceMiddleware.model.enrollment.ProfilePostBodyRequest
import com.inetpsa.pims.spaceMiddleware.util.asJson
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasEnumNullable
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

internal class AddVehicleConnectedFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>?
) : BaseFcaExecutor<AddVehicleConnectedFcaInput, Unit>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): AddVehicleConnectedFcaInput {
        val vin: String = parameters has Constants.PARAM_VIN
        val licencePlateNumber: String? = parameters hasOrNull Constants.PARAM_PLATE_NUMBER

        val tcRegistrationMap: Map<String, String> = parameters has Constants.Input.PARAM_TC_REGISTRATION
        val tcRegistration = AddVehicleConnectedFcaInput.LegalContent(
            countryCode = tcRegistrationMap has Constants.Input.PARAM_COUNTRY_CODE,
            status = tcRegistrationMap hasEnum Constants.Input.PARAM_STATUS,
            version = tcRegistrationMap has Constants.Input.PARAM_VERSION
        )

        val tcActivationMap: Map<String, String>? = parameters hasOrNull Constants.Input.PARAM_TC_ACTIVATION
        val tcActivationCountry: String? = tcActivationMap hasOrNull Constants.Input.PARAM_COUNTRY_CODE
        val tcActivationStatus: AddVehicleConnectedFcaInput.LegalContent.Status? =
            tcActivationMap.hasEnumNullable<AddVehicleConnectedFcaInput.LegalContent.Status>(
                Constants.Input.PARAM_STATUS
            )
        val tcActivationVersion: String? = tcActivationMap hasOrNull Constants.Input.PARAM_VERSION
        val tcActivation =
            if (tcActivationCountry.isNullOrBlank() ||
                tcActivationVersion.isNullOrBlank() ||
                tcActivationStatus == null
            ) {
                null
            } else {
                AddVehicleConnectedFcaInput.LegalContent(
                    countryCode = tcActivationCountry,
                    status = tcActivationStatus,
                    version = tcActivationVersion
                )
            }
        val ppActivationMap: Map<String, String> = parameters has Constants.Input.PARAM_PP_ACTIVATION
        val ppActivation = AddVehicleConnectedFcaInput.LegalContent(
            countryCode = ppActivationMap has Constants.Input.PARAM_COUNTRY_CODE,
            status = ppActivationMap hasEnum Constants.Input.PARAM_STATUS,
            version = ppActivationMap has Constants.Input.PARAM_VERSION
        )

        val contactsMap: List<Map<String, String>>? = parameters hasOrNull Constants.Input.PARAM_CONTACTS
        val contacts = contactsMap?.mapNotNull {
            val name: String? = it hasOrNull Constants.Input.PARAM_NAME
            val phone: String? = it hasOrNull Constants.Input.PARAM_PHONE
            if (name.isNullOrBlank() || phone.isNullOrBlank()) {
                null
            } else {
                AddVehicleConnectedFcaInput.Contact(
                    name = name,
                    phone = phone
                )
            }
        }

        return AddVehicleConnectedFcaInput(
            vin = vin,
            licencePlateNumber = licencePlateNumber,
            contacts = contacts,
            tcRegistration = tcRegistration,
            tcActivation = tcActivation,
            ppActivation = ppActivation
        )
    }

    override suspend fun execute(input: AddVehicleConnectedFcaInput): NetworkResponse<Unit> {
        val request = request(
            type = Unit::class.java,
            urls = arrayOf("/v4/accounts/", uid, "/vehicles/", input.vin),
            body = generateBodyRequest(input)
        )
        return communicationManager.post<Unit>(request, TokenType.AWSToken(FCAApiKey.SDP))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateBodyRequest(input: AddVehicleConnectedFcaInput): String =
        ProfilePostBodyRequest(
            licencePlateNumber = input.licencePlateNumber,
            emergencyContacts = input.contacts?.map {
                ProfilePostBodyRequest.EmergencyContact(
                    name = it.name,
                    phone = it.phone
                )
            },
            tc = ProfilePostBodyRequest.TermsAndConditions(
                registration = ProfilePostBodyRequest.LegalContent(
                    countryCode = input.tcRegistration.countryCode,
                    status = input.tcRegistration.status.name,
                    version = input.tcRegistration.version
                ),
                activation = input.tcActivation?.let {
                    ProfilePostBodyRequest.LegalContent(
                        countryCode = input.tcActivation.countryCode,
                        status = input.tcActivation.status.name,
                        version = input.tcActivation.version
                    )
                }
            ),
            pp = ProfilePostBodyRequest.PrivacyPolicy(
                activation = ProfilePostBodyRequest.LegalContent(
                    countryCode = input.ppActivation.countryCode,
                    status = input.ppActivation.status.name,
                    version = input.ppActivation.version
                )
            )
        ).asJson()
}
