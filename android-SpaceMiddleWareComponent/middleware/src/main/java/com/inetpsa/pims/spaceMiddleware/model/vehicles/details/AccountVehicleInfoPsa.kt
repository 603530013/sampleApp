package com.inetpsa.pims.spaceMiddleware.model.vehicles.details

import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ProfileResponse

@Deprecated("We should switch to use GetUserResponse")
internal data class AccountVehicleInfoPsa(
    @SerializedName("selected_vehicle") val selectedVehicle: SelectedVehicle,
    @SerializedName("profile") val profile: ProfileResponse? = null,
    @SerializedName("dealers") val dealers: Dealers? = null,
    @SerializedName("settings_update") val settingsUpdate: Long? = 0
) {

    internal data class SelectedVehicle(
        @SerializedName("type_vehicle") val type: Int,
        @SerializedName("services_connected") val servicesConnected: List<Map<String, Any>>?,
        @SerializedName("short_label") val shortName: String? = null,
        @SerializedName("attributes") val attributes: List<String>,
        @SerializedName("eligibility") val eligibility: List<String>,
        val vin: String,
        val lcdv: String? = null
    )

    internal data class Dealer(
        @SerializedName("site_geo") val siteGeo: String,
        @SerializedName("rrdi") val rrdi: String,
        @SerializedName("name") val name: String,
        @SerializedName("culture") val culture: String,
        @SerializedName("phones") val phones: Phones,
        @SerializedName("emails") val emails: Emails,
        @SerializedName("address") val address: Address,
        @SerializedName("coordinates") val coordinates: Coordinates,
        @SerializedName("distance") val distance: Float,
        @SerializedName("business") val business: List<BusinessItem>? = null,
        @SerializedName("open_hours") val openHours: List<OpeningHours>? = null,
        @SerializedName("is_carac_rdvi") val isCaracRrdvi: Boolean,
        @SerializedName("type_operateur") val typeOperator: String,
        @SerializedName("url_prdv_ercs") val urlPrdvErcs: String,
        @SerializedName("jockey") val jockey: Boolean,
        @SerializedName("o2c") val o2c: Boolean,
        @SerializedName("url_pages") val urlPages: UrlPages? = null,
        @SerializedName("websites") val websites: Websites? = null,
        @SerializedName("principal") val principal: Principal? = null,
        @SerializedName("data") val data: Data? = null
    )

    internal data class Dealers(
        @SerializedName("apv") val apv: Apv? = null
    )

    internal data class Apv(
        @SerializedName("note") val note: Note? = null,

        @SerializedName("is_secondary") val isSecondary: Boolean? = null,

        @SerializedName("distance") val distance: Int? = null,

        @SerializedName("data") val data: Data? = null,

        @SerializedName("phones") val phones: Phones? = null,

        @SerializedName("open_hours") val openHours: List<OpenHoursItem?>? = null,

        @SerializedName("jockey") val jockey: Boolean? = null,

        @SerializedName("emails") val emails: Emails? = null,

        @SerializedName("principal") val principal: Principal? = null,

        @SerializedName("url_pages") val urlPages: UrlPages? = null,

        @SerializedName("site_geo") val siteGeo: String? = null,

        @SerializedName("is_agent") val isAgent: Boolean? = null,

        @SerializedName("fax") val fax: String? = null,

        @SerializedName("is_agent_ap") val isAgentAp: Boolean? = null,

        @SerializedName("type_operateur") val typeOperateur: String? = null,

        @SerializedName("o2c") val o2c: Boolean? = null,

        @SerializedName("address") val address: Address? = null,

        @SerializedName("is_carac_rdvi") val isCaracRdvi: Boolean? = null,

        @SerializedName("business") val business: List<BusinessItem?>? = null,

        @SerializedName("coordinates") val coordinates: Coordinates? = null,

        @SerializedName("services") val services: List<ServicesItem?>? = null,

        @SerializedName("rrdi") val rrdi: String? = null,

        @SerializedName("is_succursale") val isSuccursale: Boolean? = null,

        @SerializedName("culture") val culture: String? = null,

        @SerializedName("name") val name: String? = null,

        @SerializedName("websites") val websites: Websites? = null,

        @SerializedName("url_prdv_ercs") val urlPrdvErcs: String? = null
    )

    internal data class Phones(
        @SerializedName("PhoneNumber") val phoneNumber: String
    )

    internal data class Emails(
        @SerializedName("Email") val email: String
    )

    internal data class Address(
        @SerializedName("address1") val address1: String,
        @SerializedName("address2") val address2: String,
        @SerializedName("address3") val address3: String,
        @SerializedName("department") val department: String,
        @SerializedName("city") val city: String,
        @SerializedName("region") val region: String,
        @SerializedName("zip_code") val zipCode: String,
        @SerializedName("country") val country: String
    )

    internal data class Coordinates(
        @SerializedName("latitude") val latitude: Float,
        @SerializedName("longitude") val longitude: Float
    )

    internal data class ServicesItem(

        @SerializedName("code") val code: String? = null,

        @SerializedName("opening_hours") val openingHours: String? = null,

        @SerializedName("label") val label: String? = null
    )

    internal data class Websites(

        @SerializedName("Private") val private: String? = null,

        @SerializedName("Public") val public: String? = null
    )

    internal data class BusinessItem(

        @SerializedName("code") val code: String? = null,

        @SerializedName("label") val label: String? = null,

        @SerializedName("type") val type: String? = null
    )

    internal data class UrlPages(

        @SerializedName("UrlNewCarStock") val urlNewCarStock: String? = null,

        @SerializedName("UrlUsedCarStock") val urlUsedCarStock: String? = null,

        @SerializedName("UrlContact") val urlContact: String? = null,

        @SerializedName("UrlUsefullInformation") val urlUsefullInformation: String? = null,

        @SerializedName("UrlAPVForm") val urlAPVForm: String? = null
    )

    internal data class Note(
        @SerializedName("apv") val apv: NoteDetails? = null,

        @SerializedName("vn") val vn: NoteDetails? = null,

        @SerializedName("url_apv") val urlApv: String? = null,

        @SerializedName("url_vn") val urlVn: String? = null
    )

    internal data class NoteDetails(
        @SerializedName("note") val note: Float? = null,

        @SerializedName("total") val total: Int? = null
    )

    internal data class Data(

        @SerializedName("CountryId") val countryId: String? = null,

        @SerializedName("Group") val group: Group? = null,

        @SerializedName("CodesActors") val codesActors: CodesActors? = null,

        @SerializedName("IntracommunityTVA") val intracommunityTVA: String? = null,

        @SerializedName("LegalStatus") val legalStatus: String? = null,

        @SerializedName("ParentSiteGeo") val parentSiteGeo: String? = null,

        @SerializedName("BenefitList") val benefitList: List<Any?>? = null,

        @SerializedName("Capital") val capital: String? = null,

        @SerializedName("WelcomeMessage") val welcomeMessage: String? = null,

        @SerializedName("CodesRegions") val codesRegions: CodesRegions? = null,

        @SerializedName("CommercialRegister") val commercialRegister: String? = null,

        @SerializedName("RaisonSocial") val raisonSocial: String? = null,

        @SerializedName("GmCodeList") val gmCodeList: List<Any?>? = null,

        @SerializedName("Indicator") val indicator: Indicator? = null,

        @SerializedName("PDVImporter") val pdvImporter: PDVImporter? = null,

        @SerializedName("Brand") val brand: String? = null,

        @SerializedName("PersonList") val personList: List<Any?>? = null,

        @SerializedName("RCSNumber") val rCSNumber: String? = null,

        @SerializedName("bqCaptive") val bqCaptive: String? = null,

        @SerializedName("LienVoList") val lienVoList: List<Any?>? = null,

        @SerializedName("NumSiret") val numSiret: String? = null,

        @SerializedName("Importer") val importer: Importer? = null
    )

    internal data class OpenHoursItem(

        @SerializedName("Type") val type: String? = null,

        @SerializedName("Label") val label: String? = null
    )

    internal data class Principal(

        @SerializedName("IsPrincipalPR") val isPrincipalPR: Boolean? = null,

        @SerializedName("IsPrincipalRA") val isPrincipalRA: Boolean? = null,

        @SerializedName("IsPrincipalVN") val isPrincipalVN: Boolean? = null,

        @SerializedName("IsPrincipalVO") val isPrincipalVO: Boolean? = null,

        @SerializedName("IsPrincipalAG") val isPrincipalAG: Boolean? = null
    )

    internal data class Group(

        @SerializedName("SubGroupLabel") val subGroupLabel: String? = null,

        @SerializedName("SubGroupId") val subGroupId: String? = null,

        @SerializedName("GroupId") val groupId: String? = null
    )

    internal data class Importer(

        @SerializedName("ImporterCode") val importerCode: String? = null,

        @SerializedName("Address") val address: String? = null,

        @SerializedName("ManagementCountry") val managementCountry: String? = null,

        @SerializedName("Subsidiary") val subsidiary: String? = null,

        @SerializedName("Country") val country: String? = null,

        @SerializedName("SubsidiaryName") val subsidiaryName: String? = null,

        @SerializedName("City") val city: String? = null,

        @SerializedName("ImporterName") val importerName: String? = null,

        @SerializedName("CorporateName") val corporateName: String? = null
    )

    internal data class PDVImporter(

        @SerializedName("PDVName") val pdvName: String? = null,

        @SerializedName("PDVCode") val pdvCode: String? = null,

        @SerializedName("PDVContact") val pdvContact: String? = null
    )

    internal data class Indicator(

        @SerializedName("Label") val label: String? = null,

        @SerializedName("Code") val code: String? = null
    )

    internal data class CodesRegions(

        @SerializedName("CodeRegionVN") val codeRegionVN: String? = null,

        @SerializedName("CodeRegionVO") val codeRegionVO: String? = null,

        @SerializedName("CodeRegionAG") val codeRegionAG: String? = null,

        @SerializedName("CodeRegionRA") val codeRegionRA: String? = null,

        @SerializedName("CodeRegionPR") val codeRegionPR: String? = null
    )

    internal data class CodesActors(

        @SerializedName("CodeActorAddressPR") val codeActorAddressPR: String? = null,

        @SerializedName("CodeActorCC_PR") val codeActorCCPR: String? = null,

        @SerializedName("CodeActorCC_VN") val codeActorCCVN: String? = null,

        @SerializedName("CodeActorCC_AG") val codeActorCCAG: String? = null,

        @SerializedName("CodeActorAddressRA") val codeActorAddressRA: String? = null,

        @SerializedName("CodeActorAddressVN") val codeActorAddressVN: String? = null,

        @SerializedName("CodeActorAddressVO") val codeActorAddressVO: String? = null,

        @SerializedName("CodeActorCC_VO") val codeActorCCVO: String? = null,

        @SerializedName("CodeActorCC_RA") val codeActorCCRA: String? = null,

        @SerializedName("CodeActorSearch") val codeActorSearch: String? = null
    )

    internal data class OpeningHours(

        @SerializedName("Type") val type: String? = null,

        @SerializedName("Label") val label: String? = null
    )
}
