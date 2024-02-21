package com.inetpsa.pims.spaceMiddleware.executor.settings

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.settings.Settings
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

@Deprecated("This should be replaced by GetSettingsPSAExecutorTest")
internal class GetSettingsDetailsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetSettingsDetailsPsaExecutor
    private val settings = Settings(
        eboutiqueEnabled = null,
        eboutiqueConnectPacksURL = null,
        eboutiqueNavcoUrl = null,
        samsEnabled = null,
        samsNavcoEnabled = null,
        samsNavcoURL = null,
        samsZarEnabled = null,
        samsZarURL = null,
        samsTmtsUrlEnable = null,
        samsTmtsUrl = null,
        remoteLevEnable = null,
        remoteLevSamsWebViewEnable = null,
        remoteAccessEnable = null,
        remoteLevUrl = null,
        ssoUrl = null,
        custHelpFormInApp = null,
        assistanceBrandPhone = null,
        assistanceRsaEnable = null,
        assistanceRsaPhone = null,
        assistanceRsaTypes = listOf(),
        assistanceRsaStatuses = listOf(),
        adsd = null,
        recallCampaginEnable = null,
        onlyLastTripEnable = null,
        o2xEnable = null,
        o2xServiceEnable = null,
        o2xServiceURL = null,
        o2xBoutiqueEnable = null,
        o2xBoutiqueURL = null,
        o2xEntretienEnable = null,
        o2xEntretienURL = null,
        o2xPhoneSOS = null,
        o2xVehicleElectricEnable = null,
        mobilityPassV2Enable = null,
        mobilityPassV2Url = null,
        brandUpdateEnable = null,
        samsMyaccountUrl = null,
        brandUpdateWindowsURL = null,
        brandUpdateMacURL = null,
        misterAutoUrl = null,
        cookiePolicyUrl = null,
        privacyPolicyUrl = null,
        scanMyCarEnable = null,
        updateCGU = null,
        contactNonCare = null,
        logIncidentURL = null,
        sendToNavEnable = null,
        services = null,
        contactForm = null,
        promotedServices = listOf(),
        dealerLocatorEnable = null,
        chargingTipsFaqEnable = null,
        findMyCarEnable = null,
        smartAppsEnable = null,
        dimboEnable = null,
        dimboInWebviewEnable = null,
        contractFinance = null,
        contractInsurance = null,
        prdvParams = null,
        settingsUpdate = null,
        lcvstellantis = null,
        prdvUrl = null,
        prdvType = null,
        ocrAwsEnable = null,
        appSalesForceSFID = null,
        isChargeLocatorServiceEnable = null,
        chargeLocatorServiceURL = null,
        chargeLocatorServiceIcon = null,
        chargeLocatorServiceAppName = null,
        chargeLocatorServiceAppID = null,
        chargeLocatorServicePackageName = null,
        customerHelpEnable = null,
        onlyYouDescEnable = null,
        customerHelpFAQ = null,
        customerHelpTel = null,
        customerHelpEmail = null,
        clubV2Enable = null,
        clubURL = null,
        clubV2CGU = null,
        clubv2Unsubscribe = null,
        rentEnable = null,
        rentURL = null,
        valet = null,
        valetURL = null,
        remoteAccess = null,
        fullDigital = null,
        onlyYouParams = null,
        mileageUpdateEnable = null,
        appHubEnable = null,
        infoFuelEnable = null,
        infoFuelUrl = null,
        maintenanceTimelineEnable = null,
        drivingDataEnable = null,
        hivebriteUrl = null,
        cguWebView = null,
        playListVideoEnable = null,
        voyantsEnable = null,
        myamiPlayEnable = null,
        myamiPlayStoreUrl = null,
        universEnable = null,
        boUsabilla = null,
        urlVoyants = null,
        samsPaymentMethodEnable = null,
        samsPaymentMethodUrl = null,
        featureBiometric = null,
        advisorAmiSitegeo = null
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetSettingsDetailsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val queries = mapOf(Constants.QUERY_PARAM_KEY_CULTURE to Locale.FRANCE.toString())
        coEvery { communicationManager.get<Settings>(any(), any()) } returns NetworkResponse.Success(settings)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(Settings::class.java),
                    urls = eq(arrayOf("/settings/v1/settings")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<Settings>(
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
