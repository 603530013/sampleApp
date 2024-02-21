package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.Dealer
import com.inetpsa.pims.spaceMiddleware.model.dealer.Dealers
import com.inetpsa.pims.spaceMiddleware.model.dealer.GetDealerListPsaParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Deprecated("This will be replaced by GetDealerPsaExecutorTest")
internal class GetDealerListPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetDealerListPsaExecutor

    private val dealer = Dealer(
        siteGeo = "test_siteGeo",
        rrdi = "test_rrdi",
        name = "test_name",
        phones = Dealer.Phones(
            phoneAPV = "test_phone_APV",
            phoneNumber = "test_phone_number",
            phonePR = "test_phone_pr",
            phoneVN = "test_phone_vn",
            phoneVO = "test_phone_vO"
        ),
        emails = Dealer.Emails(
            email = "test_email",
            emailAPV = "test_email",
            emailAgent = "test_email",
            emailGER = "test_email",
            emailGRC = "test_email",
            emailPR = "test_email",
            emailSales = "test_email",
            emailVO = "test_email"
        ),
        address = Dealer.Address(
            address1 = "test",
            department = "test",
            city = "test",
            region = "test",
            zipCode = "test",
            country = "test"
        ),
        coordinates = Dealer.Coordinates(latitude = 10.0f, longitude = 20.0f),
        distance = 123f,
        isAgent = true,
        isAgentAp = true,
        isSecondary = true,
        isSuccursale = true,
        businessList = listOf(Dealer.BusinessItem(code = "test", label = "test", type = "test")),
        openHours = listOf(Dealer.OpeningHours(label = "test", type = "test")),
        principal = Dealer.Principal(
            isPrincipalAG = true,
            isPrincipalPR = true,
            isPrincipalRA = true,
            isPrincipalVN = true,
            isPrincipalVO = true
        ),
        urlPages = Dealer.UrlPages(
            urlAPVForm = "test",
            urlContact = "test",
            urlNewCarStock = "test",
            urlUsedCarStock = "test",
            urlUsefullInformation = "test"
        ),
        websites = Dealer.WebSites(private = "test", public = "test"),
        culture = "fr-FR",
        data = Dealer.Data(
            benefitList = listOf(),
            codesActors = Dealer.CodesActors(
                codeActorAddressPR = "test",
                codeActorAddressRA = "test",
                codeActorAddressVN = "test",
                codeActorAddressVO = "test",
                codeActorCCAG = "test",
                codeActorCCPR = "test",
                codeActorCCRA = "test",
                codeActorCCVN = "test",
                codeActorCCVO = "test",
                codeActorSearch = "test"
            ),
            codesRegions = Dealer.CodesRegions(
                codeRegionAG = "test",
                codeRegionPR = "test",
                codeRegionRA = "test",
                codeRegionVN = "test",
                codeRegionVO = "test"
            ),
            faxNumber = "test",
            group = Dealer.Group(groupId = "test", subGroupId = "test", subGroupLabel = "test"),
            indicator = Dealer.Indicator(code = "test", label = "test"),
            welcomeMessage = "test",
            importer = Dealer.Importer(
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
            pdvImporter = Dealer.PDVImporter(pDVCode = "test", pDVName = "test", pDVContact = "test"),
            numSiret = "test",
            legalStatus = "test",
            capital = "test",
            commercialRegister = "test",
            intracommunityTVA = "test",
            brand = "test",
            parentSiteGeo = "test",
            raisonSocial = "test",
            gmCodeList = listOf(
                Dealer.GmCodeItem(
                    activity = "test",
                    bacCode = "test",
                    cvcadcCode = "test",
                    pccadcCode = "test"
                )
            ),
            lienVoList = listOf("test"),
            bqCaptive = "test",
            caracRdvi = "test",
            ftcCodeList = listOf("test")
        ),
        isCaracRdvi = true,
        typeOperateur = "test",
        urlPrdvErcs = "test",
        jockey = true,
        o2ov = true,
        serviceList = listOf(Dealer.ServiceItem(code = "test", label = "test", openingHours = "test"))
    )
    private val dealers = Dealers(dealers = listOf(dealer))

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetDealerListPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a post API call`() {
        val input = GetDealerListPsaParams(latitude = "testLatitude", longitude = "testLongitude", resultMax = "5")
        every { executor.params(any()) } returns input
        coEvery { communicationManager.get<List<Dealer>>(any(), any()) } returns Success(listOf(dealer))

        runTest {
            val response = executor.execute()
            val queries = mapOf(
                "latitude" to "testLatitude",
                "longitude" to "testLongitude",
                "resultmax" to "5"
            )
            verify {
                executor.request(
                    type = eq(object : TypeToken<List<Dealer>>() {}.type),
                    urls = eq(arrayOf("/shop/v1/dealers")),
                    queries = eq(queries)
                )
            }

            coVerify {
                communicationManager.get<List<Dealer>>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(dealers, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return GetDealerListPsaParams`() {
        val latitude = "testSiteGeo"
        val longitude = "testSiteGeo"

        val params = GetDealerListPsaParams(
            latitude = latitude,
            longitude = longitude,
            resultMax = "5"
        )

        val input = mapOf(
            Constants.PARAM_LAT to latitude,
            Constants.PARAM_LNG to longitude,
            Constants.PARAM_RESULT_MAX to "5"
        )
        val output = executor.params(input)

        Assert.assertEquals(params, output)
    }

    @Test
    fun `when execute params with missing latitude then throw missing parameter`() {
        val longitude = "testSiteGeo"
        val input = mapOf(Constants.PARAM_LNG to longitude)
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
        val latitude = "testSiteGeo"
        val input = mapOf(Constants.PARAM_LAT to latitude)
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
        val latitude = "testSiteGeo"
        val longitude = 123
        val input = mapOf(
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
}
