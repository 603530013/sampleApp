package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.DealerFavoriteInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class AddFavoriteDealerFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: AddFavoriteDealerFcaExecutor

    private val dealerInput = DealerFavoriteInput(
        vin = "testVin",
        id = "testDealerID"
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(AddFavoriteDealerFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        every { executor.params(any()) } returns dealerInput
        coEvery { communicationManager.post<Unit>(any(), any()) } returns
            Success(Unit)
        runTest {
            val response = executor.execute()
            val bodyJson = mapOf(
                Constants.PARAMS_KEY_DEALER_ID to dealerInput.id
            )
            verify {
                executor.request(
                    type = String::class.java,
                    urls = arrayOf(
                        "/v1/accounts/",
                        "testCustomerId",
                        "/vehicles/",
                        dealerInput.vin,
                        "/mydealer/preferredDealer"
                    ),
                    body = bodyJson.toJson()
                )
            }
            coVerify {
                communicationManager.post<String>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with right inputs then return an DealerFavoriteInput`() {
        val vin = "testVin"
        val id = "testDealerID"
        val input = mapOf(
            Constants.Input.VIN to vin,
            Constants.Input.ID to id
        )
        val dealerFavoriteInput = executor.params(input)
        Assert.assertEquals(vin, dealerFavoriteInput.vin)
        Assert.assertEquals(id, dealerFavoriteInput.id)
    }
}
