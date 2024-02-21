package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.BOServices
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.BOUsabilla
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.OnlyYouParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class GetSettingsPSAExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetSettingsPSAExecutor

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
        assistanceBrandPhone = "00-80-80-60-90",
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
        webRadioProvider = "testWebRadioProvider"
        // webViewEnable = "testWebViewEnable"

    )

    @Before
    override fun setup() {
        super.setup()
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        executor = spyk(GetSettingsPSAExecutor(middlewareComponent))
    }

    @Test
    fun `when execute then make a get API call`() {
        val queries = mapOf(Constants.QUERY_PARAM_KEY_CULTURE to Locale.FRANCE.toString())
        coEvery { communicationManager.get<SettingsResponse>(any(), any()) } returns NetworkResponse.Success(settings)

        every {
            dataManager.create(
                key = any(),
                data = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(SettingsResponse::class.java),
                    urls = eq(arrayOf("/settings/v1/settings")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<SettingsResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(settings, success.response)
        }
    }
}
