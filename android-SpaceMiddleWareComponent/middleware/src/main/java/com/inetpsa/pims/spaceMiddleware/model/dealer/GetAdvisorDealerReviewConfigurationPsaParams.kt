package com.inetpsa.pims.spaceMiddleware.model.dealer

@Deprecated("this should be replaced by DealerReviewInputParams")
internal data class GetAdvisorDealerReviewConfigurationPsaParams(
    val vin: String,
    val vehicleIdType: String,
    val serviceType: String
)
