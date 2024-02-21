package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.data.IDataManager
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.util.createSync
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale

internal class GetVehiclesResponseFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetVehiclesResponseFcaExecutor
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    //region vehicles Response
    private val vehiclesResponse = VehiclesResponse(
        userid = "testUserId",
        version = "testVersion",
        vehicles = listOf(
            VehicleResponse(
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
        )
    )
    //endregion

    @Before
    override fun setup() {
        super.setup()
        justRun { dataManager.delete(any(), any(), any()) }
        executor = spyk(GetVehiclesResponseFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute then make a get API call`() {
        val brandValue = "testBrand"
        coEvery { executor.saveVehicles(any()) } returns vehiclesResponse
        every { executor.getBrandValue() } returns brandValue

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_BRAND to brandValue,
            Constants.QUERY_PARAM_KEY_SDP to Constants.AWS_QUERY_VALUE_ALL,
            Constants.QUERY_PARAM_KEY_STAGE to Constants.AWS_QUERY_VALUE_ALL
        )

        coEvery { communicationManager.get<VehiclesResponse>(any(), any()) } returns
            NetworkResponse.Success(vehiclesResponse)
        coJustRun { executor.clearPartnersCache() }

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(object : TypeToken<VehiclesResponse>() {}.type),
                    urls = eq(arrayOf("/v4/accounts/", uid, "/vehicles/")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<VehiclesResponse>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            coVerify { executor.clearPartnersCache() }
            coVerify { executor.saveVehicles(vehiclesResponse) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehiclesResponse.version, success.response.version)
            Assert.assertEquals(vehiclesResponse.userid, success.response.userid)
            Assert.assertEquals(vehiclesResponse.vehicles.size, success.response.vehicles.size)
            Assert.assertEquals(
                vehiclesResponse.vehicles.sortedBy { it.vin },
                success.response.vehicles.sortedBy { it.vin }
            )
        }
    }

    @Test
    fun `when transform brand then return the right flag`() {
        every { configurationManager.brand } returns Brand.PEUGEOT
        every { configurationManager.market } returns Market.NONE
        Assert.assertEquals(Constants.AWS_QUERY_VALUE_REST, executor.getBrandValue())

        every { configurationManager.brand } returns Brand.ALFAROMEO
        every { configurationManager.market } returns Market.EMEA
        Assert.assertEquals(Brand.ALFAROMEO.name.uppercase(Locale.US), executor.getBrandValue())

        every { configurationManager.brand } returns Brand.MASERATI
        Assert.assertEquals(Brand.MASERATI.name.uppercase(Locale.US), executor.getBrandValue())

        every { configurationManager.brand } returns Brand.FIAT
        Assert.assertEquals(Brand.FIAT.name.uppercase(Locale.US), executor.getBrandValue())

        every { configurationManager.market } returns Market.LATAM
        Assert.assertEquals(Constants.AWS_QUERY_VALUE_ALL, executor.getBrandValue())

        every { configurationManager.market } returns Market.NAFTA
        Assert.assertEquals(Constants.AWS_QUERY_VALUE_ALL, executor.getBrandValue())

        every { configurationManager.brand } returns Brand.PEUGEOT
        Assert.assertEquals(Constants.AWS_QUERY_VALUE_ALL, executor.getBrandValue())
    }

    @Test
    fun `test saveVehicles() calls createSync() method`() {
        val dataManager: IDataManager = mockk(relaxed = true)
        mockkStatic(MiddlewareComponent::createSync)
        every { middlewareComponent.dataManager } returns dataManager
        coEvery { middlewareComponent.createSync(any(), any(), any()) } returns true
        runBlocking {
            val result = executor.saveVehicles(vehiclesResponse)
            Assert.assertEquals(vehiclesResponse, result)
            coVerify {
                middlewareComponent.createSync(
                    key = Constants.Storage.VEHICLES,
                    data = vehiclesResponse.toJson(),
                    mode = StoreMode.APPLICATION
                )
            }
        }
    }
}
