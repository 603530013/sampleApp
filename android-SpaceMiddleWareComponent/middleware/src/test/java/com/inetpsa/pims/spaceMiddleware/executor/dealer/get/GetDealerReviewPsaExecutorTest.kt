package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.GetReviewInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.GetReviewOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.ReviewResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetDealerReviewPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetDealerReviewPsaExecutor

    private val response = ReviewResponse(
        success = true,
        status = 200,
        vehicleIdType = "VIN",
        vinMask = "^[A-Za-z0-9]{17}\$",
        reviewMaxDate = "120",
        reviewMaxMonth = "2",
        reviewMinDelta = "72",
        reviewMaxChar = "500",
        ratingNegativeFloor = "3",
        cguLink = "https://www.citroen-advisor.fr/conditions",
        allowed = true
    )
    private val result = GetReviewOutput(
        reviewMaxDate = "120",
        reviewMaxMonth = "2",
        reviewMinDelta = "72",
        reviewMaxChar = "500",
        ratingNegativeFloor = "3",
        cguLink = "https://www.citroen-advisor.fr/conditions",
        allowed = true
    )

    @Before
    override fun setup() {
        super.setup()
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        userSessionManager.getUserSession()?.customerId
        executor = spyk(GetDealerReviewPsaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute then make a get API call`() {
        val input = GetReviewInput(
            vin = "testVin",
            vehicleIdType = "VIN",
            serviceType = "APV"
        )
        every { executor.params(any()) } returns input
        coEvery { communicationManager.get<ReviewResponse>(any(), any()) } returns
            NetworkResponse.Success(response)
        coJustRun { executor.saveOnCache(any()) }

        runTest {
            val responseExecutor = executor.execute()

            verify {
                executor.request(
                    type = eq(ReviewResponse::class.java),
                    urls = eq(arrayOf("/shop/v1/reviews/service/settings/", input.vin)),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<ReviewResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            coVerify(exactly = 1) { executor.saveOnCache(eq(response)) }

            Assert.assertEquals(true, responseExecutor is NetworkResponse.Success)
            val success = responseExecutor as NetworkResponse.Success
            Assert.assertEquals(result, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return DealerReviewInputParams`() {
        val params = GetReviewInput(
            vin = "testVin",
            vehicleIdType = "VIN",
            serviceType = "APV"
        )

        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.BODY_PARAM_VEHICLE_ID_TYPE to "VIN",
            Constants.BODY_PARAM_SERVICE_TYPE to "APV"
        )

        val output = executor.params(input)

        Assert.assertEquals(params, output)
    }

    @Test
    fun `when execute save then save in cache`() {
        every {
            dataManager.create(
                key = any(),
                data = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }

        runTest { executor.saveOnCache(response) }

        verify {
            dataManager.create(
                key = eq("PEUGEOT_PREPROD_testCustomerId_preferredDealer"),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }
}
