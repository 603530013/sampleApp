package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.ApplicationTermsOutput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.VehicleTermsConditionsInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetConnectedPrivacyFCAExecutorTest : FcaExecutorTestHelper() {

    private val applicationTermsOutput = ApplicationTermsOutput(
        country = "testCountry",
        language = "testLanguage",
        url = "testUrl",
        content = "testContent",
        update = "testTimeStamp",
        version = "testVersion"
    )

    private lateinit var executor: GetConnectedPrivacyFCAExecutor

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(FetchTermConditionFCAExecutor::class)
        coEvery {
            anyConstructed<FetchTermConditionFCAExecutor>().execute(any())
        } returns NetworkResponse.Success(applicationTermsOutput)
        executor = spyk(GetConnectedPrivacyFCAExecutor(baseCommand))
    }

    @Test
    fun `when execute params with right input then return a SDP`() {
        val paramsSDP = "testSDP"
        val paramsCountry = "testCountry"
        val params = mapOf(
            Constants.Input.Settings.COUNTRY to paramsCountry,
            Constants.Input.Settings.SDP to paramsSDP
        )
        val input = VehicleTermsConditionsInput(country = paramsCountry, sdp = paramsSDP)
        val output = executor.params(params)
        Assert.assertEquals(input, output)
    }

    @Test
    fun `when execute params with missing sdp then throw missing parameter`() {
        val country = "testCountry"
        val input = mapOf<String, Any?>(Constants.Input.Settings.COUNTRY to country)
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Settings.SDP)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid sdp then throw missing parameter`() {
        val sdp = 123
        val country = "testCountry"
        val input = mapOf(
            Constants.Input.Settings.COUNTRY to country,
            Constants.Input.Settings.SDP to sdp
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Settings.SDP)
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
            val paramSDP = "testSDP"
            val paramCountry = "EN"
            val input = VehicleTermsConditionsInput(country = paramCountry, sdp = paramSDP)
            val response = executor.execute(input)

            coVerify(exactly = 1) {
                anyConstructed<FetchTermConditionFCAExecutor>().execute(
                    eq(
                        TermsConditionsInput(
                            sdp = paramSDP,
                            country = paramCountry,
                            type = TermsConditionsInput.Type.ConnectedPP
                        )
                    )
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(applicationTermsOutput, success.response)
        }
    }
}
