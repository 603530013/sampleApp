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
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Remove
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInputVehicleService
import com.inetpsa.pims.spaceMiddleware.model.vehicles.service.ServicesOutput
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

internal class GetVehicleServicesPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVehicleServicesPsaExecutor
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
        mileage = VehicleDetailsResponse.Mileage(
            value = 1,
            timestamp = today,
            source = 0
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
                offer = VehicleDetailsResponse.ServicesConnected.Offer(
                    pricingModel = "testPricingModel",
                    fromPrice = 0.1f,
                    price = VehicleDetailsResponse.ServicesConnected.Offer.Price(
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

    private val servicesOutput = ServicesOutput(
        services = listOf(
            ServicesOutput.Service(
                id = "testServiceConnectedId",
                title = "testServiceConnectedTitle",
                category = "testServiceConnectedCategory",
                description = "testServiceConnectedDescription",
                url = "testServiceConnectedUrlCvs",
                price = 10f,
                currency = "Euro",
                offer = ServicesOutput.Service.Offer(
                    pricingModel = "testPricingModel",
                    fromPrice = 0.1f,
                    price = ServicesOutput.Service.Offer.Price(
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

        executor = spyk(GetVehicleServicesPsaExecutor(baseCommand), recordPrivateCalls = true)
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
    fun `when execute params with missing schema then return an UserInput with null schema`() {
        val vin = "testVin"
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin
        )
        val userInput = executor.params(input)
        Assert.assertEquals(Action.Get, userInput.action)
        Assert.assertEquals(vin, userInput.vin)
        Assert.assertEquals(null, userInput.schema)
    }

    @Test
    fun `when execute params with invalid schema then throw invalid parameter`() {
        val vin = "testVin"
        val schema = 123
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin,
            Constants.Input.SCHEMA to schema
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.SCHEMA)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with valid schema then return an UserInput with schema`() {
        val vin = "testVin"
        val schema = "testSchema"
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin,
            Constants.Input.SCHEMA to schema
        )
        val userInput = executor.params(input)
        Assert.assertEquals(Action.Get, userInput.action)
        Assert.assertEquals(vin, userInput.vin)
        Assert.assertEquals(schema, userInput.schema)
    }

    @Test
    fun `when execute params with right inputs then return an UserInput`() {
        val vin = "testVin"
        val schema = "testSchema"
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin,
            Constants.Input.SCHEMA to schema
        )
        val userInput = executor.params(input)
        Assert.assertEquals(Action.Get, userInput.action)
        Assert.assertEquals(vin, userInput.vin)
        Assert.assertEquals(schema, userInput.schema)
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
    fun `when execute with action get and there is not a valid cache then make a network call`() {
        val vin = "testVin"
        val schema = "testSchema"

        every {
            executor.readVehicleFromCache(any())
        } returnsMany listOf(null, vehicleResponse)

        coEvery {
            anyConstructed<GetUserPsaExecutor>().execute(any())
        } returns NetworkResponse.Success(Unit)

        coEvery { executor.getToken() } returns "testToken"

        runTest {
            executor.execute(UserInputVehicleService(Action.Get, vin, schema))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 2) { executor.readVehicleFromCache(eq(vin)) }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }
            coVerify(exactly = 1) { executor.getToken() }
        }
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        val vin = "testVin"
        val schema = "testSchema"
        every { executor.readVehicleFromCache(vin) } returns vehicleResponse
        coEvery {
            anyConstructed<GetUserPsaExecutor>().execute(any())
        } returns NetworkResponse.Success(Unit)
        coEvery { executor.getToken() } returns "testToken"

        runTest {
            val response = executor.execute(UserInputVehicleService(Get, vin, schema))
            verify(exactly = 1) { executor.readVehicleFromCache(eq(vin)) }
            coVerify(exactly = 0) { anyConstructed<GetUserPsaExecutor>().execute(any()) }
            coVerify(exactly = 1) { executor.getToken() }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(servicesOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response`() {
        val vin = "testVin"
        val schema = "testSchema"
        every { executor.readVehicleFromCache(vin) } returns vehicleResponse
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)
        coEvery { executor.getToken() } returns "testToken"

        runTest {
            val response = executor.execute(UserInputVehicleService(Refresh, vin, schema))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readVehicleFromCache(eq(vin)) }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }
            coVerify(exactly = 1) { executor.getToken() }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(servicesOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with failure response`() {
        val vin = "testVin"
        val schema = "testSchema"
        every { executor.readVehicleFromCache(vin) } returns vehicleResponse
        val error = PimsErrors.serverError(null, "test-errors")
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Failure(error)
        coEvery { executor.getToken() } returns "testToken"

        runTest {
            val response = executor.execute(UserInputVehicleService(Refresh, vin, schema))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readVehicleFromCache(eq(vin)) }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }
            coVerify(exactly = 1) { executor.getToken() }

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
        val schema = "testSchema"
        every { executor.readVehicleFromCache(vin) } returns null
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)
        coEvery { executor.getToken() } returns "testToken"

        runTest {
            val response = executor.execute(UserInputVehicleService(Refresh, vin, schema))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readVehicleFromCache(eq(vin)) }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }
            coVerify(exactly = 1) { executor.getToken() }

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
    fun `when execute with action remove then throw invalid parameter`() {
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        coEvery { executor.getToken() } returns "testToken"
        runTest {
            try {
                executor.execute(UserInputVehicleService(Remove, "testVin", "testSchema"))
                coVerify(exactly = 1) { executor.getToken() }
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when call getToken get success then return token`() {
        val token = "testToken"
        coEvery {
            executor.getToken()
        } returns token

        runTest {
            val response = executor.getToken()
            coVerify(exactly = 1) { executor.getToken() }
            Assert.assertEquals(true, response is String)
            val success = (response as String)
            Assert.assertEquals(token, success)
        }
    }

    @Test
    fun `when call getToken get failure then return null`() {
        coEvery { executor.getToken() } returns null

        runTest {
            val response = executor.getToken()
            coVerify(exactly = 1) { executor.getToken() }
            Assert.assertEquals(true, response is String?)
            val failure = (response as String?)
            Assert.assertEquals(null, failure)
        }
    }
}
