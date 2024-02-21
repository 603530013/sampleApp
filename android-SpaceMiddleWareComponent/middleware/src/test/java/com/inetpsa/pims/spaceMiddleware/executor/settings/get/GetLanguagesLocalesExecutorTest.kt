package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.settings.CountryResponse
import com.inetpsa.pims.spaceMiddleware.util.readAssetsJsonFile
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class GetLanguagesLocalesExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetLanguagesLocalesListExecutor
    private val filePath: String = "assets://testPath"
    private val countries = listOf(
        Locale.FRANCE,
        Locale.ITALY,
        Locale.UK
    )

    private val jsonResponse: Map<String, CountryResponse> =
        mapOf(
            Locale.UK.country.uppercase() to CountryResponse(listOf(Locale.UK.toLanguageTag())),
            Locale.ITALY.country.uppercase() to CountryResponse(listOf(Locale.ITALY.toLanguageTag())),
            Locale.FRANCE.country.uppercase() to CountryResponse(listOf(Locale.FRANCE.toLanguageTag()))
        )

    @Before
    override fun setup() {
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.PathExtensionsKt")
        super.setup()
        every { middlewareComponent.context } returns context
        every { configurationManager.languagePath } returns filePath
        every { filePath.readAssetsJsonFile<Map<String, CountryResponse>>(context) } returns jsonResponse
        executor = spyk(GetLanguagesLocalesListExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute params with right input then return a Unit`() {
        val output = executor.params(emptyMap())
        Assert.assertEquals(Unit, output)
    }

    @Test
    fun `when execute then make a network call with success response`() {
        every { executor.readFromJsonFile() } returns listOf(
            Locale.FRANCE,
            Locale.ITALY,
            Locale.UK
        )
        runTest {
            val data = executor.execute()
            Assert.assertEquals(true, data is Success)
            val success = (data as Success).response
            Assert.assertEquals(countries, success)
        }
    }

    @Test
    fun `when execute then make a network API call with failure response`() {
        every { executor.readFromJsonFile() } returns null
        val error = PIMSFoundationError.invalidParameter(Constants.Input.Configuration.LANGUAGE_PATH)

        runTest {
            val response = executor.execute()

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error

            Assert.assertEquals(error.code, failure?.code)
            Assert.assertEquals(error.message, failure?.message)
            Assert.assertEquals(error.subError?.status, failure?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure?.subError?.body)
        }
    }

    @Test
    fun `when execute parseJson with empty data then return empty list`() {
        val result = executor.parseJson(emptyMap())
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `when execute parseJson with valid data then return valid list`() {
        val result = executor.parseJson(
            mapOf(
                "EN" to CountryResponse(listOf("en-GB")),
                "FR" to CountryResponse(listOf("fr-FR"))
            )
        )
        Assert.assertEquals(2, result.size)
        Assert.assertEquals(Locale.FRANCE.toLanguageTag(), result[0].toLanguageTag())
        Assert.assertEquals(Locale.UK.toLanguageTag(), result[1].toLanguageTag())
    }

    @Test
    fun `when execute transformLanguage with Invalid language then return empty list`() {
        val result = executor.transformLanguage(CountryResponse(listOf("frFR")))
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `when execute transformLanguage with empty language then return empty list`() {
        val result = executor.transformLanguage(CountryResponse(listOf("")))
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `when execute transformLanguage with blanc language then return empty list`() {
        val result = executor.transformLanguage(CountryResponse(listOf(" ")))
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `when execute transformLanguage with empty language list then return empty list`() {
        val result = executor.transformLanguage(CountryResponse(listOf()))
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `when execute transformLanguage with language splitted by - then return right list`() {
        val result = executor.transformLanguage(CountryResponse(listOf("fr-FR")))
        Assert.assertEquals(1, result.size)
        Assert.assertEquals(Locale.FRANCE.toLanguageTag(), result.first().toLanguageTag())
    }

    @Test
    fun `when execute transformLanguage with language splitted by _ then return right list`() {
        val result = executor.transformLanguage(CountryResponse(listOf("fr_FR")))
        Assert.assertEquals(1, result.size)
        Assert.assertEquals(Locale.FRANCE.toLanguageTag(), result.first().toLanguageTag())
    }

//    @Test
//    fun `test readFromJsonFile()`() {
//        val map = mapOf(
//            "EN" to CountryResponse(listOf("en-GB")),
//            "FR" to CountryResponse(listOf("fr-FR"))
//        )
//        mockkConstructor(ConfigurationManager::class)
//        every { anyConstructed<ConfigurationManager>().languagePath } returns "filePath"
//        every {
//            "filePath".readAssetsJsonFile<Map<String, CountryResponse>>(
//                any()
//            )
//        } returns map
//        val result = executor.readFromJsonFile()
//        result?.let {
//            Assert.assertEquals(2, result.size)
//            Assert.assertEquals(Locale.FRANCE.toLanguageTag(), result[0].toLanguageTag())
//            Assert.assertEquals(Locale.UK.toLanguageTag(), result[1].toLanguageTag())
//        }
//    }

    @Test
    fun `test params with params as null `() {
        val executor = GetLanguagesLocalesListExecutor(middlewareComponent)
        val result = executor.params()
        assert(result is Unit)
    }
}
