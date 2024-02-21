package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Add
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleImageResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Activation
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ChannelFeature
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Pp
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Svla
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc.Registration
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehiclesOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehiclesOutput.Vehicle
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
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

internal class GetVehiclesFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetVehiclesFcaExecutor
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

    private val imageResponse = VehicleImageResponse(
        vin = "testVin",
        imageUrl = "testImageUrl",
        description = "testDescription",
        make = "testMake",
        subMake = "testSubMake",
        year = 2019,
        tcuType = "testTcuType",
        sdp = "testSdp",
        userid = "testUserId",
        tcCountryCode = "testTcCountryCode"
    )
    //endregion

    private val vehiclesOutput = VehiclesOutput(
        listOf(
            Vehicle(
                vin = "testVin",
                shortLabel = "testNickname",
                modelDescription = "testModelDescription",
                picture = "testImageUrl"
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetVehiclesResponseFcaExecutor::class)
        val successResponse = NetworkResponse.Success(vehiclesResponse)
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(null) } returns successResponse
        mockkConstructor(GetVehicleImageFcaExecutor::class)
        val successImage = NetworkResponse.Success(imageResponse)
        coEvery { anyConstructed<GetVehicleImageFcaExecutor>().execute(any()) } returns successImage

        executor = spyk(GetVehiclesFcaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with missing action then throw missing parameter`() {
        val input = emptyMap<String, Any?>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid action then throw invalid parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.Input.ACTION to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then continue execution`() {
        val input = mapOf(Constants.Input.ACTION to Constants.Input.Action.GET)
        val output = executor.params(input)
        Assert.assertEquals(Action.Get, output.action)
        Assert.assertEquals(null, output.vin)
    }

    @Test
    fun `when execute params with invalid vin then throw invalid parameter`() {
        val vin = 123
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute transformToVehicleOutput then return VehiclesOutput`() {
        val result = executor.transformToVehicleOutput(vehiclesResponse)
        Assert.assertEquals(1, result.vehicles.size)
        val firstElement = result.vehicles.firstOrNull()
        Assert.assertEquals("testVin", firstElement?.vin)
        Assert.assertEquals("testNickname", firstElement?.shortLabel)
        Assert.assertEquals("testImageUrl", firstElement?.picture)
    }

    @Test
    fun `when execute readFromCache in available case then return cache`() {
        val json = vehiclesResponse.toJson()
        every { dataManager.read(any(), any()) } returns json
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("ALFAROMEO_PREPROD_testCustomerId_vehicles"), eq(APPLICATION)) }
        Assert.assertEquals(json, cache)
    }

    @Test
    fun `when execute readFromCache in empty case then return null`() {
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("ALFAROMEO_PREPROD_testCustomerId_vehicles"), eq(APPLICATION)) }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute readFromJson with valid json then return items`() {
        val json = vehiclesResponse.toJson()
        val cache = executor.readFromJson(json)
        Assert.assertEquals(vehiclesResponse.version, cache?.version)
        Assert.assertEquals(vehiclesResponse.userid, cache?.userid)
        Assert.assertEquals(vehiclesResponse.vehicles.size, cache?.vehicles?.size)
        Assert.assertEquals(vehiclesResponse, cache)
    }

    @Test
    fun `when execute readFromJson with invalid json then return null`() {
        val cache = executor.readFromJson(null)
        Assert.assertEquals(null, cache)

        val invalidCache = executor.readFromJson("testInvalidJson")
        Assert.assertEquals(null, invalidCache)
    }

    @Test
    fun `when execute with action get and there is not a valid cache then make a network call`() {
        every { executor.readFromCache() } returnsMany listOf(null, "{testCache}")
        every { executor.readFromJson(any()) } returns vehiclesResponse
        coEvery {
            anyConstructed<GetVehiclesResponseFcaExecutor>().execute(null)
        } returns NetworkResponse.Success(vehiclesResponse)

        coEvery { executor.fetchImages(any()) } returns vehiclesResponse

        runTest {
            executor.execute(UserInput(Get, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = vehiclesResponse.vehicles.size) { executor.fetchImages(eq(vehiclesResponse)) }
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(null) }
        }
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        val cache = "{testCache}"
        every { executor.readFromCache() } returns cache
        every { executor.readFromJson(any()) } returns vehiclesResponse

        runTest {
            val response = executor.execute(UserInput(Get, null))
            verify(exactly = 1) { executor.readFromCache() }
            verify(exactly = 1) { executor.readFromJson(any()) }
            coVerify(exactly = 0) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(null) }
            coVerify(exactly = 0) { executor.fetchImages(eq(vehiclesResponse)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehiclesOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response`() {
        every { executor.readFromCache() } returns "{ test vehicles from cache }"
        every { executor.readFromJson(any()) } returns vehiclesResponse
        coEvery {
            anyConstructed<GetVehiclesResponseFcaExecutor>().execute(null)
        } returns NetworkResponse.Success(vehiclesResponse)
        coEvery { executor.fetchImages(any()) } returns vehiclesResponse

        runTest {
            val response = executor.execute(UserInput(Refresh, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readFromCache() }
            verify(exactly = 0) { executor.readFromJson(any()) }
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(null) }
            coVerify(exactly = vehiclesResponse.vehicles.size) { executor.fetchImages(eq(vehiclesResponse)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehiclesOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with failure response`() {
        every { executor.readFromCache() } returns "{ test vehicles from cache }"
        every { executor.readFromJson(any()) } returns vehiclesResponse
        val error = PimsErrors.serverError(null, "test-errors")
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(null) } returns NetworkResponse.Failure(
            error
        )

        runTest {
            val response = executor.execute(UserInput(Refresh, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readFromCache() }
            verify(exactly = 0) { executor.readFromJson(any()) }
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(null) }
            coVerify(exactly = 0) { executor.fetchImages(eq(vehiclesResponse)) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }

    @Test
    fun `when execute fetchImage then use the GetVehicleImageFcaExecutor with failure`() {
        val failureAnswer = NetworkResponse.Failure(PimsErrors.apiNotSupported())
        coEvery { anyConstructed<GetVehicleImageFcaExecutor>().execute(any()) } returns failureAnswer

        runTest {
            val response = executor.fetchImages(vehiclesResponse)
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            coVerify(exactly = vehiclesResponse.vehicles.size) {
                anyConstructed<GetVehicleImageFcaExecutor>().execute(any())
            }
            Assert.assertEquals(null, response.vehicles.firstOrNull()?.imageUrl)
        }
    }

    @Test
    fun `when execute fetchImage then use the GetVehicleImageFcaExecutor with Success`() {
        val successAnswer = NetworkResponse.Success(
            VehicleImageResponse(
                vin = "testVin1",
                imageUrl = "testImage1",
                description = "testDescription",
                make = "testMake",
                subMake = "testSubMake",
                year = 2019,
                tcuType = "testTcuType",
                sdp = "testSdp",
                userid = "testUserId",
                tcCountryCode = "testTcCountryCode"
            )
        )
        coEvery { anyConstructed<GetVehicleImageFcaExecutor>().execute(any()) } returns successAnswer

        runTest {
            val response = executor.fetchImages(vehiclesResponse)
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            coVerify(exactly = vehiclesResponse.vehicles.size) {
                anyConstructed<GetVehicleImageFcaExecutor>().execute(any())
            }
            Assert.assertEquals("testImage1", response.vehicles.firstOrNull()?.imageUrl)
        }
    }

    @Test
    fun `when execute with invalid action add then throw exception`() {
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)

        runTest {
            try {
                executor.execute(UserInput(Add, "testVin"))
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }
}
