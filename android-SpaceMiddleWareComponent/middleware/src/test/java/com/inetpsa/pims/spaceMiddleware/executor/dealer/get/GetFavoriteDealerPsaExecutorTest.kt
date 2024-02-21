package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.DealerPSAMapper
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.OpeningHour
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.Service
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.PreferredDealerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Address
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.BusinessItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Coordinates
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Note.Vn
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.OpeningHours
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetFavoriteDealerPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetFavoriteDealerPsaExecutor

    //region DATA
    private val dealerResponse = DetailsResponse(
        siteGeo = "test_siteGeo",
        rrdi = "test_rrdi",
        name = "test_name",
        phones = mapOf(
            "PhoneNumber" to "test_phone_number",
            "PhoneAPV" to "test_phone_APV",
            "PhonePR" to "test_phone_pr",
            "PhoneVN" to "test_phone_vn",
            "PhoneVO" to "test_phone_vo"
        ),
        emails = mapOf(
            "Email" to "test_email",
            "EmailAPV" to "test_email_apv",
            "EmailAgent" to "test_email_agent",
            "EmailGER" to "test_email_ger",
            "EmailGRC" to "test_email_grc",
            "EmailPR" to "test_email_pr",
            "EmailSales" to "test_email_sales",
            "EmailVO" to "test_email_vo"

        ),
        address = Address(
            address1 = "test",
            department = "test",
            city = "test",
            region = "test",
            zipCode = "test",
            country = "test"
        ),
        coordinates = Coordinates(latitude = 10.0, longitude = 20.0),
        distance = 123.0,
        isAgent = true,
        isAgentAp = true,
        isSecondary = true,
        isSuccursale = true,
        business = listOf(
            BusinessItem(code = "testCode1", label = "testLabel1", type = "testType1"),
            BusinessItem(code = "testCode2", label = "testLabel2", type = "testType2")
        ),
        openHours = listOf(
            OpeningHours(
                label = "Lundi:08:00-12:00 14:00-18:00<br/> Mardi:08:00-12:00 14:00-18:00<br /> " +
                    "Mercredi:08:00-12:00 14:00-18:00<br /> Jeudi:08:00-12:00 14:00-18:00<br /> " +
                    "Vendredi:08:00-12:00 14:00-17:00<br /> Samedi: Ferm\u00e9<br /> Dimanche: Ferm\u00e9<br />",
                type = "APV"
            ),
            OpeningHours(
                label = "Lundi:08:00-12:00 14:00-19:00<br /> Mardi:08:00-12:00 14:00-19:00<br /> " +
                    "Mercredi:08:00-12:00 14:00-19:00<br /> Jeudi:08:00-12:00 14:00-19:00<br /> " +
                    "Vendredi:08:00-12:00 14:00-19:00<br /> Samedi:09:00-12:00 14:00-18:00<br /> " +
                    "Dimanche: Ferm\u00e9<br />",
                type = "VN"
            )
        ),
        principal = mapOf(
            "isPrincipalAG" to true,
            "isPrincipalPR" to true,
            "isPrincipalRA" to true,
            "isPrincipalVN" to true,
            "isPrincipalVO" to true
        ),
        urlPages = mapOf(
            "urlAPVForm" to "test",
            "urlContact" to "test",
            "urlNewCarStock" to "test",
            "urlUsedCarStock" to "test",
            "urlUsefullInformation" to "test"
        ),
        websites = DetailsResponse.WebSites(private = "test", public = "test"),
        culture = "fr_FR",
        data = DetailsResponse.Data(
            benefitList = listOf(),
            codesActors = mapOf(
                "codeActorAddressPR" to "test",
                "codeActorAddressRA" to "test",
                "codeActorAddressVN" to "test",
                "codeActorAddressVO" to "test",
                "codeActorCCAG" to "test",
                "codeActorCCPR" to "test",
                "codeActorCCRA" to "test",
                "codeActorCCVN" to "test",
                "codeActorCCVO" to "test",
                "codeActorSearch" to "test"
            ),
            codesRegions = mapOf(
                "codeRegionAG" to "test",
                "codeRegionPR" to "test",
                "codeRegionRA" to "test",
                "codeRegionVN" to "test",
                "codeRegionVO" to "test"
            ),
            faxNumber = "test",
            group = DetailsResponse.Data.Group(
                groupId = "test",
                subGroupId = "test",
                subGroupLabel = "test",
                isLeader = false
            ),
            indicator = DetailsResponse.Data.Indicator(code = "test", label = "test"),
            welcomeMessage = "test",
            importer = DetailsResponse.Data.Importer(
                importerCode = "test",
                corporateName = "test",
                importerName = "test",
                address = "test",
                city = "test",
                managementCountry = "test",
                country = "test",
                subsidiary = "test",
                subsidiaryName = "test"
            ),
            pdvImporter = DetailsResponse.Data.PDVImporter(
                pDVCode = "test",
                pDVName = "test",
                pDVContact = "test"
            ),
            numSiret = "test",
            legalStatus = "test",
            capital = "test",
            commercialRegister = "test",
            intracommunityTVA = "test",
            brand = "test",
            parentSiteGeo = "test",
            raisonSocial = "test",
            gmCodeList = listOf(
                DetailsResponse.Data.GmCodeItem(
                    activity = "test",
                    bacCode = "test",
                    cvcadcCode = "test",
                    pccadcCode = "test"
                )
            ),
            lienVoList = listOf("test"),
            bqCaptive = "test",
            caracRdvi = "test",
            ftcCodeList = listOf("test"),
            countryId = "Mexico",
            personList = listOf(
                DetailsResponse.Person(
                    email = "testEmail",
                    firstName = "testFirstName",
                    functionCode = "testFunctionCode",
                    functionLabel = "testFunctionLabel",
                    id = "impetus",
                    lastName = "testLastName",
                    mobile = "testMobile",
                    order = 2,
                    phone = "testPhone",
                    serviceCode = "testServiceCode",
                    titleCode = "testTitleCode",
                    titleLabel = "testTitleLabel"
                )
            ),
            rCSNumber = "mel"
        ),
        isCaracRdvi = true,
        typeOperateur = "test",
        urlPrdvErcs = "test",
        jockey = true,
        o2ov = true,
        services = listOf(DetailsResponse.ServiceItem(code = "test", label = "test", openingHours = "test")),
        fax = "morbi",
        note = DetailsResponse.Note(
            apv = DetailsResponse.Note.Apv(note = 0.1, total = 4570),
            urlApv = "testUrlApv",
            urlHome = "testUrlHome",
            urlVn = "testUrlVn",
            vn = Vn(note = 2.3, total = 2347)
        ),
        o2c = false
    )

    private val dealerOutput = DealerOutput(
        id = "test_siteGeo",
        name = "test_name",
        address = "test TEST Test",
        latitude = "10.0",
        longitude = "20.0",
        phones = mapOf(
            "APV" to "test_phone_APV",
            "PR" to "test_phone_pr",
            "VN" to "test_phone_vn",
            "VO" to "test_phone_vo",
            Constants.DEFAULT to "test_phone_number"
        ),
        bookingId = null,
        bookingLocation = null,
        preferred = true,
        emails = mapOf(
            "APV" to "test_email_apv",
            "Agent" to "test_email_agent",
            "GER" to "test_email_ger",
            "GRC" to "test_email_grc",
            "PR" to "test_email_pr",
            "Sales" to "test_email_sales",
            "VO" to "test_email_vo",
            Constants.DEFAULT to "test_email"
        ),
        website = "test",
        bookable = true,
        openingHours = mapOf(
            "APV" to mapOf(
                "MONDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-18:00")
                ),
                "TUESDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-18:00")
                ),
                "WEDNESDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-18:00")
                ),
                "THURSDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-18:00")
                ),
                "FRIDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-17:00")
                ),
                "SATURDAY" to OpeningHour(
                    closed = true,
                    time = null
                ),
                "SUNDAY" to OpeningHour(
                    closed = true,
                    time = null
                )
            ),
            "VN" to mapOf(
                "MONDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-18:00")
                ),
                "TUESDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-18:00")
                ),
                "WEDNESDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-18:00")
                ),
                "THURSDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-18:00")
                ),
                "FRIDAY" to OpeningHour(
                    closed = false,
                    time = listOf("08:00-12:00", "14:00-17:00")
                ),
                "SATURDAY" to OpeningHour(
                    closed = true,
                    time = null
                ),
                "SUNDAY" to OpeningHour(
                    closed = true,
                    time = null
                )
            )
        ),
        services = listOf(
            Service(code = "testCode1", type = "testType1", label = "testLabel1"),
            Service(code = "testCode2", type = "testType2", label = "testLabel2")
        )
    )
    private val dealersOutput = DealersOutput(listOf(dealerOutput))
    private val preferredDealerResponse = PreferredDealerResponse(dealerResponse)
    //endregion

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(DealerPSAMapper::class)
        every { anyConstructed<DealerPSAMapper>().transformDealer(dealerResponse, any(), any()) } returns dealerOutput

        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        userSessionManager.getUserSession()?.customerId

        executor = spyk(GetFavoriteDealerPsaExecutor(baseCommand), recordPrivateCalls = true)
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
        Assert.assertEquals(Action.Get, userInput.action)
        Assert.assertEquals(vin, userInput.vin)
    }

    @Test
    fun `when execute readFromCache in available case then return cache`() {
        every { dataManager.read(any(), any()) } returns dealerResponse.toJson()
        val cache = executor.readFromCache()
        Assert.assertEquals(dealerResponse, cache)
    }

    @Test
    fun `when execute readFromCache in empty case then return null`() {
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readFromCache()
        Assert.assertNull(cache)
    }

    @Test
    fun `when execute with action get and there is not a valid cache then make a network call`() {
        val vin = "testVin"
        coJustRun { executor.saveOnCache(eq(dealerResponse)) }
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
        every { executor.readFromCache() } returns null
        coEvery { communicationManager.get<PreferredDealerResponse>(any(), any()) } returns NetworkResponse.Success(
            preferredDealerResponse
        )
        runTest {
            val response = executor.execute(UserInput(Action.Get, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = 1) {
                communicationManager.get<PreferredDealerResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(dealersOutput, success.response)
        }
    }

    @Test
    fun `when execute then make a post API call return failure response`() {
        val vin = "testVin"
        val input = UserInput(vin = vin, action = Action.Get)
        coJustRun { executor.saveOnCache(eq(dealerResponse)) }

        val error = PimsErrors.serverError(null, "test-errors")

        mockkConstructor(DealerPSAMapper::class)
        every { executor.params(any()) } returns input

        coEvery { communicationManager.get<PreferredDealerResponse>(any(), any()) } returns
            NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute(UserInput(Action.Get, vin))

            coVerify {
                communicationManager.get<PreferredDealerResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Failure)
            val error = PimsErrors.serverError(null, "test-errors")
            val failure = response as Failure
            Assert.assertEquals(2204, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            error.subError.let {
                Assert.assertEquals(2006, failure.error?.subError?.status)
                Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
            }
        }
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        val vin = "testVin"
        every { executor.readFromCache() } returns dealerResponse

        runTest {
            val response = executor.execute(UserInput(Action.Get, vin))
            verify(exactly = 1) { executor.readFromCache() }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(dealersOutput, success.response)
        }
    }

    @Test
    fun `when execute then make a with action refresh then make a network call with success response`() {
        val vin = "testVin"
        coJustRun { executor.saveOnCache(eq(dealerResponse)) }
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

        coEvery {
            communicationManager.get<PreferredDealerResponse>(any(), any())
        } returns NetworkResponse.Success(
            preferredDealerResponse
        )

        runTest {
            val response = executor.execute(UserInput(Action.Refresh, vin))
            verify(exactly = 0) { executor.readFromCache() }
            verify {
                executor.request(
                    type = eq(PreferredDealerResponse::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/favorite_dealer")),
                    headers = any(),
                    body = any()
                )
            }
            coVerify {
                communicationManager.get<PreferredDealerResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(dealersOutput, success.response)
        }
    }

    @Test
    fun `when execute save then save in cache`() {
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

        runTest { executor.saveOnCache(dealerResponse) }

        verify {
            dataManager.create(
                key = eq("PEUGEOT_PREPROD_testCustomerId_preferredDealer"),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }
}
