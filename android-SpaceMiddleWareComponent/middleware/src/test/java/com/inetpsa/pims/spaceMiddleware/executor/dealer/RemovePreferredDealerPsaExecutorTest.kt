package com.inetpsa.pims.spaceMiddleware.executor.dealer

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Deprecated("should be replaced by a RemovePreferredDealerPsaExecutorTest")
internal class RemovePreferredDealerPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: RemovePreferredDealerPsaExecutor

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(RemovePreferredDealerPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call with the right response`() {
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

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute then make a get API call with the wrong response`() {
        coEvery { communicationManager.delete<String>(any(), any()) } returns NetworkResponse.Success(
            "not successfully deleted!"
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

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            val error = PimsErrors.serverError(
                null,
                "error occurred when add preferred dealer not successfully deleted!"
            )
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
        }
    }
}
