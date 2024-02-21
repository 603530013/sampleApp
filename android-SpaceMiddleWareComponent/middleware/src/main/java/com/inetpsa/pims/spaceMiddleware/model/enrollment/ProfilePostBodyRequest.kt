package com.inetpsa.pims.spaceMiddleware.model.enrollment

import com.google.gson.annotations.SerializedName

internal data class ProfilePostBodyRequest(
    @SerializedName("role") val role: UserRole = UserRole.VEHICLE_OWNER,
    @SerializedName("licencePlateNumber") val licencePlateNumber: String? = null,
    @SerializedName("emergencyContacts") val emergencyContacts: List<EmergencyContact>?,
    @SerializedName("tc") val tc: TermsAndConditions,
    @SerializedName("pp") val pp: PrivacyPolicy
) {

    enum class UserRole {
        VEHICLE_OWNER
    }

    data class EmergencyContact(
        @SerializedName("name") val name: String,
        @SerializedName("phone") val phone: String
    )

    data class TermsAndConditions(
        @SerializedName("registration") val registration: LegalContent,
        @SerializedName("activation") val activation: LegalContent?
    )

    data class PrivacyPolicy(
        @SerializedName("activation") val activation: LegalContent
    )

    data class LegalContent(
        @SerializedName("countryCode") val countryCode: String,
        @SerializedName("status") val status: String,
        @SerializedName("version") val version: String
    )
}
