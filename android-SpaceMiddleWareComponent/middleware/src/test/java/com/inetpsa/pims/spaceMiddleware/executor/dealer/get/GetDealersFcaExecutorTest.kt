package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.DealerFcaMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.OpeningHour
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.Coupon
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.OpeningHours.HourIndication
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.Services
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetDealersFcaExecutorTest : FcaExecutorTestHelper() {

    //region DATA
    private val dealerResponse = DealerDetailsResponse(
        id = "test_dealerId",
        name = "test_name",
        address = "test_address",
        phoneNumber = "test_phone_number",
        ossDealerId = "test_ossDealerId",
        website = "test_website",
        latitude = "40",
        longitude = "9",
        preferred = true,
        serviceScheduling = null,
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
                "monday" to DealerDetailsResponse.OpeningHours(
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
                "tuesday" to DealerDetailsResponse.OpeningHours(
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
                "wednesday" to DealerDetailsResponse.OpeningHours(
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
                "thursday" to DealerDetailsResponse.OpeningHours(
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
                "friday" to DealerDetailsResponse.OpeningHours(
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
                "saturday" to DealerDetailsResponse.OpeningHours(
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
                "sunday" to DealerDetailsResponse.OpeningHours(
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
                "monday" to DealerDetailsResponse.OpeningHours(
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
                "tuesday" to DealerDetailsResponse.OpeningHours(
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
                "wednesday" to DealerDetailsResponse.OpeningHours(
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
                "thursday" to DealerDetailsResponse.OpeningHours(
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
                "friday" to DealerDetailsResponse.OpeningHours(
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
                "saturday" to DealerDetailsResponse.OpeningHours(
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
                "sunday" to DealerDetailsResponse.OpeningHours(
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
        id = "test_dealerId",
        name = "test_name",
        address = "test_address",
        latitude = "40",
        longitude = "9",
        phones = mapOf(Constants.DEFAULT to "test_phone_number"),
        bookingId = null,
        bookingLocation = null,
        emails = null,
        website = "test_website",
        preferred = true,
        bookable = false,
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
            DealerOutput.Service(code = "testServices1", type = null, label = null),
            DealerOutput.Service(code = "testServices2", type = null, label = null)
        )
    )
    private val dealersOutput = DealersOutput(listOf(dealerOutput, dealerOutput))
    //endregion

    private lateinit var executor: GetDealersFcaExecutor
    private val vehicleResponse: VehicleResponse = mockk()

    @Before
    override fun setup() {
        super.setup()
        mockkObject(CachedVehicles)
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse
        executor = spyk(GetDealersFcaExecutor(baseCommand))
        every { vehicleResponse.channelFeatures } returns null
    }

    @Test
    fun `when execute then make a post API call`() {
        val latitude = 44.0
        val longitude = 5.0
        val max = 5
        val vin = "testVin"
        val input = DealersInput(vin = vin, latitude = latitude, longitude = longitude, max = max)

        every { executor.params(any()) } returns input
        coEvery {
            communicationManager.get<List<DealerDetailsResponse>>(any(), any())
        } returns Success(listOf(dealerResponse, dealerResponse))

        runTest {
            val response = executor.execute()
            val queries = mapOf(
                Constants.PARAM_LAT to latitude.toString(),
                Constants.PARAM_LNG to longitude.toString(),
                Constants.PARAM_LIMIT to max.toString()
            )
            verify {
                val type = object : TypeToken<List<DealerDetailsResponse>>() {}.type
                executor.request(
                    type = eq(type),
                    urls = eq(
                        arrayOf(
                            "/v2/accounts/",
                            "testCustomerId",
                            "/vehicles/",
                            vin,
                            "/mydealer/searchdealer"
                        )
                    ),
                    queries = eq(queries)
                )
            }

            coVerify {
                communicationManager.get<List<DealerDetailsResponse>>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            verify(exactly = 2) { executor.transformDealer(any(), eq(dealerResponse)) }

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
    fun `when transform DealerDetailsResponse then return DealerOutput`() {
        val mapper = DealerFcaMapper(vehicleResponse)
        val response = executor.transformDealer(mapper, dealerResponse)
        Assert.assertEquals(dealerOutput, response)
    }
}
