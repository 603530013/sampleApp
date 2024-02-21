package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetRoadSideContactFCAExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetRoadSideContactFCAExecutor

    private val fcaSettings = listOf(
        SettingsCallCenterItemResponse(
            settingType = "testSettingsType",
            version = "test_version",
            settings = listOf(
                SettingsCallCenterItemResponse.CallCenterSettingFca(
                    primaryNumber = "test_primaryNumber",
                    secondaryNumber = "test_secondaryNumber",
                    callType = "test_callType",
                    settingCategory = "test_settins",
                    callCenterType = "ROADSIDE"
                )
            )
        )
    )

    private val roadSideResponse = mapOf(
        Constants.Output.Common.PHONES to mapOf(
            Constants.PRIMARY to "test_primaryNumber",
            Constants.SECONDARY to "test_secondaryNumber"
        )
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetSettingsFCAExecutor::class)
        coEvery { anyConstructed<GetSettingsFCAExecutor>().execute(any()) } returns NetworkResponse.Success(fcaSettings)
        executor = spyk(GetRoadSideContactFCAExecutor(baseCommand))
    }

    @Test
    fun `when execute params with right input then return a Vin`() {
        val params = "testVin"
        val input = mapOf(Constants.PARAM_VIN to params)
        val output = executor.params(input)
        Assert.assertEquals(params, output)
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
        val input = mapOf(Constants.PARAM_VIN to vin)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute then make a network call with success response`() {
        runTest {
            val paramsVin = "testVin"
            val response = executor.execute(paramsVin)

            coVerify(exactly = 1) { anyConstructed<GetSettingsFCAExecutor>().execute(eq(paramsVin)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(roadSideResponse, success.response)
        }
    }

    @Test
    fun `when execute then make a network call with failure response`() {
        runTest {
            val paramsVin = "testVin"
            val error = PIMSFoundationError.serverError(1, "test_body")
            coEvery { anyConstructed<GetSettingsFCAExecutor>().execute(any()) } returns NetworkResponse.Failure(error)
            val response = executor.execute(paramsVin)

            coVerify(exactly = 1) { anyConstructed<GetSettingsFCAExecutor>().execute(eq(paramsVin)) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError, failure.error?.subError)
        }
    }
}
