package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetSettingsPSAExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.BOServices
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.BOUsabilla
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.OnlyYouParams
import com.inetpsa.pims.spaceMiddleware.model.vehicles.manual.OwnerManualOutput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetVehicleManualPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVehicleManualPsaExecutor

    private val settings = SettingsResponse(
        eboutiqueEnabled = false,
        eboutiqueConnectPacksURL = "test_eboutiqueConnectUrl",
        eboutiqueNavcoUrl = "test_eboutiqueNavcoUrl",
        samsEnabled = false,
        remoteLevEnable = false,
        remoteLevSamsWebViewEnable = true,
        remoteAccessEnable = true,
        remoteLevUrl = "test_remoteLevUrl",
        ssoUrl = "test_ssoUrl",
        custHelpFormInApp = false,
        assistanceBrandPhone = "+33600000001",
        assistanceRsaEnable = true,
        assistanceRsaPhone = "00-88-99-77-11",
        assistanceRsaTypes = listOf(),
        assistanceRsaStatuses = listOf(),
        adsd = 2,
        recallCampaginEnable = true,
        onlyLastTripEnable = true,
        o2xEnable = true,
        o2xServiceEnable = true,
        o2xServiceURL = "test_o2xServiceUrl",
        o2xBoutiqueEnable = false,
        o2xBoutiqueURL = "test_o2xBoutiqueURL",
        o2xEntretienEnable = true,
        o2xEntretienURL = "test_o2xEntretienURL",
        o2xPhoneSOS = "test_o2xPhoneSOS",
        o2xVehicleElectricEnable = true,
        mobilityPassV2Enable = true,
        brandUpdateEnable = true,
        brandUpdateWindowsURL = "test_brandUpdateWindowsURL",
        brandUpdateMacURL = "test_brandUpdateWindowsURL",
        misterAutoUrl = "test_misterAutoUrl",
        cookiePolicyUrl = "test_cookiePolicyUrl",
        privacyPolicyUrl = "test_privacyPolicyUrl",
        scanMyCarEnable = true,
        updateCGU = 10,
        contactNonCare = true,
        logIncidentURL = "test_logIncidentURL",
        sendToNavEnable = true,
        services = BOServices(
            true,
            true,
            true,
            true
        ),
        contactForm = "test_contactForm",
        promotedServices = listOf(),
        dealerLocatorEnable = true,
        chargingTipsFaqEnable = true,
        findMyCarEnable = true,
        smartAppsEnable = true,
        dimboEnable = true,
        dimboInWebviewEnable = true,
        contractFinance = "test_contract",
        contractInsurance = "test_contractInsurance",
        prdvParams = "test_prdvParams",
        settingsUpdate = "test_settingsUpdate",
        lcvstellantis = true,
        prdvUrl = "test_prdvUrl",
        prdvType = "test_prdvType",
        ocrAwsEnable = true,
        appSalesForceSFID = "test_forceSFID",
        isChargeLocatorServiceEnable = true,
        chargeLocatorServiceURL = "test_locatorUrl",
        chargeLocatorServiceIcon = "test_locatorIcon",
        chargeLocatorServiceAppName = "testServiceAppName",
        chargeLocatorServiceAppID = "testServiceAppID",
        chargeLocatorServicePackageName = "testServicePackageName",
        customerHelpEnable = true,
        onlyYouDescEnable = true,
        customerHelpFAQ = "test_helpFAQ",
        customerHelpTel = "test_HelpTel",
        customerHelpEmail = "test_HelpEmail",
        clubURL = "test_clubURL",
        clubV2CGU = "test_clubV2CGU",
        clubv2Unsubscribe = "test_clubv2Unsubscribe",
        rentEnable = true,
        rentURL = "test_rentURL",
        valet = true,
        valetURL = "test_valetUrl",
        fullDigital = true,
        mileageUpdateEnable = true,
        appHubEnable = true,
        infoFuelEnable = true,
        infoFuelUrl = "test_infoFuelurl",
        maintenanceTimelineEnable = true,
        drivingDataEnable = true,
        cguWebView = "test_cguWebView",
        playListVideoEnable = true,
        voyantsEnable = true,
        myamiPlayEnable = true,
        myamiPlayStoreUrl = "test_myamiPlayStoreUrl",
        universEnable = true,
        boUsabilla = BOUsabilla("test_appid", listOf(), "test_formID", true),
        samsPaymentMethodEnable = true,
        samsPaymentMethodUrl = "test_methodUrl",
        featureBiometric = true,
        advisorAmiSitegeo = "test_siteGeo",
        buddyEnable = true,
        buddyIcon = "test_buddyIcon",
        buddyiOSUrl = "test_url",
        connectedAlaram = "test_connectedAlaram",
        evRoutingEnable = "test_evRoutingEnable",
        insuranceAppointment = "testAppointment",
        insuranceeQuote = "testInsurence",
        mapUpdateEnable = true,
        mapUpdateUrl = "testMapUpdateUrl",
        mobilityPassV2Url = "testMobility",
        onlyYouParams = OnlyYouParams(true, true, false, false),
        prdvEnable = "testPrdv",
        resetButton = "testButton",
        samsAccountUrl = "testSamsAccountUrl",
        samsNavcoEnabled = false,
        samsNavcoURL = "testSams",
        samsTmtsUrl = "testSamsTmts",
        samsTmtsUrlEnable = false,
        samsZarEnabled = false,
        samsZarURL = "testSamsZarUrl",
        webRadioEnable = "testWebRadioEnable",
        webRadioProvider = "testWebRadioProvider",
        ownerManualWebViewUrl = "https://public-servicebox.opel.com/OVddb/OV/index.html?"
    )

    val settingsResponse = SettingsResponse(
        eboutiqueEnabled = false,
        eboutiqueConnectPacksURL = "test_eboutiqueConnectUrl",
        eboutiqueNavcoUrl = "test_eboutiqueNavcoUrl",
        samsEnabled = false,
        remoteLevEnable = false,
        remoteLevSamsWebViewEnable = true,
        remoteAccessEnable = true,
        remoteLevUrl = "test_remoteLevUrl",
        ssoUrl = "test_ssoUrl",
        custHelpFormInApp = false,
        assistanceBrandPhone = "+33600000001",
        assistanceRsaEnable = true,
        assistanceRsaPhone = "00-88-99-77-11",
        assistanceRsaTypes = listOf(),
        assistanceRsaStatuses = listOf(),
        adsd = 2,
        recallCampaginEnable = true,
        onlyLastTripEnable = true,
        o2xEnable = true,
        o2xServiceEnable = true,
        o2xServiceURL = "test_o2xServiceUrl",
        o2xBoutiqueEnable = false,
        o2xBoutiqueURL = "test_o2xBoutiqueURL",
        o2xEntretienEnable = true,
        o2xEntretienURL = "test_o2xEntretienURL",
        o2xPhoneSOS = "test_o2xPhoneSOS",
        o2xVehicleElectricEnable = true,
        mobilityPassV2Enable = true,
        brandUpdateEnable = true,
        brandUpdateWindowsURL = "test_brandUpdateWindowsURL",
        brandUpdateMacURL = "test_brandUpdateWindowsURL",
        misterAutoUrl = "test_misterAutoUrl",
        cookiePolicyUrl = "test_cookiePolicyUrl",
        privacyPolicyUrl = "test_privacyPolicyUrl",
        scanMyCarEnable = true,
        updateCGU = 10,
        contactNonCare = true,
        logIncidentURL = "test_logIncidentURL",
        sendToNavEnable = true,
        services = BOServices(
            true,
            true,
            true,
            true
        ),
        contactForm = "test_contactForm",
        promotedServices = listOf(),
        dealerLocatorEnable = true,
        chargingTipsFaqEnable = true,
        findMyCarEnable = true,
        smartAppsEnable = true,
        dimboEnable = true,
        dimboInWebviewEnable = true,
        contractFinance = "test_contract",
        contractInsurance = "test_contractInsurance",
        prdvParams = "test_prdvParams",
        settingsUpdate = "test_settingsUpdate",
        lcvstellantis = true,
        prdvUrl = "test_prdvUrl",
        prdvType = "test_prdvType",
        ocrAwsEnable = true,
        appSalesForceSFID = "test_forceSFID",
        isChargeLocatorServiceEnable = true,
        chargeLocatorServiceURL = "test_locatorUrl",
        chargeLocatorServiceIcon = "test_locatorIcon",
        chargeLocatorServiceAppName = "testServiceAppName",
        chargeLocatorServiceAppID = "testServiceAppID",
        chargeLocatorServicePackageName = "testServicePackageName",
        customerHelpEnable = true,
        onlyYouDescEnable = true,
        customerHelpFAQ = "test_helpFAQ",
        customerHelpTel = "test_HelpTel",
        customerHelpEmail = "test_HelpEmail",
        clubURL = "test_clubURL",
        clubV2CGU = "test_clubV2CGU",
        clubv2Unsubscribe = "test_clubv2Unsubscribe",
        rentEnable = true,
        rentURL = "test_rentURL",
        valet = true,
        valetURL = "test_valetUrl",
        fullDigital = true,
        mileageUpdateEnable = true,
        appHubEnable = true,
        infoFuelEnable = true,
        infoFuelUrl = "test_infoFuelurl",
        maintenanceTimelineEnable = true,
        drivingDataEnable = true,
        cguWebView = "test_cguWebView",
        playListVideoEnable = true,
        voyantsEnable = true,
        myamiPlayEnable = true,
        myamiPlayStoreUrl = "test_myamiPlayStoreUrl",
        universEnable = true,
        boUsabilla = BOUsabilla("test_appid", listOf(), "test_formID", true),
        samsPaymentMethodEnable = true,
        samsPaymentMethodUrl = "test_methodUrl",
        featureBiometric = true,
        advisorAmiSitegeo = "test_siteGeo",
        buddyEnable = true,
        buddyIcon = "test_buddyIcon",
        buddyiOSUrl = "test_url",
        connectedAlaram = "test_connectedAlaram",
        evRoutingEnable = "test_evRoutingEnable",
        insuranceAppointment = "testAppointment",
        insuranceeQuote = "testInsurence",
        mapUpdateEnable = true,
        mapUpdateUrl = "testMapUpdateUrl",
        mobilityPassV2Url = "testMobility",
        onlyYouParams = OnlyYouParams(true, true, false, false),
        prdvEnable = "testPrdv",
        resetButton = "testButton",
        samsAccountUrl = "testSamsAccountUrl",
        samsNavcoEnabled = false,
        samsNavcoURL = "testSams",
        samsTmtsUrl = "testSamsTmts",
        samsTmtsUrlEnable = false,
        samsZarEnabled = false,
        samsZarURL = "testSamsZarUrl",
        webRadioEnable = "testWebRadioEnable",
        webRadioProvider = "testWebRadioProvider",
        ownerManualWebViewUrl = ""
    )

    private val ownerManualSdkData = OwnerManualOutput(
        type = "sdk",
        url = null
    )

    private val ownerManualWebData = OwnerManualOutput(
        type = "web",
        url = "https://public-servicebox.opel.com/OVddb/OV/index.html?FCAHBTTG2P9003558"
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetSettingsPSAExecutor::class)
        coEvery { anyConstructed<GetSettingsPSAExecutor>().execute() } returns Success(settings)
        executor = spyk(GetVehicleManualPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with right input then return a Vin`() {
        val params = "testVin"
        val input = mapOf(Constants.PARAM_VIN to params)
        val output = executor.params(input)
        Assert.assertEquals(params, output)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf<String, Any?>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val vin = 123
        val input = mapOf(Constants.PARAM_VIN to vin)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when brand is Citroen execute then return a VehicleManualSdk`() {
        every { configurationManager.brand } returns Brand.CITROEN
        runTest {
            val paramsVin = "testVin"
            val response = executor.execute(paramsVin)
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(ownerManualSdkData, success.response)
        }
    }

    @Test
    fun `when brand is Peugeot execute then return a VehicleManualSdk`() {
        every { configurationManager.brand } returns Brand.PEUGEOT
        runTest {
            val paramsVin = "testVin"
            val response = executor.execute(paramsVin)
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(ownerManualSdkData, success.response)
        }
    }

    @Test
    fun `when brand is DS execute then return a VehicleManualSdk`() {
        every { configurationManager.brand } returns Brand.DS
        runTest {
            val paramsVin = "testVin"
            val response = executor.execute(paramsVin)
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(ownerManualSdkData, success.response)
        }
    }

    @Test
    fun `when brand is Opel execute then return a WebOwnerManual`() {
        every { configurationManager.brand } returns Brand.OPEL
        runTest {
            val paramsVin = "FCAHBTTG2P9003558"
            val response = executor.execute(paramsVin)
            coVerify(exactly = 1) { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(ownerManualWebData, success.response)
        }
    }

    @Test
    fun `when brand is Vauxhall execute then return a WebOwnerManual`() {
        every { configurationManager.brand } returns Brand.VAUXHALL
        runTest {
            val paramsVin = "FCAHBTTG2P9003558"
            val response = executor.execute(paramsVin)
            coVerify(exactly = 1) { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(ownerManualWebData, success.response)
        }
    }

    @Test
    fun `when look for PSA executor with invalid Brand then throw PimsError exception`() {
        runTest {
            every { configurationManager.brand } returns Brand.JEEP
            val exception = PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)

            try {
                executor.execute("FCAHBTTG2P9003558")
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when brand is Opel owner manual webview is blank then return ownermanualoutput`() {
        val ownerManualData = OwnerManualOutput(
            type = null,
            url = null
        )

        every { configurationManager.brand } returns Brand.OPEL
        coEvery { anyConstructed<GetSettingsPSAExecutor>().execute() } returns Success(settingsResponse)
        runTest {
            val paramsVin = "FCAHBTTG2P9003558"
            val response = executor.execute(paramsVin)
            coVerify(exactly = 1) { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(ownerManualData, success.response)
        }
    }
}
