package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetSettingsFCAExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.assistance.CallCenter
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse.CallCenterSettingFca
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse.CallCenterSettingFca.Category
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Activation
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ChannelFeature
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Pp
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.SDP
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Svla
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc.Registration
import io.mockk.coEvery
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale

internal class GetAssistancePhonesFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetAssistancePhonesFcaExecutor

    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    private val fcaSettings = listOf(
        SettingsCallCenterItemResponse(
            settingType = "testSettingsType",
            version = "test_version",
            settings = listOf(
                CallCenterSettingFca(
                    primaryNumber = "+1234567890",
                    secondaryNumber = "+0987654321",
                    callType = Category.B_CALL.input,
                    settingCategory = Category.B_CALL.input,
                    callCenterType = CallCenterSettingFca.CallCenterType.RoadSide.input
                )
            )
        )
    )

    private val vehicleResponse = VehicleResponse(
        activationSource = "testActivationSource",
        brandCode = "testBrandCode",
        channelFeatures = listOf(
            ChannelFeature(
                channels = listOf("testChannel"),
                featureCode = "testFeatureCode"
            )
        ),
        color = "testColor",
        company = "testCompany",
        customerRegStatus = "testCustomerRegStatus",
        enrollmentStatus = "testEnrollmentStatus",
        fuelType = "testFuelType",
        isCompanyCar = false,
        language = "testLanguage",
        make = "testMake",
        market = "testMarket",
        model = "testModel",
        modelDescription = "testModelDescription",
        navEnabledHU = false,
        nickname = "testNickname",
        pp = Pp(Activation(status = "testStatus", version = "testVersion")),
        privacyMode = "testPrivacyMode",
        radio = "testRadio",
        regStatus = "testRegStatus",
        regTimestamp = today,
        sdp = "SXM",
        services = listOf(
            Service(service = "bCall", serviceEnabled = true, vehicleCapable = true)
        ),
        soldRegion = "testSoldRegion",
        subMake = "testSubMake",
        svla = Svla(status = "testStatus", timestamp = today),
        tc = Tc(
            activation = Activation(
                status = "testStatus",
                version = "testVersion"
            ),
            registration = Registration(
                status = "testStatus",
                version = "testVersion"
            )
        ),
        tcuType = "testTcuType",
        tsoBodyCode = "testTsoBodyCode",
        tsoModelYear = "testTsoModelYear",
        vin = "testVin",
        year = 1986,
        imageUrl = "testImageUrl"
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetSettingsFCAExecutor::class)
        mockkObject(CachedVehicles)
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse
        coEvery { anyConstructed<GetSettingsFCAExecutor>().execute(any()) } returns NetworkResponse.Success(fcaSettings)
        executor = spyk(GetAssistancePhonesFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute hasService with SDP SXM and settingCategory as BCALL`() {
        val sdp = SDP.SXM
        val services = listOf(Category.B_CALL.input, Category.E_CALL.input)
        val callCenterSettings = CallCenterSettingFca(
            primaryNumber = "+1234567890",
            secondaryNumber = "+0987654321",
            callType = Category.B_CALL.input,
            settingCategory = Category.B_CALL.input,
            callCenterType = "ROADSIDE"
        )
        assertEquals(true, executor.hasService(sdp, services, callCenterSettings))
    }

    @Test
    fun `when execute hasService with SDP SXM and settingCategory different than BCALL`() {
        val sdp = SDP.SXM
        val services = listOf(Category.B_CALL.input, Category.E_CALL.input)
        val callCenterSettings = CallCenterSettingFca(
            primaryNumber = "+1234567890",
            secondaryNumber = "+0987654321",
            callType = Category.E_CALL.input,
            settingCategory = Category.E_CALL.input,
            callCenterType = "ROADSIDE"
        )
        assertEquals(false, executor.hasService(sdp, services, callCenterSettings))
    }

    @Test
    fun `when execute hasService with SDP IGNITE`() {
        val sdp = SDP.IGNITE
        val services = listOf(Category.B_CALL.input, Category.E_CALL.input)
        var callCenterSettings = CallCenterSettingFca(
            primaryNumber = "+1234567890",
            secondaryNumber = "+0987654321",
            callType = "test_callType",
            settingCategory = "test_settins",
            callCenterType = "ROADSIDE"
        )
        assertEquals(false, executor.hasService(sdp, services, callCenterSettings))
        callCenterSettings = CallCenterSettingFca(
            primaryNumber = "+1234567890",
            secondaryNumber = "+0987654321",
            callType = "test_callType",
            settingCategory = Category.B_CALL.input,
            callCenterType = "ROADSIDE"
        )
        assertEquals(true, executor.hasService(sdp, services, callCenterSettings))
        callCenterSettings = CallCenterSettingFca(
            primaryNumber = "+1234567890",
            secondaryNumber = "+0987654321",
            callType = "test_callType",
            settingCategory = Category.E_CALL.input,
            callCenterType = "ROADSIDE"
        )
        assertEquals(true, executor.hasService(sdp, services, callCenterSettings))
    }

    @Test
    fun `when execute hasService with SDP SPRINT`() {
        val sdp = SDP.SPRINT
        val services = listOf(Category.B_CALL.input, Category.E_CALL.input)
        val callCenterSettings = CallCenterSettingFca(
            primaryNumber = "+1234567890",
            secondaryNumber = "+0987654321",
            callType = Category.E_CALL.input,
            settingCategory = Category.B_CALL.input,
            callCenterType = "ROADSIDE"
        )
        assertEquals(false, executor.hasService(sdp, services, callCenterSettings))
    }

    @Test
    fun `when execute callCenterTypeFilter with CallCenterType RoadSide`() {
        val callCenterType = CallCenterSettingFca.CallCenterType.RoadSide
        assertEquals(
            CallCenterSettingFca.CallCenterType.RoadSide,
            executor.callCenterTypeFilter(callCenterType)
        )
    }

    @Test
    fun `when execute callCenterTypeFilter with CallCenterType UConnect`() {
        val callCenterType = CallCenterSettingFca.CallCenterType.UConnect
        assertEquals(
            CallCenterSettingFca.CallCenterType.UConnect,
            executor.callCenterTypeFilter(callCenterType)
        )
    }

    @Test
    fun `when execute callCenterTypeFilter with CallCenterType Brand`() {
        val callCenterType = CallCenterSettingFca.CallCenterType.Brand
        assertEquals(
            CallCenterSettingFca.CallCenterType.Brand,
            executor.callCenterTypeFilter(callCenterType)
        )
    }

    @Test
    fun `when execute callCenterTypeFilter with CallCenterType None`() {
        val callCenterType = CallCenterSettingFca.CallCenterType.None
        val type = executor.callCenterTypeFilter(callCenterType)
        assertEquals(
            CallCenterSettingFca.CallCenterType.None,
            type
        )
    }

    @Test
    fun `when execute params with right input then return a Vin`() {
        val params = "testVin"
        val input = mapOf(Constants.PARAM_VIN to params)
        val output = executor.params(input)
        assertEquals(params, output)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf<String, Any?>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
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
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute embeddedCallCenter with make FIAT and market US`() {
        val make = "FIAT"
        val result = executor.embeddedCallCenter(make, Locale.US.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.Brand.output to CallCenter(primary = "+18882426342"),
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241"),
            CallCenterSettingFca.CallCenterType.RoadSide.output to CallCenter(primary = "+18005212779")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with make FIAT and market Canada`() {
        val make = "FIAT"
        val result = executor.embeddedCallCenter(make, Locale.CANADA.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.Brand.output to CallCenter(primary = "+18882426342"),
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241"),
            CallCenterSettingFca.CallCenterType.RoadSide.output to CallCenter(primary = "+18003634869")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with make FIAT and market Italy`() {
        val make = "FIAT"
        val result = executor.embeddedCallCenter(make, Locale.ITALY.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.Brand.output to CallCenter(primary = "+18882426342"),
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with make RAM and market Italy`() {
        val make = "RAM"
        val result = executor.embeddedCallCenter(make, Locale.ITALY.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.Brand.output to CallCenter(primary = "+18667264636"),
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with make JEEP and market Italy`() {
        val make = "JEEP"
        val result = executor.embeddedCallCenter(make, Locale.ITALY.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.Brand.output to CallCenter(primary = "+18774265337"),
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with make DODGE and market Italy`() {
        val make = "DODGE"
        val result = executor.embeddedCallCenter(make, Locale.ITALY.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.Brand.output to CallCenter(primary = "+18004236343"),
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with make CHRYSLER and market Italy`() {
        val make = "CHRYSLER"
        val result = executor.embeddedCallCenter(make, Locale.ITALY.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.Brand.output to CallCenter(primary = "+18002479753"),
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with make MASERATI and market Italy`() {
        val make = "MASERATI"
        val result = executor.embeddedCallCenter(make, Locale.ITALY.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with make LANCIA and market Italy`() {
        val make = "LANCIA"
        val result = executor.embeddedCallCenter(make, Locale.ITALY.country)
        val expected = mutableMapOf(
            CallCenterSettingFca.CallCenterType.UConnect.output to CallCenter(primary = "+18557924241")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `when execute embeddedCallCenter with embeddedCallCenterForUConnect with make FIAT,market US`() {
        val make = "FIAT"
        val market = "US"
        val callCenterMap = executor.embeddedCallCenter(make, market)
        val brandCallCenter = mutableMapOf(
            "uConnect" to CallCenter(primary = "+18557924241")
        )
        assertEquals(brandCallCenter["uConnect"]?.primary, callCenterMap["uConnect"]?.primary)
    }

    @Test
    fun `when execute embeddedCallCenter with embeddedCallCenterForRoadSide with make FIAT,market US`() {
        val make = "FIAT"
        val market = "US"
        val callCenterMap = executor.embeddedCallCenter(make, market)
        val brandCallCenter = mutableMapOf(
            "roadSide" to CallCenter(primary = "+18005212779")
        )
        assertEquals(brandCallCenter["roadSide"]?.primary, callCenterMap["roadSide"]?.primary)
    }

    @Test
    fun `when execute hasServices with service bCall,serviceEnabled true,vehicleCapable true`() {
        val services = listOf("bCall")
        val result = executor.hasService(SDP.SXM, services, fcaSettings[0].settings?.get(0)!!)
        assertEquals(true, result)
    }

    @Test
    fun `when execute transformSettings `() {
        val settings = listOf(
            SettingsCallCenterItemResponse(
                settingType = "testSettingsType",
                version = "test_version",
                settings = listOf(
                    CallCenterSettingFca(
                        primaryNumber = "111-111-1111",
                        secondaryNumber = "222-222-2222",
                        callType = Category.B_CALL.input,
                        settingCategory = Category.B_CALL.input,
                        callCenterType = CallCenterSettingFca.CallCenterType.RoadSide.input
                    ),
                    CallCenterSettingFca(
                        primaryNumber = "333-333-3333",
                        secondaryNumber = "444-444-4444",
                        callType = "Call Center 2",
                        settingCategory = Category.B_CALL.input,
                        callCenterType = CallCenterSettingFca.CallCenterType.RoadSide.input
                    )
                )
            )
        )
        // val vehicle = VehicleResponse(sdpEnum = "SDP1")
        val services = listOf(Category.B_CALL.input, Category.E_CALL.input)
        val expectedMap = mutableMapOf(
            "roadSide" to CallCenter(
                primary = "111-111-1111",
                secondary = "222-222-2222"
            )
        )

        val result = executor.transformSettings(settings, vehicleResponse, services)
        assertEquals(expectedMap, result)
    }

    @Test
    fun `when execute then return the right response `() {
        val expectedMap = mutableMapOf(
            "roadSide" to CallCenter(
                primary = "+1234567890",
                secondary = "+0987654321"
            )
        )
        runTest {
            val response = executor.execute("testVin")
            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(expectedMap, success.response)
        }
    }

    @Test
    fun `when execute with sdp SPRINT then return the right response `() {
        val vehicle = vehicleResponse.copy(sdp = "SPRINT")
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicle
        val expectedMap = mutableMapOf(
            "uConnect" to CallCenter(
                primary = "+18557924241",
                secondary = null
            )
        )
        runTest {
            val response = executor.execute("testVin")
            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(expectedMap, success.response)
        }
    }
}
