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
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class GetVehicleVinNormalFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetVehicleVinNormalFcaExecutor
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

    private val vehicleOutput = VehicleOutput(
        vin = "testVin",
        lcdv = null,
        eligibility = null,
        attributes = null,
        type = VehicleOutput.Type.UNKNOWN,
        name = "testNickname",
        regTimeStamp = today,
        year = 1986,
        lastUpdate = null,
        sdp = "testSdp",
        market = "testMarket",
        make = "testMake",
        subMake = "testSubMake",
        picture = "testImageUrl",
        enrollmentStatus = "testEnrollmentStatus"
    )
    //endregion

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetVehiclesResponseFcaExecutor::class)
        val successResponse = NetworkResponse.Success(vehiclesResponse)
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(any()) } returns successResponse
        mockkConstructor(GetVehicleImageFcaExecutor::class)
        val successImage = NetworkResponse.Success(imageResponse)
        coEvery { anyConstructed<GetVehicleImageFcaExecutor>().execute(any()) } returns successImage

        executor = spyk(GetVehicleVinNormalFcaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with missing action then throw missing parameter`() {
        val input = emptyMap<String, String>()
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
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(Constants.Input.ACTION to Constants.Input.Action.GET)
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
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
    fun `when execute params with right inputs then return an UserInput`() {
        val vin = "testVin"
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin
        )
        val userInput = executor.params(input)
        Assert.assertEquals(Action.Get, userInput.action)
        Assert.assertEquals(vin, userInput.vin)
    }

    @Test
    fun `when execute transformToVehicleOutput then return VehiclesOutput`() {
        val result = executor.transformToVehicleOutput(vehiclesResponse, "testVin")
        Assert.assertEquals(vehicleOutput, result)
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
    fun `when execute readFromJson in empty case then return null`() {
        var model = executor.readFromJson("  ")
        Assert.assertEquals(null, model)

        model = executor.readFromJson("{ test vehicle }")
        Assert.assertEquals(null, model)
    }

    @Test
    fun `when execute with action get and there is not a valid cache then make a network call`() {
        val vin = "testVin"
        every { executor.readFromCache() } returnsMany listOf(null, vehiclesResponse.toJson())
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(vin)) } returns NetworkResponse.Success(
            vehiclesResponse
        )
        coEvery { executor.fetchImages(any(), any()) } returns vehiclesResponse

        runTest {
            executor.execute(UserInput(Get, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(vin)) }
            coVerify(exactly = vehiclesResponse.vehicles.size) { executor.fetchImages(eq(vehiclesResponse), eq(vin)) }
        }
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        val vin = "testVin"
        every { executor.readFromCache() } returns vehiclesResponse.toJson()

        runTest {
            val response = executor.execute(UserInput(Get, vin))
            verify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = 0) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(vin)) }
            coVerify(exactly = 0) { executor.fetchImages(eq(vehiclesResponse), eq(vin)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehicleOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response`() {
        val vin = "testVin"
        every { executor.readFromCache() } returns vehiclesResponse.toJson()
        val successResponse = NetworkResponse.Success(vehiclesResponse)
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(vin)) } returns successResponse
        coEvery { executor.fetchImages(any(), any()) } returns vehiclesResponse

        runTest {
            val response = executor.execute(UserInput(Refresh, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(vin)) }
            coVerify(exactly = vehiclesResponse.vehicles.size) { executor.fetchImages(eq(vehiclesResponse), eq(vin)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehicleOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with failure response`() {
        val vin = "testVin"
        every { executor.readFromCache() } returns vehiclesResponse.toJson()
        val error = PimsErrors.serverError(null, "test-errors")
        val failureResponse = NetworkResponse.Failure(error)
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(vin)) } returns failureResponse

        runTest {
            val response = executor.execute(UserInput(Refresh, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(vin)) }
            coVerify(exactly = 0) { executor.fetchImages(eq(vehiclesResponse), eq(vin)) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }

    @Test
    fun `when execute transformType then return the right common type`() {
        runTest {
            val mockVehicleResponse = mockk<VehicleResponse>()

            every { mockVehicleResponse.fuelType } returns "e"
            var type = executor.transformType(mockVehicleResponse) // "e"
            Assert.assertEquals(VehicleOutput.Type.ELECTRIC, type)

            every { mockVehicleResponse.fuelType } returns "E"
            type = executor.transformType(mockVehicleResponse) // "E"
            Assert.assertEquals(VehicleOutput.Type.ELECTRIC, type)

            every { mockVehicleResponse.fuelType } returns "h"
            type = executor.transformType(mockVehicleResponse) // "h"
            Assert.assertEquals(VehicleOutput.Type.HYBRID, type)

            every { mockVehicleResponse.fuelType } returns "H"
            type = executor.transformType(mockVehicleResponse) // "H"
            Assert.assertEquals(VehicleOutput.Type.HYBRID, type)

            every { mockVehicleResponse.fuelType } returns "d"
            type = executor.transformType(mockVehicleResponse) // "d"
            Assert.assertEquals(VehicleOutput.Type.THERMIC, type)

            every { mockVehicleResponse.fuelType } returns "D"
            type = executor.transformType(mockVehicleResponse) // "D"
            Assert.assertEquals(VehicleOutput.Type.THERMIC, type)

            every { mockVehicleResponse.fuelType } returns "g"
            type = executor.transformType(mockVehicleResponse) // "g"
            Assert.assertEquals(VehicleOutput.Type.THERMIC, type)

            every { mockVehicleResponse.fuelType } returns "G"
            type = executor.transformType(mockVehicleResponse) // "G"
            Assert.assertEquals(VehicleOutput.Type.THERMIC, type)

            every { mockVehicleResponse.fuelType } returns "f"
            type = executor.transformType(mockVehicleResponse) // "f"
            Assert.assertEquals(VehicleOutput.Type.UNKNOWN, type)

            every { mockVehicleResponse.fuelType } returns "F"
            type = executor.transformType(mockVehicleResponse) // "F"
            Assert.assertEquals(VehicleOutput.Type.UNKNOWN, type)
        }
    }

    @Test
    fun `when execute fetchImage then use the GetVehicleImageFcaExecutor with failure`() {
        val failureAnswer = NetworkResponse.Failure(PimsErrors.apiNotSupported())
        coEvery { anyConstructed<GetVehicleImageFcaExecutor>().execute(any()) } returns failureAnswer

        runTest {
            val response = executor.fetchImages(vehiclesResponse, "testVin")
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
            val response = executor.fetchImages(vehiclesResponse, "testVin")
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            coVerify(exactly = 1) {
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
