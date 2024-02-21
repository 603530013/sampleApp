package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.AdvisorDealerReviewConfiguration
import com.inetpsa.pims.spaceMiddleware.model.dealer.GetAdvisorDealerReviewConfigurationPsaParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
@Deprecated("This will be replaced by GetDealerReviewPsaExecutorTest")
internal class GetAdvisorDealerReviewConfigurationExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetAdvisorDealerReviewConfigurationPsaExecutor

    private val advisorDealerReviewConfiguration = AdvisorDealerReviewConfiguration(
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

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetAdvisorDealerReviewConfigurationPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val input = GetAdvisorDealerReviewConfigurationPsaParams(
            vin = "testVin",
            vehicleIdType = "VIN",
            serviceType = "APV"
        )
        every { executor.params(any()) } returns input

        coEvery { communicationManager.get<AdvisorDealerReviewConfiguration>(any(), any()) } returns
            NetworkResponse.Success(advisorDealerReviewConfiguration)

        runBlockingTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(AdvisorDealerReviewConfiguration::class.java),
                    urls = eq(arrayOf("/shop/v1/reviews/service/settings/", input.vin)),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<AdvisorDealerReviewConfiguration>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(advisorDealerReviewConfiguration, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return GetAdvisorDealerReviewConfigurationPsaParams`() {
        val params = GetAdvisorDealerReviewConfigurationPsaParams(
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
}
