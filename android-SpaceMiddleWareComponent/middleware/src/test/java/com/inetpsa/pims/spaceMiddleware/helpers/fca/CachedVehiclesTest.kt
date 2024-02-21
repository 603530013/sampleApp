package com.inetpsa.pims.spaceMiddleware.helpers.fca

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.vehicle.get.GetVehiclesResponseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Delete
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.common.Action.OnlyCache
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Activation
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ChannelFeature
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Pp
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Svla
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc.Registration
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class CachedVehiclesTest : FcaExecutorTestHelper() {

    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    private val vehicleResponse = VehicleResponse(
        activationSource = "testActivationSource",
        brandCode = "testBrandCode",
        channelFeatures = listOf(
            ChannelFeature(
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
        pp = Pp(Activation(status = "testStatus", version = "testVersion")),
        privacyMode = "testPrivacyMode",
        radio = "testRadio",
        regStatus = "testRegStatus",
        regTimestamp = today,
        sdp = "testSdp",
        services = listOf(
            Service(service = "testService1", serviceEnabled = false, vehicleCapable = false),
            Service(service = "testService2", serviceEnabled = true, vehicleCapable = true)
        ),
        soldRegion = "testSoldRegion",
        subMake = "testSubMake",
        svla = Svla(status = "testStatus", timestamp = today),
        tc = Tc(
            activation = Activation(
                status = "testStatus",
                version = "testVersion"
            ),
            registration = Registration(
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

    //region vehicles Response
    private val vehiclesResponse = VehiclesResponse(
        userid = "testUserId",
        version = "testVersion",
        vehicles = listOf(vehicleResponse)
    )

    override fun setup() {
        super.setup()
        mockkObject(CachedVehicles)
        mockkConstructor(GetVehiclesResponseFcaExecutor::class)
        val successResponse = NetworkResponse.Success(vehiclesResponse)
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(any()) } returns successResponse
    }

    @Test
    fun `when execute readFromCache in available case then return cache`() {
        val json = vehiclesResponse.toJson()
        every { dataManager.read(any(), any()) } returns json
        val cache = CachedVehicles.readFromCache(middlewareComponent)
        verify { dataManager.read(eq("ALFAROMEO_PREPROD_testCustomerId_vehicles"), eq(APPLICATION)) }
        Assert.assertEquals(json, cache)
    }

    @Test
    fun `when execute readFromCache in unavailable case then return null`() {
        every { dataManager.read(any(), any()) } returns null
        val cache = CachedVehicles.readFromCache(middlewareComponent)
        verify { dataManager.read(eq("ALFAROMEO_PREPROD_testCustomerId_vehicles"), eq(APPLICATION)) }
        Assert.assertNull(cache)
    }

    @Test
    fun `when execute readFromJson in available case then return vehiclesResponse`() {
        runTest {
            val json = vehiclesResponse.toJson()
            val vehicles = CachedVehicles.readFromJson(json)
            coVerify { CachedVehicles.readFromJson(json) }
            Assert.assertEquals(vehiclesResponse, vehicles)
        }
    }

    @Test
    fun `when execute readFromJson in unavailable case then return null`() {
        val vehicles = CachedVehicles.readFromJson(null)
        Assert.assertNull(vehicles)
    }

    @Test
    fun `when execute readFromJson with parsing error then return exception`() {
        val vehicles = CachedVehicles.readFromJson("test")
        Assert.assertNull(vehicles)
    }

    @Test
    fun `when execute readFromLocal in available case then return vehiclesResponse`() {
        coEvery {
            CachedVehicles.readFromCache(middlewareComponent)
        } returns vehiclesResponse.toJson()

        runTest {
            val vehicles = CachedVehicles.readFromLocal(middlewareComponent)
            coVerify(exactly = 1) { CachedVehicles.readFromCache(middlewareComponent) }
            Assert.assertEquals(vehiclesResponse, vehicles)
        }
    }

    @Test
    fun `when execute readFromLocal return vehiclesResponse`() {
        val json = vehiclesResponse.toJson()
        every { dataManager.read(any(), any()) } returns json
        val vehicles = CachedVehicles.readFromLocal(middlewareComponent)
        verify { dataManager.read(eq("ALFAROMEO_PREPROD_testCustomerId_vehicles"), eq(APPLICATION)) }
        Assert.assertEquals(vehiclesResponse, vehicles)
    }

    @Test
    fun `when execute readFromLocal in unavailable case then return null`() {
        every { dataManager.read(any(), any()) } returns null
        val vehicles = CachedVehicles.readFromLocal(middlewareComponent)
        verify { dataManager.read(eq("ALFAROMEO_PREPROD_testCustomerId_vehicles"), eq(APPLICATION)) }
        Assert.assertNull(vehicles)
    }

    @Test
    fun `when execute readFromRemote then return vehiclesResponse`() {
        runTest {
            val vehicles = CachedVehicles.readFromRemote(middlewareComponent)
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(any()) }
            Assert.assertEquals(vehiclesResponse, vehicles)
        }
    }

    @Test
    fun `when execute getAll with action Refresh then return vehiclesResponse`() {
        coEvery { CachedVehicles.readFromRemote(middlewareComponent) } returns vehiclesResponse

        runTest {
            val vehicles = CachedVehicles.getAll(middlewareComponent, Refresh)
            coVerify { CachedVehicles.readFromRemote(middlewareComponent) }
            Assert.assertEquals(vehiclesResponse, vehicles)
        }
    }

    @Test
    fun `when execute getAll with default action then return vehiclesResponse`() {
        coEvery { CachedVehicles.readFromRemote(middlewareComponent) } returns vehiclesResponse

        runTest {
            val vehicles = CachedVehicles.getAll(middlewareComponent)
            coVerify { CachedVehicles.readFromRemote(middlewareComponent) }
            Assert.assertEquals(vehiclesResponse, vehicles)
        }
    }

    @Test
    fun `when execute getAll with action OnlyCache then return vehiclesResponse`() {
        coEvery {
            CachedVehicles.readFromLocal(middlewareComponent)
        } returns vehiclesResponse

        runTest {
            val vehicles = CachedVehicles.getAll(middlewareComponent, OnlyCache)
            coVerify { CachedVehicles.readFromLocal(middlewareComponent) }
            Assert.assertEquals(vehiclesResponse, vehicles)
        }
    }

    @Test
    fun `when execute getAll with action Get then return vehiclesResponse`() {
        coEvery {
            CachedVehicles.readFromLocal(middlewareComponent) ?: CachedVehicles.readFromRemote(middlewareComponent)
        } returns vehiclesResponse

        runTest {
            val vehicles = CachedVehicles.getAll(middlewareComponent, Get)
            coVerify {
                CachedVehicles.readFromLocal(middlewareComponent) ?: CachedVehicles.readFromRemote(middlewareComponent)
            }
            Assert.assertEquals(vehiclesResponse, vehicles)
        }
    }

    @Test
    fun `when execute getAll with action other than Refresh, OnlyCache and Get then throw exception`() {
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)

        runTest {
            try {
                CachedVehicles.getAll(middlewareComponent, Delete)
                coVerify { CachedVehicles.getAll(middlewareComponent, Delete) }
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when execute get with action Refresh then return vehicleResponse`() {
        coEvery {
            CachedVehicles.getAll(middlewareComponent, Refresh)
        } returns vehiclesResponse

        runTest {
            val vehicle = CachedVehicles.get(middlewareComponent, "testVin", Refresh)
            coVerify {
                CachedVehicles.getAll(middlewareComponent, Refresh)
            }
            Assert.assertEquals(vehicleResponse, vehicle)
        }
    }

    @Test
    fun `when execute get with action OnlyCache then return vehicleResponse`() {
        coEvery {
            CachedVehicles.getAll(middlewareComponent, OnlyCache)
        } returns vehiclesResponse

        runTest {
            val vehicle = CachedVehicles.get(middlewareComponent, "testVin", OnlyCache)
            coVerify {
                CachedVehicles.getAll(middlewareComponent, OnlyCache)
            }
            Assert.assertEquals(vehicleResponse, vehicle)
        }
    }

    @Test
    fun `when execute get with action Get then return vehicleResponse`() {
        coEvery {
            CachedVehicles.getAll(middlewareComponent, Get)
        } returns vehiclesResponse

        runTest {
            val vehicle = CachedVehicles.get(middlewareComponent, "testVin", Get)
            coVerify { CachedVehicles.getAll(middlewareComponent, Get) }
            Assert.assertEquals(vehicleResponse, vehicle)
        }
    }

    @Test
    fun `when execute get with default action then return vehicleResponse`() {
        coEvery {
            CachedVehicles.getAll(middlewareComponent)
        } returns vehiclesResponse

        runTest {
            val vehicle = CachedVehicles.get(middlewareComponent, "testVin")
            coVerify {
                CachedVehicles.getAll(middlewareComponent)
            }
            Assert.assertEquals(vehicleResponse, vehicle)
        }
    }

    @Test
    fun `when execute get with action other than Refresh, OnlyCache and Get then throw exception`() {
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        coEvery {
            CachedVehicles.getAll(middlewareComponent, Delete)
        } returns vehiclesResponse

        runTest {
            try {
                CachedVehicles.get(middlewareComponent, "testVin", Delete)
                coVerify { CachedVehicles.getAll(middlewareComponent, Delete) }
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when execute getOrThrow with action Refresh then return vehicleResponse`() {
        coEvery {
            CachedVehicles.getAll(middlewareComponent, Refresh)
        } returns vehiclesResponse

        runTest {
            val vehicle = CachedVehicles.getOrThrow(middlewareComponent, "testVin", Refresh)
            coVerify { CachedVehicles.getAll(middlewareComponent, Refresh) }
            Assert.assertEquals(vehicleResponse, vehicle)
        }
    }

    @Test
    fun `when execute getOrThrow with action OnlyCache then return vehicleResponse`() {
        coEvery {
            CachedVehicles.getAll(middlewareComponent, OnlyCache)
        } returns vehiclesResponse

        runTest {
            val vehicle = CachedVehicles.getOrThrow(middlewareComponent, "testVin", OnlyCache)
            coVerify { CachedVehicles.getAll(middlewareComponent, OnlyCache) }
            Assert.assertEquals(vehicleResponse, vehicle)
        }
    }

    @Test
    fun `when execute getOrThrow with action Get then return vehicleResponse`() {
        coEvery {
            CachedVehicles.getAll(middlewareComponent, Get)
        } returns vehiclesResponse

        runTest {
            val vehicle = CachedVehicles.getOrThrow(middlewareComponent, "testVin", Get)
            coVerify { CachedVehicles.getAll(middlewareComponent, Get) }
            Assert.assertEquals(vehicleResponse, vehicle)
        }
    }

    @Test
    fun `when execute getOrThrow with default action then return vehicleResponse`() {
        coEvery {
            CachedVehicles.getAll(middlewareComponent)
        } returns vehiclesResponse

        runTest {
            val vehicle = CachedVehicles.getOrThrow(middlewareComponent, "testVin")
            coVerify { CachedVehicles.getAll(middlewareComponent) }
            Assert.assertEquals(vehicleResponse, vehicle)
        }
    }

    @Test
    fun `when execute getOrThrow with invalid vin then return exception`() {
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        val vin = "123"
        coEvery {
            CachedVehicles.getAll(middlewareComponent, Get)
        } returns null

        runTest {
            try {
                CachedVehicles.getOrThrow(middlewareComponent, vin, action = Get)
                coVerify { CachedVehicles.getAll(middlewareComponent, Get) }
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }
}
