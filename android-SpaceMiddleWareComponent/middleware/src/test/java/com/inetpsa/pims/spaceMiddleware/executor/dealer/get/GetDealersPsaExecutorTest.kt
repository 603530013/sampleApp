package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.DealerPSAMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.OpeningHour
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Address
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.BusinessItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Coordinates
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Note.Vn
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.OpeningHours
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
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

internal class GetDealersPsaExecutorTest : PsaExecutorTestHelper() {

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
        preferred = true,
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
            DealerOutput.Service(code = "testCode1", type = "testType1", label = "testLabel1"),
            DealerOutput.Service(code = "testCode2", type = "testType2", label = "testLabel2")
        )
    )
    private val dealersOutput = DealersOutput(listOf(dealerOutput, dealerOutput))

    //endregion

    private lateinit var executor: GetDealersPsaExecutor

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(DealerPSAMapper::class)
        every { anyConstructed<DealerPSAMapper>().transformDealer(dealerResponse, any(), any()) } returns dealerOutput

        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager

        executor = spyk(GetDealersPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a post API call`() {
        val latitude = 44.0
        val longitude = 5.0
        val max = 5
        val vin = "testVin"
        val input = DealersInput(vin = vin, latitude = latitude, longitude = longitude, max = max)

        every { executor.params(any()) } returns input
        every { executor.transformToDealerOutput(eq(dealerResponse), "test_siteGeo") } returns dealerOutput
        coEvery {
            communicationManager.get<List<DetailsResponse>>(any(), any())
        } returns Success(listOf(dealerResponse, dealerResponse))

        runTest {
            val response = executor.execute()
            val queries = mapOf(
                "latitude" to latitude.toString(),
                "longitude" to longitude.toString(),
                "resultmax" to max.toString()
            )
            verify {
                val type = object : TypeToken<List<DetailsResponse>>() {}.type
                executor.request(
                    type = eq(type),
                    urls = eq(arrayOf("/shop/v1/dealers")),
                    queries = eq(queries)
                )
            }

            coVerify {
                communicationManager.get<List<DetailsResponse>>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            verify(exactly = 2) { executor.transformToDealerOutput(any(), any()) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(dealersOutput, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return DealersInput`() {
        val latitude = 44.0
        val longitude = 6.0
        val max = 5
        val vin = "testVin"
        val input = DealersInput(
            vin = vin,
            latitude = latitude,
            longitude = longitude,
            max = max
        )

        val params = mapOf(
            Constants.PARAM_VIN to "testVin",
            Constants.PARAM_LAT to latitude,
            Constants.PARAM_LNG to longitude,
            Constants.PARAM_MAX to max
        )
        val paramsInput = executor.params(params)

        Assert.assertEquals(input, paramsInput)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(emptyMap())
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val input = mapOf(Constants.PARAM_VIN to 123)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing latitude then throw missing parameter`() {
        val longitude = "testSiteGeo"
        val input = mapOf(
            Constants.PARAM_VIN to "testVin",
            Constants.PARAM_LNG to longitude
        )
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_LAT)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid latitude then throw missing parameter`() {
        val latitude = 123
        val longitude = "testSiteGeo"
        val input = mapOf(
            Constants.PARAM_VIN to "testVin",
            Constants.PARAM_LAT to latitude,
            Constants.PARAM_LNG to longitude
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_LAT)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing longitude then throw missing parameter`() {
        val latitude = 44.0
        val input = mapOf(
            Constants.PARAM_VIN to "testVin",
            Constants.PARAM_LAT to latitude
        )
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_LNG)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid longitude then throw missing parameter`() {
        val latitude = 123.0
        val longitude = "123"
        val input = mapOf(
            Constants.PARAM_VIN to "testVin",
            Constants.PARAM_LAT to latitude,
            Constants.PARAM_LNG to longitude
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_LNG)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when transformToDealerOutput then return DealerOutput`() {
        val response = dealerResponse.copy(culture = null)
        every { anyConstructed<DealerPSAMapper>().transformDealer(response, any(), any()) } returns dealerOutput
        val result = executor.transformToDealerOutput(response, response.siteGeo)
        Assert.assertEquals(dealerOutput, result)
    }

    @Test
    fun `when execute readDataFromCache in available case then return cache`() {
        every { dataManager.read(any(), any()) } returns dealerResponse.siteGeo
        val cache = executor.preferredDealerCache()
        verify {
            dataManager.read(
                eq("PEUGEOT_PREPROD_testCustomerId_preferredDealerId"),
                eq(StoreMode.APPLICATION)
            )
        }
        Assert.assertEquals(dealerResponse.siteGeo, cache)
    }

    @Test
    fun `when execute preferredDealerCache then return the cached value`() {
        every { dataManager.read(any(), any()) } returns "   "
        val id = "testSiteGeo"
        val cache = executor.preferredDealerCache()
        verify {
            dataManager.read(
                eq("PEUGEOT_PREPROD_testCustomerId_preferredDealerId"),
                eq(StoreMode.APPLICATION)
            )
        }
        Assert.assertEquals("   ", cache)
    }
}
