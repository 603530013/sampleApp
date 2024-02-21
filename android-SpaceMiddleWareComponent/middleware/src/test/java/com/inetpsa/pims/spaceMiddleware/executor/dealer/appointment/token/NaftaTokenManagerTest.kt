package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.ServiceSchedulerToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.net.HttpURLConnection
import java.time.LocalDateTime

internal class NaftaTokenManagerTest : FcaExecutorTestHelper() {

    private lateinit var manager: NaftaTokenManager
    private val input = OssTokenInput("8356")

    override fun setup() {
        super.setup()
        mockkObject(BookingOnlineCache)
        mockkConstructor(NaftaTokenManager::class)
        manager = spyk(NaftaTokenManager())
    }

    @Test
    fun removeAll() {
        manager.removeAll()
        verify(exactly = 1) { BookingOnlineCache.clear() }
    }

    @Test
    fun remove() {
        manager.remove(input)
        verify(exactly = 1) { BookingOnlineCache.removeToken(input) }
    }

    @Test
    fun updateToken() {
        val response = ServiceSchedulerToken("testAccessToken", 1000, "bearer")
        manager.updateToken(input, response)
        val data = OssTokenCache(response.accessToken, LocalDateTime.now().plusSeconds(response.expiresIn))
        verify(exactly = 1) { BookingOnlineCache.write(input, data) }
    }

    @Test
    fun isTokenUnauthorized() {
        var error = PIMSFoundationError.serverError(HttpURLConnection.HTTP_UNAUTHORIZED, "Token expired")
        var failureResponse = NetworkResponse.Failure(error)
        Assert.assertTrue(manager.isTokenUnauthorized(failureResponse))

        error = PIMSFoundationError.serverError(HttpURLConnection.HTTP_NOT_FOUND, "page not found")
        failureResponse = NetworkResponse.Failure(error)
        Assert.assertFalse(manager.isTokenUnauthorized(failureResponse))

        val successResponse = NetworkResponse.Success(Unit)
        Assert.assertFalse(manager.isTokenUnauthorized(successResponse))
    }

    @Test
    fun refreshedToken() {
        mockkConstructor(GetOssTokenFcaExecutor::class)
        coEvery { anyConstructed<GetOssTokenFcaExecutor>().execute(any()) } returns NetworkResponse.Success(
            ServiceSchedulerToken(accessToken = "turpis", expiresIn = 4079, tokenType = "possim")
        )
        runTest {
            manager.refreshedToken(middlewareComponent, input)
            coVerify(exactly = 1) { anyConstructed<GetOssTokenFcaExecutor>().execute(any()) }
        }
    }

    @Test
    fun getOssTokenFromCache() {
        every { BookingOnlineCache.readToken(input) } returns null
        Assert.assertNull(manager.getOssTokenFromCache(input))

        val expiredCache = OssTokenCache(
            "testToken",
            LocalDateTime.now()
        )
        every { BookingOnlineCache.readToken(input) } returns expiredCache
        val data = manager.getOssTokenFromCache(input)
        Assert.assertNull(data)
    }

    @Test
    fun `when getOssTokenFromCache then return token response`() {
        val notExpiredCache = OssTokenCache(
            "testToken",
            LocalDateTime.now().plusMonths(1)
        )
        every { BookingOnlineCache.readToken(input) } returns notExpiredCache
        val data = manager.getOssTokenFromCache(input)
        Assert.assertEquals("testToken", data)
    }

    @Test
    fun withOssToken() {
        runTest {
            every { manager.getOssTokenFromCache(input) } returns null
            coEvery { manager.refreshedToken(middlewareComponent, input) } returns NetworkResponse.Success(
                ServiceSchedulerToken(accessToken = "ultricies", expiresIn = 2458, tokenType = "mei")
            )
            val response = manager.withOssToken(middlewareComponent, input) { _, _ ->
                NetworkResponse.Success(Unit)
            }
            Assert.assertEquals(NetworkResponse.Success(Unit), response)
        }
    }
}
