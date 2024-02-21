package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Storage
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.user.GetUserPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.Mileage
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.ServicesConnected.Offer
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.ServicesConnected.Offer.Price
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

internal class GetVehicleDetailsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVehicleDetailsPsaExecutor
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    private val vehicleResponse = VehicleDetailsResponse(
        vin = "testVin",
        lcdv = "textLCDV",
        visual = "testVisual",
        shortName = "testShortName",
        // warrantyStartDate = today,
        attributes = listOf("testAttributes"),
        eligibility = listOf("testEligibility"),
        typeVehicle = VehicleDetailsResponse.VEHICLE_ELECTRIC,
        mileage = Mileage(
            value = 1,
            timestamp = today,
            source = 10
        ),
        reviewMaxDate = null,
        servicesConnected = listOf(
            VehicleDetailsResponse.ServicesConnected(
                id = "testServiceConnectedId",
                title = "testServiceConnectedTitle",
                category = "testServiceConnectedCategory",
                description = "testServiceConnectedDescription",
                url = "testServiceConnectedUrl",
                urlSso = "testServiceConnectedUrlSso",
                urlCvs = "testServiceConnectedUrlCvs",
                price = 10f,
                currency = "Euro",
                offer = Offer(
                    pricingModel = "testPricingModel",
                    fromPrice = 0.1f,
                    price = Price(
                        periodType = "testPeriodType",
                        price = 10f,
                        currency = "Euro",
                        typeDiscount = "testTypeDiscount"
                    ),
                    isFreeTrial = 1
                )
            )
        )
    )

    private val vehicleOutput = VehicleOutput(
        vin = "testVin",
        lcdv = "textLCDV",
        eligibility = listOf("testEligibility"),
        attributes = listOf("testAttributes"),
        type = VehicleOutput.Type.ELECTRIC,
        name = "testShortName",
        regTimeStamp = 0,
        year = null,
        lastUpdate = today,
        sdp = null,
        market = null,
        make = null,
        picture = "testVisual",
        enrollmentStatus = null
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetUserPsaExecutor::class)
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) }
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        userSessionManager.getUserSession()?.customerId

        executor = spyk(GetVehicleDetailsPsaExecutor(baseCommand), recordPrivateCalls = true)
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
        val result = executor.transformToVehicleOutput(vehicleResponse, today)
        Assert.assertEquals(vehicleOutput, result)
    }

    @Test
    fun `when execute readVehicleFromCache in available case then return cache`() {
        every { dataManager.read(any(), any()) } returns vehicleResponse.toJson()
        val cache = executor.readVehicleFromCache("testVin")
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_vehicle_testVin"), eq(APPLICATION)) }
        Assert.assertEquals(vehicleResponse, cache)
    }

    @Test
    fun `when execute readVehicleFromCache in empty case then return null`() {
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readVehicleFromCache("testVin")
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_vehicle_testVin"), eq(APPLICATION)) }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute readLastUpdateFromCache in available case then return cache`() {
        every { dataManager.read(any(), any()) } returns today
        val cache = executor.readLastUpdateFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_lastUpdate"), eq(APPLICATION)) }
        Assert.assertEquals(today, cache)
    }

    @Test
    fun `when execute readLastUpdateFromCache in empty case then return null`() {
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readLastUpdateFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_lastUpdate"), eq(APPLICATION)) }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute with action get and there is not a valid cache then make a network call`() {
        val vin = "testVin"
        every { executor.readVehicleFromCache(any()) } returnsMany listOf(null, vehicleResponse)
        every { executor.readLastUpdateFromCache() } returns null
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            executor.execute(UserInput(Action.Get, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 2) { executor.readVehicleFromCache(eq(vin)) }
            verify(exactly = 1) { executor.readLastUpdateFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }
        }
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        val vin = "testVin"
        every { executor.readVehicleFromCache(vin) } returns vehicleResponse
        every { executor.readLastUpdateFromCache() } returns today

        runTest {
            val response = executor.execute(UserInput(Action.Get, vin))
            verify(exactly = 1) { executor.readVehicleFromCache(eq(vin)) }
            verify(exactly = 1) { executor.readLastUpdateFromCache() }
            coVerify(exactly = 0) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehicleOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response`() {
        val vin = "testVin"
        every { executor.readVehicleFromCache(vin) } returns vehicleResponse
        every { executor.readLastUpdateFromCache() } returns today
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            val response = executor.execute(UserInput(Action.Refresh, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readVehicleFromCache(eq(vin)) }
            verify(exactly = 1) { executor.readLastUpdateFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehicleOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with failure response`() {
        val vin = "testVin"
        every { executor.readVehicleFromCache(vin) } returns vehicleResponse
        every { executor.readLastUpdateFromCache() } returns today
        val error = PimsErrors.serverError(null, "test-errors")
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute(UserInput(Action.Refresh, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readVehicleFromCache(eq(vin)) }
            verify(exactly = 0) { executor.readLastUpdateFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response but empty cache`() {
        val vin = "testVin"
        every { executor.readVehicleFromCache(vin) } returns null
        every { executor.readLastUpdateFromCache() } returns today
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            val response = executor.execute(UserInput(Action.Refresh, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readVehicleFromCache(eq(vin)) }
            verify(exactly = 0) { executor.readLastUpdateFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            val error = PIMSFoundationError.invalidReturnParam(Storage.VEHICLE)
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
            val mockVehicleResponse = mockk<VehicleDetailsResponse>()
            every { mockVehicleResponse.typeVehicle } returnsMany listOf(
                VehicleDetailsResponse.VEHICLE_THERMIC,
                VehicleDetailsResponse.VEHICLE_ELECTRIC,
                VehicleDetailsResponse.VEHICLE_HYBRID_A,
                VehicleDetailsResponse.VEHICLE_HYBRID_B,
                VehicleDetailsResponse.VEHICLE_HYBRID_C,
                6
            )

            var type = executor.transformType(mockVehicleResponse)
            Assert.assertEquals(VehicleOutput.Type.THERMIC, type)

            type = executor.transformType(mockVehicleResponse)
            Assert.assertEquals(VehicleOutput.Type.ELECTRIC, type)

            type = executor.transformType(mockVehicleResponse)
            Assert.assertEquals(VehicleOutput.Type.HYBRID, type)

            type = executor.transformType(mockVehicleResponse)
            Assert.assertEquals(VehicleOutput.Type.HYBRID, type)

            type = executor.transformType(mockVehicleResponse)
            Assert.assertEquals(VehicleOutput.Type.HYBRID, type)

            type = executor.transformType(mockVehicleResponse)
            Assert.assertEquals(VehicleOutput.Type.UNKNOWN, type)
        }
    }
}
