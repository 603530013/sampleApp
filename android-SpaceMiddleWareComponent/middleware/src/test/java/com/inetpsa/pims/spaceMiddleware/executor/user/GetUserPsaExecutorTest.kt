package com.inetpsa.pims.spaceMiddleware.executor.user

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.GetUserResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.GetUserResponse.DealersResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ProfileResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.Mileage
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.ServicesConnected.Offer
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.ServicesConnected.Offer.Price
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleListResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Address
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Coordinates
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Data
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Data.Group
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Data.Importer
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Data.Indicator
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Data.PDVImporter
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Note
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Note.Apv
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.Note.Vn
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse.WebSites
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class GetUserPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetUserPsaExecutor
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    //region user Response
    private val userResponse = GetUserResponse(
        profile = ProfileResponse(
            idClient = "testIdClient",
            email = "testEmail",
            lastName = "testLastName",
            firstName = "testFirstName",
            address1 = "testAddress1",
            address2 = "testAddress2",
            city = "testCity",
            zipCode = "testZipCode",
            country = "testCountry",
            civility = "testCivility",
            civilityCode = "testCivilityCode",
            cpf = "testCpf",
            rut = "testRut",
            phone = "testPhone",
            mobile = "testMobile",
            mobilePro = "testMobilePro"
        ),
        dealers = DealersResponse(
            apv = DetailsResponse(
                address = Address(
                    address1 = "",
                    city = "",
                    department = "",
                    region = "",
                    zipCode = ""
                ),
                business = listOf(),
                coordinates = Coordinates(
                    latitude = 0.0,
                    longitude = 0.0
                ),
                culture = "",
                data = Data(
                    bqCaptive = "",
                    brand = "",
                    capital = "",
                    codesActors = mapOf(
                        "codeActorAddressPR" to "",
                        "codeActorAddressRA" to "",
                        "codeActorAddressVN" to "",
                        "codeActorAddressVO" to "",
                        "codeActorCCAG" to "",
                        "codeActorCCPR" to "",
                        "codeActorCCRA" to "",
                        "codeActorCCVN" to "",
                        "codeActorCCVO" to "",
                        "codeActorSearch" to ""
                    ),
                    codesRegions = mapOf(
                        "codeRegionAG" to "",
                        "codeRegionPR" to "",
                        "codeRegionRA" to "",
                        "codeRegionVN" to "",
                        "codeRegionVO" to ""
                    ),
                    commercialRegister = "",
                    countryId = "",
                    group = Group(
                        groupId = "",
                        isLeader = false,
                        subGroupId = "",
                        subGroupLabel = ""
                    ),
                    importer = Importer(
                        address = "",
                        city = "",
                        corporateName = "",
                        country = "",
                        importerCode = "",
                        importerName = "",
                        managementCountry = "",
                        subsidiary = "",
                        subsidiaryName = ""
                    ),
                    indicator = Indicator(
                        code = "",
                        label = ""
                    ),
                    intracommunityTVA = "",
                    legalStatus = "",
                    numSiret = "",
                    pdvImporter = PDVImporter(
                        pDVCode = "",
                        pDVContact = "",
                        pDVName = ""
                    ),
                    parentSiteGeo = "testParentSiteGeo",
                    personList = listOf(),
                    rCSNumber = "testRCSNumber",
                    raisonSocial = "testRaisonSocial",
                    welcomeMessage = "testWelcomeMessage"
                ),
                distance = 40.0,
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
                fax = "testFax",
                isAgent = false,
                isAgentAp = false,
                isCaracRdvi = false,
                isSecondary = false,
                isSuccursale = false,
                jockey = false,
                name = "testName",
                note = Note(
                    apv = Apv(
                        note = 20.0,
                        total = 20
                    ),
                    urlApv = "testUrlApv",
                    urlHome = "testUrlHome",
                    urlVn = "testUrlVn",
                    vn = Vn(
                        note = 1.0,
                        total = 10
                    )
                ),
                o2c = false,
                openHours = listOf(),
                phones = mapOf(
                    "PhoneNumber" to "test_phone_number",
                    "PhoneAPV" to "test_phone_APV",
                    "PhonePR" to "test_phone_pr",
                    "PhoneVN" to "test_phone_vn",
                    "PhoneVO" to "test_phone_vo"
                ),
                principal = mapOf(
                    "isPrincipalAG" to false,
                    "isPrincipalPR" to false,
                    "isPrincipalRA" to false,
                    "isPrincipalVN" to false,
                    "isPrincipalVO" to false
                ),
                rrdi = "",
                services = listOf(),
                siteGeo = "",
                typeOperateur = "",
                urlPages = mapOf(
                    "urlAPVForm" to "testUrlAPVForm",
                    "urlContact" to "testUrlContact",
                    "urlNewCarStock" to "testUrlNewCarStock",
                    "urlUsedCarStock" to "testUrlUsedCarStock",
                    "urlUsefullInformation" to "testUrlUsefullInformation"
                ),
                urlPrdvErcs = "",
                websites = WebSites(
                    private = "testPrivateWebsite",
                    public = "testPublicWebsite"
                )
            )
        ),
        vehicles = listOf(
            VehicleListResponse(
                vin = "testVin1",
                lcdv = "testLCDV1",
                shortLabel = "testShortLabel1",
                visual = "testLinkVisual1",
                // warrantyStartDate = today,
                command = "testCommand1"
            ),
            VehicleListResponse(
                vin = "testVin2",
                lcdv = "testLCDV2",
                shortLabel = "testShortLabel2",
                visual = "testVisual",
                // warrantyStartDate = null,
                command = "testCommand"
            )
        ),
        selectedVehicle = VehicleDetailsResponse(
            vin = "testVin",
            lcdv = "textLCDV",
            visual = "testVisual",
            shortName = "testShortName",
            // warrantyStartDate = today,
            attributes = listOf("testAttributes"),
            eligibility = listOf("testEligibility"),
            typeVehicle = VehicleDetailsResponse.VEHICLE_ELECTRIC,
            mileage = Mileage(
                value = 1,
                timestamp = today,
                source = 0
            ),
            reviewMaxDate = null,
            servicesConnected = listOf(
                VehicleDetailsResponse.ServicesConnected(
                    id = "testServiceConnectedId",
                    title = "testServiceConnectedTitle",
                    category = "testServiceConnectedCategory",
                    description = "testServiceConnectedDescription",
                    url = "testServiceConnectedUrl",
                    urlSso = "testServiceConnectedUrlSso",
                    urlCvs = "testServiceConnectedUrlCvs",
                    price = 10f,
                    currency = "Euro",
                    offer = Offer(
                        pricingModel = "",
                        fromPrice = 0.1f,
                        price = Price(
                            periodType = "",
                            price = 10f,
                            currency = "",
                            typeDiscount = ""
                        ),
                        isFreeTrial = 0
                    )
                )
            )
        ),
        lastUpdate = today
    )
    //endregion

    @Before
    override fun setup() {
        super.setup()
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        executor = spyk(GetUserPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        val vin = "testID"
        every { executor.params(any()) } returns vin
        coEvery {
            communicationManager.get<GetUserResponse>(
                any(),
                any()
            )
        } returns NetworkResponse.Success(userResponse)
        coEvery { executor.save(any(), any()) } returns true
        coJustRun { executor.saveLastUpdate(any()) }
        coJustRun { executor.saveVehicle(any()) }
        coJustRun { executor.saveVehicles(any()) }
        coJustRun { executor.savePreferredDealer(any()) }

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(GetUserResponse::class.java),
                    urls = eq(arrayOf("/me/v1/user")),
                    headers = eq(mapOf("refresh-sams-cache" to "1")),
                    queries = eq(mapOf(Constants.PARAM_VIN to vin)),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<GetUserResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            coVerify(exactly = 1) { executor.saveLastUpdate(eq(today)) }
            coVerify(exactly = 1) { executor.saveVehicle(eq(userResponse.selectedVehicle)) }
            coVerify(exactly = 1) { executor.saveVehicles(eq(userResponse.vehicles)) }
            coVerify(exactly = 1) { executor.savePreferredDealer(eq(userResponse.dealers.apv!!)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(Unit, success)
        }
    }

    @Test
    fun `when execute then make a network API call with failure response`() {
        val error = PimsErrors.serverError(null, "testError")
        every { executor.params(any()) } returns null
        coEvery {
            communicationManager.get<GetUserResponse>(any(), any())
        } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(GetUserResponse::class.java),
                    urls = eq(arrayOf("/me/v1/user")),
                    headers = eq(mapOf("refresh-sams-cache" to "1")),
                    queries = eq(emptyMap()),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<GetUserResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            coVerify(exactly = 0) { executor.saveLastUpdate(eq(today)) }
            coVerify(exactly = 0) { executor.saveVehicle(eq(userResponse.selectedVehicle)) }
            coVerify(exactly = 0) { executor.saveVehicles(eq(userResponse.vehicles)) }
            coVerify(exactly = 0) { executor.savePreferredDealer(eq(userResponse.dealers.apv!!)) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(error, failure)
        }
    }

    @Test
    fun `when execute params with the right vin then return vin`() {
        val vin = "testVin"
        val input = mapOf(Constants.PARAM_VIN to vin)
        val param = executor.params(input)

        Assert.assertEquals(vin, param)
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

        runTest { executor.save("testKey", "testData") }

        verify {
            dataManager.create(
                key = eq("PEUGEOT_PREPROD_testCustomerId_testKey"),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }

    @Test
    fun `when execute saveLastUpdate then save in cache`() {
        coEvery { executor.save(any(), any()) } returns true
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
        every {
            dataManager.delete(
                key = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }

        runTest {
            executor.saveLastUpdate(today)

            coVerify {
                executor.save(
                    eq(Constants.Storage.LAST_UPDATE),
                    eq(today)
                )
            }
        }
    }

    @Test
    fun `when execute saveVehicle then save in cache`() {
        coEvery { executor.save(any(), any()) } returns true
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

        runTest {
            val vehicle = userResponse.selectedVehicle
            executor.saveVehicle(vehicle)

            coVerify {
                executor.save(
                    eq("${Constants.Storage.VEHICLE}_${vehicle.vin}"),
                    eq(vehicle)
                )
            }
        }
    }

    @Test
    fun `when execute saveVehicles then save in cache`() {
        coEvery { executor.save(any(), any()) } returns true
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

        runTest {
            val vehicles = userResponse.vehicles
            executor.saveVehicles(vehicles)

            coVerify {
                executor.save(
                    eq(Constants.Storage.VEHICLES),
                    eq(vehicles.toJson())
                )
            }
        }
    }

    @Test
    fun `when execute savePreferredDealer then save in cache`() {
        coEvery { executor.save(any(), any()) } returns true
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

        runTest {
            val dealer = userResponse.dealers.apv
            executor.savePreferredDealer(dealer!!)

            coVerify {
                executor.save(
                    eq(Constants.Storage.PREFERRED_DEALER),
                    eq(dealer)
                )
            }
        }
    }
}
