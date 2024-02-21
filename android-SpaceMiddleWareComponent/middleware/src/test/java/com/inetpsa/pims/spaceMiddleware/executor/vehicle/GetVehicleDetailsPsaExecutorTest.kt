package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.data.IDataManager
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ProfileResponse
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.AccountVehicleInfoPsa
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.VehiclePsaParam
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.Vehicles.Vehicle
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetVehicleDetailsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVehicleDetailsPsaExecutor

    private val accountVehicleInfo = AccountVehicleInfoPsa(
        selectedVehicle = AccountVehicleInfoPsa.SelectedVehicle(
            type = GetVehicleDetailsPsaExecutor.VEHICLE_THERMIC,
            servicesConnected = listOf(),
            eligibility = listOf("BSRF"),
            attributes = listOf("DCX01CD"),
            shortName = null,
            vin = "",
            lcdv = null
        ),
        profile = ProfileResponse(
            email = "testEmail",
            firstName = "testFirstName",
            lastName = "testLastName",
            civility = "testCivility",
            civilityCode = "testCivilityCode",
            address1 = "testAddress1",
            address2 = "testAddress2",
            zipCode = "TestZipCode",
            city = "testCity",
            country = "testCountry",
            idClient = "testIdClient"
        ),
        dealers = AccountVehicleInfoPsa.Dealers(
            AccountVehicleInfoPsa.Apv(
                note = AccountVehicleInfoPsa.Note(
                    apv = AccountVehicleInfoPsa.NoteDetails(
                        note = 1.1f,
                        total = 1
                    ),
                    vn = AccountVehicleInfoPsa.NoteDetails(
                        note = 2.2f,
                        total = 2
                    ),
                    urlApv = "urlApv",
                    urlVn = "urlVn"
                ),
                isSecondary = null,
                distance = null,
                data = null,
                phones = null,
                openHours = listOf(
                    AccountVehicleInfoPsa.OpenHoursItem(
                        type = "type",
                        label = "label"
                    )
                ),
                jockey = null,
                emails = null,
                principal = null,
                urlPages = AccountVehicleInfoPsa.UrlPages(
                    urlNewCarStock = "urlNewCarStock",
                    urlUsedCarStock = "urlUsedCarStock",
                    urlContact = "urlContact",
                    urlUsefullInformation = "urlUsefullInformation",
                    urlAPVForm = "urlAPVForm"
                ),
                siteGeo = null,
                isAgent = null,
                fax = null,
                isAgentAp = null,
                typeOperateur = null,
                o2c = null,
                address = null,
                isCaracRdvi = null,
                business = listOf(),
                coordinates = null,
                services = listOf(
                    AccountVehicleInfoPsa.ServicesItem(
                        code = "code",
                        openingHours = "openingHours",
                        label = "label"
                    )
                ),
                rrdi = null,
                isSuccursale = null,
                culture = null,
                name = null,
                websites = null,
                urlPrdvErcs = null
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetVehicleDetailsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val vin = "testVin"
        val input = VehiclePsaParam(vin, true)
        every { executor.params(any()) } returns input

        val vehicleResponse = Vehicle(
            vin = "",
            lcdv = "",
            eligibility = listOf(),
            attributes = listOf("DCX01CD"),
            servicesConnected = "",
            preferredDealer = null,
            type = Vehicle.Type.ELECTRIC,
            name = "",
            regTimeStamp = 0,
            nickname = "",
            year = 0
        )
        every { executor.asVehicleResponseItem(any()) } returns vehicleResponse

        coJustRun { executor.saveProfile(any()) }
        coJustRun { executor.saveDealers(any()) }
        coJustRun { executor.saveVehicle(any()) }

        coEvery { communicationManager.get<AccountVehicleInfoPsa>(any(), any()) } returns
            NetworkResponse.Success(accountVehicleInfo)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(AccountVehicleInfoPsa::class.java),
                    urls = eq(arrayOf("/me/v1/user")),
                    headers = any(),
                    queries = eq(mapOf(Constants.PARAM_VIN to vin)),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<AccountVehicleInfoPsa>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            coVerify { executor.saveProfile(accountVehicleInfo.profile!!) }
            coVerify { executor.saveDealers(accountVehicleInfo.dealers!!) }
            coVerify { executor.saveVehicle(accountVehicleInfo.selectedVehicle) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehicleResponse, success.response)
        }
    }

    @Test
    fun `when execute params with the right vin then return vin`() {
        val vin = "testVin"
        val output = VehiclePsaParam(vin, false)
        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAM_SAVE_PROFILE to false
        )
        val param = executor.params(input)

        Assert.assertEquals(output, param)
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

    @Suppress("LongMethod")
    @Test
    fun `when asVehicleResponseItem then return the Vehicle item`() {
        val dealers = AccountVehicleInfoPsa.Dealers(
            AccountVehicleInfoPsa.Apv(
                note = AccountVehicleInfoPsa.Note(
                    apv = AccountVehicleInfoPsa.NoteDetails(
                        note = 1.1f,
                        total = 1
                    ),
                    vn = AccountVehicleInfoPsa.NoteDetails(
                        note = 2.2f,
                        total = 2
                    ),
                    urlApv = "urlApv",
                    urlVn = "urlVn"
                ),
                isSecondary = null,
                distance = null,
                data = null,
                phones = null,
                openHours = listOf(
                    AccountVehicleInfoPsa.OpenHoursItem(
                        type = "type",
                        label = "label"
                    )
                ),
                jockey = null,
                emails = null,
                principal = null,
                urlPages = AccountVehicleInfoPsa.UrlPages(
                    urlNewCarStock = "urlNewCarStock",
                    urlUsedCarStock = "urlUsedCarStock",
                    urlContact = "urlContact",
                    urlUsefullInformation = "urlUsefullInformation",
                    urlAPVForm = "urlAPVForm"
                ),
                siteGeo = null,
                isAgent = null,
                fax = null,
                isAgentAp = null,
                typeOperateur = null,
                o2c = null,
                address = null,
                isCaracRdvi = null,
                business = listOf(),
                coordinates = null,
                services = listOf(
                    AccountVehicleInfoPsa.ServicesItem(
                        code = "code",
                        openingHours = "openingHours",
                        label = "label"
                    )
                ),
                rrdi = null,
                isSuccursale = null,
                culture = null,
                name = null,
                websites = null,
                urlPrdvErcs = null
            )
        )

        val result = Vehicle(
            vin = "",
            lcdv = "",
            eligibility = listOf("BSRF"),
            attributes = listOf("DCX01CD"),
            servicesConnected = "[]",
            preferredDealer = dealers,
            type = Vehicle.Type.THERMIC,
            name = "",
            regTimeStamp = 0,
            nickname = "",
            year = 1900,
            settingsUpdate = 0
        )

        val response = executor.asVehicleResponseItem(accountVehicleInfo)

        Assert.assertEquals(result, response)
        Assert.assertEquals(result.eligibility, response?.eligibility)
    }

    @Test
    fun `when asVehicleResponseItem then return null`() {
        val vehicleInfo = AccountVehicleInfoPsa(
            selectedVehicle = AccountVehicleInfoPsa.SelectedVehicle(
                type = 99,
                servicesConnected = listOf(),
                eligibility = listOf(),
                attributes = listOf(),
                shortName = null,
                vin = "",
                lcdv = null
            )
        )

        val response = executor.asVehicleResponseItem(vehicleInfo)

        Assert.assertEquals(null, response)
    }

    @Test
    fun `when saveProfile then save the profile`() {
        val dataManager: IDataManager = mockk(relaxed = true)
        mockkStatic(MiddlewareComponent::createSync)
        val profile = ProfileResponse(
            email = "testEmail",
            firstName = "testFirstName",
            lastName = "testLastName",
            civility = "testCivility",
            civilityCode = "testCivilityCode",
            address1 = "testAddress1",
            address2 = "testAddress2",
            zipCode = "TestZipCode",
            city = "testCity",
            country = "testCountry",
            idClient = "testIdClient"
        )

        every { middlewareComponent.dataManager } returns dataManager
        coEvery { middlewareComponent.createSync(any(), any(), any()) } returns true

        runTest {
            executor.saveProfile(profile)

            coVerify {
                middlewareComponent.createSync(
                    Constants.STORAGE_KEY_PROFILE,
                    any(),
                    StoreMode.APPLICATION
                )
            }
        }
    }

    @Suppress("LongMethod")
    @Test
    fun `when saveProfile then save the dealer`() {
        val dataManager: IDataManager = mockk(relaxed = true)
        mockkStatic(MiddlewareComponent::createSync)
        val dealer = AccountVehicleInfoPsa.Dealer(
            siteGeo = "site_geo",
            rrdi = "rrdi",
            name = "name",
            culture = "culture",
            phones = AccountVehicleInfoPsa.Phones("phones"),
            emails = AccountVehicleInfoPsa.Emails("Emails"),
            address = AccountVehicleInfoPsa.Address(
                address1 = "address",
                address2 = "address",
                address3 = "address",
                department = "department",
                city = "city",
                region = "region",
                zipCode = "zipCode",
                country = "testCountry"
            ),
            coordinates = AccountVehicleInfoPsa.Coordinates(123.4f, 432.1f),
            distance = 1234.0f,
            business = listOf(
                AccountVehicleInfoPsa.BusinessItem(
                    code = "code",
                    label = "label",
                    type = "type"
                )
            ),
            openHours = listOf(
                AccountVehicleInfoPsa.OpeningHours(
                    type = "type",
                    label = "label"
                )
            ),
            isCaracRrdvi = true,
            typeOperator = "typeOperator",
            urlPrdvErcs = "urlPrdvErcs",
            jockey = true,
            o2c = true,
            urlPages = AccountVehicleInfoPsa.UrlPages(
                urlNewCarStock = "urlNewCarStock",
                urlUsedCarStock = "urlUsedCarStock",
                urlContact = "urlContact",
                urlUsefullInformation = "urlUsefullInformation",
                urlAPVForm = "urlAPVForm"
            ),
            websites = AccountVehicleInfoPsa.Websites(
                private = "private",
                public = "public"
            ),
            principal = AccountVehicleInfoPsa.Principal(
                isPrincipalPR = true,
                isPrincipalRA = true,
                isPrincipalVN = true,
                isPrincipalVO = true,
                isPrincipalAG = true
            ),
            data = AccountVehicleInfoPsa.Data(
                countryId = "countryId",
                group = AccountVehicleInfoPsa.Group(
                    subGroupLabel = "subGroupLabel",
                    subGroupId = "subGroupId",
                    groupId = "groupId"
                ),
                codesActors = AccountVehicleInfoPsa.CodesActors(
                    codeActorAddressPR = "codeActorAddressPR",
                    codeActorCCPR = "codeActorCCPR",
                    codeActorCCVN = "codeActorCCVN",
                    codeActorCCAG = "codeActorCCAG",
                    codeActorAddressRA = "codeActorAddressRA",
                    codeActorAddressVN = "codeActorAddressVN",
                    codeActorAddressVO = "codeActorAddressVO",
                    codeActorCCVO = "codeActorCCVO",
                    codeActorCCRA = "codeActorCCRA",
                    codeActorSearch = "codeActorSearch"

                ),
                intracommunityTVA = "intracommunityTVA",
                legalStatus = "legalStatus",
                parentSiteGeo = "parentSiteGeo",
                benefitList = listOf("benefitList"),
                capital = "capital",
                welcomeMessage = "welcomeMessage",
                codesRegions = AccountVehicleInfoPsa.CodesRegions(
                    codeRegionVN = "codeRegionVN",
                    codeRegionVO = "codeRegionVO",
                    codeRegionAG = "codeRegionAG",
                    codeRegionRA = "codeRegionRA",
                    codeRegionPR = "codeRegionPR"
                ),
                commercialRegister = "commercialRegister",
                raisonSocial = "raisonSocial",
                gmCodeList = listOf("gmCodeList"),
                indicator = AccountVehicleInfoPsa.Indicator(
                    label = "label",
                    code = "code"
                ),
                pdvImporter = AccountVehicleInfoPsa.PDVImporter(
                    pdvName = "pdvName",
                    pdvCode = "pdvCode",
                    pdvContact = "pdvContact"
                ),
                brand = "brand",
                personList = listOf("personList"),
                rCSNumber = "rCSNumber",
                bqCaptive = "bqCaptive",
                lienVoList = listOf("lienVoList"),
                numSiret = "bqCaptive",
                importer = AccountVehicleInfoPsa.Importer(
                    importerCode = "importerCode",
                    address = "address",
                    managementCountry = "managementCountry",
                    subsidiary = "subsidiary",
                    country = "country",
                    subsidiaryName = "subsidiaryName",
                    city = "city",
                    importerName = "importerName",
                    corporateName = "corporateName"
                )
            )
        )

        every { middlewareComponent.dataManager } returns dataManager
        coEvery { middlewareComponent.createSync(any(), any(), any()) } returns true

        runTest {
            executor.saveDealer(dealer)
        }
    }
}
