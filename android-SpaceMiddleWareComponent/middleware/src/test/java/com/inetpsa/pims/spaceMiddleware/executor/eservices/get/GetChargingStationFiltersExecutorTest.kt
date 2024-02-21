package com.inetpsa.pims.spaceMiddleware.executor.eservices.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.CustomExtension
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.DeepLinksItem
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider.BOSCH
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider.F2M
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider.TOMTOM
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.AcceptablePayment
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Access
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.AccessTypes
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.ConnectorTypes
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.PowerType
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.SpecialRestriction
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargeStationFiltersOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargeStationFiltersOutput.FilterItem
import com.inetpsa.pims.spaceMiddleware.model.vehicles.filters.ChargingStationFilters
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetChargingStationFiltersExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetChargingStationFiltersExecutor
    private val vehicleResponse: VehicleResponse = mockk()

    private val f2mOutput = ChargeStationFiltersOutput(
        filters = listOf(
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_CONNECTOR_TYPES,
                type = ChargingStationFilters.LIST_TYPE,
                data = ConnectorTypes.values().map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_POWER_TYPES,
                type = ChargingStationFilters.LIST_TYPE,
                data = PowerType.values().filter { it != PowerType.SLOW_CHARGE }.map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_ACCESS,
                type = ChargingStationFilters.LIST_TYPE,
                data = Access.values().filter { it != Access.PLUG_AND_CHARGE }.map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_OPEN_24_HOURS,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = ChargingStationFilters.F2M.KEY_FILTER_CHARGING_CABLE_ATTACHED,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),

            FilterItem(
                key = ChargingStationFilters.F2M.KEY_FILTER_FREE,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),

            FilterItem(
                key = ChargingStationFilters.F2M.KEY_FILTER_INDOOR,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            )
        ),
        hasPartner = true
    )

    private val tomTomOutput = ChargeStationFiltersOutput(
        filters = listOf(
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_CONNECTOR_TYPES,
                type = ChargingStationFilters.LIST_TYPE,
                data = ConnectorTypes.values().map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_POWER_TYPES,
                type = ChargingStationFilters.LIST_TYPE,
                data = PowerType.values().filter { it != PowerType.SLOW_CHARGE }.map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_ACCESS,
                type = ChargingStationFilters.LIST_TYPE,
                data = Access.values().filter { it != Access.PLUG_AND_CHARGE }.map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_OPEN_24_HOURS,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = ChargingStationFilters.Tomtom.KEY_FILTER_ONLY_AVAILABLE,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = ChargingStationFilters.Tomtom.KEY_FILTER_PARTNER_ONLY,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = ChargingStationFilters.Tomtom.KEY_FILTER_MINIMUM_AVAILABLE,
                type = ChargingStationFilters.INTEGER_TYPE,
                data = null
            ),
            FilterItem(
                key = ChargingStationFilters.Tomtom.KEY_FILTER_COMPATIBLE_ONLY,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = ChargingStationFilters.Tomtom.KEY_FILTER_ACCESS_TYPES,
                type = ChargingStationFilters.LIST_TYPE,
                data = AccessTypes.values().map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.Tomtom.KEY_FILTER_ACCEPTABLE_PAYMENTS,
                type = ChargingStationFilters.LIST_TYPE,
                data = AcceptablePayment.values().map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.Tomtom.KEY_FILTER_SPECIAL_RESTRICTIONS,
                type = ChargingStationFilters.LIST_TYPE,
                data = SpecialRestriction.values().map { it.name }
            )
        ),
        hasPartner = true
    )

    private val boschOutput = ChargeStationFiltersOutput(
        filters = listOf(
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_CONNECTOR_TYPES,
                type = ChargingStationFilters.LIST_TYPE,
                data = ConnectorTypes.values().map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_POWER_TYPES,
                type = ChargingStationFilters.LIST_TYPE,
                data = PowerType.values().map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_ACCESS,
                type = ChargingStationFilters.LIST_TYPE,
                data = Access.values().map { it.name }
            ),
            FilterItem(
                key = ChargingStationFilters.KEY_FILTER_OPEN_24_HOURS,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = ChargingStationFilters.Bosch.KEY_FILTER_RENEWABLE_ENERGY,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            ),
            FilterItem(
                key = ChargingStationFilters.Bosch.KEY_FILTER_OPEN_ONLY,
                type = ChargingStationFilters.BOOLEAN_TYPE,
                data = null
            )
        ),
        hasPartner = true
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

    private val noneOutput: ChargeStationFiltersOutput? = null

    @Before
    override fun setup() {
        super.setup()

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

        mockkObject(CachedVehicles)
        executor = spyk(GetChargingStationFiltersExecutor(baseCommand))
        every { vehicleResponse.channelFeatures } returns null
    }

    @Test
    fun `when execute params with missing VIN then throw missing parameter`() {
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
    fun `when execute params with invalid VIN then throw missing parameter`() {
        val input = mapOf(Constants.PARAM_VIN to true)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with cslProvider F2M then return f2mOutput`() {
        every { executor.params(any()) } returns "testVin"
        every { vehicleResponse.cslProvider } returns F2M
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse

        runTest {
            val response = executor.execute()

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(f2mOutput, success.response)
        }
    }

    @Test
    fun `when execute with cslProvider TOMTOM then return tomTomOutput`() {
        every { executor.params(any()) } returns "testVin"
        every { vehicleResponse.cslProvider } returns TOMTOM
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse

        runTest {
            val response = executor.execute()

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(tomTomOutput, success.response)
        }
    }

    @Test
    fun `when execute with cslProvider BOSCH then return boschOutput`() {
        every { executor.params(any()) } returns "testVin"
        every { vehicleResponse.cslProvider } returns BOSCH
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse

        runTest {
            val response = executor.execute()

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(boschOutput, success.response)
        }
    }

    @Test
    fun `when execute with cslProvider unknown then return without filter`() {
        every { executor.params(any()) } returns "testVin"
        every { vehicleResponse.cslProvider } returns CslProvider.NONE
        coEvery { CachedVehicles.getOrThrow(any(), any()) } returns vehicleResponse

        runTest {
            val response = executor.execute()

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(noneOutput, success.response)
        }
    }
}
