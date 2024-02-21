package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.ServiceSchedulerToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class BaseNaftaTokenDealerFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: TestBaseNaftaTokenDealerFcaExecutor
    private val token = "testToken"
    private val tokenResponse = NetworkResponse.Success(ServiceSchedulerToken(token, 100000, "Bearer"))
    private val input = OssTokenInput(
        dealerId = "7019500"
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(NaftaTokenManager::class)
        every { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) } returns token
        coEvery { anyConstructed<NaftaTokenManager>().refreshedToken(any(), any()) } returns tokenResponse
        every { anyConstructed<NaftaTokenManager>().isTokenUnauthorized<Unit>(any()) } returns false

        executor = spyk(TestBaseNaftaTokenDealerFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when ossDealer = null on Cash then call getRefreshedToken`() {
        every { executor.params(any()) } returns input

        runTest {
            val result = executor.execute()
            coVerify(exactly = 1) { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) }
            Assert.assertTrue(result is NetworkResponse.Success)
            val success = result as NetworkResponse.Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute header then add authorisation header`() {
        val headers = executor.headers("testToken")
        Assert.assertEquals("testToken", headers["dealer-authorization"])
    }

    private class TestBaseNaftaTokenDealerFcaExecutor(
        middlewareComponent: MiddlewareComponent,
        params: Map<String, Any?>? = null
    ) : BaseNaftaTokenDealerFcaExecutor<OssTokenInput, Unit>(middlewareComponent, params) {

        override fun params(parameters: Map<String, Any?>?): OssTokenInput = OssTokenInput(
            dealerId = "7019500"
        )

        override suspend fun execute(input: OssTokenInput, token: String): NetworkResponse<Unit> =
            NetworkResponse.Success(Unit)
    }
}
