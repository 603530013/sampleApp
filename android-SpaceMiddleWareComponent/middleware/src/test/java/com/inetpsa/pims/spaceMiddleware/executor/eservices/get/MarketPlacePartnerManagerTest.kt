package com.inetpsa.pims.spaceMiddleware.executor.eservices.get

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.partners.GetMarketPlacePartnersFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.CustomExtension
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.MarketPlacePartnerResponse.DeepLinksItem
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class MarketPlacePartnerManagerTest {

    private val marketPlacePartnerResponse = MarketPlacePartnerResponse(
        customExtension = CustomExtension(
            deepLinks = listOf(
                DeepLinksItem(key = "BOOK_PREFERRED_CHARGING_STATION"),
                DeepLinksItem(key = "OWNERS_MANUAL")
            )
        ),
        allowedBrands = null,
        partnerBackgroundColor = "partnerBackgroundColor",
        partnerName = "partnerName",
        section = "section",
        allowedFuelTypes = null,
        isConsentApplicable = true,
        consents = null,
        partnerID = 1,
        serviceID = "CSL serviceID",
        partnerImage = "partnerImage",
        partnerThumbnail = "partnerThumbnail",
        contextHelp = "contextHelp",
        allowedMvs = null,
        partnerStackedLogo = "partnerStackedLogo",
        partnerStatus = "active"
    )

    private lateinit var marketPlacePartnerManager: MarketPlacePartnerManager
    private val middlewareComponent: MiddlewareComponent = mockk()

    @Before
    fun setUp() {
        marketPlacePartnerManager = MarketPlacePartnerManager()
    }

    @Test
    fun `test fetchChargingStationLocator() filters the response list properly`() {
        mockkConstructor(GetMarketPlacePartnersFcaExecutor::class)
        coEvery {
            GetMarketPlacePartnersFcaExecutor(middlewareComponent, null)
                .execute(UserInput(action = Get, vin = "testVin"))
        } returns Success(listOf(marketPlacePartnerResponse))

        runBlocking {
            val result = marketPlacePartnerManager.fetchChargingStationLocator(
                middlewareComponent,
                vin = "testVin"
            )
            assertEquals(listOf(marketPlacePartnerResponse), result)
        }
    }

    @Test
    fun `test fetchChargingStationLocator() filters the response`() {
        val testMarketPlace = MarketPlacePartnerResponse(
            partnerStatus = "active",
            customExtension = CustomExtension(
                deepLinks = listOf(
                    DeepLinksItem(key = "BOOK_PREFERRED_CHARGING_STATION"),
                    DeepLinksItem(key = "OWNERS_MANUAL")
                )
            )
        )
        val testResponse = listOf(
            marketPlacePartnerResponse,
            MarketPlacePartnerResponse(),
            MarketPlacePartnerResponse(partnerStatus = "notActive"),
            testMarketPlace
        )
        mockkConstructor(GetMarketPlacePartnersFcaExecutor::class)
        coEvery {
            GetMarketPlacePartnersFcaExecutor(middlewareComponent, null)
                .execute(UserInput(action = Get, vin = "testVin"))
        } returns Success(testResponse)

        runBlocking {
            val result = marketPlacePartnerManager.fetchChargingStationLocator(
                middlewareComponent,
                vin = "testVin"
            )
            assert(result?.size == 2)
            assertTrue(result?.contains(testMarketPlace) ?: false)
            assertTrue(result?.contains(marketPlacePartnerResponse) ?: false)
            assertFalse(result?.contains(MarketPlacePartnerResponse()) ?: false)
        }
    }

    @Test
    fun `test partnerIsActive()`() {
        with(marketPlacePartnerManager) {
            assertTrue(partnerIsActive(marketPlacePartnerResponse))
            assertFalse(partnerIsActive(MarketPlacePartnerResponse()))
            assertFalse(partnerIsActive(MarketPlacePartnerResponse(partnerStatus = "notActive")))
        }
    }

    @Test
    fun `test hasCSLServiceID()`() {
        with(marketPlacePartnerManager) {
            assertTrue(hasCSLServiceID(marketPlacePartnerResponse))
            assertFalse(hasCSLServiceID(MarketPlacePartnerResponse()))
            assertFalse(hasCSLServiceID(MarketPlacePartnerResponse(serviceID = "serviceID")))
        }
    }

    @Test
    fun `test supportDeeplink()`() {
        val marketPlacePartnerResponse1 = MarketPlacePartnerResponse(
            customExtension = CustomExtension(
                deepLinks = listOf(DeepLinksItem(key = "test key"))
            )
        )
        val marketPlacePartnerResponse2 = MarketPlacePartnerResponse(
            customExtension = CustomExtension(
                deepLinks = listOf(DeepLinksItem(key = "MASERATI_CHARGING_STATION"))
            )
        )
        with(marketPlacePartnerManager) {
            assertTrue(supportDeeplink(marketPlacePartnerResponse))
            assertTrue(supportDeeplink(marketPlacePartnerResponse2))
            assertFalse(supportDeeplink(MarketPlacePartnerResponse()))
            assertFalse(supportDeeplink(marketPlacePartnerResponse1))
        }
    }

    @Test
    fun `test supportOwnerManual`() {
        val marketPlacePartnerResponse1 = MarketPlacePartnerResponse(
            customExtension = CustomExtension(
                deepLinks = listOf(DeepLinksItem(key = "test key"))
            )
        )
        with(marketPlacePartnerManager) {
            assertTrue(supportOwnerManual(marketPlacePartnerResponse))
            assertFalse(supportOwnerManual(MarketPlacePartnerResponse()))
            assertFalse(supportOwnerManual(marketPlacePartnerResponse1))
        }
    }

    @Test
    fun `test fetchOwnerManual() with partnerIsActive=true and supportOwnerManual=true returns same response`() {
        val testResponse = listOf(marketPlacePartnerResponse)
        val result = marketPlacePartnerManager.fetchOwnerManual(testResponse)
        assertEquals(testResponse, result)
    }

    @Test
    fun `test fetchOwnerManual() with partnerIsActive=false and supportOwnerManual=true filters response`() {
        val testResponse = listOf(
            MarketPlacePartnerResponse(
                partnerStatus = "notActive",
                customExtension = CustomExtension(
                    deepLinks = listOf(
                        DeepLinksItem(key = "BOOK_PREFERRED_CHARGING_STATION"),
                        DeepLinksItem(key = "OWNERS_MANUAL")
                    )
                )
            ),
            MarketPlacePartnerResponse(
                partnerStatus = "active",
                customExtension = CustomExtension(
                    deepLinks = listOf(
                        DeepLinksItem(key = "BOOK_PREFERRED_CHARGING_STATION"),
                        DeepLinksItem(key = "OWNERS_MANUAL")
                    )
                )
            )
        )
        val result = marketPlacePartnerManager.fetchOwnerManual(testResponse)
        result?.let {
            assertTrue(marketPlacePartnerManager.supportOwnerManual(result[0]))
        }
        assert(result?.size == 1)
    }

    @Test
    fun `test getDeepLinkSupportedPartners() with supportDeeplink=true`() {
        val result = marketPlacePartnerManager.getDeepLinkSupportedPartners(listOf(marketPlacePartnerResponse))
        assertEquals(marketPlacePartnerResponse, result)
    }

    @Test
    fun `test getDeepLinkSupportedPartners() returns first response which has supportDeeplink=true`() {
        val marketPlacePartnerResponse1 = MarketPlacePartnerResponse(
            customExtension = CustomExtension(
                deepLinks = listOf(DeepLinksItem(key = "test key"))
            )
        )
        val marketPlacePartnerResponse2 = MarketPlacePartnerResponse(
            customExtension = CustomExtension(
                deepLinks = listOf(DeepLinksItem(key = "MASERATI_CHARGING_STATION"))
            )
        )
        val result = marketPlacePartnerManager.getDeepLinkSupportedPartners(
            listOf(
                marketPlacePartnerResponse1,
                marketPlacePartnerResponse2,
                marketPlacePartnerResponse
            )
        )
        assertEquals(marketPlacePartnerResponse2, result)
    }
}
