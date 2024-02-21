package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Add
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.VehicleVinOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.VinType
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleImageFcaInput
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

internal class GetVehicleDetailsFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetVehicleDetailsFcaExecutor
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
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

    private val vehicleVinOutput = VehicleVinOutput(
        vin = "testVin",
        description = "testDescription",
        picture = "testImageUrl",
        make = "testMake",
        subMake = "testSubMake",
        year = 1986,
        sdp = "testSdp",
        tcuType = "tcuType",
        userid = "userId",
        tcCountryCode = "testCountryCode"

    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetVehicleDetailsFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute params with the right type then return type`() {
        val vinType = VinType.LastCharacters.name
        val input = mapOf(Constants.Input.TYPE to vinType)
        val param = executor.params(input)
        Assert.assertEquals(VinType.LastCharacters, param)
    }

    @Test
    fun `when execute params with missing type then return default type`() {
        val input = emptyMap<String, String>()
        val param = executor.params(input)
        Assert.assertEquals(VinType.Normal, param)
    }

    @Test
    fun `when execute params with invalid type then return default type`() {
        val paramsId = 123
        val input = mapOf(Constants.Input.TYPE to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.TYPE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with type normal then invoke executeNormal function`() {
        every { executor.params(any()) } returns VinType.Normal
        coEvery { executor.executeNormal() } returns NetworkResponse.Success(vehicleOutput)

        runTest {
            val response = executor.execute()
            coVerify(exactly = 1) { executor.executeNormal() }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(vehicleOutput, success)
        }
    }

    @Test
    fun `when execute with type LastCharacters then invoke executeLastCharacters function`() {
        every { executor.params(any()) } returns VinType.LastCharacters
        coEvery { executor.executeLastCharacters() } returns NetworkResponse.Success(vehicleVinOutput)

        runTest {
            val response = executor.execute()
            coVerify(exactly = 1) { executor.executeLastCharacters() }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(vehicleVinOutput, success)
        }
    }

    @Test
    fun `when execute with type Encrypted then invoke executeEncrypted function`() {
        every { executor.params(any()) } returns VinType.Encrypted
        coEvery { executor.executeEncrypted() } returns NetworkResponse.Success(vehicleVinOutput)

        runTest {
            val response = executor.execute()
            coVerify(exactly = 1) { executor.executeEncrypted() }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(vehicleVinOutput, success)
        }
    }

    @Test
    fun `when execute executeEncrypted then invoke the right executor`() {
        val input = VehicleImageFcaInput(
            vin = "00000005",
            width = 0,
            height = 0,
            imageFormat = "png"
        )
        mockkConstructor(GetVehicleVinEncryptedFcaExecutor::class)
        every { anyConstructed<GetVehicleVinEncryptedFcaExecutor>().params(any()) } returns input
        coEvery { anyConstructed<GetVehicleVinEncryptedFcaExecutor>().execute(any()) } returns
            NetworkResponse.Success(vehicleVinOutput)

        runTest {
            val response = executor.executeEncrypted()
            coVerify(exactly = 1) { anyConstructed<GetVehicleVinEncryptedFcaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(vehicleVinOutput, success)
        }
    }

    @Test
    fun `when execute executeLastCharacters then invoke the right executor`() {
        val input = VehicleImageFcaInput(
            vin = "00000005",
            width = 0,
            height = 0,
            imageFormat = "png"
        )
        mockkConstructor(GetVehicleVinLastCharactersFcaExecutor::class)
        every { anyConstructed<GetVehicleVinLastCharactersFcaExecutor>().params(any()) } returns input
        coEvery { anyConstructed<GetVehicleVinLastCharactersFcaExecutor>().execute(any()) } returns
            NetworkResponse.Success(vehicleVinOutput)

        runTest {
            val response = executor.executeLastCharacters()
            coVerify(exactly = 1) { anyConstructed<GetVehicleVinLastCharactersFcaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(vehicleVinOutput, success)
        }
    }

    @Test
    fun `when execute executeNormal then invoke the right executor`() {
        val input = UserInput(Add, "testVin")
        val vehicleOutput = VehicleOutput(
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
            picture = "testImageUrl"

        )
        mockkConstructor(GetVehicleVinNormalFcaExecutor::class)
        every { anyConstructed<GetVehicleVinNormalFcaExecutor>().params(any()) } returns input
        coEvery { anyConstructed<GetVehicleVinNormalFcaExecutor>().execute(any()) } returns
            NetworkResponse.Success(vehicleOutput)

        runTest {
            val response = executor.executeNormal()
            coVerify(exactly = 1) { anyConstructed<GetVehicleVinNormalFcaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(vehicleOutput, success)
        }
    }
}
