package com.inetpsa.pims.spaceMiddleware.model.enrollment

internal data class AddVehicleConnectedFcaInput(
    val vin: String,
    val licencePlateNumber: String? = null,
    val contacts: List<Contact>? = null,
    val tcRegistration: LegalContent,
    val tcActivation: LegalContent? = null,
    val ppActivation: LegalContent
) {

    data class LegalContent(val countryCode: String, val status: Status, val version: String) {
        enum class Status {
            AGREE,
            DISAGREE
        }
    }

    data class Contact(val name: String, val phone: String)
}
