package com.inetpsa.pims.spaceMiddleware.model.dealer

internal data class SendAdvisorDealerReviewPsaParams(
    val siteGeo: String,
    val vin: String,
    val rating: String,
    val comment: String,
    val serviceType: String,
    val sendConfirmEmail: String,
    val serviceDate: String
)
