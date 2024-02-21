package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Deprecated("This will be replaced by AddFavoriteDealerPsaExecutorTest")
internal class EnrollPreferredDealerPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: EnrollPreferredDealerPsaExecutor

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(EnrollPreferredDealerPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a post API call with the right response`() {
        val input = "testSiteGeo"
        every { executor.params(any()) } returns input

        coEvery { communicationManager.post<String>(any(), any()) } returns Success(
            "Favourite dealer successfully added!"
        )

        runTest {
            val response = executor.execute()
            val body = mapOf(Constants.PARAM_SITE_GEO to input).toJson()

            verify {
                executor.request(
                    type = eq(String::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/favorite_dealer")),
                    headers = any(),
                    queries = any(),
                    body = eq(body)
                )
            }

            coVerify {
                communicationManager.post<String>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute then make a post API call with wrong response`() {
        val input = "testSiteGeo"
        every { executor.params(any()) } returns input

        coEvery { communicationManager.post<String>(any(), any()) } returns Success(
            "Favorite dealer successfully added!"
        )

        runTest {
            val response = executor.execute()
            val body = mapOf(Constants.PARAM_SITE_GEO to input).toJson()

            verify {
                executor.request(
                    type = eq(String::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/favorite_dealer")),
                    headers = any(),
                    queries = any(),
                    body = eq(body)
                )
            }

            coVerify {
                communicationManager.post<String>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Failure)
            val failure = response as Failure
            val error = PimsErrors.serverError(
                null,
                "error occurred when adding preferred dealer Favorite dealer successfully added!"
            )
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
        }
    }

    @Test
    fun `when execute params with the right input then return siteGeo`() {
        val siteGeo = "testSiteGeo"

        val input = mapOf(
            Constants.PARAM_SITE_GEO to siteGeo
        )
        val output = executor.params(input)

        Assert.assertEquals(siteGeo, output)
    }

    @Test
    fun `when execute params with missing siteGeo then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_SITE_GEO)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid siteGeo then throw missing parameter`() {
        val siteGeo = 123
        val input = mapOf(Constants.PARAM_SITE_GEO to siteGeo)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_SITE_GEO)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
