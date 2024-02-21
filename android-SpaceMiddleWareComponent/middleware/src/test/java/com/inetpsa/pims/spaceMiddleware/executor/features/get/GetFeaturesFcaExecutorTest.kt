package com.inetpsa.pims.spaceMiddleware.executor.features.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.features.mapper.FeaturesFcaOutputMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config.Engine.ICE
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config.Protocol.NETWORK
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config.Type.INTERNAL
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Status
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetFeaturesFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetFeaturesFcaExecutor

    private val vehicleResponse: VehicleResponse = mockk()

    private val featuresOutput = FeaturesOutput(
        features = listOf(
            Feature(
                code = "code",
                value = "value",
                config = Config(
                    schedule = 1,
                    shared = true,
                    repeat = true,
                    daysType = listOf(Config.DaysType.WORK_DAYS),
                    version = 1F,
                    engine = ICE,
                    type = INTERNAL,
                    protocol = NETWORK
                ),
                status = Status.CAPABLE
            )
        )
    )

    override fun setup() {
        super.setup()
        mockkObject(CachedVehicles)
        mockkConstructor(FeaturesFcaOutputMapper::class)
        every { vehicleResponse.fuelType } returns "H"
        every { anyConstructed<FeaturesFcaOutputMapper>().vehicle } returns vehicleResponse
        every { anyConstructed<FeaturesFcaOutputMapper>().transformFeatureOutput() } returns featuresOutput
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse

        executor = spyk(GetFeaturesFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with missing action then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with remove action then throw invalid parameter`() {
        val input = mapOf(Constants.Input.ACTION to "Remove", Constants.Input.VIN to "vin")
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with action Get then return UserInput`() {
        val vin = "testVin"
        val input = mapOf(Constants.Input.ACTION to "Get", Constants.Input.VIN to vin)
        val expectedInput = executor.params(input)
        assertEquals(expectedInput, UserInput(vin = vin, action = Get))
    }

    @Test
    fun `when execute with action Refresh then return UserInput`() {
        val vin = "testVin"
        val input = mapOf(Constants.Input.ACTION to "Refresh", Constants.Input.VIN to vin)
        val expectedInput = executor.params(input)
        assertEquals(expectedInput, UserInput(vin = vin, action = Refresh))
    }

    @Test
    fun `when execute with action get and vin then return FeaturesOutput`() {
        val vin = "testVin"
        val input = UserInput(vin = vin, action = Get)
        every { executor.params(any()) } returns input

        runTest {
            val response = executor.execute(input)
            verify(exactly = 1) { anyConstructed<FeaturesFcaOutputMapper>().transformFeatureOutput() }

            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(featuresOutput, success.response)
        }
    }
}
