package com.inetpsa.pims.spaceMiddleware.executor.settings

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutorTest
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetCurrentLanguageExecutor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetCurrentLanguageExecutorTest : BaseLocalExecutorTest() {

    private lateinit var executor: GetCurrentLanguageExecutor
    private val currentCountryInfo = mapOf(Constants.Output.LANGUAGE to "fr-FR")

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetCurrentLanguageExecutor(baseCommand))
    }

    @Test
    fun `when execute then call configuataion Manager locale`() {
        runTest {
            val response = executor.execute(Unit)

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(currentCountryInfo, success.response)
        }
    }

    @Test
    fun `when execute params with empty input then continue execution`() {
        val output = executor.params(null)
        Assert.assertEquals(Unit, output)
    }
}
