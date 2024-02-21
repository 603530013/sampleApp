package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesResponseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Activation
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ChannelFeature
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Pp
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Svla
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc.Registration
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.update.NicknameInput
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
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class NicknameUpdateFCAExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: NicknameUpdateFCAExecutor

    private val nicknameInput = NicknameInput(
        vin = "testVin",
        name = "testName"
    )
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    //region vehicles Response
    private val vehiclesResponse = VehiclesResponse(
        userid = "testUserId",
        version = "testVersion",
        vehicles = listOf(
            VehicleResponse(
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
                    Service(service = "testService2", serviceEnabled = true, vehicleCapable = true)
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
        )
    )
    //endregion

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetVehiclesResponseFcaExecutor::class)
        executor = spyk(NicknameUpdateFCAExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a update nickname API call with the right response`() {
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(any()) } returns NetworkResponse.Success(
            vehiclesResponse
        )
        every { executor.params(any()) } returns nicknameInput
        coEvery { communicationManager.post<Unit>(any(), any()) } returns Success(Unit)

        runTest {
            val bodyJson = mapOf(
                Constants.PARAMS_KEY_NICKNAME to nicknameInput.name
            )
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            "testCustomerId",
                            "/vehicles/",
                            "testVin",
                            "/",
                            "nickname/"
                        )
                    ),
                    body = bodyJson.toJson()
                )
            }

            coVerify {
                communicationManager.post<Unit>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }
            coVerify(exactly = 1) { executor.fetchVehicles() }
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute then make a update nickname API call with failure response`() {
        every { executor.params(any()) } returns nicknameInput
        coEvery { communicationManager.post<Unit>(any(), any()) } returns NetworkResponse.Failure(
            PIMSFoundationError.networkError
        )

        runTest {
            val bodyJson = mapOf(
                Constants.PARAMS_KEY_NICKNAME to nicknameInput.name
            )
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            "testCustomerId",
                            "/vehicles/",
                            "testVin",
                            "/",
                            "nickname/"
                        )
                    ),
                    body = bodyJson.toJson()
                )
            }

            coVerify {
                communicationManager.post<Unit>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }
            coVerify(exactly = 0) { executor.fetchVehicles() }
            coVerify(exactly = 0) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(PIMSFoundationError.networkError.code, failure.error?.code)
            Assert.assertEquals(PIMSFoundationError.networkError.message, failure.error?.message)
        }
    }

    @Test
    fun `when execute params with the right vin, right nickname then return params`() {
        val vin = "testVin"
        val name = "testName"

        val input = mapOf(
            Constants.PARAM_VIN to nicknameInput.vin,
            Constants.PARAMS_KEY_NAME to nicknameInput.name
        )
        val output = NicknameInput(vin, name)

        val param = executor.params(input)

        Assert.assertEquals(output, param)
    }

    @Test
    fun `when execute params with missing then throw invalid parameter`() {
        val vin = " "
        val name = "testName"

        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAMS_KEY_NAME to name
        )
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with the right vin, empty nickname then return params`() {
        val vin = "testVin"
        val name = ""

        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAMS_KEY_NAME to name
        )
        val output = NicknameInput(vin, NicknameUpdateFCAExecutor.NICKNAME_DELETE_VALUE)

        val param = executor.params(input)

        Assert.assertEquals(output, param)
        Assert.assertEquals(output, param)
    }

    @Test
    fun `when execute params with the right vin, null nickname then return params`() {
        val vin = "testVin"
        val name = null

        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAMS_KEY_NAME to name
        )
        val output = NicknameInput(vin, NicknameUpdateFCAExecutor.NICKNAME_DELETE_VALUE)

        val param = executor.params(input)

        Assert.assertEquals(output, param)
        Assert.assertEquals(output, param)
    }

    @Test
    fun `when execute params with the right vin, blank nickname then return params`() {
        val vin = "testVin"
        val name = "           "

        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAMS_KEY_NAME to name
        )
        val output = NicknameInput(vin, NicknameUpdateFCAExecutor.NICKNAME_DELETE_VALUE)

        val param = executor.params(input)

        Assert.assertEquals(output, param)
    }

    @Test
    fun `when execute params with the right vin, invalid nickname then throw exception`() {
        val vin = "testVin"
        val name = 123

        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAMS_KEY_NAME to name
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAMS_KEY_NAME)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with the right vin, without nickname then throw exception`() {
        val vin = "testVin"

        val input = mapOf(Constants.PARAM_VIN to vin)
        val exception = PIMSFoundationError.missingParameter(Constants.PARAMS_KEY_NAME)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
