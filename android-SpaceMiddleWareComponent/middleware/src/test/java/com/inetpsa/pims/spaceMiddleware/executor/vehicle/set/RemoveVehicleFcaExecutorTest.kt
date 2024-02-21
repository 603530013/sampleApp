package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Activation
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ChannelFeature
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Pp
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Svla
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc.Registration
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.remove.RemoveVehicleInput
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import com.inetpsa.pims.spaceMiddleware.util.getToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class RemoveVehicleFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: RemoveVehicleFcaExecutor
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    private val vehicleParams = RemoveVehicleInput(
        vin = "testVin",
        reason = "testReason",
        reasonId = "10"
    )

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
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.SessionManagerExtensionKt")
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.DataManagerExtensionsKt")
        executor = spyk(RemoveVehicleFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a delete API call with the right response`() {
        val pin = "testPin"
        val vin = "testVin"
        coEvery { communicationManager.delete<Unit>(any(), any()) } returns NetworkResponse.Success(Unit)
        coEvery { executor.removeFromVehicleCache(eq(vin)) } returns true
        coEvery { executor.removeFromVehiclesCache(eq(vin)) } returns true
        coEvery { executor.getPinToken() } returns NetworkResponse.Success(pin)

        val reason = mapOf(
            Constants.PARAM_REASON_ID to vehicleParams.reasonId,
            Constants.PARAM_REASON to vehicleParams.reason
        ).filterValues { it?.isNotBlank() == true }

        val bodyInfoFca = mapOf(
            Constants.PARAM_PIN_AUTH to pin,
            Constants.PARAM_REASON to reason
        ).toJson()

        runTest {
            val response = executor.execute(vehicleParams)

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(arrayOf("/v1/accounts/", uid, "/vehicles/", vehicleParams.vin, "/")),
                    body = eq(bodyInfoFca)
                )
            }

            coVerify {
                communicationManager.delete<Unit>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }
            coVerify(exactly = 1) { executor.removeFromVehicleCache(eq(vin)) }
            coVerify(exactly = 1) { executor.removeFromVehiclesCache(eq(vin)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute params with the right vin, right reason and right reasonId then return vin`() {
        val vin = "testVin"
        val reason = "delete"
        val reasonId = "0"
        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAM_REASON to reason,
            Constants.PARAM_REASON_ID to reasonId
        )
        val output = RemoveVehicleInput(vin, reason, reasonId)

        val param = executor.params(input)

        Assert.assertEquals(output, param)
    }

    @Test
    fun `when execute params with missing params then throw missing parameter`() {
        val vin = " "
        val reason = "delete"
        val reasonId = "0"
        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAM_REASON to reason,
            Constants.PARAM_REASON_ID to reasonId
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
    fun `when execute params with invalid reason then throw invalid parameter`() {
        val vin = "testVin"
        val reason = "invalid"
        val reasonId = "0"
        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAM_REASON to reason,
            Constants.PARAM_REASON_ID to reasonId
        )

        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_REASON)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute removeFromVehiclesCache then remove and save the new cache`() {
        mockkObject(CachedVehicles)
        coEvery { CachedVehicles.getAll(any(), any()) } returns vehiclesResponse
        coEvery { executor.saveVehicles(any()) } returns true

        runTest {
            val response = executor.removeFromVehiclesCache("testVin")
            Assert.assertEquals(true, response)
            coVerify { CachedVehicles.getAll(any(), Action.OnlyCache) }
            val vehicles = vehiclesResponse.vehicles.toMutableList()
            vehicles.removeIf { it.vin == "testVin" }
            val newResponse = vehiclesResponse.copy(vehicles = vehicles)
            coVerify { executor.saveVehicles(newResponse) }
        }
    }

    @Test
    fun `when removeFromVehicleCache then we remove from dataManager`() {
        runTest {
            val vin = "testVin"
            coEvery { middlewareComponent.deleteSync(any(), any()) } returns true
            executor.removeFromVehicleCache(vin)
            coVerify {
                middlewareComponent.deleteSync(
                    eq("vehicle_$vin"),
                    eq(APPLICATION)
                )
            }
        }
    }

    @Test
    fun `when execute saveVehicles then save them in cache`() {
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
            executor.saveVehicles(vehiclesResponse)
        }

        verify {
            dataManager.create(
                key = eq("ALFAROMEO_PREPROD_testCustomerId_vehicles"),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }

    @Test
    fun `when getPinToken get success then return pin token`() {
        val pin = "testPin"
        coEvery { userSessionManager.getToken(TokenType.OTPToken) } returns NetworkResponse.Success(pin)

        runTest {
            val response = executor.getPinToken()
            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(pin, success)
        }
    }

    @Test
    fun `when getPinToken get Failure then return failure`() {
        val error = PIMSFoundationError.serverError(404, "resource not found")
        coEvery { userSessionManager.getToken(TokenType.OTPToken) } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.getPinToken()
            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(404, failure?.subError?.status)
        }
    }
}
