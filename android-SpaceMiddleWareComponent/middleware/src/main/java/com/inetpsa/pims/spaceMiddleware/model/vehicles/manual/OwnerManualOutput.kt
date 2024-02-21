package com.inetpsa.pims.spaceMiddleware.model.vehicles.manual

internal data class OwnerManualOutput(
    val type: String? = null,
    val url: String? = null
) {

    enum class Type(val value: String) {
        Web("web"),
        SDK("sdk"),
        PDF("pdf")
    }
}
