package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.Dealer
import com.inetpsa.pims.spaceMiddleware.model.dealer.PreferredDealer
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Deprecated("We should use GetFavoriteDealerPsaExecutorTest instead")
internal class GetPreferredDealerPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetPreferredDealerPsaExecutor

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
    private val preferredDealer = PreferredDealer(dealer = dealer)

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetPreferredDealerPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        coEvery { communicationManager.get<PreferredDealer>(any(), any()) } returns
            NetworkResponse.Success(preferredDealer)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(PreferredDealer::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/favorite_dealer")),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<PreferredDealer>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(dealer, success.response)
        }
    }
}
