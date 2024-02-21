package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleDetailsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AddVehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.add.VehicleInput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
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

internal class AddVehiclePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: AddVehiclePsaExecutor
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    private val vehicleInput = VehicleInput(
        vin = "testVin",
        mileage = 1
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

    private val newVehicleResponse = AddVehicleResponse(
        success = true
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetVehicleDetailsPsaExecutor::class)
        coEvery { anyConstructed<GetVehicleDetailsPsaExecutor>().execute(any()) } returns NetworkResponse.Success(
            vehicleOutput
        )

        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        executor = spyk(AddVehiclePsaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with the right input then return AddVehiclePsaParams`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.PARAM_MILEAGE to 1
        )

        val output = executor.params(input)
        Assert.assertEquals(vehicleInput, output)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        every { executor.params(any()) } returns vehicleInput

        coEvery {
            communicationManager.post<AddVehicleResponse>(
                any(),
                any()
            )
        } returns Success(newVehicleResponse)

        runTest {
            val response = executor.execute()
            val body = vehicleInput.toJson()

            verify {
                executor.request(
                    type = eq(AddVehicleResponse::class.java),
                    urls = eq(arrayOf("/me/v1/user/vehicles/add")),
                    headers = any(),
                    queries = any(),
                    body = eq(body)
                )
            }

            coVerify {
                communicationManager.post<AddVehicleResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            val userInput = UserInput(
                action = Refresh,
                vin = vehicleInput.vin
            )

            coVerify(exactly = 1) { anyConstructed<GetVehicleDetailsPsaExecutor>().execute(eq(userInput)) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(vehicleOutput, success.response)
        }
    }

    @Test
    fun `when execute then make a network API call with failure response`() {
        every { executor.params(any()) } returns vehicleInput
        every { executor.params(any()) } returns vehicleInput

        val error = PimsErrors.serverError(null, "testError")
        coEvery {
            communicationManager.post<AddVehicleResponse>(any(), any())
        } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute()
            val body = vehicleInput.toJson()

            verify {
                executor.request(
                    type = eq(AddVehicleResponse::class.java),
                    urls = eq(arrayOf("/me/v1/user/vehicles/add")),
                    headers = any(),
                    queries = any(),
                    body = eq(body)
                )
            }

            val userInput = UserInput(
                action = Refresh,
                vin = vehicleInput.vin
            )

            coVerify(exactly = 0) { anyConstructed<GetVehicleDetailsPsaExecutor>().execute(eq(userInput)) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(error, failure)
        }
    }
}
