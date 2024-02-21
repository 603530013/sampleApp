package com.inetpsa.pims.spaceMiddleware.model.dealer

import com.google.gson.annotations.SerializedName
@Deprecated("this should be replaced by PreferredDealerOutput")
internal data class PreferredDealer(
    @SerializedName("Dealer") val dealer: Dealer
)
