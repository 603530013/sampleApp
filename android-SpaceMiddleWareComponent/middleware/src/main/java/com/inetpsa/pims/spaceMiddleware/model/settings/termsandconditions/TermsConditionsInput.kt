package com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions

import com.inetpsa.mmx.foundation.extensions.EnumValue

internal data class TermsConditionsInput(
    val country: String? = null,
    val sdp: String? = null,
    val type: Type
) {

    internal enum class Type(override val value: String, val mode: Mode) : EnumValue {
        AppTerms("ENDPOINT_APPLICATION_TERMS", Mode.User),
        ProfileIncomplete("ENDPOINT_PROFILE_INCOMPLETE", Mode.User),
        Privacy("ENDPOINT_PRIVACY_POLICY", Mode.User),
        ConnectedPP("ENDPOINT_PRIVACY_TERMS", Mode.Vehicle),
        ConnectedTC("ENDPOINT_TERMS_AND_CONDITIONS", Mode.Vehicle),
        WebTerms("ENDPOINT_WEB_TERMS", Mode.User)
    }

    enum class Mode {
        User, Vehicle
    }
}
