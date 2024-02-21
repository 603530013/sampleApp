package com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer

import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Data.Importer
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Data.PDVImporter

internal data class DetailsResponse(
    @SerializedName("address", alternate = ["Address"]) val address: Address? = null,
    @SerializedName("business", alternate = ["BusinessList"]) val business: List<BusinessItem>? = null,
    @SerializedName("coordinates", alternate = ["Coordinates"]) val coordinates: Coordinates? = null,
    @SerializedName("culture", alternate = ["Culture"]) val culture: String? = null, // fr_FR
    @SerializedName("data") val data: Data? = null,
    @SerializedName("CodesActors") val codesActors: Map<String, String>? = null,
    @SerializedName("CodesRegions") val codesRegions: Map<String, String>? = null,
    @SerializedName("distance", alternate = ["DistanceFromPoint"]) val distance: Double? = null, // 0.0
    @SerializedName("emails", alternate = ["Emails"]) val emails: Map<String, String>? = null,
    @SerializedName("fax", alternate = ["FaxNumber"]) val fax: String? = null,
    @SerializedName("is_agent", alternate = ["IsAgent"]) val isAgent: Boolean? = null,
    @SerializedName("is_agent_ap", alternate = ["is_agent_a_p", "IsAgentAP"]) val isAgentAp: Boolean? = null,
    @SerializedName("is_carac_rdvi", alternate = ["isCaracRdvi"]) val isCaracRdvi: Boolean? = null,
    @SerializedName("is_secondary", alternate = ["IsSecondary"]) val isSecondary: Boolean? = null,
    @SerializedName("is_succursale", alternate = ["IsSuccursale"]) val isSuccursale: Boolean? = null,
    @SerializedName("jockey") val jockey: Boolean? = null,
    @SerializedName("name", alternate = ["Name"]) val name: String? = null,
    @SerializedName("note") val note: Note? = null,
    @SerializedName("o2c") val o2c: Boolean? = null,
    @SerializedName("o2ov") val o2ov: Boolean? = null,
    @SerializedName("o2x") val o2x: Boolean? = null,
    @SerializedName(
        "open_hours",
        alternate = ["OpeningHoursList", "openHours"]
    ) val openHours: List<OpeningHours>? = null,
    @SerializedName("phones", alternate = ["Phones"]) val phones: Map<String, String>? = null,
    @SerializedName("principal", alternate = ["Principal"]) val principal: Map<String, Boolean>? = null,
    @SerializedName("rrdi", alternate = ["RRDI"]) val rrdi: String? = null,
    @SerializedName("services", alternate = ["ServiceList"]) val services: List<ServiceItem>? = null,
    @SerializedName("site_geo", alternate = ["SiteGeo", "siteGeo"]) val siteGeo: String,
    @SerializedName("type_operateur", alternate = ["typeOperateur"]) val typeOperateur: String? = null,
    @SerializedName("url_pages", alternate = ["UrlPages"]) val urlPages: Map<String, String>? = null,
    @SerializedName("url_prdv_ercs", alternate = ["urlPrdvErcs"]) val urlPrdvErcs: String? = null,
    @SerializedName("websites", alternate = ["WebSites"]) val websites: WebSites? = null,
    @SerializedName("Importer") val importer: Importer? = null,
    @SerializedName("PDVImporter") val pdvImporter: PDVImporter? = null
) {

    internal data class Address(
        @SerializedName("address1", alternate = ["Line1"]) val address1: String? = null,
        @SerializedName("address2", alternate = ["Line2"]) val address2: String? = null,
        @SerializedName("address3", alternate = ["Line3"]) val address3: String? = null,
        @SerializedName("city", alternate = ["City"]) val city: String? = null,
        @SerializedName("country", alternate = ["Country"]) val country: String? = null,
        @SerializedName("department", alternate = ["Department"]) val department: String? = null,
        @SerializedName("region", alternate = ["Region"]) val region: String? = null,
        @SerializedName("zip_code", alternate = ["ZipCode"]) val zipCode: String? = null
    )

    internal data class BusinessItem(
        @SerializedName("code", alternate = ["Code"]) val code: String? = null,
        @SerializedName("label", alternate = ["Label"]) val label: String? = null,
        @SerializedName("type", alternate = ["Type"]) val type: String? = null
    )

    internal data class Coordinates(
        @SerializedName("latitude", alternate = ["Latitude"]) val latitude: Double,
        @SerializedName("longitude", alternate = ["Longitude"]) val longitude: Double
    )

    internal data class Data(
        @SerializedName("BenefitList") val benefitList: List<String?>? = null,
        @SerializedName("bqCaptive") val bqCaptive: String? = null,
        @SerializedName("Brand") val brand: String? = null,
        @SerializedName("Capital") val capital: String? = null,
        @SerializedName("CodesActors") val codesActors: Map<String, String?>? = null,
        @SerializedName("CodesRegions") val codesRegions: Map<String, String?>? = null,
        @SerializedName("CommercialRegister") val commercialRegister: String? = null,
        @SerializedName("CountryId") val countryId: String? = null, // FR
        @SerializedName("FaxNumber") val faxNumber: String? = null,
        @SerializedName("FtcCodeList") val ftcCodeList: List<String?>? = null,
        @SerializedName("GmCodeList") val gmCodeList: List<GmCodeItem>? = null,
        @SerializedName("Group") val group: Group? = null,
        @SerializedName("Importer") val importer: Importer? = null,
        @SerializedName("Indicator") val indicator: Indicator? = null,
        @SerializedName("IntracommunityTVA") val intracommunityTVA: String? = null,
        @SerializedName("LegalStatus") val legalStatus: String? = null,
        @SerializedName("LienVoList") val lienVoList: List<String?>? = null,
        @SerializedName("NumSiret") val numSiret: String? = null,
        @SerializedName("PDVImporter") val pdvImporter: PDVImporter? = null,
        @SerializedName("ParentSiteGeo") val parentSiteGeo: String? = null,
        @SerializedName("PersonList") val personList: List<Person>? = null,
        @SerializedName("RCSNumber") val rCSNumber: String? = null, // RCS VERSAILLES B 302 475 041
        @SerializedName("RaisonSocial") val raisonSocial: String? = null,
        @SerializedName("WelcomeMessage") val welcomeMessage: String? = null,
        @SerializedName("carac_rdvi") val caracRdvi: String? = null
    ) {

        internal data class GmCodeItem(
            @SerializedName("Activity") val activity: String? = null,
            @SerializedName("BACCode") val bacCode: String? = null,
            @SerializedName("CVCADCCode") val cvcadcCode: String? = null,
            @SerializedName("PCCADCCode") val pccadcCode: String? = null
        )

        internal data class CodesRegions(
            @SerializedName("CodeRegionAG") val codeRegionAG: String? = null,
            @SerializedName("CodeRegionPR") val codeRegionPR: String? = null,
            @SerializedName("CodeRegionRA") val codeRegionRA: String? = null,
            @SerializedName("CodeRegionVN") val codeRegionVN: String? = null,
            @SerializedName("CodeRegionVO") val codeRegionVO: String? = null
        )

        internal data class Group(
            @SerializedName("GroupId") val groupId: String? = null,
            @SerializedName("IsLeader") val isLeader: Boolean, // true
            @SerializedName("SubGroupId") val subGroupId: String? = null,
            @SerializedName("SubGroupLabel") val subGroupLabel: String? = null
        )

        internal data class Importer(
            @SerializedName("Address") val address: String? = null,
            @SerializedName("City") val city: String? = null,
            @SerializedName("CorporateName") val corporateName: String? = null,
            @SerializedName("Country") val country: String? = null,
            @SerializedName("ImporterCode") val importerCode: String? = null,
            @SerializedName("ImporterName") val importerName: String? = null,
            @SerializedName("ManagementCountry") val managementCountry: String? = null,
            @SerializedName("Subsidiary") val subsidiary: String? = null,
            @SerializedName("SubsidiaryName") val subsidiaryName: String? = null
        )

        internal data class Indicator(
            @SerializedName("Code") val code: String? = null,
            @SerializedName("Label") val label: String? = null
        )

        internal data class PDVImporter(
            @SerializedName("PDVCode") val pDVCode: String? = null,
            @SerializedName("PDVContact") val pDVContact: String? = null,
            @SerializedName("PDVName") val pDVName: String? = null
        )
    }

    internal data class Person(
        @SerializedName("Email") val email: String? = null, // eric.alizard@mpsa.com
        @SerializedName("FirstName") val firstName: String? = null, // ERIC
        @SerializedName("FunctionCode") val functionCode: String? = null, // CMP/M4/F03
        @SerializedName("FunctionLabel") val functionLabel: String? = null, // Conseiller Commercial Véhicule Neuf
        @SerializedName("Id") val id: String, // 123234
        @SerializedName("LastName") val lastName: String? = null, // ALIZARD
        @SerializedName("Mobile") val mobile: String? = null,
        @SerializedName("Order") val order: Int? = null, // 0
        @SerializedName("Phone") val phone: String? = null,
        @SerializedName("ServiceCode") val serviceCode: String? = null, // VN_VENTEVN
        @SerializedName("TitleCode") val titleCode: String? = null, // c1
        @SerializedName("TitleLabel") val titleLabel: String? = null // M.
    )

    internal data class Note(
        @SerializedName("apv") val apv: Apv? = null,
        @SerializedName("url_apv") val urlApv: String? = null,
        @SerializedName("url_home") val urlHome: String? = null,
        @SerializedName("url_vn") val urlVn: String? = null,
        @SerializedName("vn") val vn: Vn? = null
    ) {

        internal data class Apv(
            @SerializedName("note") val note: Double,
            @SerializedName("total") val total: Int
        )

        internal data class Vn(
            @SerializedName("note") val note: Double,
            @SerializedName("total") val total: Int
        )
    }

    internal data class OpeningHours(
        @SerializedName("Label") val label: String? = null,
        @SerializedName("Type") val type: String? = null
    )

    internal data class Principal(
        @SerializedName("IsPrincipalAG") val isPrincipalAG: Boolean? = null,
        @SerializedName("IsPrincipalPR") val isPrincipalPR: Boolean? = null,
        @SerializedName("IsPrincipalRA") val isPrincipalRA: Boolean? = null,
        @SerializedName("IsPrincipalVN") val isPrincipalVN: Boolean? = null,
        @SerializedName("IsPrincipalVO") val isPrincipalVO: Boolean? = null
    )

    internal data class ServiceItem(
        @SerializedName("code", alternate = ["Code"]) val code: String? = null,
        @SerializedName("label", alternate = ["Label"]) val label: String? = null,
        @SerializedName("opening_hours", alternate = ["OpeningHours&"]) val openingHours: String? = null
    )

    internal data class UrlPages(
        @SerializedName("UrlAPVForm") val urlAPVForm: String? = null,
        @SerializedName("UrlContact") val urlContact: String? = null,
        @SerializedName("UrlNewCarStock") val urlNewCarStock: String? = null,
        @SerializedName("UrlUsedCarStock") val urlUsedCarStock: String? = null,
        @SerializedName("UrlUsefullInformation") val urlUsefullInformation: String? = null
    )

    internal data class WebSites(
        @SerializedName("Private") val private: String? = null,
        @SerializedName("Public") val public: String? = null
    )
}
