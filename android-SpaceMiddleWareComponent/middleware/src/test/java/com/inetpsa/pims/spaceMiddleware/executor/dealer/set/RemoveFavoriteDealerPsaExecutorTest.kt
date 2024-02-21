package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants.Storage
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
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

internal class RemoveFavoriteDealerPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: RemoveFavoriteDealerPsaExecutor

    @Before
    override fun setup() {
        super.setup()
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.DataManagerExtensionsKt")
        executor = spyk(RemoveFavoriteDealerPsaExecutor(baseCommand))
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        userSessionManager.getUserSession()?.customerId
        every { middlewareComponent.dataManager } returns dataManager
    }

    @Test
    fun `when execute then make a get API call with the right response`() {
        coEvery { middlewareComponent.deleteSync(any(), any()) } returns true
        coJustRun { executor.removeDealerFromCache() }
        coEvery { communicationManager.delete<String>(any(), any()) } returns NetworkResponse.Success(
            "successfully deleted!"
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(String::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/favorite_dealer")),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.delete<String>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            coVerify(exactly = 1) { executor.removeDealerFromCache() }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute then make a get API call with the wrong response`() {
        coEvery { communicationManager.delete<String>(any(), any()) } returns NetworkResponse.Success(
            "not successfully deleted!"
        )
        val error = PIMSFoundationError.unknownError
        coEvery { communicationManager.post<String>(any(), any()) } returns NetworkResponse.Failure(error)
        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(String::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/favorite_dealer")),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.delete<String>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            Assert.assertEquals(true, response is Failure)
            val failure = response as Failure
            Assert.assertEquals(true, failure.equals(response))
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
    fun `when removeFavoriteDealer then we add favorite in cache`() {
        runTest {
            coEvery { middlewareComponent.deleteSync(any(), any()) } returns true
            executor.removeDealerFromCache()
            coVerify {
                middlewareComponent.deleteSync(
                    eq(Storage.PREFERRED_DEALER_ID),
                    eq(StoreMode.APPLICATION)
                )
            }
        }
    }
}
