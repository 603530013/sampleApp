package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.destination.Destination
import com.inetpsa.pims.spaceMiddleware.model.destination.Destination.Location
import com.inetpsa.pims.spaceMiddleware.model.destination.Destination.Location.Address
import com.inetpsa.pims.spaceMiddleware.model.destination.DestinationInput
import com.inetpsa.pims.spaceMiddleware.model.destination.DestinationOutput
import com.inetpsa.pims.spaceMiddleware.model.destination.PoiInfoProvider
import com.inetpsa.pims.spaceMiddleware.model.destination.VehicleDestinationResponseFca
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class SetDestinationFcaExecutorTest : FcaExecutorTestHelper() {

    private val params = DestinationInput(
        vin = "VIN00000123",
        provider = PoiInfoProvider.GOOGLE_MAPS,
        destination = Destination(
            routePreference = "testRoutePreference",
            location = Location(
                placeId = "testPlaceId",
                latitude = 123.456,
                longitude = 123.456,
                name = "testName",
                description = "testDescription",
                url = "testUrl",
                phoneNumber = "testPhoneNumber",
                address = Address(
                    streetName = "testStreetName",
                    houseNumber = "testHouseNumber",
                    postalNumber = "testPostalNumber",
                    cityName = "testCityName",
                    countryName = "testCountryName",
                    countryCode = "testCountryCode",
                    provinceName = "testProvinceName",
                    provinceCode = "testProvinceCode"
                )
            )
        )
    )
    private lateinit var executor: SetDestinationFcaExecutor

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(SetDestinationFcaExecutor(baseCommand))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when execute then return failure response`() {
        every { executor.params(any()) } returns params

        val pimsError = PimsErrors.serverError(null, "404 not found")

        coEvery {
            communicationManager.post<VehicleDestinationResponseFca>(
                any(),
                any()
            )
        } returns NetworkResponse.Failure(pimsError)

        runTest {
            val response = executor.execute()

            Assert.assertTrue(response is NetworkResponse.Failure)
            val result = response as NetworkResponse.Failure
            Assert.assertEquals(result.error?.code, pimsError.code)
            Assert.assertEquals(result.error?.message, pimsError.message)

            coVerify {
                communicationManager.post<VehicleDestinationResponseFca>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when execute then return success response`() {
        val vehicleDestinationResponseFca = VehicleDestinationResponseFca(
            command = "testCommand",
            correlationId = "testId",
            error = VehicleDestinationResponseFca.Error(
                code = "",
                message = ""
            ),
            responseStatus = "testStatus",
            statusTimestamp = 100000L,
            asyncRespTimeout = 3500
        )

        every { executor.params(any()) } returns params

        coEvery { communicationManager.post<VehicleDestinationResponseFca>(any(), any()) } returns
            NetworkResponse.Success(vehicleDestinationResponseFca)

        runTest {
            val response = executor.execute()

            val expected = DestinationOutput(
                command = "testCommand",
                correlationId = "testId",
                responseStatus = "testStatus",
                statusTimestamp = 100000
            )

            coVerify {
                communicationManager.post<VehicleDestinationResponseFca>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(success.response, expected)
        }
    }

    @Test
    fun `when execute params with the right input then return GetDealerListPsaParams`() {
        val input = mapOf(
            Constants.PARAM_VIN to "VIN00000123",
            Constants.PARAMS_KEY_PROVIDER to PoiInfoProvider.GOOGLE_MAPS.name,
            Constants.PARAMS_KEY_PLACE_ID to "testPlaceId",
            Constants.PARAMS_KEY_ROUTE_PREFERENCE to "testRoutePreference",
            Constants.PARAMS_KEY_PLACE_ID to "testPlaceId",
            Constants.PARAMS_KEY_LATITUDE to 123.456,
            Constants.PARAMS_KEY_LONGITUDE to 123.456,
            Constants.PARAMS_KEY_NAME to "testName",
            Constants.PARAMS_KEY_DESCRIPTION to "testDescription",
            Constants.PARAMS_KEY_URL to "testUrl",
            Constants.PARAMS_KEY_PHONE_NUMBER to "testPhoneNumber",
            Constants.PARAMS_KEY_STREET_NAME to "testStreetName",
            Constants.PARAMS_KEY_HOUSE_NUMBER to "testHouseNumber",
            Constants.PARAMS_KEY_POSTAL_NUMBER to "testPostalNumber",
            Constants.PARAMS_KEY_CITY_NAME to "testCityName",
            Constants.PARAMS_KEY_COUNTRY_NAME to "testCountryName",
            Constants.PARAMS_KEY_COUNTRY_CODE to "testCountryCode",
            Constants.PARAMS_KEY_PROVINCE_NAME to "testProvinceName",
            Constants.PARAMS_KEY_PROVINCE_CODE to "testProvinceCode"

        )
        val output = executor.params(input)

        Assert.assertEquals(params.vin, output.vin)
        Assert.assertEquals(params.provider, output.provider)
        Assert.assertEquals(params.destination.location, output.destination.location)
        Assert.assertEquals(params.destination.routePreference, output.destination.routePreference)
    }
}
