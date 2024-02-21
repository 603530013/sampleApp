package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.OpeningHour
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.Coupon
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.OpeningHours
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.OpeningHours.HourIndication
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.Services
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class DealerFcaMapperTest {

    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    //region DATA
    private val dealerResponse = DealerDetailsResponse(
        id = "testBookingId|testBookingLocation",
        name = "test_name",
        address = "test_address",
        phoneNumber = "test_phone_number",
        ossDealerId = "test_ossDealerId",
        website = "test_website",
        latitude = "40",
        longitude = "9",
        preferred = true,
        serviceScheduling = true,
        coupons = Coupon(
            category = "testCategory",
            couponId = "testCoup",
            includes = "testIncludes",
            dealerId = "testDealerId",
            header = "testHeader",
            expires = 10.0,
            priceHeader1 = " ",
            price1 = " ",
            postPrice1 = " ",
            priceHeader2 = " ",
            price2 = " ",
            postPrice2 = " ",
            priceHeader3 = " ",
            price3 = " ",
            postPrice3 = " ",
            priceHeader4 = " ",
            price4 = " ",
            postPrice4 = " ",
            imageURL = " ",
            url = " "
        ),
        services = listOf(
            Services(
                servicesForBrand = "testBrand1",
                services = listOf("testServices1"),
                hasBusinessLink = false,
                hasShuttle = false,
                hasCertifiedPreOwnedVehicles = false,
                hasExpressLube = false,
                hasServiceContract = false,
                hasMoparAccessories = false,
                hasSaturdayService = false,
                hasRentalService = false,
                hasSpanishPersonnel = false,
                isCustomerFirst = false,
                isSupportingOSS = false,
                isSupportingPUDO = false,
                hasCertifiedWagoneer = false
            ),

            Services(
                servicesForBrand = "testBrand2",
                services = listOf("testServices2"),
                hasBusinessLink = false,
                hasShuttle = false,
                hasCertifiedPreOwnedVehicles = false,
                hasExpressLube = false,
                hasServiceContract = false,
                hasMoparAccessories = false,
                hasSaturdayService = false,
                hasRentalService = false,
                hasSpanishPersonnel = false,
                isCustomerFirst = false,
                isSupportingOSS = false,
                isSupportingPUDO = false,
                hasCertifiedWagoneer = false
            )
        ),
        departments = mapOf(
            "sales" to mapOf(
                "monday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "tuesday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "wednesday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "thursday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "friday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "saturday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "sunday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                )
            ),
            "service" to mapOf(
                "monday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "tuesday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "wednesday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "thursday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "10:00",
                        ampm = "pm"
                    )
                ),
                "friday" to OpeningHours(
                    closed = false,
                    hour24 = false,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "am"
                    ),
                    close = HourIndication(
                        time = "5:00",
                        ampm = "Pm"
                    )
                ),
                "saturday" to OpeningHours(
                    closed = false,
                    hour24 = true,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "AM"
                    ),
                    close = HourIndication(
                        time = "19:00",
                        ampm = "PM"
                    )
                ),
                "sunday" to OpeningHours(
                    closed = true,
                    hour24 = true,
                    open = HourIndication(
                        time = "10:00",
                        ampm = "AM"
                    ),
                    close = HourIndication(
                        time = "19:00",
                        ampm = "PM"
                    )
                )
            )
        )
    )

    private val dealerOutput = DealerOutput(
        id = "testBookingId|testBookingLocation",
        name = "test_name",
        address = "test_address",
        latitude = "40",
        longitude = "9",
        phones = mapOf(Constants.DEFAULT to "test_phone_number"),
        bookingId = null,
        bookingLocation = null,
        preferred = true,
        emails = null,
        bookable = false,
        website = "test_website",
        openingHours = mapOf(
            "sales" to mapOf(
                "MONDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "TUESDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "WEDNESDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "THURSDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "FRIDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "SATURDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "SUNDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00"))
            ),
            "service" to mapOf(
                "MONDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "TUESDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "WEDNESDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "THURSDAY" to OpeningHour(closed = false, time = listOf("10:00-22:00")),
                "FRIDAY" to OpeningHour(closed = false, time = listOf("10:00-17:00")),
                "SATURDAY" to OpeningHour(closed = false, time = listOf("10:00-19:00")),
                "SUNDAY" to OpeningHour(closed = true, time = null)
            )
        ),
        services = listOf(
            Service(code = "testServices1", type = null, label = null),
            Service(code = "testServices2", type = null, label = null)
        )
    )

    val vehicle = VehicleResponse(
        activationSource = "testActivationSource",
        brandCode = "testBrandCode",
        channelFeatures = listOf(
            VehicleResponse.ChannelFeature(
                channels = listOf("testChannel"),
                featureCode = "testFeatureCode"
            )
        ),
        color = "testColor",
        company = "testCompany",
        customerRegStatus = "testCustomerRegStatus",
        enrollmentStatus = "testEnrollmentStatus",
        fuelType = "testFuelType",
        isCompanyCar = false,
        language = "testLanguage",
        make = "testMake",
        market = "testMarket",
        model = "testModel",
        modelDescription = "testModelDescription",
        navEnabledHU = false,
        nickname = "testNickname",
        pp = VehicleResponse.Pp(VehicleResponse.Activation(status = "testStatus", version = "testVersion")),
        privacyMode = "testPrivacyMode",
        radio = "testRadio",
        regStatus = "testRegStatus",
        regTimestamp = today,
        sdp = "testSdp",
        services = listOf(
            VehicleResponse.Service(service = "testService1", serviceEnabled = false, vehicleCapable = false),
            VehicleResponse.Service(service = "testService2", serviceEnabled = true, vehicleCapable = true)
        ),
        soldRegion = "testSoldRegion",
        subMake = "testSubMake",
        svla = VehicleResponse.Svla(status = "testStatus", timestamp = today),
        tc = VehicleResponse.Tc(
            activation = VehicleResponse.Activation(
                status = "testStatus",
                version = "testVersion"
            ),
            registration = VehicleResponse.Tc.Registration(
                status = "testStatus",
                version = "testVersion"
            )
        ),
        tcuType = "testTcuType",
        tsoBodyCode = "testTsoBodyCode",
        tsoModelYear = "testTsoModelYear",
        vin = "testVin",
        year = 1986,
        imageUrl = "testImageUrl"
    )
    //endregion

    private val mapper: DealerFcaMapper = spyk(DealerFcaMapper(vehicle), recordPrivateCalls = true)

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when transform DealerDetailsResponse then return DealerOutput`() {
        val response = mapper.transformDealer(dealerResponse)
        Assert.assertEquals(dealerOutput, response)
    }

    @Test
    fun `when parseDayOfWeek with invalid data then return null`() {
        val response = mapper.parseDayOfWeek(null)
        Assert.assertNull(response)
    }

    @Test
    fun `when execute day of week  with invalid day then throw parsing failure`() {
        val response = mapper.parseDayOfWeek("manday")
        Assert.assertEquals(null, response)
    }

    @Test
    fun `when transform opening hours  with null then throw null value`() {
        val openingHours = OpeningHours(
            closed = null,
            hour24 = null,
            open = null,
            close = null
        )

        val response = mapper.transformOpeningHours(openingHours)
        Assert.assertEquals(null, response)
    }

    @Test
    fun `when parse time  with null then throw null value`() {
        val pattern = DateTimeFormatter.ofPattern("hh:mm")
        val response = mapper.parseTime("124", pattern)
        Assert.assertEquals(null, response)
    }

    @Suppress("LongMethod")
    @Test
    fun `when transformChannelFeature Then return different ServiceSchedulerCase`() {
        var feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_E",
            channels = listOf("CWP", "GMA")
        )
        var response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.EMEA, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.EMEA, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_E",
            channels = listOf("CWP")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.V1, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.NONE, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_N",
            channels = listOf("CWP", "GMA")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.NAFTA, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.NAFTA, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_N",
            channels = listOf("CWP")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.V1, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.NONE, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_L",
            channels = listOf("CWP", "GMA")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.LATAM, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.LATAM, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_L",
            channels = listOf("CWP")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.V1, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.NONE, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_ME",
            channels = listOf("CWP", "GMA")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.MASERATI, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.MASERATI, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_ME",
            channels = listOf("CWP")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.V1, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.NONE, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_MN",
            channels = listOf("CWP", "GMA")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.MASERATI, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.MASERATI, response)

        feature = VehicleResponse.ChannelFeature(
            featureCode = "f_0097_MN",
            channels = listOf("CWP")
        )
        response = mapper.transformChannelFeature(feature, Locale.US.country)
        Assert.assertEquals(ServiceSchedulerCase.V1, response)

        response = mapper.transformChannelFeature(feature, Locale.FRANCE.country)
        Assert.assertEquals(ServiceSchedulerCase.NONE, response)
    }

    @Test
    fun `when parseOnlineBookingId with serviceScheduling false then return null`() {
        val dealerMock = mockk<DealerDetailsResponse>()
        every { dealerMock.serviceScheduling } returns false
        every { mapper.transformChannelFeatures(any()) } returns ServiceSchedulerCase.EMEA
        val response = mapper.parseOnlineBookingId(dealerMock)
        Assert.assertNull(response)
    }

    @Test
    fun `when parseOnlineBookingId with serviceSchedulerCase EMEA then return split ID`() {
        val dealerMock = mockk<DealerDetailsResponse>()
        every { dealerMock.serviceScheduling } returns true
        every { dealerMock.id } returns "testBookingId|testBookingLocation"
        every { mapper.transformChannelFeatures(any()) } returns ServiceSchedulerCase.EMEA
        val response = mapper.parseOnlineBookingId(dealerMock)
        Assert.assertEquals("testBookingId", response?.first)
        Assert.assertEquals("testBookingLocation", response?.second)
    }

    @Test
    fun `when parseOnlineBookingId with serviceSchedulerCase LATAM then return split ID`() {
        val dealerMock = mockk<DealerDetailsResponse>()
        every { dealerMock.serviceScheduling } returns true
        every { dealerMock.id } returns "testBookingId|testBookingLocation"
        every { mapper.transformChannelFeatures(any()) } returns ServiceSchedulerCase.LATAM
        val response = mapper.parseOnlineBookingId(dealerMock)
        Assert.assertEquals("testBookingId", response?.first)
        Assert.assertEquals("testBookingLocation", response?.second)
    }

    @Test
    fun `when parseOnlineBookingId with serviceSchedulerCase MASERATI then return split ID`() {
        val dealerMock = mockk<DealerDetailsResponse>()
        every { dealerMock.serviceScheduling } returns true
        every { dealerMock.id } returns "testBookingId|testBookingLocation"
        every { mapper.transformChannelFeatures(any()) } returns ServiceSchedulerCase.MASERATI
        val response = mapper.parseOnlineBookingId(dealerMock)
        Assert.assertEquals("testBookingId", response?.first)
        Assert.assertEquals("testBookingLocation", response?.second)
    }

    @Test
    fun `when parseOnlineBookingId with serviceSchedulerCase NAFTA then return split ID`() {
        val dealerMock = mockk<DealerDetailsResponse>()
        every { dealerMock.serviceScheduling } returns true
        every { dealerMock.ossDealerId } returns "testBookingId|testBookingLocation"
        every { mapper.transformChannelFeatures(any()) } returns ServiceSchedulerCase.NAFTA
        val response = mapper.parseOnlineBookingId(dealerMock)
        Assert.assertEquals("testBookingId|testBookingLocation", response?.first)
        Assert.assertNull(response?.second)
    }

    @Test
    fun `when parseOnlineBookingId with serviceSchedulerCase V1 then return split ID`() {
        val dealerMock = mockk<DealerDetailsResponse>()
        every { dealerMock.serviceScheduling } returns true
        every { dealerMock.ossDealerId } returns "testBookingId|testBookingLocation"
        every { mapper.transformChannelFeatures(any()) } returns ServiceSchedulerCase.V1
        val response = mapper.parseOnlineBookingId(dealerMock)
        Assert.assertEquals("testBookingId|testBookingLocation", response?.first)
        Assert.assertNull(response?.second)
    }

    @Test
    fun `when parseOnlineBookingId with serviceSchedulerCase NONE then return split ID`() {
        val dealerMock = mockk<DealerDetailsResponse>()
        every { dealerMock.serviceScheduling } returns true
        every { dealerMock.id } returns "testBookingId|testBookingLocation"
        every { mapper.transformChannelFeatures(any()) } returns ServiceSchedulerCase.NONE
        val response = mapper.parseOnlineBookingId(dealerMock)
        Assert.assertNull(response)
    }
}
