package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.OpeningHour
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Address
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.BusinessItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Coordinates
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Note.Vn
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.OpeningHours
import com.inetpsa.pims.spaceMiddleware.util.normaliseFromHtml
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

class DealerPSAMapperTest {

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
                label = "Lundi:08:00-12:00 14:00-18:00<br /> Mardi:08:00-12:00 14:00-18:00<br /> " +
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
        bookingId = "test_siteGeo",
        bookingLocation = null,
        preferred = false,
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
    //endregion

    private val mapper: DealerPSAMapper = spyk()

    @Before
    fun setup() {
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.StringExtensionKt")
        val separator = System.lineSeparator()
        every {
            any<String>().normaliseFromHtml()
        } returns "Lundi:08:00-12:00 14:00-18:00$separator Mardi:08:00-12:00 14:00-18:00$separator " +
            "Mercredi:08:00-12:00 14:00-18:00$separator Jeudi:08:00-12:00 14:00-18:00$separator Vendredi:08:00-12:00 " +
            "14:00-17:00$separator Samedi: Ferm\u00e9$separator Dimanche: Ferm\u00e9$separator"
    }

    @Test
    fun `when transform DealerDetailsResponse then return DealerOutput`() {
        val response = mapper.transformDealer(dealerResponse, Locale.FRANCE, false)
        Assert.assertEquals(dealerOutput, response)
    }

    @Test
    fun `when parseDayOfWeek with invalid string then return null`() {
        var response = mapper.parseDayOfWeek(null, Locale.FRANCE)
        Assert.assertNull(response)

        response = mapper.parseDayOfWeek("test", Locale.FRANCE)
        Assert.assertNull(response)

        response = mapper.parseDayOfWeek("manday", Locale.FRENCH)
        Assert.assertEquals(null, response)
    }

    @Test
    fun `when transform Address then return formatted address string`() {
        val address = Address(
            address1 = "testAddress_1",
            address2 = "testAddress_2",
            city = "testCity",
            country = "testCountry",
            department = "testDepartment",
            region = "testRegion",
            zipCode = "testZipCode"
        )
        var response = mapper.transformAddress(address, Locale.FRENCH)
        Assert.assertEquals("testAddress_1 testAddress_2 TESTZIPCODE TestCity", response)

        response = mapper.transformAddress(address, Locale.UK)
        Assert.assertEquals("testAddress_1 testAddress_2 TestCity TESTZIPCODE", response)
    }

    @Test
    fun `when transform DealerDetailsResponse is null then return null`() {
        val response = DetailsResponse(
            siteGeo = "test_site_geo",
            rrdi = null,
            name = null,
            phones = null,
            emails = null,
            address = null,
            coordinates = null,
            distance = null,
            isAgent = null,
            isAgentAp = null,
            isSecondary = null,
            isSuccursale = null,
            business = null,
            openHours = null,
            principal = null,
            urlPages = null,
            websites = null,
            culture = null,
            data = null,
            isCaracRdvi = null,
            typeOperateur = null,
            urlPrdvErcs = null,
            jockey = null,
            o2ov = null,
            services = null,
            fax = null,
            note = null,
            o2c = null
        )

        val dealerOutput = DealerOutput(
            id = "test_site_geo",
            name = null,
            address = null,
            latitude = null,
            longitude = null,
            phones = null,
            bookingId = "test_site_geo",
            bookingLocation = null,
            preferred = false,
            bookable = false,
            emails = null,
            website = null,
            openingHours = null,
            services = null
        )
        val result = mapper.transformDealer(response, Locale.FRANCE, false)
        Assert.assertEquals(dealerOutput, result)
    }

    @Test
    fun `when transform DealerDetailsResponse with isCaracRdvi false then return output with booking false`() {
        // Given
        val response = dealerResponse.copy(isCaracRdvi = false)
        val dealerOutput = dealerOutput.copy(bookable = false)

        // When
        val result = mapper.transformDealer(response, Locale.FRANCE, false)

        // Then
        Assert.assertEquals(dealerOutput, result)
    }

    @Test
    fun `when transform DealerDetailsResponse with isCaracRdvi null then return output with booking false`() {
        // Given
        val response = dealerResponse.copy(isCaracRdvi = null)
        val dealerOutput = dealerOutput.copy(bookable = false)

        // When
        val result = mapper.transformDealer(response, Locale.FRANCE, false)

        // Then
        Assert.assertEquals(dealerOutput, result)
    }
}
