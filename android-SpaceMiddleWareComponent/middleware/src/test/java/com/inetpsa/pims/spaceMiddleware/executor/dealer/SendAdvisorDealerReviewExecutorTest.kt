package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.AdvisorDealerReviewConfiguration
import com.inetpsa.pims.spaceMiddleware.model.dealer.SendAdvisorDealerReviewPsaParams
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

internal class SendAdvisorDealerReviewExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: SendAdvisorDealerReviewPsaExecutor

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
        executor = spyk(SendAdvisorDealerReviewPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val input = SendAdvisorDealerReviewPsaParams(
            vin = "testVin",
            siteGeo = "testSiteGeo",
            rating = "5",
            comment = "testComment",
            serviceType = "APV",
            sendConfirmEmail = "testEmail",
            serviceDate = "20/12/2022"
        )

        every { executor.params(any()) } returns input

        coEvery { communicationManager.post<AdvisorDealerReviewConfiguration>(any(), any()) } returns
            NetworkResponse.Success(advisorDealerReviewConfiguration)

        runBlockingTest {
            val response = executor.execute()
            val body = mapOf(
                "comment" to "testComment",
                "rating" to "5",
                "sendConfirmEmail" to "testEmail",
                "site_geo" to "testSiteGeo",
                "serviceDate" to "20/12/2022"
            ).toJson()
            val queries = mapOf(
                "serviceType" to "APV"
            )
            verify {
                executor.request(
                    type = eq(AdvisorDealerReviewConfiguration::class.java),
                    urls = eq(arrayOf("/shop/v1/reviews/service/perform/", input.vin)),
                    headers = any(),
                    queries = eq(queries),
                    body = eq(body)
                )
            }

            coVerify {
                communicationManager.post<AdvisorDealerReviewConfiguration>(
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
        val params = SendAdvisorDealerReviewPsaParams(
            vin = "testVin",
            siteGeo = "testSiteGeo",
            rating = "5",
            comment = "testComment",
            serviceType = "APV",
            sendConfirmEmail = "testEmail",
            serviceDate = "20/12/2022"
        )

        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.PARAM_SITE_GEO to "testSiteGeo",
            Constants.BODY_PARAM_RATING to "5",
            Constants.BODY_PARAM_COMMENT to "testComment",
            Constants.BODY_PARAM_SERVICE_TYPE to "APV",
            Constants.BODY_PARAM_SEND_CONFIRM_EMAIL to "testEmail",
            Constants.BODY_PARAM_SERVICE_DATE to "20/12/2022"
        )

        val output = executor.params(input)

        Assert.assertEquals(params, output)
    }
}
