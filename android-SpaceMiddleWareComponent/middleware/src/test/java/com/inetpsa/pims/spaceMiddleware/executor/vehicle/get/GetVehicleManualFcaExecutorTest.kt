package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.mmx.foundation.tools.Market.LATAM
import com.inetpsa.mmx.foundation.tools.Market.NAFTA
import com.inetpsa.mmx.foundation.tools.Market.NONE
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.manual.OwnerManualOutput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetVehicleManualFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetVehicleManualFcaExecutor

    private val vehicleResponse: VehicleResponse = mockk()
    private val ownerManualWebData = OwnerManualOutput(
        type = "web",
        url = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx?vin=testVin&lang=fr&defLang=en&source=APP"
    )

    private val ownerManualPdfData = OwnerManualOutput(
        type = "pdf",
        url = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx?dummyPdf=Pdf"
    )

    private val link = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx?vin=testVin&lang=fr&defLang=en&source=APP"
    private val pdfLink = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx?dummyPdf=Pdf"

    override fun setup() {
        super.setup()
        every { vehicleResponse.market } returns ""
        every { vehicleResponse.subMake } returns ""
        mockkObject(CachedVehicles)
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse
        mockkConstructor(GetCountryOwnerManualFcaExecutor::class)
        coEvery { anyConstructed<GetCountryOwnerManualFcaExecutor>().execute(any()) } returns Success(pdfLink)
        mockkConstructor(GetMarketOwnerManualFcaExecutor::class)
        coEvery { anyConstructed<GetMarketOwnerManualFcaExecutor>().execute(any()) } returns Success(link)
        executor = spyk(GetVehicleManualFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with right input then return a Vin`() {
        val params = "testVin"
        val input = mapOf(Constants.PARAM_VIN to params)
        val output = executor.params(input)
        Assert.assertEquals(params, output)
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
        val input = mapOf(Constants.PARAM_VIN to vin)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when look for FCA executor with Maserati Brand then throw Api not supported exception`() {
        runTest {
            every { vehicleResponse.market } returns "US"
            every { vehicleResponse.subMake } returns "maserati"
            every { configurationManager.brand } returns Brand.MASERATI
            val exception = PimsErrors.apiNotSupported()

            try {
                executor.execute("FCAHBTTG2P9003558")
            } catch (ex: PIMSError) {
                assertEquals(exception.code, ex.code)
                assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when look for FCA executor with invalid market then throw PimsError exception`() {
        runTest {
            every { configurationManager.market } returns NONE
            val exception = PIMSFoundationError.invalidParameter(Constants.CONTEXT_KEY_MARKET)

            try {
                executor.execute("FCAHBTTG2P9003558")
            } catch (ex: PIMSError) {
                assertEquals(exception.code, ex.code)
                assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when market is EMEA execute then return a WebOwnerManual`() {
        val inputVin = "testVin"
        every { executor.params(any()) } returns inputVin
        every { configurationManager.market } returns EMEA
        runTest {
            val paramsVin = "testVin"
            val response = executor.execute(paramsVin)

            coVerify(exactly = 1) { anyConstructed<GetMarketOwnerManualFcaExecutor>().execute(eq(paramsVin)) }

            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(ownerManualWebData, success.response)
        }
    }

    @Test
    fun `when market is LATAM execute then return a WebOwnerManual`() {
        val inputVin = "testVin"
        every { vehicleResponse.market } returns "FR"
        every { vehicleResponse.subMake } returns "fiat"
        every { executor.params(any()) } returns inputVin
        every { configurationManager.market } returns LATAM
        runTest {
            val paramsVin = "testVin"
            val response = executor.execute(paramsVin)

            coVerify(exactly = 1) { anyConstructed<GetMarketOwnerManualFcaExecutor>().execute(eq(paramsVin)) }

            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(ownerManualWebData, success.response)
        }
    }

    @Test
    fun `when market is NAFTA execute then return a WebOwnerManual`() {
        val inputVin = "testVin"
        every { executor.params(any()) } returns inputVin
        every { configurationManager.market } returns NAFTA
        runTest {
            val paramsVin = "testVin"
            val response = executor.execute(paramsVin)

            coVerify(exactly = 1) { anyConstructed<GetMarketOwnerManualFcaExecutor>().execute(eq(paramsVin)) }

            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(ownerManualWebData, success.response)
        }
    }

    @Test
    fun `when market is NAFTA and US vehicle market execute then return a PdfOwnerManual`() {
        val inputVin = "testVin"
        every { executor.params(any()) } returns inputVin
        every { configurationManager.market } returns NAFTA
        every { vehicleResponse.market } returns "US"
        runTest {
            val paramsVin = "testVin"
            val response = executor.execute(paramsVin)
            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(ownerManualPdfData, success.response)
        }
    }
}
