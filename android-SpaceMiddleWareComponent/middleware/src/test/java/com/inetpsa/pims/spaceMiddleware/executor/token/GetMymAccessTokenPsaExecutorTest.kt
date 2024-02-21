package com.inetpsa.pims.spaceMiddleware.executor.token

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.TokenType.CVSToken
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.MymAccessToken
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ProfileResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetMymAccessTokenPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetMymAccessTokenPsaExecutor
    private val token = MymAccessToken(
        accessToken = "testAccessToken",
        profile = ProfileResponse(
            idClient = "testIdClient",
            email = "testEmail",
            lastName = "testLastName",
            firstName = "testFirstName",
            address1 = "testAddress1",
            address2 = "testAddress2",
            city = "testCity",
            zipCode = "testZipCode",
            country = "testCountry",
            civility = "testCivility",
            civilityCode = "testCivilityCode",
            cpf = "testCPF",
            rut = "testRut",
            phone = "testPhone",
            mobile = "testMobile",
            mobilePro = "testMobilePro"
        )
    )

    @Before
    override fun setup() {
        super.setup()
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        executor = spyk(GetMymAccessTokenPsaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute then make a get API call`() {
        every {
            dataManager.create(
                key = any(),
                data = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }

        coEvery { communicationManager.get<MymAccessToken>(any(), any()) } returns NetworkResponse.Success(token)
        justRun { userSessionManager.setToken(any(), any()) }

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(MymAccessToken::class.java),
                    urls = eq(arrayOf("/session/v2/accesstoken")),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            verify {
                userSessionManager.setToken(eq(token.accessToken), eq(MiddlewareCommunicationManager.MymToken))
            }

            coVerify {
                communicationManager.get<MymAccessToken>(
                    request = any(),
                    tokenType = eq(CVSToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(token, success.response)
        }
    }
}
