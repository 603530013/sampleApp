package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment

import com.google.gson.annotations.SerializedName

internal enum class ServiceType {
    @SerializedName("factory")
    Factory,

    @SerializedName("repair")
    Repair,

    @SerializedName("dealer")
    Dealer,

    @SerializedName("recall")
    Recall,

    @SerializedName("generic")
    Generic,

    @SerializedName("intervention")
    Intervention,

    @SerializedName("maintenance")
    Maintenance,

    @SerializedName("package")
    Package
}
