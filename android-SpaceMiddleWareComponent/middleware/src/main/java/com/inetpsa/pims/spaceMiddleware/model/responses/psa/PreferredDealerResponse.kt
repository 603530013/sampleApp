package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse

internal data class PreferredDealerResponse(

    @field:SerializedName("Dealer")
    val dealer: DetailsResponse
)
