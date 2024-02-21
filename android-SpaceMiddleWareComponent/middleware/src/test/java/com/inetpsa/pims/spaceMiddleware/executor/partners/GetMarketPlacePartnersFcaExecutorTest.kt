package com.inetpsa.pims.spaceMiddleware.executor.partners

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.AppIds
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.Consents
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.CustomExtension
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.DeepLinksItem
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetMarketPlacePartnersFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetMarketPlacePartnersFcaExecutor

    private val vin = "testVin"
    private val marketPlacePartnerResponse = MarketPlacePartnerResponse(
        customExtension = CustomExtension(
            appIds = AppIds(
                android = "testAndroid",
                ios = "testIos"
            ),
            deepLinks = listOf(
                DeepLinksItem(
                    key = "OWNERS_MANUAL",
                    androidLink = "testAndroidLink",
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
        partnerStatus = "testPartnerStatus"
    )

    @Before
    override fun setup() {
        super.setup()
        every { dataManager.read(any(), any()) } returns marketPlacePartnerResponse.toJson()
        justRun { dataManager.create(any(), any(), any()) }
        executor = spyk(GetMarketPlacePartnersFcaExecutor(middlewareComponent))
    }

    @Test
    fun `writeToCache should call createSync with correct parameters`() {
        every {
            dataManager.create(
                key = any(),
                data = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }
        val response = listOf(marketPlacePartnerResponse)
        // val expectedData = response.toJson()
        runTest { executor.writeToCache(vin, response) }
        verify {
            dataManager.create(
                key = eq("ALFAROMEO_PREPROD_testCustomerId_mp_partners"),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }

    @Test
    fun `when execute readFromJson with valid json then return items`() {
        val marketOwnerManualResponse = listOf(marketPlacePartnerResponse)
        val json = marketOwnerManualResponse.toJson()
        val cache = executor.readFromJson(json)
        Assert.assertEquals(marketOwnerManualResponse.size, cache?.size)
        Assert.assertEquals(marketOwnerManualResponse, cache)
    }

    @Test
    fun `test readFromJson returns null when there is a exception`() {
        val result = executor.readFromJson("invalidJsonString")
        Assert.assertNull(result)
    }

    @Test
    fun `test execute() throws PIMSFoundationError when the ACTION is invalid`() {
        val error = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        val params = UserInput(
            vin = "testVin",
            action = Action.Delete
        )
        runBlocking {
            try {
                executor.execute(params)
            } catch (e: PIMSError) {
                Assert.assertEquals(error.message, e.message)
                Assert.assertEquals(error.code, e.code)
            }
        }
    }

    @Test
    fun `when execute readFromCache in available case then return cache`() {
        val marketOwnerManualResponse = listOf(marketPlacePartnerResponse)
        val json = marketOwnerManualResponse.toJson()
        every { dataManager.read(any(), any()) } returns json
        val cache = executor.readFromCache(vin)
        verify {
            dataManager.read(eq("ALFAROMEO_PREPROD_testCustomerId_testVin_mp_partners"), eq(APPLICATION))
        }
        Assert.assertEquals(json, cache)
    }

    @Test
    fun `when execute params with the right input then return UserInput`() {
        val action = "get"
        val vin = "testVin"
        val input = UserInput(
            vin = vin,
            action = Get
        )

        val params = mapOf(
            Constants.Input.VIN to "testVin",
            Constants.Input.ACTION to action
        )
        val paramsInput = executor.params(params)
        Assert.assertEquals(input, paramsInput)
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
        Assert.assertEquals(Get, userInput.action)
        Assert.assertEquals(vin, userInput.vin)
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        val manualOwnerManualResponse = listOf(marketPlacePartnerResponse)
        every { executor.readFromCache(vin) } returns manualOwnerManualResponse.toJson()

        runTest {
            val response = executor.execute(UserInput(Get, vin))
            verify(exactly = 1) { executor.readFromCache(vin) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(manualOwnerManualResponse, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response`() {
        val vin = "testVin"
        val input = UserInput(vin = vin, action = Refresh)
        every { executor.params(any()) } returns input
        val manualOwnerManualResponse = listOf(marketPlacePartnerResponse)
        every { executor.readFromCache(vin) } returns manualOwnerManualResponse.toJson()
        coJustRun { executor.writeToCache(vin, any()) }

        coEvery {
            communicationManager.get<List<MarketPlacePartnerResponse>>(any(), any())
        } returns Success(manualOwnerManualResponse)

        runTest {
            val response = executor.execute(input)
            verify {
                val type = object : TypeToken<List<MarketPlacePartnerResponse>>() {}.type
                executor.request(
                    type = eq(type),
                    urls = eq(
                        arrayOf(
                            "/v2/accounts/",
                            "testCustomerId",
                            "/vehicles/",
                            vin,
                            "/mp-partners"
                        )
                    )
                )
            }

            coVerify(exactly = 1) {
                communicationManager.get<List<MarketPlacePartnerResponse>>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }
            coVerify(exactly = 1) { executor.writeToCache(vin, manualOwnerManualResponse) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(manualOwnerManualResponse, success.response)
        }
    }
}
