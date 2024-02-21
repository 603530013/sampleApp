package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.DealerFcaMapper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.OpeningHour
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerOutput.Service
import com.inetpsa.pims.spaceMiddleware.model.dealer.list.DealersOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.Coupon
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.OpeningHours
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.OpeningHours.HourIndication
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.DealerDetailsResponse.Services
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
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

internal class GetFavoriteDealerFcaExecutorTest : FcaExecutorTestHelper() {

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
        id = "test_dealerId",
        name = "test_name",
        address = "test_address",
        latitude = "40",
        longitude = "9",
        phones = mapOf(Constants.DEFAULT to "test_phone_number"),
        bookingId = null,
        bookingLocation = null,
        preferred = false,
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
    private val dealersOutput = DealersOutput(listOf(dealerOutput))
    //endregion

    private lateinit var executor: GetFavoriteDealerFcaExecutor
    private val vehicleResponse: VehicleResponse = mockk()

    @Before
    override fun setup() {
        super.setup()
        mockkObject(CachedVehicles)
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse
        executor = spyk(GetFavoriteDealerFcaExecutor(baseCommand))
        every { vehicleResponse.channelFeatures } returns null
    }

    @Test
    fun `when execute then make a post API call`() {
        val vin = "testVin"
        val input = UserInput(vin = vin, action = Action.Get)

        mockkConstructor(DealerFcaMapper::class)
        every { anyConstructed<DealerFcaMapper>().transformDealer(dealerResponse) } returns dealerOutput
        every { executor.params(any()) } returns input
        coEvery {
            communicationManager.get<List<DealerDetailsResponse>>(any(), any())
        } returns Success(listOf(dealerResponse))
        runTest {
            val response = executor.execute()
            verify {
                val type = object : TypeToken<List<DealerDetailsResponse>>() {}.type
                executor.request(
                    type = eq(type),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            "testCustomerId",
                            "/vehicles/",
                            vin,
                            "/mydealer/preferredDealer"
                        )
                    ),
                    queries = null,
                    body = null
                )
            }

            coVerify {
                communicationManager.get<List<DealerDetailsResponse>>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            verify(exactly = 1) { anyConstructed<DealerFcaMapper>().transformDealer(any()) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(dealersOutput, success.response)
        }
    }

    @Test
    fun `when execute then make a post API call return failure response`() {
        val vin = "testVin"
        val input = UserInput(vin = vin, action = Action.Get)
        val error = PimsErrors.serverError(null, "test-errors")

        mockkConstructor(DealerFcaMapper::class)
        every { executor.params(any()) } returns input

        coEvery {
            communicationManager.get<List<DealerDetailsResponse>>(any(), any())
        } returns Failure(error)

        runTest {
            val response = executor.execute(input)
            verify {
                val type = object : TypeToken<List<DealerDetailsResponse>>() {}.type
                executor.request(
                    type = eq(type),
                    urls = eq(
                        arrayOf(
                            "/v1/accounts/",
                            "testCustomerId",
                            "/vehicles/",
                            vin,
                            "/mydealer/preferredDealer"
                        )
                    ),
                    queries = null,
                    body = null
                )
            }

            coVerify {
                communicationManager.get<List<DealerDetailsResponse>>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Failure)
            val failure = response as Failure
            Assert.assertEquals(2204, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            error.subError.let {
                Assert.assertEquals(2006, failure.error?.subError?.status)
                Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
            }
        }
    }

    @Test
    fun `when execute params with the right input then return DealersInput`() {
        val action = "get"
        val vin = "testVin"
        val input = UserInput(
            vin = vin,
            action = Action.Get
        )

        val params = mapOf(
            Constants.Input.VIN to "testVin",
            Constants.Input.ACTION to action
        )
        val paramsInput = executor.params(params)

        Assert.assertEquals(input, paramsInput)
    }

    @Test
    fun `when execute params with missing action then throw missing parameter`() {
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ACTION)
        try {
            executor.params(emptyMap())
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid action then throw missing parameter`() {
        val input = mapOf(Constants.Input.ACTION to 123)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.ACTION to "get"
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.VIN to 123,
            Constants.Input.ACTION to "get"
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
