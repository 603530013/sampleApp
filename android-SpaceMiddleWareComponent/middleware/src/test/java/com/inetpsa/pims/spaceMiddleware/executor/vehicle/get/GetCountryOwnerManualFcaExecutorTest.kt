package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.OwnerManualPdfResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class GetCountryOwnerManualFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetCountryOwnerManualFcaExecutor

    private val vehiclesResponse = VehicleResponse(
        activationSource = null,
        brandCode = null,
        channelFeatures = null,
        color = null,
        company = null,
        customerRegStatus = null,
        enrollmentStatus = null,
        fuelType = null,
        isCompanyCar = null,
        language = null,
        make = null,
        market = null,
        model = null,
        modelDescription = null,
        navEnabledHU = null,
        nickname = null,
        pp = null,
        privacyMode = null,
        radio = null,
        regStatus = null,
        regTimestamp = null,
        sdp = null,
        services = null,
        soldRegion = null,
        subMake = null,
        svla = null,
        tc = null,
        tcuType = null,
        tsoBodyCode = null,
        tsoModelYear = null,
        vin = "testVin",
        year = null,
        imageUrl = null
    )

    val vehiclesResponses = VehicleResponse(
        activationSource = null,
        brandCode = null,
        channelFeatures = null,
        color = null,
        company = null,
        customerRegStatus = null,
        enrollmentStatus = null,
        fuelType = null,
        isCompanyCar = null,
        language = null,
        make = null,
        market = null,
        model = "testmodel",
        modelDescription = null,
        navEnabledHU = null,
        nickname = null,
        pp = null,
        privacyMode = null,
        radio = null,
        regStatus = null,
        regTimestamp = null,
        sdp = null,
        services = null,
        soldRegion = null,
        subMake = "testbrand",
        svla = null,
        tc = null,
        tcuType = null,
        tsoBodyCode = null,
        tsoModelYear = null,
        vin = "testVin",
        year = 2023,
        imageUrl = null
    )

    private val omManualResponse = OwnerManualPdfResponse(
        pdf = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx?dummyPdf=Pdf"
    )

    override fun setup() {
        super.setup()
        executor = spyk(GetCountryOwnerManualFcaExecutor(middlewareComponent))
    }

    @Test
    fun `when execute params with the right input then return Param Vin`() {
        val vin = "testVin"
        val input = mapOf(Constants.PARAM_VIN to vin)
        val output = executor.params(input)
        Assert.assertEquals(vehiclesResponse, output)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf<String, Any?>()
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
        val vin = 123
        val input = mapOf(
            Constants.PARAM_VIN to vin
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute then make a get API call that return a non nullable link`() {
        every { executor.params(any()) } returns vehiclesResponses
        coEvery {
            communicationManager.get<OwnerManualPdfResponse>(any(), any())
        } returns Success(omManualResponse)

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_BRAND to "testbrand",
            Constants.QUERY_PARAM_KEY_MODEL to "testmodel",
            Constants.QUERY_PARAM_KEY_YEAR to "2023"
        )

        runTest {
            val response = executor.execute()
            verify {
                executor.request(
                    type = eq(OwnerManualPdfResponse::class.java),
                    urls = eq(arrayOf("/v1/digitalglovebox/ownermanual/manual")),
                    queries = queries
                )
            }
            coVerify {
                communicationManager.get<OwnerManualPdfResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(omManualResponse.pdf, success.response)
        }
    }

    @Test
    fun `when execute then make a get API call that return a nullable link`() {
        every { executor.params(any()) } returns vehiclesResponses
        coEvery {
            communicationManager.get<OwnerManualPdfResponse>(any(), any())
        } returns Success(OwnerManualPdfResponse())

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_BRAND to "testbrand",
            Constants.QUERY_PARAM_KEY_MODEL to "testmodel",
            Constants.QUERY_PARAM_KEY_YEAR to "2023"
        )

        runTest {
            val response = executor.execute()
            verify {
                executor.request(
                    type = eq(OwnerManualPdfResponse::class.java),
                    urls = eq(arrayOf("/v1/digitalglovebox/ownermanual/manual")),
                    queries = queries
                )
            }
            coVerify {
                communicationManager.get<OwnerManualPdfResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertNull(success.response)
        }
    }
}
