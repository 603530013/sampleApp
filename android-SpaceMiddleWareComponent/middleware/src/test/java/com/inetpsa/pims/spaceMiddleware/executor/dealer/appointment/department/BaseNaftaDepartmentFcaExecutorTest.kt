package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.token.NaftaTokenManager
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class BaseNaftaDepartmentFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: BaseNaftaExecutor
    private val token = "testToken"
    private val departmentId = 7658

    private val input = OssTokenInput(
        dealerId = "7019500"
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(NaftaTokenManager::class)
        mockkConstructor(GetNaftaDepartmentIdExecutor::class)
        every { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) } returns token
        every { anyConstructed<NaftaTokenManager>().isTokenUnauthorized<Unit>(any()) } returns false
        coEvery { anyConstructed<GetNaftaDepartmentIdExecutor>().execute(any()) } returns Success(departmentId)
        coEvery { anyConstructed<GetNaftaDepartmentIdExecutor>().execute(any(), any()) } returns Success(departmentId)

        executor = spyk(BaseNaftaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when ossDealer = null on Cash then call getRefreshedToken`() {
        every { executor.params(any()) } returns input

        runTest {
            executor.execute()
        }
    }

    @Test
    fun `when call execute method return success response`() {
        val input = OssTokenInput(
            dealerId = "7019500"
        )
        every { executor.params(any()) } returns input

        runTest {
            coEvery {
                executor.execute(input, token, departmentId.toString())
            } returns Success("testResponseSuccess")

            val response = executor.execute()
            coVerify { anyConstructed<NaftaTokenManager>().getOssTokenFromCache(any()) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals("testResponseSuccess", success.response)
        }
    }

    @Test
    fun `when execute is called with valid input and token, it should return Success response`() {
        val input = OssTokenInput(
            dealerId = "7019500"
        )
        val token = "testToken"
        val expectedResponse = "testResponseSuccess"
        coEvery {
            anyConstructed<GetNaftaDepartmentIdExecutor>().execute(any(), any())
        } returns Success(departmentId)

        runTest {
            val response = executor.execute(input, token)
            coVerify { anyConstructed<GetNaftaDepartmentIdExecutor>().execute(any()) }
            coVerify { executor.execute(input, token, departmentId.toString()) }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(expectedResponse, success.response)
        }
    }

    private class BaseNaftaExecutor(middleware: MiddlewareComponent, params: Map<String, Any?>? = null) :
        BaseNaftaDepartmentFcaExecutor<OssTokenInput, String>(middleware, params) {

        override suspend fun execute(
            input: OssTokenInput,
            token: String,
            departmentId: String
        ): NetworkResponse<String> {
            return Success("testResponseSuccess")
        }

        override fun params(parameters: Map<String, Any?>?): OssTokenInput {
            return OssTokenInput(
                dealerId = "7019500"
            )
        }
    }
}
