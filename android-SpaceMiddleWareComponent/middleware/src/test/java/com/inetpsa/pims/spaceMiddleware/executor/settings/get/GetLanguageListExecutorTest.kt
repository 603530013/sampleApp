package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import io.mockk.coEvery
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class GetLanguageListExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetLanguageListExecutor
    private val countries: Map<String, List<String>> =
        mapOf(
            Constants.PARAMS_KEY_LANGUAGES to listOf(
                Locale.FRANCE.toLanguageTag(),
                Locale.ITALY.toLanguageTag(),
                Locale.UK.toLanguageTag()
            )
        )

    private val locales = listOf(
        Locale.FRANCE,
        Locale.ITALY,
        Locale.UK
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetLanguageListExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute params with right input then return a Unit`() {
        val output = executor.params(emptyMap())
        Assert.assertEquals(Unit, output)
    }

    @Test
    fun `when execute then make a network call with success response`() {
        runTest {
            mockkConstructor(GetLanguagesLocalesListExecutor::class, recordPrivateCalls = true, localToThread = true)
            coEvery { anyConstructed<GetLanguagesLocalesListExecutor>().execute(Unit) } returns
                NetworkResponse.Success(locales)

            val data = executor.execute()
            Assert.assertEquals(true, data is Success)
            val success = (data as Success).response
            Assert.assertEquals(success, countries)
        }
    }
}
