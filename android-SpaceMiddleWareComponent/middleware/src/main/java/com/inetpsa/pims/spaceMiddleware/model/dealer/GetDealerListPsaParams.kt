package com.inetpsa.pims.spaceMiddleware.model.dealer

@Deprecated("should be replaced by a DealersInput")
internal data class GetDealerListPsaParams(
    val latitude: String,
    val longitude: String,
    val resultMax: String?
)
