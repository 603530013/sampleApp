package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehicleVinNormalFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.enrollment.AddVehicleConnectedFcaInput.LegalContent
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.add.VehicleInput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class AddVehicleFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: AddVehicleFcaExecutor
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    private val vehicleInput = VehicleInput(vin = "testVin")

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
        enrollmentStatus = "testEnrollmentStatus"
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(AddVehicleNoConnectedFcaExecutor::class)
        mockkConstructor(AddVehicleConnectedFcaExecutor::class)
        mockkConstructor(GetVehicleVinNormalFcaExecutor::class)
        coEvery { anyConstructed<AddVehicleNoConnectedFcaExecutor>().execute(any()) } returns
            NetworkResponse.Success(Unit)
        coEvery { anyConstructed<AddVehicleConnectedFcaExecutor>().execute(any()) } returns
            NetworkResponse.Success(Unit)
        coEvery { anyConstructed<GetVehicleVinNormalFcaExecutor>().execute(any()) } returns
            NetworkResponse.Success(vehicleOutput)

        every { baseCommand.parameters } returns mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.PARAM_CONNECTED to false,
            Constants.PARAM_PLATE_NUMBER to "testPlateNumber",
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to "IT",
                Constants.Input.PARAM_STATUS to LegalContent.Status.AGREE.name,
                Constants.Input.PARAM_VERSION to "1.0.0"
            ),
            Constants.Input.PARAM_TC_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to "IT",
                Constants.Input.PARAM_STATUS to LegalContent.Status.AGREE.name,
                Constants.Input.PARAM_VERSION to "1.0.0"
            ),
            Constants.Input.PARAM_PP_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to "IT",
                Constants.Input.PARAM_STATUS to LegalContent.Status.AGREE.name,
                Constants.Input.PARAM_VERSION to "1.0.0"
            ),
            Constants.Input.PARAM_CONTACTS to listOf(
                mapOf(
                    Constants.Input.PARAM_NAME to "testName",
                    Constants.Input.PARAM_PHONE to "testPhone"
                )
            )
        )
        executor = spyk(AddVehicleFcaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with the right input then return AddVehiclePsaParams`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.PARAM_CONNECTED to false
        )

        val output = executor.params(input)
        Assert.assertEquals(vehicleInput, output)
    }

    @Test
    fun `when execute params with missing connected input then return AddVehiclePsaParams`() {
        val input = mapOf(Constants.BODY_PARAM_VIN to "testVin")

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
    fun `when execute with connected then make a network API call with success response`() {
        val vehicleInput = VehicleInput("testVin", connected = true)
        every { executor.params(any()) } returns vehicleInput

        runTest {
            val response = executor.execute()

            val userInput = UserInput(
                action = Refresh,
                vin = vehicleInput.vin
            )
            coVerify(exactly = 1) { anyConstructed<AddVehicleConnectedFcaExecutor>().execute(any()) }
            coVerify(exactly = 1) { anyConstructed<GetVehicleVinNormalFcaExecutor>().execute(eq(userInput)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehicleOutput, success.response)
        }
    }

    @Test
    fun `when execute with no connected then make a network API call with success response`() {
        every { executor.params(any()) } returns vehicleInput

        runTest {
            val response = executor.execute()

            val userInput = UserInput(
                action = Refresh,
                vin = vehicleInput.vin
            )
            coVerify(exactly = 1) { anyConstructed<AddVehicleNoConnectedFcaExecutor>().execute(eq(vehicleInput.vin)) }
            coVerify(exactly = 1) { anyConstructed<GetVehicleVinNormalFcaExecutor>().execute(eq(userInput)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehicleOutput, success.response)
        }
    }

    @Test
    fun `when execute then make a network API call with addVehicle failure response`() {
        every { executor.params(any()) } returns vehicleInput
        val error = PimsErrors.serverError(fallback = "Server error", errors = null)
        coEvery {
            anyConstructed<AddVehicleNoConnectedFcaExecutor>().execute(any())
        } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute()

            val userInput = UserInput(
                action = Refresh,
                vin = vehicleInput.vin
            )
            coVerify(exactly = 1) { anyConstructed<AddVehicleNoConnectedFcaExecutor>().execute(eq(vehicleInput.vin)) }
            coVerify(exactly = 0) { anyConstructed<GetVehicleVinNormalFcaExecutor>().execute(eq(userInput)) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }

    @Test
    fun `when execute then make a network API call with GetVehicleDetails failure response`() {
        every { executor.params(any()) } returns vehicleInput
        val error = PimsErrors.serverError(fallback = "Server error", errors = null)
        coEvery {
            anyConstructed<GetVehicleVinNormalFcaExecutor>().execute(any())
        } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute()

            val userInput = UserInput(
                action = Refresh,
                vin = vehicleInput.vin
            )
            coVerify(exactly = 1) { anyConstructed<AddVehicleNoConnectedFcaExecutor>().execute(eq(vehicleInput.vin)) }
            coVerify(exactly = 1) { anyConstructed<GetVehicleVinNormalFcaExecutor>().execute(eq(userInput)) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }
}
