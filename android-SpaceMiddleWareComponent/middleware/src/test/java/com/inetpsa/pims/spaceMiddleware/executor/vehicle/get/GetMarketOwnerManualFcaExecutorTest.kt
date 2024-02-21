package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.partners.GetMarketPlacePartnersFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.AppIds
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.Consents
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.CustomExtension
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.DeepLinksItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class GetMarketOwnerManualFcaExecutorTest : FcaExecutorTestHelper() {

    private val link = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx?vin=testVin&lang=fr&defLang=en&source=APP"
    private lateinit var executor: GetMarketOwnerManualFcaExecutor

    /* private val marketOwnerManualResponse = listOf(
         MarketOwnerManualResponse(
             status = "ACTIVE",
             customExtension = CustomExtension(
                 deepLinks = listOf(
                     DeepLinksItem(
                         androidLink = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx",
                         key = "OWNERS_MANUAL"
                     )
                 )
             )
         )
     )*/

    private val marketOwnerManualResponse = listOf(
        MarketPlacePartnerResponse(
            customExtension = CustomExtension(
                appIds = AppIds(
                    android = "testAndroid",
                    ios = "testIos"
                ),
                deepLinks = listOf(
                    DeepLinksItem(
                        key = "OWNERS_MANUAL",
                        androidLink = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx",
                        iosLink = "testIosLink"
                    )
                )
            ),
            allowedBrands = listOf("00"),
            partnerBackgroundColor = "testPartnerBackgroundColor",
            partnerName = "testPartnerName",
            section = "testSection",
            allowedFuelTypes = listOf("testAllowedFuelTypes"),
            isConsentApplicable = true,
            consents = Consents(
                consent4 = true
            ),
            partnerID = 1,
            serviceID = "testServiceId",
            partnerImage = "testPartnerImage",
            partnerThumbnail = "testPartnerThumbnail",
            contextHelp = "testContextHelp",
            allowedMvs = listOf("testAllowedMvs"),
            partnerStackedLogo = "testPartnerStackedLogo",
            partnerStatus = "active"
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetMarketPlacePartnersFcaExecutor::class)
        executor = spyk(GetMarketOwnerManualFcaExecutor(middlewareComponent), recordPrivateCalls = true)
        every { executor.generateLink(any(), any(), any()) } returns link
    }

    @Test
    fun `when execute then make a get API call`() {
        val inputVin = "testVin"
        every { executor.params(any()) } returns inputVin
        coEvery { anyConstructed<GetMarketPlacePartnersFcaExecutor>().execute(any()) } returns Success(
            marketOwnerManualResponse
        )

        runTest {
            val response = executor.execute()
            coVerify { anyConstructed<GetMarketPlacePartnersFcaExecutor>().execute(any()) }
            assertEquals(true, response is Success)
            val success = response as Success
            assertEquals(link, success.response)
        }
    }

    @Test
    fun `when execute getMarketOwnerManualData then return url link`() {
        val result = executor.getMarketOwnerManualData(marketOwnerManualResponse, "testVin")
        assertEquals(link, result)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf<String, Any?>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
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
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with the right input then return Param Vin`() {
        val vin = "testVin"
        val input = mapOf(Constants.PARAM_VIN to vin)
        val output = executor.params(input)
        assertEquals(vin, output)
    }

    @Test
    fun `when execute generateLink then return url`() {
        val androidLink = "https://lab-elearneditor.fiat.com/eLumWeb/Login.aspx"
        val result = executor.generateLink(androidLink, "testVin", "fr")
        assertEquals(link, result)
    }
}
