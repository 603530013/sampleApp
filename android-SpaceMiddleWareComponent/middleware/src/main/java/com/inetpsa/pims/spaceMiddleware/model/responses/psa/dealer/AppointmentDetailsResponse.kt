package com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer

import com.google.gson.annotations.SerializedName

internal data class AppointmentDetailsResponse(
    @SerializedName("basketId") val basketId: String? = null,
    @SerializedName("amount") val amount: Float? = null,
    @SerializedName("commandNumber") val commandNumber: String? = null,
    @SerializedName("hasBeenExtracted") val hasBeenExtracted: Boolean? = null,
    @SerializedName("CustomerComent") val customerComment: String? = null,
    @SerializedName("CreationDate") val creationDate: Long? = null,
    @SerializedName("RdvDate") val rdvDate: Long? = null,
    @SerializedName("reduction") val reduction: Int? = null,
    @SerializedName("rdvId") val rdvId: Long? = null,
    @SerializedName("payStatus") val payStatus: Int? = null,
    @SerializedName("isStoredInRussiaEnvironment") val isStoredInRussiaEnvironment: Boolean? = null,
    @SerializedName("typeDepot") val typeDepot: Int? = null,
    @SerializedName("dateLimite") val dateLimit: Long? = null,
    @SerializedName("dealer") val dealer: Dealer,
    @SerializedName("customer") val customer: Customer? = null,
    @SerializedName("listeLigneBaskets") val listLoginBaskets: List<String>? = null

) {

    internal data class Dealer(
        @SerializedName("geoId") val geoId: String? = null,
        @SerializedName("rrdiId") val rrdiId: String? = null,
        @SerializedName("codeContratRA") val codeContractRA: String? = null,
        @SerializedName("brand") val brand: String? = null,
        @SerializedName("culture") val culture: String? = null
    )

    internal data class Customer(
        @SerializedName("customerId") val customerId: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("firstname") val firstname: String? = null,
        @SerializedName("email") val email: String? = null,
        @SerializedName("isUsablePersonalInfo") val isUsablePersonalInfo: Boolean? = null,
        @SerializedName("telephone") val telephone: String? = null,
        @SerializedName("isAcceptingLealMention") val isAcceptingLegalMention: Boolean? = null,
        @SerializedName("isNeedMobilSolution") val isNeedMobilSolution: Boolean? = null,
        @SerializedName("contactMode") val contactMode: String? = null,
        @SerializedName("civilityCode") val civilityCode: String? = null,
        @SerializedName("isOfferCommercialAccepted") val isOfferCommercialAccepted: Boolean? = null,
        @SerializedName("isOfferCompanyAccepted") val isOfferCompanyAccepted: Boolean? = null,
        @SerializedName("isOfferPartnersAccepted") val isOfferPartnersAccepted: Boolean? = null,
        @SerializedName("origin") val origin: String? = null,
        @SerializedName("creationDate") val creationDate: Long? = null,
        @SerializedName("carID") val carID: String? = null,
        @SerializedName("energyLabel") val energyLabel: String? = null,
        @SerializedName("brandLabel") val brandLabel: String? = null,
        @SerializedName("modelLabel") val modelLabel: String? = null,
        @SerializedName("familyCode") val familyCode: String? = null,
        @SerializedName("mileage") val mileage: Float?,
        @SerializedName("serialNumber") val serialNumber: String? = null,
        @SerializedName("hasServiceContract") val hasServiceContract: Boolean? = null
    )
}
