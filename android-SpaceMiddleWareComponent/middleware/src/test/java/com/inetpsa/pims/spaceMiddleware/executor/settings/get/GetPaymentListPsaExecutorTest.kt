package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.mmx.foundation.userSession.model.TokenResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.BOServices
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.BOUsabilla
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.SettingsResponse.OnlyYouParams
import com.inetpsa.pims.spaceMiddleware.model.settings.PaymentListOutput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetPaymentListPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetPaymentListPsaExecutor

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
        ownerManualWebViewUrl = "testOwnerManualWebViewUrl"

    )
    private val tokenResponse = TokenResponse.Success("testTokenResponseSuccess")
    private val paymentUrl = PaymentListOutput("test_methodUrl")

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetSettingsPSAExecutor::class)
        coEvery { anyConstructed<GetSettingsPSAExecutor>().execute() } returns Success(settings)
        executor = spyk(GetPaymentListPsaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with right input then return a Unit`() {
        val input = mapOf<String, Any>()
        val param = executor.params(input)
        Assert.assertEquals(Unit, param)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params()
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
    fun `when execute then make a network call with success response`() {
        val pin = "test_token"
        coEvery { executor.getToken() } returns pin

        runTest {
            val response = executor.execute()
            coVerify(exactly = 1) { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) }
            coVerify(exactly = 1) { executor.getToken() }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(paymentUrl, success.response)
        }
    }

    @Test
    fun `when execute then make a get API call and get a failure`() {
        val error = PIMSFoundationError.serverError(1, "test_body")
        coEvery { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute()
            coVerify(exactly = 1) { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(error, failure)
        }
    }

    @Test
    fun `when execute with paymentMethod enable false then make a network call with success response`() {
        val pin = "test_token"
        coEvery { executor.getToken() } returns pin
        runTest {
            val newSettings = settings.copy(samsPaymentMethodEnable = false, samsPaymentMethodUrl = "")
            coEvery { anyConstructed<GetSettingsPSAExecutor>().execute() } returns Success(newSettings)

            val response = executor.execute()

            coVerify(exactly = 1) { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) }
            coVerify(exactly = 1) { executor.getToken() }

            val expected = PaymentListOutput(null)

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(expected, success.response)
        }
    }

    @Test
    fun `when execute with paymentMethod enable false then make a network call with Failure response`() {
        runTest {
            val newSettings = settings.copy(samsPaymentMethodEnable = false, samsPaymentMethodUrl = "")
            val error = PIMSFoundationError.serverError(1, "test_body")
            coEvery { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) } returns NetworkResponse.Failure(error)

            val response = executor.execute()

            coVerify(exactly = 1) { anyConstructed<GetSettingsPSAExecutor>().execute(Unit) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(error, failure)
        }
    }

    @Test
    fun `when getPinToken get success then return pin token`() {
        every {
            userSessionManager.getToken(
                tokenType = any(),
                callback = captureLambda()
            )
        } answers {
            lambda<(TokenResponse) -> Unit>().captured.invoke(tokenResponse)
        }

        runTest {
            val response = executor.getToken()
            verify { userSessionManager.getToken(eq(TokenType.IDToken), any()) }

            Assert.assertEquals(tokenResponse.token, response)
        }
    }
}
