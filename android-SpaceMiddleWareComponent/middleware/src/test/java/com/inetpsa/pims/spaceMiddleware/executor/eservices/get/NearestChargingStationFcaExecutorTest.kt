package com.inetpsa.pims.spaceMiddleware.executor.eservices.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey.SDP
import com.inetpsa.mmx.foundation.tools.TokenType.AWSToken
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationBoschMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationF2MMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationNoneMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationOutputMapper
import com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper.ChargingStationTomTomMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.CustomExtension
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.DeepLinksItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.F2mFilter
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.FilterInput
import com.inetpsa.pims.spaceMiddleware.util.asJson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class NearestChargingStationFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: NearestChargingStationFcaExecutor

    private val vehicleResponse: VehicleResponse = mockk()

    private val chargeStationInput = ChargeStationLocatorInput(
        vin = "testVin",
        latitude = 43.5,
        longitude = 5.8,
        filters = null
    )

    private val chargeStationBodyRequest = ChargeStationLocatorFCARequest(
        location = ChargeStationLocatorFCARequest.Location(
            latitude = 43.5,
            longitude = 5.8
        ),
        filters = mapOf("f2m" to F2mFilter(open24Hours = true))
    )

    private val filterInput = FilterInput(open24Hours = true)

    private val chargeStationResponse = ChargeStationLocatorResponse(
        chargeStations = listOf(
            ChargeStationLocatorResponse.ChargeStations(
                id = "testId",
                locationId = "testLocationId",
                dist = 12.34,
                poi = ChargeStationLocatorResponse.ChargeStations.PointInfoProvider(
                    name = "testName",
                    openHours = listOf(
                        ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours(
                            startTime = ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours.Time(
                                date = "2023-08-25",
                                hour = 1,
                                minute = 30
                            ),
                            endTime = ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours.Time(
                                date = "2023-09-25",
                                hour = 11,
                                minute = 30
                            )
                        )
                    ),
                    accessType = "testAccessType",
                    acceptablePayments = listOf(
                        "ELECTRONIC_PURSE",
                        "ELECTRONIC_TOLL_COLLECTION",
                        "SERVICE_PROVIDER_PAYMENT_METHOD",
                        "FUEL_CARD"
                    )
                ),
                address = ChargeStationLocatorResponse.ChargeStations.Address(
                    streetName = "testStreetName",
                    freeformAddress = "testFreeformAddress"
                ),
                position = ChargeStationLocatorResponse.ChargeStations.Position(
                    latitude = 12.34,
                    longitude = 56.78
                ),
                connectors = listOf(
                    ChargeStationLocatorResponse.ChargeStations.Connectors(
                        type = "testType",
                        compatible = true,
                        powerLevel = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel(
                            chargeTypeAvailability = ChargeStationLocatorResponse.ChargeStations.Connectors
                                .PowerLevel.ChargeTypeAvailability(
                                    fastCharge = 0,
                                    slowCharge = 1,
                                    regularCharge = 2,
                                    unknown = 3
                                ),
                            chargingCapacities = listOf(
                                ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel.ChargingCapacities(
                                    type = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel
                                        .ChargingCapacities.Type.AC,
                                    powerKw = 1,
                                    chargingMode = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel
                                        .ChargingCapacities.ChargingMode.Fast
                                )
                            )

                        ),
                        total = 20,
                        availability = ChargeStationLocatorResponse.ChargeStations.Connectors.Availability(
                            available = 1,
                            occupied = 2,
                            reserved = 3,
                            unknown = 4,
                            outOfService = 5
                        )
                    )
                ),
                cslProviderData = mapOf(
                    "f2m" to ChargeStationLocatorResponse.ChargeStations.CslProviderData(
                        isOpen24Hours = true,
                        renewableEnergy = true,
                        indoor = false,
                        floor = 3,
                        hotline = "hotline number",
                        status = ChargeStationLocatorResponse.ChargeStations.CslProviderData.Status.Charging,
                        access = listOf(
                            ChargeStationLocatorResponse.ChargeStations.CslProviderData.Access.ChargingCard,
                            ChargeStationLocatorResponse.ChargeStations.CslProviderData.Access.NoAuthentication
                        ),
                        canBeReserved = true
                    )
                )
            )
        )
    )

    private val marketPlaceResponse = MarketPlacePartnerResponse(
        customExtension = CustomExtension(
            deepLinks = listOf(
                DeepLinksItem(
                    key = "BOOK_PREFERRED_CHARGING_STATION",
                    androidLink = "free2movetest://publiccpdetail?id={chargePointId}&from=GMA"
                )
            )
        ),
        allowedBrands = listOf(),
        partnerBackgroundColor = null,
        partnerName = "MP_183",
        section = "MARKETPLACE",
        allowedFuelTypes = listOf(),
        isConsentApplicable = null,
        consents = null,
        partnerID = 606254847524,
        serviceID = "CSL",
        partnerImage = null,
        partnerThumbnail = null,
        contextHelp = null,
        allowedMvs = listOf(),
        partnerStackedLogo = null,
        partnerStatus = "ACTIVE"
    )

    private val result = ChargeStationLocatorOutput(
        chargingStations = listOf(
            ChargeStationLocatorOutput.ChargeStation(
                id = "testId",
                locationId = "testLocationId",
                name = "testName",
                openHours = listOf(
                    ChargeStationLocatorOutput.ChargeStation.OpeningHours(
                        startTime = "2023-08-25T01:30",
                        endTime = "2023-09-25T11:30"
                    )

                ),
                accessType = "testAccessType",
                address = "testFreeformAddress",
                acceptablePayments = listOf(
                    "ELECTRONIC_PURSE",
                    "ELECTRONIC_TOLL_COLLECTION",
                    "SERVICE_PROVIDER_PAYMENT_METHOD",
                    "FUEL_CARD"
                ),
                position = ChargeStationLocatorOutput.ChargeStation.Position(
                    latitude = 12.34,
                    longitude = 56.78
                ),
                connectors = listOf(
                    ChargeStationLocatorOutput.ChargeStation.Connectors(
                        type = "testType",
                        compatible = true,
                        powerLevel = ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel(
                            chargeTypeAvailability =
                            ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargeTypeAvailability(
                                fastCharge = 0,
                                slowCharge = 1,
                                regularCharge = 2,
                                unknown = 3
                            ),
                            chargingCapacities = listOf(
                                ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargingCapacities(
                                    type = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel
                                        .ChargingCapacities.Type.AC.name,
                                    powerKw = 1,
                                    chargingMode = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel
                                        .ChargingCapacities.ChargingMode.Fast.name
                                )
                            )

                        ),
                        total = 20,
                        availability = ChargeStationLocatorOutput.ChargeStation.Connectors.Availability(
                            available = 1,
                            occupied = 2,
                            reserved = 3,
                            unknown = 4,
                            outOfService = 5
                        )
                    )
                ),
                providers = mapOf(
                    "f2m" to ChargeStationLocatorOutput.ChargeStation.ProviderInfo(
                        open24Hours = true,
                        renewableEnergy = true,
                        indoor = false,
                        floor = 3,
                        hotline = "hotline number",
                        status = ChargeStationLocatorOutput.ChargeStation.ProviderInfo.Status.Charging,
                        access = listOf(
                            ChargeStationLocatorOutput.ChargeStation.ProviderInfo.Access.ChargingCard,
                            ChargeStationLocatorOutput.ChargeStation.ProviderInfo.Access.NoAuthentication
                        ),
                        canBeReserved = true
                    )
                )

            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkObject(CachedVehicles)
        every { vehicleResponse.cslProvider } returns CslProvider.F2M

        mockkConstructor(ChargingStationF2MMapper::class)
        every {
            anyConstructed<ChargingStationF2MMapper>().transformToBodyRequest(any())
        } returns chargeStationBodyRequest
        every { anyConstructed<ChargingStationF2MMapper>().transformParamsToInput(any()) } returns filterInput

        mockkConstructor(ChargingStationOutputMapper::class)
        every { anyConstructed<ChargingStationOutputMapper>().transformToOutput(any(), any()) } returns result

        mockkConstructor(MarketPlacePartnerManager::class)
        coEvery {
            anyConstructed<MarketPlacePartnerManager>().fetchChargingStationLocator(
                any(),
                any(),
                any()
            )
        } returns listOf(marketPlaceResponse)

        every {
            anyConstructed<MarketPlacePartnerManager>().getDeepLinkSupportedPartners(any())
        } returns marketPlaceResponse

        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse
        every { vehicleResponse.channelFeatures } returns null

        executor = spyk(NearestChargingStationFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute then return the different charging stations response`() {
        every { executor.params(any()) } returns chargeStationInput

        coEvery {
            communicationManager.post<ChargeStationLocatorResponse>(any(), any())
        } returns Success(chargeStationResponse)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(ChargeStationLocatorResponse::class.java),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            "testCustomerId",
                            "/vehicles/",
                            chargeStationInput.vin,
                            "/chargeStations"
                        )
                    ),
                    body = chargeStationBodyRequest.asJson()
                )
            }

            coVerify { executor.initializeMapper(any(), chargeStationInput.vin) }
            coVerify { anyConstructed<ChargingStationF2MMapper>().transformParamsToInput(any()) }
            coVerify { anyConstructed<ChargingStationF2MMapper>().transformToBodyRequest(any()) }

            coVerify {
                communicationManager.post<ChargeStationLocatorResponse>(
                    request = any(),
                    tokenType = eq(AWSToken(SDP))
                )
            }

            coVerify { anyConstructed<ChargingStationOutputMapper>().transformToOutput(any(), any()) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(result, success.response)
        }
    }

    @Test
    fun `when execute params with the right vin, right location then return params`() {
        val vin = "testVin"
        val latitude = 43.5
        val longitude = 5.8

        val input = mapOf(
            Constants.PARAM_VIN to chargeStationInput.vin,
            Constants.Input.LATITUDE to chargeStationInput.latitude,
            Constants.Input.LONGITUDE to chargeStationInput.longitude,
            Constants.Input.FILTERS to chargeStationInput.filters
        )
        val output = ChargeStationLocatorInput(
            vin = vin,
            latitude = latitude,
            longitude = longitude,
            filters = null
        )
        val param = executor.params(input)
        Assert.assertEquals(output, param)
    }

    @Test
    fun `when execute params with missing lat then throw missing parameter`() {
        val vin = " "
        val lat = 43.5
        val lon = 5.8

        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.Input.LATITUDE to lat,
            Constants.Input.LONGITUDE to lon
        )

        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with the invalid vin, then throw return invalid params`() {
        val vin = "testVin"
        val latitude = "0.00"
        val longitude = 5.8

        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.Input.LATITUDE to latitude,
            Constants.Input.LONGITUDE to longitude,
            Constants.Input.FILTERS to emptyMap<String, Any>()
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.LATITUDE)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when initializeMapper then return the right one for the right provider`() =
        runTest {
            Assert.assertEquals(
                ChargingStationNoneMapper::class.java,
                executor.initializeMapper(CslProvider.NONE, chargeStationInput.vin).javaClass
            )
            Assert.assertEquals(
                ChargingStationF2MMapper::class.java,
                executor.initializeMapper(CslProvider.F2M, chargeStationInput.vin).javaClass
            )
            Assert.assertEquals(
                ChargingStationBoschMapper::class.java,
                executor.initializeMapper(CslProvider.BOSCH, chargeStationInput.vin).javaClass
            )
            Assert.assertEquals(
                ChargingStationTomTomMapper::class.java,
                executor.initializeMapper(CslProvider.TOMTOM, chargeStationInput.vin).javaClass
            )
        }
}
