package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse.ChargeStations.CslProviderData
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse.ChargeStations.CslProviderData.Access.ChargingCard
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse.ChargeStations.CslProviderData.Access.NoAuthentication
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.ChargeStationLocatorResponse.ChargeStations.CslProviderData.Status.Charging
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.CustomExtension
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider.F2M
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorOutput.ChargeStation.ProviderInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ChargingStationOutputMapperTest {

    private lateinit var executor: ChargingStationOutputMapper

    private val vehicleResponse: VehicleResponse = mockk()

    private val marketPlaceResponse = MarketPlacePartnerResponse(
        customExtension = CustomExtension(
            deepLinks = listOf(
                MarketPlacePartnerResponse.DeepLinksItem(
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
                    "f2m" to CslProviderData(
                        isOpen24Hours = true,
                        renewableEnergy = true,
                        indoor = false,
                        floor = 3,
                        hotline = "hotline number",
                        status = Charging,
                        access = listOf(
                            ChargingCard,
                            NoAuthentication
                        ),
                        canBeReserved = true
                    )
                )
            )
        )
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
                    "f2m" to ProviderInfo(
                        open24Hours = true,
                        renewableEnergy = true,
                        indoor = false,
                        floor = 3,
                        hotline = "hotline number",
                        status = ProviderInfo.Status.Charging,
                        access = listOf(
                            ProviderInfo.Access.ChargingCard,
                            ProviderInfo.Access.NoAuthentication
                        ),
                        canBeReserved = true
                    )
                )

            )
        )
    )

    @Before
    fun setup() {
        executor = spyk(ChargingStationOutputMapper())
        every { vehicleResponse.cslProvider } returns F2M
        every { vehicleResponse.channelFeatures } returns null
    }

    @Test
    fun `when execute transformToOpenHours method with empty input list `() {
        val input = emptyList<ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours>()
        val expectedOutput = emptyList<ChargeStationLocatorOutput.ChargeStation.OpeningHours>()
        Assert.assertEquals(expectedOutput, executor.transformToOpenHours(input))
    }

    @Test
    fun `when execute transformToOpenHours method with null input list `() {
        Assert.assertEquals(null, executor.transformToOpenHours(null))
    }

    @Test
    fun `transformOfTime should return LocalDateTime when given a valid time`() {
        val time = ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours.Time(
            date = "2022-11-11",
            hour = 11,
            minute = 11
        )
        val result = executor.transformOfTime(time)
        Assert.assertEquals(LocalDateTime.of(LocalDate.parse("2022-11-11"), LocalTime.of(11, 11)), result)
    }

    @Test
    fun `transformOfTime should return null when given a null time`() {
        val time: ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours.Time? = null
        val result = executor.transformOfTime(time)
        Assert.assertEquals(null, result)
    }

    @Test
    fun `transformToOpenHours should return null when input is null`() {
        val result = executor.transformToOpenHours(null)
        Assert.assertNull(result)
    }

    @Test
    fun `transformToOpenHours should transform opening hours correctly`() {
        val input = listOf(
            ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours(
                startTime = ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours.Time(
                    date = "2022-01-01",
                    hour = 10,
                    minute = 0
                ),
                endTime = ChargeStationLocatorResponse.ChargeStations.PointInfoProvider.OpeningHours.Time(
                    date = "2022-01-02",
                    hour = 12,
                    minute = 30
                )
            )
        )

        val expected = listOf(
            ChargeStationLocatorOutput.ChargeStation.OpeningHours(
                startTime = LocalDateTime.of(2022, 1, 1, 10, 0).toString(),
                endTime = LocalDateTime.of(2022, 1, 2, 12, 30).toString()
            )
        )

        val result = executor.transformToOpenHours(input)
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `transformChargeAvailable should transform charge type availability correctly`() {
        val input = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel.ChargeTypeAvailability(
            fastCharge = 0,
            slowCharge = 0,
            regularCharge = 0,
            unknown = 0
        )

        val output = executor.transformChargeAvailable(input)
        Assert.assertEquals(
            ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargeTypeAvailability(
                fastCharge = 0,
                slowCharge = 0,
                regularCharge = 0,
                unknown = 0
            ),
            output
        )
    }

    @Test
    fun `transformChargeCapabilities should return null when given null`() {
        val result = executor.transformChargeCapabilities(null)
        Assert.assertEquals(null, result)
    }

    @Test
    fun `transformChargeCapabilities should return a list of ChargingCapacities`() {
        val input = listOf(
            ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel.ChargingCapacities(
                type = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel.ChargingCapacities.Type.AC,
                powerKw = 7,
                chargingMode = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel.ChargingCapacities
                    .ChargingMode.Regular
            ),
            ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel.ChargingCapacities(
                type = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel.ChargingCapacities.Type.DC,
                powerKw = 50,
                chargingMode = ChargeStationLocatorResponse.ChargeStations.Connectors.PowerLevel.ChargingCapacities
                    .ChargingMode.Regular
            )
        )

        val expectedOutput = listOf(
            ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargingCapacities(
                type = "AC",
                powerKw = 7,
                chargingMode = "Regular"
            ),
            ChargeStationLocatorOutput.ChargeStation.Connectors.PowerLevel.ChargingCapacities(
                type = "DC",
                powerKw = 50,
                chargingMode = "Regular"
            )
        )

        val result = executor.transformChargeCapabilities(input)
        Assert.assertEquals(expectedOutput, result)
    }

    @Test
    fun `transformAvailability should map ChargeStationLocatorResponse to ChargeStationLocatorOutput`() {
        val response = ChargeStationLocatorResponse.ChargeStations.Connectors.Availability(
            available = 1,
            occupied = 2,
            reserved = 3,
            unknown = 4,
            outOfService = 5
        )
        val result = executor.transformAvailability(response)
        Assert.assertEquals(response.available, result.available)
        Assert.assertEquals(response.occupied, result.occupied)
        Assert.assertEquals(response.reserved, result.reserved)
        Assert.assertEquals(response.unknown, result.unknown)
        Assert.assertEquals(response.outOfService, result.outOfService)
    }

    @Test
    fun `transformToSetChargeStation should map ChargeStationLocatorResponse to ChargeStationLocatorOutput`() {
        val response = ChargeStationLocatorResponse(
            chargeStations = listOf(
                ChargeStationLocatorResponse.ChargeStations(
                    id = "1",
                    locationId = "2",
                    poi = null,
                    address = null,
                    position = null,
                    connectors = null
                ),
                ChargeStationLocatorResponse.ChargeStations(
                    id = "3",
                    locationId = "4",
                    poi = null,
                    address = null,
                    position = null,
                    connectors = null
                )
            )
        )
        val result = executor.transformToOutput(response, marketPlaceResponse)
        Assert.assertEquals(response.chargeStations?.size, result.chargingStations?.size)
        Assert.assertEquals(response.chargeStations?.get(0)?.id, result.chargingStations?.get(0)?.id)
        Assert.assertEquals(response.chargeStations?.get(0)?.locationId, result.chargingStations?.get(0)?.locationId)
        Assert.assertEquals(response.chargeStations?.get(1)?.id, result.chargingStations?.get(1)?.id)
        Assert.assertEquals(response.chargeStations?.get(1)?.locationId, result.chargingStations?.get(1)?.locationId)
    }

    @Test
    fun `when execute transformToProviderInfo method should return expected output`() {
        val providers = chargeStationResponse.chargeStations?.firstOrNull()?.cslProviderData
        val output = executor.transformToProviderInfo(providers?.get("f2m")!!)
        Assert.assertEquals(result.chargingStations?.firstOrNull()?.providers?.get("f2m"), output)
    }

    @Test
    fun `when execute transformProviderStatus method should return expected output`() {
        var output = executor.transformProviderStatus(CslProviderData.Status.Unknown)
        Assert.assertEquals(ProviderInfo.Status.Unknown, output)
        output = executor.transformProviderStatus(CslProviderData.Status.Available)
        Assert.assertEquals(ProviderInfo.Status.Available, output)
        output = executor.transformProviderStatus(CslProviderData.Status.Charging)
        Assert.assertEquals(ProviderInfo.Status.Charging, output)
        output = executor.transformProviderStatus(CslProviderData.Status.OutOfService)
        Assert.assertEquals(ProviderInfo.Status.OutOfService, output)
        output = executor.transformProviderStatus(CslProviderData.Status.Reserved)
        Assert.assertEquals(ProviderInfo.Status.Reserved, output)
        output = executor.transformProviderStatus(CslProviderData.Status.Removed)
        Assert.assertEquals(ProviderInfo.Status.Removed, output)
        output = executor.transformProviderStatus(null)
        Assert.assertEquals(null, output)
    }

    @Test
    fun `when execute transformProviderAccess method should return expected output`() {
        var output = executor.transformProviderAccess(CslProviderData.Access.ChargingCard)
        Assert.assertEquals(ProviderInfo.Access.ChargingCard, output)
        output = executor.transformProviderAccess(CslProviderData.Access.NoAuthentication)
        Assert.assertEquals(ProviderInfo.Access.NoAuthentication, output)
        output = executor.transformProviderAccess(CslProviderData.Access.App)
        Assert.assertEquals(ProviderInfo.Access.App, output)
        output = executor.transformProviderAccess(null)
        Assert.assertEquals(null, output)
    }

    @Test
    fun `test extractLinkFromPartner() replaces deeplinkID successfully`() {
        val link = executor.extractLinkFromPartner("testDeepLinkID", marketPlaceResponse)
        Assert.assertEquals("free2movetest://publiccpdetail?id=testDeepLinkID&from=GMA", link)
    }

    @Test
    fun `test extractLinkFromPartner() returns null when deepLink key not matches`() {
        val marketPlacePartnerResponse = MarketPlacePartnerResponse(
            customExtension = CustomExtension(
                deepLinks = listOf(
                    MarketPlacePartnerResponse.DeepLinksItem(
                        key = "testKey",
                        androidLink = null
                    )
                )
            )
        )
        val link = executor.extractLinkFromPartner("testDeepLinkID", marketPlacePartnerResponse)
        Assert.assertEquals(null, link)
    }
}
