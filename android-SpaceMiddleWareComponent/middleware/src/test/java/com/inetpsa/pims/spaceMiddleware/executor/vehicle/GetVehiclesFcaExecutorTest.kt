package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.Market
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.AccountVehicleInfoPsa
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.FcaVehicleItem
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.FcaVehicleItem.Service
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.FcaVehicleItem.Svla
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.FcaVehicleItem.Tc
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.FcaVehicleItem.Tc.Activation
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.FcaVehicleItem.Tc.Registration
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.AccountVehicleListFca
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.Vehicles.Vehicle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class GetVehiclesFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetVehiclesFcaExecutor

    //region vehicles FCA Response
    private val vehiclesFcaResponse = listOf(
        FcaVehicleItem(
            activationSource = "",
            brandCode = "",
            channelFeatures = listOf(),
            color = "",
            company = "",
            customerRegStatus = "",
            enrollmentStatus = "",
            fuelType = "e",
            language = "",
            make = "",
            market = "",
            model = "testModel1",
            modelDescription = "",
            navEnabledHU = false,
            nickname = "testNickName1",
            privacyMode = "",
            radio = "",
            regStatus = "",
            regTimestamp = 1,
            sdp = "",
            services = listOf(
                Service(
                    service = "testService1",
                    serviceEnabled = false,
                    vehicleCapable =
                    false
                )
            ),
            soldRegion = "",
            subMake = "",
            svla = Svla(
                status = "",
                timestamp = 0
            ),
            tc = Tc(
                activation = Activation(
                    status = "",
                    version = ""
                ),
                registration = Registration(
                    status = "",
                    version = ""
                )
            ),
            tcuType = "",
            tsoBodyCode = "",
            tsoModelYear = "",
            vin = "testVin1",
            year = 1
        ),
        FcaVehicleItem(
            activationSource = "",
            brandCode = "",
            channelFeatures = listOf(),
            color = "",
            company = "",
            customerRegStatus = "",
            enrollmentStatus = "",
            fuelType = "h",
            language = "",
            make = "",
            market = "",
            model = "testModel2",
            modelDescription = "",
            navEnabledHU = false,
            nickname = "testNickName2",
            privacyMode = "",
            radio = "",
            regStatus = "",
            regTimestamp = 2,
            sdp = "",
            services = listOf(
                FcaVehicleItem.Service(
                    service = "testService2",
                    serviceEnabled = false,
                    vehicleCapable =
                    false
                )
            ),
            soldRegion = "",
            subMake = "",
            svla = Svla(
                status = "",
                timestamp = 0
            ),
            tc = Tc(
                activation = Activation(
                    status = "",
                    version = ""
                ),
                registration = Registration(
                    status = "",
                    version = ""
                )
            ),
            tcuType = "",
            tsoBodyCode = "",
            tsoModelYear = "",
            vin = "testVin2",
            year = 2
        ),
        FcaVehicleItem(
            activationSource = "",
            brandCode = "",
            channelFeatures = listOf(),
            color = "",
            company = "",
            customerRegStatus = "",
            enrollmentStatus = "",
            fuelType = "d",
            language = "",
            make = "",
            market = "",
            model = "testModel3",
            modelDescription = "",
            navEnabledHU = false,
            nickname = "testNickName3",
            privacyMode = "",
            radio = "",
            regStatus = "",
            regTimestamp = 3,
            sdp = "",
            services = listOf(
                FcaVehicleItem.Service(
                    service = "testService3",
                    serviceEnabled = false,
                    vehicleCapable =
                    false
                )
            ),
            soldRegion = "",
            subMake = "",
            svla = Svla(
                status = "",
                timestamp = 0
            ),
            tc = Tc(
                activation = Activation(
                    status = "",
                    version = ""
                ),
                registration = Registration(
                    status = "",
                    version = ""
                )
            ),
            tcuType = "",
            tsoBodyCode = "",
            tsoModelYear = "",
            vin = "testVin3",
            year = 3
        ),
        FcaVehicleItem(
            activationSource = "",
            brandCode = "",
            channelFeatures = listOf(),
            color = "",
            company = "",
            customerRegStatus = "",
            enrollmentStatus = "",
            fuelType = "g",
            language = "",
            make = "",
            market = "",
            model = "testModel4",
            modelDescription = "",
            navEnabledHU = false,
            nickname = "testNickName4",
            privacyMode = "",
            radio = "",
            regStatus = "",
            regTimestamp = 4,
            sdp = "",
            services = listOf(
                FcaVehicleItem.Service(
                    service = "testService4",
                    serviceEnabled = false,
                    vehicleCapable =
                    false
                )
            ),
            soldRegion = "",
            subMake = "",
            svla = Svla(
                status = "",
                timestamp = 0
            ),
            tc = Tc(
                activation = Activation(
                    status = "",
                    version = ""
                ),
                registration = Registration(
                    status = "",
                    version = ""
                )
            ),
            tcuType = "",
            tsoBodyCode = "",
            tsoModelYear = "",
            vin = "testVin4",
            year = 4
        ),
        FcaVehicleItem(
            activationSource = "",
            brandCode = "",
            channelFeatures = listOf(),
            color = "",
            company = "",
            customerRegStatus = "",
            enrollmentStatus = "",
            fuelType = "t",
            language = "",
            make = "",
            market = "",
            model = "testModel5",
            modelDescription = "",
            navEnabledHU = false,
            nickname = "testNickName5",
            privacyMode = "",
            radio = "",
            regStatus = "",
            regTimestamp = 5,
            sdp = "",
            services = listOf(
                FcaVehicleItem.Service(
                    service = "testService5",
                    serviceEnabled = false,
                    vehicleCapable =
                    false
                )
            ),
            soldRegion = "",
            subMake = "",
            svla = Svla(
                status = "",
                timestamp = 0
            ),
            tc = Tc(
                activation = Activation(
                    status = "",
                    version = ""
                ),
                registration = Registration(
                    status = "",
                    version = ""
                )
            ),
            tcuType = "",
            tsoBodyCode = "",
            tsoModelYear = "",
            vin = "testVin5",
            year = 5
        )
    )
    //endregion

    //region vehicles Response
    private val vehiclesResponse = listOf(
        Vehicle(
            name = "testModel1",
            nickname = "testNickName1",
            eligibility = null,
            attributes = null,
            vin = "testVin1",
            year = 1,
            lcdv = "",
            servicesConnected = "[{\"service\":\"testService1\",\"serviceEnabled\":false,\"vehicleCapable\":false}]",
            preferredDealer = null,
            type = Vehicle.Type.ELECTRIC,
            regTimeStamp = 1
        ),
        Vehicle(
            name = "testModel2",
            nickname = "testNickName2",
            eligibility = null,
            attributes = null,
            vin = "testVin2",
            year = 2,
            lcdv = "",
            servicesConnected = "[{\"service\":\"testService2\",\"serviceEnabled\":false,\"vehicleCapable\":false}]",
            preferredDealer = null,
            type = Vehicle.Type.HYBRID,
            regTimeStamp = 2
        ),
        Vehicle(
            name = "testModel3",
            nickname = "testNickName3",
            eligibility = null,
            attributes = null,
            vin = "testVin3",
            year = 3,
            lcdv = "",
            servicesConnected = "[{\"service\":\"testService3\",\"serviceEnabled\":false,\"vehicleCapable\":false}]",
            preferredDealer = null,
            type = Vehicle.Type.THERMIC,
            regTimeStamp = 3
        ),
        Vehicle(
            name = "testModel4",
            nickname = "testNickName4",
            eligibility = null,
            attributes = null,
            vin = "testVin4",
            year = 4,
            lcdv = "",
            servicesConnected = "[{\"service\":\"testService4\",\"serviceEnabled\":false,\"vehicleCapable\":false}]",
            preferredDealer = null,
            type = Vehicle.Type.THERMIC,
            regTimeStamp = 4
        )
    )
    //endregion

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetVehiclesFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        every { executor.asVehicleResponse(any()) } returns vehiclesResponse

        val brandValue = "testBrand"
        every { executor.getBrandValue() } returns brandValue

        val queries = mapOf(
            Constants.QUERY_PARAM_KEY_BRAND to brandValue,
            Constants.QUERY_PARAM_KEY_SDP to Constants.AWS_QUERY_VALUE_ALL,
            Constants.QUERY_PARAM_KEY_STAGE to Constants.AWS_QUERY_VALUE_ALL
        )

        coEvery { communicationManager.get<AccountVehicleListFca>(any(), any()) } returns
            NetworkResponse.Success(AccountVehicleListFca(vehiclesFcaResponse))

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(object : TypeToken<AccountVehicleListFca>() {}.type),
                    urls = eq(arrayOf("/v4/accounts/", uid, "/vehicles/")),
                    headers = any(),
                    queries = eq(queries),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<AccountVehicleInfoPsa>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehiclesResponse.size, success.response.vehiclesList.size)
            Assert.assertEquals(vehiclesResponse.sortedBy { it.vin }, success.response.vehiclesList.sortedBy { it.vin })
        }
    }

    @Test
    fun `when transform Fca vehicle then return common vehicle response`() {
        val response = executor.asVehicleResponse(vehiclesFcaResponse)
        Assert.assertEquals(vehiclesResponse.size, response.size)
        Assert.assertEquals(vehiclesResponse.sortedBy { it.vin }, response.sortedBy { it.vin })
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
}
