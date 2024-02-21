package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

internal data class SettingsResponse(
    @SerializedName("eboutique.enable") val eboutiqueEnabled: Boolean? = null,
    @SerializedName("eboutique.connectpacks_url") val eboutiqueConnectPacksURL: String? = null,
    @SerializedName("eboutique.navco_url") val eboutiqueNavcoUrl: String? = null,
    @SerializedName("sams.navco.url.enable") val samsNavcoEnabled: Boolean?,
    @SerializedName("sams.enable") val samsEnabled: Boolean? = null,
    @SerializedName("remotelev.enable") val remoteLevEnable: Boolean? = null,
    @SerializedName("sams.navco.url") val samsNavcoURL: String? = null,
    @SerializedName("sams.zar.url.enable") val samsZarEnabled: Boolean? = null,
    @SerializedName("sams.zar.url") val samsZarURL: String? = null,
    @SerializedName("sams.tmts.url.enable") val samsTmtsUrlEnable: Boolean? = null,
    @SerializedName("sams.tmts.url") val samsTmtsUrl: String? = null,
    @SerializedName("remotelev.sams_webview.enable") val remoteLevSamsWebViewEnable: Boolean? = null,
    @SerializedName("lev.souscription.url") val remoteLevUrl: String? = null,
    @SerializedName("sso.url") val ssoUrl: String? = null,
    @SerializedName("customerhelp.form_care_inapp") val custHelpFormInApp: Boolean? = null,
    @SerializedName("assistance.brand.phone") val assistanceBrandPhone: String? = null,
    @SerializedName("assistance.rsa.enable") val assistanceRsaEnable: Boolean? = null,
    @SerializedName("assistance.rsa.phone") var assistanceRsaPhone: String? = null,
    @SerializedName("assistance.rsa.type") val assistanceRsaTypes: List<AssistanceRsaType>? = null,
    @SerializedName("assistance.rsa.status") val assistanceRsaStatuses: List<AssistanceRsaStatus>? = null,

    @SerializedName("adsd") val adsd: Int? = null,
    @SerializedName("recall_campaigns.enable") val recallCampaginEnable: Boolean? = null,
    @SerializedName("onlyLastTrip.enable") val onlyLastTripEnable: Boolean? = null,
    @SerializedName("o2c.enable") val o2xEnable: Boolean? = null,
    @SerializedName("o2c.service.enable") val o2xServiceEnable: Boolean? = null,
    @SerializedName("o2c.service.url") val o2xServiceURL: String? = null,
    @SerializedName("o2c.boutique.enable") val o2xBoutiqueEnable: Boolean? = null,
    @SerializedName("o2c.boutique.url") val o2xBoutiqueURL: String? = null,
    @SerializedName("o2c.entretien.enable") val o2xEntretienEnable: Boolean? = null,
    @SerializedName("o2c.entretien.url") val o2xEntretienURL: String? = null,
    @SerializedName("o2c.phone.sos") val o2xPhoneSOS: String? = null,

    @SerializedName("mobility.pass.enable") val mobilityPassV2Enable: Boolean? = null,
    @SerializedName("mobility.pass.url") val mobilityPassV2Url: String? = null,
    @SerializedName("carto_soft.enable") val brandUpdateEnable: Boolean? = null,
    @SerializedName("carto_soft.url.windows") val brandUpdateWindowsURL: String? = null,
    @SerializedName("carto_soft.url.mac") val brandUpdateMacURL: String? = null,
    @SerializedName("mister_auto.url") val misterAutoUrl: String? = null,
    @SerializedName("cta_cookie_policy") val cookiePolicyUrl: String? = null,
    @SerializedName("url_privacy_policy") val privacyPolicyUrl: String? = null,
    @SerializedName("scan_mycar.enable") val scanMyCarEnable: Boolean? = null,
    @SerializedName("updateCGU") val updateCGU: Long? = null,
    @SerializedName("contact.non.care") val contactNonCare: Boolean? = null,
    @SerializedName("log_incident.url") val logIncidentURL: String? = null,
    @SerializedName("promoted_services") val promotedServices: List<PromotedServices>? = null,
    @SerializedName("send_2_nav.enable") val sendToNavEnable: Boolean? = null,
    @SerializedName("services") val services: BOServices? = null,
    @SerializedName("contact.form") val contactForm: String? = null,
    @SerializedName("dealer_locator.enable") val dealerLocatorEnable: Boolean? = null,
    @SerializedName("findmycar.enable") val findMyCarEnable: Boolean? = null,
    @SerializedName("smartapps.enable") val smartAppsEnable: Boolean? = null,
    @SerializedName("financeContract.finance_url") val contractFinance: String? = null,
    @SerializedName("financeContract.assurance_url") val contractInsurance: String? = null,
    @SerializedName("financeContract.connectedinsurance_quote_url") val insuranceeQuote: String? = null,
    @SerializedName("financeContract.connectedinsurance_appointment_url") val insuranceAppointment: String? =
        null,
    @SerializedName("chargingtipsfaq.enable") val chargingTipsFaqEnable: Boolean? = null,
    @SerializedName("dimbo.enable") val dimboEnable: Boolean? = null,
    @SerializedName("dimbo_webview.enable") val dimboInWebviewEnable: Boolean? = null,
    @SerializedName("typePRDV") val prdvType: String? = null,
    @SerializedName("prdv.url") val prdvUrl: String? = null,
    @SerializedName("prdv.params") val prdvParams: String? = null,
    @SerializedName("settings_update") val settingsUpdate: String? = null,
    @SerializedName("appSalesforce") val appSalesForceSFID: String? = null,
    @SerializedName("ChargerLocatorService.enable") val isChargeLocatorServiceEnable: Boolean? = null,
    @SerializedName("ChargerLocatorService.url") val chargeLocatorServiceURL: String? = null,
    @SerializedName("ChargerLocatorService.icon") val chargeLocatorServiceIcon: String? = null,
    @SerializedName("ChargerLocatorService.AppName") val chargeLocatorServiceAppName: String? = null,
    @SerializedName("ChargerLocatorService.AppID") val chargeLocatorServiceAppID: String? = null,
    @SerializedName("ChargerLocatorService.PackageName") val chargeLocatorServicePackageName: String? = null,
    @SerializedName("onlyyoudesc.enable") val onlyYouDescEnable: Boolean? = null,
    @SerializedName("ds_oly_params") val onlyYouParams: OnlyYouParams? = null,
    @SerializedName("club_url") val clubURL: String? = null,
    @SerializedName("clubv2Cgu") val clubV2CGU: String? = null,
    @SerializedName("clubv2Unsubscribe") val clubv2Unsubscribe: String? = null,
    @SerializedName("rent_enable") val rentEnable: Boolean? = null,
    @SerializedName("urlRent") val rentURL: String? = null,
    @SerializedName("valet") val valet: Boolean? = null,
    @SerializedName("urlValet") val valetURL: String? = null,
    @SerializedName("ws_ocr_aws.enable") val ocrAwsEnable: Boolean? = null,
    @SerializedName("customerhelp.enable") val customerHelpEnable: Boolean? = null,
    @SerializedName("customerhelp.url_faq") val customerHelpFAQ: String? = null,
    @SerializedName("customerhelp.tel_rlc") val customerHelpTel: String? = null,
    @SerializedName("customerhelp.email_rlc") val customerHelpEmail: String? = null,
    @SerializedName("lcvstellantis") val lcvstellantis: Boolean? = null,
    @SerializedName("ws_sams_myaccount.url") val samsAccountUrl: String? = null,
    @SerializedName("fulldigital.enable") val fullDigital: Boolean? = null,
    @SerializedName("o2c.vehicle.electric.enable") val o2xVehicleElectricEnable: Boolean? = null,
    @SerializedName("remoteaccess.enable") val remoteAccessEnable: Boolean? = null,
    @SerializedName("mileage_update.enable") val mileageUpdateEnable: Boolean? = null,
    @SerializedName("app_hub.enable") val appHubEnable: Boolean? = null,
    @SerializedName("infoFuel.enable") val infoFuelEnable: Boolean? = null,
    @SerializedName("infoFuel.url") val infoFuelUrl: String? = null,
    @SerializedName("driving_data.enable") val drivingDataEnable: Boolean? = null,
    @SerializedName("maintenance_timeline.enable") val maintenanceTimelineEnable: Boolean? = null,
    @SerializedName("usabila") val boUsabilla: BOUsabilla? = null,
    @SerializedName("univers.enable") val universEnable: Boolean? = null,
    @SerializedName("playlistvideos.enable") val playListVideoEnable: Boolean? = null,
    @SerializedName("voyants.enable") val voyantsEnable: Boolean? = null,
    @SerializedName("myami_play.enabled") val myamiPlayEnable: Boolean? = null,
    @SerializedName("myami_play.store_url") val myamiPlayStoreUrl: String? = null,
    @SerializedName("cgu.webview") val cguWebView: String? = null,
    @SerializedName("sams.payment_method.enable") val samsPaymentMethodEnable: Boolean? = null,
    @SerializedName("sams.payment_method.url") val samsPaymentMethodUrl: String? = null,
    @SerializedName("biometrics.enable") val featureBiometric: Boolean? = null,

    @SerializedName("c_buddy.enable") val buddyEnable: Boolean? = null,
    @SerializedName("c_buddy.android.url") val buddyAndroidUrl: String? = null,
    @SerializedName("c_buddy.ios.url") val buddyiOSUrl: String? = null,
    @SerializedName("c_buddy.icon") val buddyIcon: String? = null,
    @SerializedName("map_update.enable") val mapUpdateEnable: Boolean? = null,
    @SerializedName("map_update.url") val mapUpdateUrl: String? = null,
    @SerializedName("advisor.ami_sitegeo") val advisorAmiSitegeo: String? = null,
    @SerializedName("evRouting.enable") val evRoutingEnable: String? = null,
    @SerializedName("prdvquote.enable") val prdvEnable: String? = null,
    @SerializedName("webradio.enable") val webRadioEnable: String? = null,
    @SerializedName("webradio.provider") val webRadioProvider: String? = null,
    @SerializedName("connected_alarm.enable") val connectedAlaram: String? = null,
    @SerializedName("ownermanual.webview.url") val ownerManualWebViewUrl: String? = null,
    @SerializedName("ownermanual.webview.enable") val ownerManualWebViewEnable: Boolean? = null,
    @SerializedName("resetbutton.enable") val resetButton: String? = null

) {

    internal data class AssistanceRsaType(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("label")
        val label: String? = null
    )

    internal data class AssistanceRsaStatus(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("label")
        val label: String? = null,
        @SerializedName("actionType")
        val actionType: String? = null,
        @SerializedName("pullingFrequency")
        val pullingFrequency: Int? = null
    )

    internal data class BOServices(
        @SerializedName("sams.enable") val samsEnabled: Boolean? = null,
        @SerializedName("sams.navco.in_webview") val samsNavcoInWebview: Boolean? = null,
        @SerializedName("sams.tmts.in_webview") val samsTmtsInWebview: Boolean? = null,
        @SerializedName("sams.remotelev.in_webview") val samsRemotelevInWebview: Boolean? = null
    )

    internal data class PromotedServices(
        @SerializedName("priority")
        val priority: Int? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("text")
        val text: String? = null,
        @SerializedName("cta")
        val cta: String? = null,
        @SerializedName("service_code")
        val serviceCode: String? = null
    )

    internal data class OnlyYouParams(
        @SerializedName("oly_you.enable")
        val isOnlyYouEnabled: Boolean? = null,
        @SerializedName("rent.enable")
        val isRentEnabled: Boolean? = null,
        @SerializedName("valet.enable")
        val isValetEnabled: Boolean? = null,
        @SerializedName("privileges.enable")
        val isPrivilegesEnabled: Boolean? = null
    )

    internal data class BOUsabilla(
        @SerializedName("appid") val appId: String? = null,
        @SerializedName("events") val events: List<String>? = null,
        @SerializedName("formid") val formId: String? = null,
        @SerializedName("enabled") val usabillaEnabled: Boolean? = null
    )
}
