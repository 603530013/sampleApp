package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetSettingsFCAExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetSettingsFCAExecutor

    private val settings = listOf(
        SettingsCallCenterItemResponse(
            settingType = "testSettingsType",
            version = "test_version",
            settings = listOf(
                SettingsCallCenterItemResponse.CallCenterSettingFca(
                    primaryNumber = "test_primaryNumber",
                    secondaryNumber = "test_secondaryNumber",
                    callType = "test_callType",
                    settingCategory = "test_settins",
                    callCenterType = "test_call_CenterType"
                )
            )

        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetSettingsFCAExecutor(middlewareComponent))
    }

    @Test
    fun `when execute then make a get API call`() {
        val vin = "testVin"
        every { executor.params(any()) } returns vin

        coEvery { communicationManager.get<List<SettingsCallCenterItemResponse>>(any(), any()) } returns Success(
            settings
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(object : TypeToken<List<SettingsCallCenterItemResponse>>() {}.type),
                    urls = eq(
                        arrayOf(
                            "/v3/accounts/",
                            uid,
                            "/vehicles/",
                            vin,
                            "/settings"
                        )
                    ),
                    headers = any(),
                    queries = eq(
                        mapOf("settingType" to "PhoneNumbers")
                    ),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<List<SettingsCallCenterItemResponse>>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(settings, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return SendInfoForAssistancePsaParams`() {
        val vin = "testVin"

        val input = mapOf(Constants.PARAM_VIN to vin)

        val output = executor.params(input)
        Assert.assertEquals(vin, output)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf<String, Any?>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val vin = 123
        val input = mapOf(
            Constants.PARAM_VIN to vin
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
