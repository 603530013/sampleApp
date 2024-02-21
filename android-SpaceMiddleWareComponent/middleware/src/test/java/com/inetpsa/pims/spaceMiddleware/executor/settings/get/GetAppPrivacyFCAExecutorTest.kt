package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.ApplicationTermsOutput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetAppPrivacyFCAExecutorTest : FcaExecutorTestHelper() {

    private val applicationTermsOutput = ApplicationTermsOutput(
        country = "testCountry",
        language = "testLanguage",
        url = "testUrl",
        content = "testContent",
        update = "testTimeStamp",
        version = "testVersion"
    )

    private lateinit var executor: GetAppPrivacyFCAExecutor

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(FetchTermConditionFCAExecutor::class)
        coEvery {
            anyConstructed<FetchTermConditionFCAExecutor>().execute(any())
        } returns NetworkResponse.Success(applicationTermsOutput)
        executor = spyk(GetAppPrivacyFCAExecutor(baseCommand))
    }

    @Test
    fun `when execute params with right input then return a sdp`() {
        val params = "testSDP"
        val input = mapOf(Constants.Input.Settings.SDP to params)
        val output = executor.params(input)
        Assert.assertEquals(params, output)
    }

    @Test
    fun `when execute params with missing sdp then throw missing parameter`() {
        val input = mapOf<String, Any?>()
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
        val input = mapOf(Constants.Input.Settings.SDP to sdp)
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
            val response = executor.execute(paramSDP)

            coVerify(exactly = 1) {
                anyConstructed<FetchTermConditionFCAExecutor>().execute(
                    eq(
                        TermsConditionsInput(
                            country = "FR",
                            sdp = paramSDP,
                            type = TermsConditionsInput.Type.Privacy
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
