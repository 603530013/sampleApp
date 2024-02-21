package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Storage
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class AddFavoriteDealerPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: AddFavoriteDealerPsaExecutor

    @Before
    override fun setup() {
        super.setup()
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.DataManagerExtensionsKt")
        executor = spyk(AddFavoriteDealerPsaExecutor(baseCommand))
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        userSessionManager.getUserSession()?.customerId
        every { middlewareComponent.dataManager } returns dataManager
    }

    @Test
    fun `when execute then make a post API call with the right response`() {
        val input = "testSiteGeo"
        every { executor.params(any()) } returns input
        coEvery { middlewareComponent.deleteSync(any(), any()) } returns true
        coJustRun { executor.removeDealerFromCache() }
        coJustRun { executor.saveDealerId(input) }
        coEvery {
            communicationManager.post<String>(any(), any())
        } returns Success(AddFavoriteDealerPsaExecutor.RESPONSE_SUCCESSFULLY)

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
            coVerify(exactly = 1) { executor.removeDealerFromCache() }
            coVerify(exactly = 1) { executor.saveDealerId(input) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute then make a post API call with wrong response`() {
        val input = "testSiteGeo"
        every { executor.params(any()) } returns input
        val reason = "Error occurred when adding favorite dealer: Error occurred when adding favorite dealer..!"
        val error = PIMSFoundationError.unknownError(reason)
        coEvery { communicationManager.post<String>(any(), any()) } returns Success(
            "Error occurred when adding favorite dealer..!"
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
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
        }
    }

    @Test
    fun `when execute params with the right input then return siteGeo`() {
        val siteGeo = "testSiteGeo"

        val input = mapOf(
            Constants.Input.ID to siteGeo
        )
        val output = executor.params(input)

        Assert.assertEquals(siteGeo, output)
    }

    @Test
    fun `when execute params with missing siteGeo then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ID)
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
        val input = mapOf(Constants.Input.ID to siteGeo)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when removeFromVehicleCache then we remove from dataManager`() {
        runTest {
            val favDealer = "preferredDealer"
            coEvery { middlewareComponent.deleteSync(any(), any()) } returns true
            executor.removeDealerFromCache()
            coVerify {
                middlewareComponent.deleteSync(
                    eq(favDealer),
                    eq(APPLICATION)
                )
            }
        }
    }

    @Test
    fun `when saveFavoriteDealer then we add favorite in cache`() {
        runTest {
            val favDealer = "preferredDealer"
            coEvery { middlewareComponent.createSync(any(), any(), any()) } returns true
            executor.saveDealerId(favDealer)
            coVerify {
                middlewareComponent.createSync(
                    eq(Storage.PREFERRED_DEALER_ID),
                    any(),
                    eq(StoreMode.APPLICATION)
                )
            }
        }
    }
}
