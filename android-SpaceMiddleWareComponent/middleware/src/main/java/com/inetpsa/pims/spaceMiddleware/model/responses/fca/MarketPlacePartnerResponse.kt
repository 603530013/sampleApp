package com.inetpsa.pims.spaceMiddleware.model.responses.fca

import com.google.gson.annotations.SerializedName

internal data class MarketPlacePartnerResponse(

    @field:SerializedName("customExtension")
    val customExtension: CustomExtension? = null,

    @field:SerializedName("allowedBrands")
    val allowedBrands: List<String?>? = null,

    @field:SerializedName("partnerBackgroundColor")
    val partnerBackgroundColor: String? = null,

    @field:SerializedName("partnerName")
    val partnerName: String? = null,

    @field:SerializedName("section")
    val section: String? = null,

    @field:SerializedName("allowedFuelTypes")
    val allowedFuelTypes: List<String?>? = null,

    @field:SerializedName("isConsentApplicable")
    val isConsentApplicable: Boolean? = null,

    @field:SerializedName("consents")
    val consents: Consents? = null,

    @field:SerializedName("partnerID")
    val partnerID: Long? = null,

    @field:SerializedName("serviceID")
    val serviceID: String? = null,

    @field:SerializedName("partnerImage")
    val partnerImage: String? = null,

    @field:SerializedName("partnerThumbnail")
    val partnerThumbnail: String? = null,

    @field:SerializedName("contextHelp")
    val contextHelp: String? = null,

    @field:SerializedName("allowedMvs")
    val allowedMvs: List<String?>? = null,

    @field:SerializedName("partnerStackedLogo")
    val partnerStackedLogo: String? = null,

    @field:SerializedName("partnerStatus")
    val partnerStatus: String? = null
) {

    internal data class CustomExtension(

        @field:SerializedName("appIds")
        val appIds: AppIds? = null,

        @field:SerializedName("deeplinks")
        val deepLinks: List<DeepLinksItem>? = null
    )

    internal data class Consents(

        @field:SerializedName("consent4")
        val consent4: Boolean? = null
    )

    internal data class DeepLinksItem(

        @field:SerializedName("androidLink")
        val androidLink: String? = null,

        @field:SerializedName("iosLink")
        val iosLink: String? = null,

        @field:SerializedName("key")
        val key: String? = null
    )

    internal data class AppIds(
        @field:SerializedName("android")
        val android: String? = null,

        @field:SerializedName("ios")
        val ios: String? = null
    )
}
