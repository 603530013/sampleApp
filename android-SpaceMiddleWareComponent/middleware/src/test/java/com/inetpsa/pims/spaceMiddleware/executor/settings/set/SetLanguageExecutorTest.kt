package com.inetpsa.pims.spaceMiddleware.executor.settings.set

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.manager.Config
import io.mockk.every
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class SetLanguageExecutorTest : BaseLocalExecutorTestHelper() {

    private lateinit var executor: SetLanguageExecutor

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(SetLanguageExecutor(baseCommand))
    }

    @Test
    fun `when execute params with missing locale then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.LANGUAGE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid locale then throw missing parameter`() {
        val locale = 123
        val input = mapOf(Constants.Input.LANGUAGE to locale)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.LANGUAGE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid locale value then throw missing parameter`() {
        val locale = "test-ping"
        val input = mapOf(Constants.Input.LANGUAGE to locale)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.LANGUAGE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with right input then return Locale Params`() {
        val inputlocale = Locale.FRANCE
        val input = mapOf(Constants.Input.LANGUAGE to inputlocale.toLanguageTag())
        val param = executor.params(input)
        Assert.assertEquals(inputlocale, param)
    }

    @Test
    fun `when execute then set a language`() {
        val inputlocale = Locale.FRANCE

        every { executor.params(any()) } returns inputlocale

        justRun { configurationManager.update(middlewareComponent, config = Config(locale = inputlocale)) }

        runTest {
            val response = executor.execute()

            verify {
                configurationManager.update(middlewareComponent, config = Config(locale = inputlocale))
            }
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }
}
