package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleImageResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleDetailsFca
import com.inetpsa.pims.spaceMiddleware.model.vehicles.image.VehicleImageFcaInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetVehicleImageFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetVehicleImageFcaExecutor

    private val vehicle = VehicleImageResponse(
        vin = "testVin",
        imageUrl = "https://image.test.com",
        description = "testDescription",
        make = "testMake",
        subMake = "testSubMake",
        year = 2019,
        tcuType = "testTcuType",
        sdp = "testSdp",
        userid = "testUserId",
        tcCountryCode = "testTcCountryCode"
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetVehicleImageFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute then make a get API call`() {
        val params = VehicleImageFcaInput(vin = "testVin", width = 0, height = 0, imageFormat = "png")
        every { executor.params(any()) } returns params
        coEvery {
            communicationManager.get<VehicleImageResponse>(
                any(),
                any()
            )
        } returns NetworkResponse.Success(vehicle)
        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_VIN to params.vin,
            Constants.PARAM_WIDTH to params.width.toString(),
            Constants.PARAM_HEIGHT to params.height.toString(),
            Constants.QUERY_PARAM_KEY_IMAGE_FORMAT to params.imageFormat
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(VehicleImageResponse::class.java),
                    urls = eq(arrayOf("/v1/vehicles/details/")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<VehicleDetailsFca>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = (response as Success).response
            Assert.assertEquals(vehicle.vin, success.vin)
            Assert.assertEquals(vehicle.imageUrl, success.imageUrl)
        }
    }

    @Test
    fun `when execute params with the right vin then return vin`() {
        val vin = "testVin"
        val input = mapOf(Constants.PARAM_VIN to vin)
        val param = executor.params(input)
        val expected = VehicleImageFcaInput(vin = "testVin", width = 300, height = 300, imageFormat = "png")
        Assert.assertEquals(expected, param)
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
    fun `when execute params with invalid vin then throw missing parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.PARAM_VIN to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
