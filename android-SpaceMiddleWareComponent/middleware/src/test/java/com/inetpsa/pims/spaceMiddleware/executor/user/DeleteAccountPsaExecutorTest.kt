package com.inetpsa.pims.spaceMiddleware.executor.user

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class DeleteAccountPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: DeleteAccountPsaExecutor

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(DeleteAccountPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with right input then return a Unit`() {
        val output = executor.params(emptyMap())
        Assert.assertEquals(Unit, output)
    }

    @Test
    fun `when execute then make a delete API call`() {
        coEvery { communicationManager.update<Unit>(any(), any()) } returns NetworkResponse.Success(Unit)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/unsubscribe")),
                    body = "{}"
                )
            }

            coVerify {
                communicationManager.update<Unit>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(Unit, success)
        }
    }
}
