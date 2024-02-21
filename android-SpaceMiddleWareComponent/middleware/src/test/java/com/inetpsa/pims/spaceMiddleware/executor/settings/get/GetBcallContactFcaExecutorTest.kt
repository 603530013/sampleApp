package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetBcallContactFcaExecutor.CallCenterType
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesResponseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse.CallCenterSettingFca
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Activation
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ChannelFeature
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Pp
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Svla
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc.Registration
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class GetBcallContactFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetBcallContactFcaExecutor
    private val paramsVin = "testVin"
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    private val brandFcaSettings = listOf(
        SettingsCallCenterItemResponse(
            settingType = "testSettingsType",
            version = "test_version",
            settings = listOf(
                CallCenterSettingFca(
                    primaryNumber = "test_primaryNumber",
                    secondaryNumber = "test_secondaryNumber",
                    callType = "test_callType",
                    settingCategory = "BCALL",
                    callCenterType = "BRAND"
                )
            )
        )
    )
    private val uConnectFcaSettings = listOf(
        SettingsCallCenterItemResponse(
            settingType = "testSettingsType",
            version = "test_version",
            settings = listOf(
                CallCenterSettingFca(
                    primaryNumber = "test_primaryNumber",
                    secondaryNumber = "test_secondaryNumber",
                    callType = "test_callType",
                    settingCategory = "BCALL",
                    callCenterType = "UCONNECT"
                )
            )
        )
    )

    //region vehicles Response
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
        sdp = "testSdp",
        services = listOf(
            Service(service = "testService1", serviceEnabled = false, vehicleCapable = false),
            Service(service = "BCALL", serviceEnabled = true, vehicleCapable = true)
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

    private val bCallSettingsResponse = mapOf(
        Constants.Output.Common.PHONES to mapOf(
            Constants.PRIMARY to "test_primaryNumber",
            Constants.SECONDARY to "test_secondaryNumber"
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetSettingsFCAExecutor::class)
        mockkConstructor(GetVehiclesResponseFcaExecutor::class)
        mockkObject(CachedVehicles)
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse
        executor = spyk(GetBcallContactFcaExecutor(baseCommand), recordPrivateCalls = true)
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
    fun `when execute params with invalid vin then throw invalid parameter`() {
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
        coEvery { executor.executeByCallCenterType(paramsVin, CallCenterType.BRAND) } returns NetworkResponse.Success(
            bCallSettingsResponse
        )
        coEvery { executor.hasServices(paramsVin) } returns true

        runTest {
            val response = executor.execute(paramsVin)

            coVerify { executor.executeByCallCenterType(paramsVin, CallCenterType.BRAND) }
            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(bCallSettingsResponse, success.response)
        }
    }

    @Test
    fun `when execute then make a network call with failure response`() {
        coEvery { executor.hasServices(paramsVin) } returns false

        runTest {
            val response = executor.execute(paramsVin)
            val error = PimsErrors.apiNotSupported()

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
        }
    }

    @Test
    fun `when execute hasSOSServices with available service then return true`() {
        runTest {
            coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse
            val result = executor.hasServices(paramsVin)
            Assert.assertEquals(result, true)
        }
    }

    @Test
    fun `when execute hasSOSServices with unavailable service then return false`() {
        runTest {
            coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse.copy(
                services = emptyList()
            )
            val result = executor.hasServices(paramsVin)
            Assert.assertEquals(result, false)
        }
    }

    @Test
    fun `when execute on EMEA with valid vin then return phones`() {
        coEvery { executor.hasServices(paramsVin) } returns true
        coEvery { executor.readSettings(paramsVin) } returns NetworkResponse.Success(brandFcaSettings)
        coEvery { configurationManager.market } returns Market.EMEA
        runTest {
            val result = executor.execute(paramsVin)

            coVerify { executor.executeByCallCenterType(paramsVin, CallCenterType.BRAND) }
            coVerify { executor.readSettings(paramsVin) }
            coVerify(exactly = 1) {
                executor.putPhoneNumber(
                    any(),
                    "test_primaryNumber",
                    "primary"
                )
            }
            coVerify(exactly = 1) {
                executor.putPhoneNumber(
                    any(),
                    "test_secondaryNumber",
                    "secondary"
                )
            }

            Assert.assertEquals(true, result is NetworkResponse.Success)
            val success = result as NetworkResponse.Success
            Assert.assertEquals(bCallSettingsResponse, success.response)
        }
    }

    @Test
    fun `when execute on NAFTA with valid vin then return phones`() {
        coEvery { executor.hasServices(paramsVin) } returns true
        coEvery { executor.readSettings(paramsVin) } returns NetworkResponse.Success(uConnectFcaSettings)
        coEvery { configurationManager.market } returns Market.NAFTA
        runTest {
            val result = executor.execute(paramsVin)

            coVerify { executor.executeByCallCenterType(paramsVin, CallCenterType.UCONNECT) }
            coVerify { executor.readSettings(paramsVin) }
            coVerify(exactly = 1) {
                executor.putPhoneNumber(
                    any(),
                    "test_primaryNumber",
                    "primary"
                )
            }
            coVerify(exactly = 1) {
                executor.putPhoneNumber(
                    any(),
                    "test_secondaryNumber",
                    "secondary"
                )
            }

            Assert.assertEquals(true, result is NetworkResponse.Success)
            val success = result as NetworkResponse.Success
            Assert.assertEquals(bCallSettingsResponse, success.response)
        }
    }

    @Test
    fun `when execute on LATAM with valid vin then return phones`() {
        coEvery { executor.hasServices(paramsVin) } returns true
        coEvery { executor.readSettings(paramsVin) } returns NetworkResponse.Success(uConnectFcaSettings)
        coEvery { configurationManager.market } returns Market.LATAM
        runTest {
            val result = executor.execute(paramsVin)

            coVerify { executor.executeByCallCenterType(paramsVin, CallCenterType.UCONNECT) }
            coVerify { executor.readSettings(paramsVin) }
            coVerify(exactly = 1) {
                executor.putPhoneNumber(
                    any(),
                    "test_primaryNumber",
                    "primary"
                )
            }
            coVerify(exactly = 1) {
                executor.putPhoneNumber(
                    any(),
                    "test_secondaryNumber",
                    "secondary"
                )
            }

            Assert.assertEquals(true, result is NetworkResponse.Success)
            val success = result as NetworkResponse.Success
            Assert.assertEquals(bCallSettingsResponse, success.response)
        }
    }

    @Test
    fun `when execute readSettings with valid vin then return brand bcall`() {
        runTest {
            coEvery {
                anyConstructed<GetSettingsFCAExecutor>().execute(any())
            } returns NetworkResponse.Success(brandFcaSettings)
            executor.readSettings(paramsVin)
            coVerify(exactly = 1) { anyConstructed<GetSettingsFCAExecutor>().execute(eq(paramsVin)) }
        }
    }

    @Test
    fun `when execute readSettings with valid vin then return uConnect bcall`() {
        runTest {
            coEvery {
                anyConstructed<GetSettingsFCAExecutor>().execute(any())
            } returns NetworkResponse.Success(uConnectFcaSettings)
            executor.readSettings(paramsVin)
            coVerify(exactly = 1) { anyConstructed<GetSettingsFCAExecutor>().execute(eq(paramsVin)) }
        }
    }
}
