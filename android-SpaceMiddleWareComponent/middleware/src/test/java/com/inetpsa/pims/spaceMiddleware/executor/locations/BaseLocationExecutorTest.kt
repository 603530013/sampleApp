package com.inetpsa.pims.spaceMiddleware.executor.locations

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class BaseLocationExecutorTest : LocationExecutorTestHelper() {

    private lateinit var executor: TestExecutor

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(TestExecutor(baseCommand))
    }

    @Test
    fun `when execute then return success with test result`() {
        runTest {
            val response = executor.execute()
            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals("testResponseSuccess", success.response)
        }
    }

    @Test
    fun `when googleApiKey is available then return it`() {
        val key = "testGoogleApiKey"
        every { configurationManager.googleApiKey } returns key
        Assert.assertEquals(key, executor.googleApiKey)
    }

    @Test
    fun `when googleApiKey is missing then throw missing parameter`() {
        every<String?> { configurationManager.googleApiKey } returns null
        val exception = PIMSFoundationError.missingParameter(Constants.PARAMS_KEY_GOOGLE_API_KEY)
        try {
            executor.googleApiKey
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when look for base url then return the test url`() {
        val url = executor.baseUrl(arrayOf("test1", "test2"))
        Assert.assertEquals("https://maps.googleapis.com/maps/api/test1test2", url)
    }

    @Test
    fun `when look for base headers then return the test headers`() {
        val headers = executor.baseHeaders()
        Assert.assertEquals(true, headers.isEmpty())
    }

    @Test
    fun `when look for base queries then return the test headers`() {
        val expected = mapOf("language" to configurationManager.locale.toLanguageTag())
        val queries = executor.baseQueries()
        Assert.assertEquals(expected, queries)
    }

    @Test
    fun `check that we use the right communication manager from foundation`() {
        Assert.assertEquals(communicationManager, executor.communicationManager)
    }

    internal class TestExecutor(command: BaseCommand) : BaseLocationExecutor<String, String>(command) {

        override fun params(parameters: Map<String, Any?>?): String = "testParam"

        override suspend fun execute(input: String): NetworkResponse<String> =
            NetworkResponse.Success("testResponseSuccess")
    }
}
