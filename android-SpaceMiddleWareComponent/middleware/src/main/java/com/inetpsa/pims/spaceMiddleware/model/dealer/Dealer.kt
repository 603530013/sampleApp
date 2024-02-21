package com.inetpsa.pims.spaceMiddleware.model.dealer

import com.google.gson.annotations.SerializedName

@Deprecated("this should be replaced by DealerResponse")
internal data class Dealer(
    @SerializedName("site_geo") val siteGeo: String? = null,
    @SerializedName("rrdi") val rrdi: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("phones") val phones: Phones? = null,
    @SerializedName("emails") val emails: Emails? = null,
    @SerializedName("address") val address: Address? = null,
    @SerializedName("coordinates") val coordinates: Coordinates? = null,
    @SerializedName("distance") val distance: Float? = null,
    @SerializedName("is_agent") val isAgent: Boolean? = null,
    @SerializedName("is_agent_a_p") val isAgentAp: Boolean? = null,
    @SerializedName("is_secondary") val isSecondary: Boolean? = null,
    @SerializedName("is_succursale") val isSuccursale: Boolean? = null,
    @SerializedName("business") val businessList: List<BusinessItem>? = null,
    @SerializedName("open_hours") val openHours: List<OpeningHours>? = null,
    @SerializedName("principal") val principal: Principal? = null,
    @SerializedName("url_pages") val urlPages: UrlPages? = null,
    @SerializedName("websites") val websites: WebSites? = null,
    @SerializedName("culture") val culture: String? = null,
    @SerializedName("data") val data: Data? = null,
    @SerializedName("is_carac_rdvi") val isCaracRdvi: Boolean? = null,
    @SerializedName("type_operateur") val typeOperateur: String? = null,
    @SerializedName("url_prdv_ercs") val urlPrdvErcs: String? = null,
    @SerializedName("jockey") val jockey: Boolean? = null,
    @SerializedName("o2ov") val o2ov: Boolean? = null,
    @SerializedName("ServiceList") val serviceList: List<ServiceItem?>? = null
) {

    internal data class Data(
        @SerializedName("BenefitList") val benefitList: List<String?>? = null,
        @SerializedName("CodesActors") val codesActors: CodesActors? = null,
        @SerializedName("CodesRegions") val codesRegions: CodesRegions? = null,
        @SerializedName("FaxNumber") val faxNumber: String? = null,
        @SerializedName("Group") val group: Group? = null,
        @SerializedName("Indicator") val indicator: Indicator? = null,
        @SerializedName("WelcomeMessage") val welcomeMessage: String? = null,
        @SerializedName("Importer") val importer: Importer? = null,
        @SerializedName("PDVImporter") val pdvImporter: PDVImporter? = null,
        @SerializedName("NumSiret") val numSiret: String? = null,
        @SerializedName("LegalStatus") val legalStatus: String? = null,
        @SerializedName("Capital") val capital: String? = null,
        @SerializedName("CommercialRegister") val commercialRegister: String? = null,
        @SerializedName("IntracommunityTVA") val intracommunityTVA: String? = null,
        @SerializedName("Brand") val brand: String? = null,
        @SerializedName("ParentSiteGeo") val parentSiteGeo: String? = null,
        @SerializedName("RaisonSocial") val raisonSocial: String? = null,
        @SerializedName("GmCodeList") val gmCodeList: List<GmCodeItem>? = null,
        @SerializedName("LienVoList") val lienVoList: List<String?>? = null,
        @SerializedName("bqCaptive") val bqCaptive: String? = null,
        @SerializedName("carac_rdvi") val caracRdvi: String? = null,
        @SerializedName("FtcCodeList") val ftcCodeList: List<String?>? = null
    )

    internal data class Address(
        @SerializedName("address1") val address1: String? = null,
        @SerializedName("department") val department: String? = null,
        @SerializedName("city") val city: String? = null,
        @SerializedName("region") val region: String? = null,
        @SerializedName("zip_code") val zipCode: String? = null,
        @SerializedName("country") val country: String? = null
    )

    internal data class BusinessItem(
        @SerializedName("code") val code: String? = null,
        @SerializedName("label") val label: String? = null,
        @SerializedName("type") val type: String? = null
    )

    internal data class Coordinates(
        @SerializedName("latitude") val latitude: Float,
        @SerializedName("longitude") val longitude: Float
    )

    internal data class CodesRegions(
        @SerializedName("CodeRegionAG") val codeRegionAG: String? = null,
        @SerializedName("CodeRegionPR") val codeRegionPR: String? = null,
        @SerializedName("CodeRegionRA") val codeRegionRA: String? = null,
        @SerializedName("CodeRegionVN") val codeRegionVN: String? = null,
        @SerializedName("CodeRegionVO") val codeRegionVO: String? = null
    )

    internal data class CodesActors(
        @SerializedName("CodeActorAddressPR") val codeActorAddressPR: String? = null,
        @SerializedName("CodeActorAddressRA") val codeActorAddressRA: String? = null,
        @SerializedName("CodeActorAddressVN") val codeActorAddressVN: String? = null,
        @SerializedName("CodeActorAddressVO") val codeActorAddressVO: String? = null,
        @SerializedName("CodeActorCC_AG") val codeActorCCAG: String? = null,
        @SerializedName("CodeActorCC_PR") val codeActorCCPR: String? = null,
        @SerializedName("CodeActorCC_RA") val codeActorCCRA: String? = null,
        @SerializedName("CodeActorCC_VN") val codeActorCCVN: String? = null,
        @SerializedName("CodeActorCC_VO") val codeActorCCVO: String? = null,
        @SerializedName("CodeActorSearch") val codeActorSearch: String? = null
    )

    internal data class Emails(
        @SerializedName("Email") val email: String,
        @SerializedName("EmailAPV") val emailAPV: String,
        @SerializedName("EmailAgent") val emailAgent: String,
        @SerializedName("EmailGER") val emailGER: String,
        @SerializedName("EmailGRC") val emailGRC: String,
        @SerializedName("EmailPR") val emailPR: String,
        @SerializedName("EmailSales") val emailSales: String,
        @SerializedName("EmailVO") val emailVO: String
    )

    internal data class GmCodeItem(
        @SerializedName("Activity") val activity: String? = null,
        @SerializedName("BACCode") val bacCode: String? = null,
        @SerializedName("CVCADCCode") val cvcadcCode: String? = null,
        @SerializedName("PCCADCCode") val pccadcCode: String? = null
    )

    internal data class Phones(
        @SerializedName("PhoneAPV") val phoneAPV: String,
        @SerializedName("PhoneNumber") val phoneNumber: String,
        @SerializedName("PhonePR") val phonePR: String,
        @SerializedName("PhoneVN") val phoneVN: String,
        @SerializedName("PhoneVO") val phoneVO: String
    )

    internal data class ServiceItem(
        @SerializedName("Code") val code: String? = null,
        @SerializedName("Label") val label: String? = null,
        @SerializedName("OpeningHours") val openingHours: String? = null
    )

    internal data class WebSites(
        @SerializedName("Private") val private: String? = null,
        @SerializedName("Public") val public: String? = null
    )

    internal data class UrlPages(
        @SerializedName("UrlAPVForm") val urlAPVForm: String? = null,
        @SerializedName("UrlContact") val urlContact: String? = null,
        @SerializedName("UrlNewCarStock") val urlNewCarStock: String? = null,
        @SerializedName("UrlUsedCarStock") val urlUsedCarStock: String? = null,
        @SerializedName("UrlUsefullInformation") val urlUsefullInformation: String? = null
    )

    internal data class Principal(
        @SerializedName("IsPrincipalAG") val isPrincipalAG: Boolean? = null,
        @SerializedName("IsPrincipalPR") val isPrincipalPR: Boolean? = null,
        @SerializedName("IsPrincipalRA") val isPrincipalRA: Boolean? = null,
        @SerializedName("IsPrincipalVN") val isPrincipalVN: Boolean? = null,
        @SerializedName("IsPrincipalVO") val isPrincipalVO: Boolean? = null
    )

    internal data class Group(
        @SerializedName("GroupId") val groupId: String? = null,
        @SerializedName("SubGroupId") val subGroupId: String? = null,
        @SerializedName("SubGroupLabel") val subGroupLabel: String? = null
    )

    internal data class Importer(
        @SerializedName("ImporterCode") val importerCode: String? = null,
        @SerializedName("CorporateName") val corporateName: String? = null,
        @SerializedName("ImporterName") val importerName: String? = null,
        @SerializedName("Address") val address: String? = null,
        @SerializedName("City") val city: String? = null,
        @SerializedName("ManagementCountry") val managementCountry: String? = null,
        @SerializedName("Country") val country: String? = null,
        @SerializedName("Subsidiary") val subsidiary: String? = null,
        @SerializedName("SubsidiaryName") val subsidiaryName: String? = null
    )

    internal data class PDVImporter(
        @SerializedName("PDVCode") val pDVCode: String? = null,
        @SerializedName("PDVName") val pDVName: String? = null,
        @SerializedName("PDVContact") val pDVContact: String? = null
    )

    internal data class Indicator(
        @SerializedName("Code") val code: String? = null,
        @SerializedName("Label") val label: String? = null
    )

    internal data class OpeningHours(
        @SerializedName("Label") val label: String? = null,
        @SerializedName("Type") val type: String? = null
    )
}
