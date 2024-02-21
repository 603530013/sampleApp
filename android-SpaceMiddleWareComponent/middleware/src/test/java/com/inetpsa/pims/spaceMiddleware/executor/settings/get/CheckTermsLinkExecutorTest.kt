package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.CheckTermsLinkInput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class CheckTermsLinkExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: CheckTermsLinkExecutor<String>
    private lateinit var languageExecutor: GetLanguagesLocalesListExecutor

    @Before
    override fun setup() {
        mockkStatic("com.inetpsa.pims.spaceMiddleware.util.PathExtensionsKt")
        super.setup()
        executor = spyk(CheckTermsLinkExecutor(middlewareComponent, emptyMap()))
        languageExecutor = spyk(GetLanguagesLocalesListExecutor(middlewareComponent, emptyMap()))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when execute with country != config country then call fetchLanguages with currentLanguage == null`() {
        val input = CheckTermsLinkInput(
            country = "IT",
            url = "https://aemstagems4-connectivity.fiat.com/{market}/{language}/privacy-terms/latest.jsonview"
        )

        coEvery { executor.fetchLanguages(any(), any()) } returns listOfNotNull("IT")
        every { configurationManager.brand } returns Brand.FIAT
        coEvery {
            communicationManager.get<String>(any(), any())
        } returns Success("")

        runTest {
            executor.execute(input)
        }
        coVerify(exactly = 1) {
            executor.fetchLanguages(
                country = eq("IT"),
                currentLanguage = null
            )
        }

        coVerify(exactly = 1) {
            executor.fetchLinkByLanguage(
                brand = eq(Brand.FIAT.name.lowercase()),
                country = eq("IT"),
                url = eq(input.url),
                languages = eq(listOfNotNull("IT"))
            )
        }
    }

    @Test
    fun `when execute with country == config country then call fetchLanguages with currentLanguage`() {
        val input = CheckTermsLinkInput(
            country = "IT",
            url = "https://aemstagems4-connectivity.fiat.com/{market}/{language}/privacy-terms/latest.jsonview"
        )

        every { configurationManager.locale } returns Locale.ITALY
        every { configurationManager.brand } returns Brand.FIAT
        coEvery { executor.fetchLanguages(any(), any()) } returns listOfNotNull("IT")
        coEvery {
            communicationManager.get<String>(any(), any())
        } returns Success("")

        runTest {
            executor.execute(input)
        }
        coVerify(exactly = 1) {
            executor.fetchLanguages(
                country = eq("IT"),
                currentLanguage = "it"
            )
        }

        coVerify(exactly = 1) {
            executor.fetchLinkByLanguage(
                brand = eq(Brand.FIAT.name.lowercase()),
                country = eq("IT"),
                url = eq(input.url),
                languages = eq(listOfNotNull("IT"))
            )
        }
    }

    @Test
    fun `when execute with country that support many languages`() {
        val input = CheckTermsLinkInput(
            country = "CH",
            url = "https://aemstagems4-connectivity.fiat.com/{market}/{language}/privacy-terms/latest.jsonview"
        )

        every { configurationManager.brand } returns Brand.FIAT

        coEvery { executor.fetchLanguages(any(), any()) } returns listOf("de", "it", "fr")

        coEvery {
            communicationManager.get<String>(any(), any())
        } returns Success("")

        runTest {
            executor.execute(input)

            coVerify(exactly = 1) {
                executor.generateLink(
                    url = eq(
                        "https://aemstagems4-connectivity.fiat.com/{market}/{language}/privacy-terms/latest" +
                            ".jsonview"
                    ),
                    brand = eq(Brand.FIAT.name.lowercase()),
                    country = eq("CH"),
                    language = eq("de")
                )
            }
        }
    }

    @Suppress("LongMethod")
    @Test
    fun `when execute with country that support many languages and try to heat the url`() {
        val input = CheckTermsLinkInput(
            country = "CH",
            url = "https://aemstagems4-connectivity.fiat.com/{market}/{language}/privacy-terms/latest.jsonview"
        )

        every { configurationManager.brand } returns Brand.FIAT

        coEvery { executor.fetchLanguages(any(), any()) } returns listOf("de", "it", "fr")

        coEvery {
            communicationManager.get<String>(any(), any())
        } returns Failure(PimsErrors.invalidServerUrl(input.url))

        runTest {
            executor.execute(input)

            coVerify(exactly = 1) {
                executor.generateLink(
                    url = eq(
                        "https://aemstagems4-connectivity.fiat.com/{market}/{language}/privacy-terms/latest" +
                            ".jsonview"
                    ),
                    brand = eq(Brand.FIAT.name.lowercase()),
                    country = eq("CH"),
                    language = eq("de")
                )
            }

            coVerify(exactly = 1) {
                executor.generateRequest(
                    link = eq(
                        "https://aemstagems4-connectivity.fiat.com/ch/de/privacy-terms/latest" +
                            ".jsonview"
                    )
                )
            }

            coVerify(exactly = 1) {
                executor.generateLink(
                    url = eq(
                        "https://aemstagems4-connectivity.fiat.com/{market}/{language}/privacy-terms/latest" +
                            ".jsonview"
                    ),
                    brand = eq(Brand.FIAT.name.lowercase()),
                    country = eq("CH"),
                    language = eq("it")
                )
            }

            coVerify(exactly = 1) {
                executor.generateRequest(
                    link = eq(
                        "https://aemstagems4-connectivity.fiat.com/ch/it/privacy-terms/latest" +
                            ".jsonview"
                    )
                )
            }

            coVerify(exactly = 1) {
                executor.generateLink(
                    url = eq(
                        "https://aemstagems4-connectivity.fiat.com/{market}/{language}/privacy-terms/latest" +
                            ".jsonview"
                    ),
                    brand = eq(Brand.FIAT.name.lowercase()),
                    country = eq("CH"),
                    language = eq("fr")
                )
            }

            coVerify(exactly = 1) {
                executor.generateRequest(
                    link = eq(
                        "https://aemstagems4-connectivity.fiat.com/ch/fr/privacy-terms/latest" +
                            ".jsonview"
                    )
                )
            }
        }
    }

    @Test
    fun `test fetchLanguages() returns list with current language when there is failure response`() {
        mockkConstructor(GetLanguagesLocalesListExecutor::class)
        every { GetLanguagesLocalesListExecutor(middlewareComponent, emptyMap()) } returns mockk {
            coEvery { GetLanguagesLocalesListExecutor(middlewareComponent, emptyMap()).execute() } returns Failure(
                PIMSError(4001, "error")
            )
        }
        runBlocking {
            val result = executor.fetchLanguages("CA", "en")
            Assert.assertNotNull(result)
            assertEquals(listOf("en"), result)
        }
    }

    @Test
    fun `test fetchLanguages() returns list of languages`() {
        val sampleLocaleList = listOf(
            Locale.US,
            Locale.UK,
            Locale.CANADA_FRENCH,
            Locale.CANADA,
            Locale("de", "CA")
        )
        val expectedOutput = listOf("en", "fr", "de")
        mockkConstructor(GetLanguagesLocalesListExecutor::class)
        every { GetLanguagesLocalesListExecutor(middlewareComponent, emptyMap()) } returns mockk {
            coEvery { GetLanguagesLocalesListExecutor(middlewareComponent, emptyMap()).execute() } returns Success(
                sampleLocaleList
            )
        }
        runBlocking {
            val result = executor.fetchLanguages("CA", "en")
            assertEquals(expectedOutput, result)
        }
    }

    @Test
    fun `test fetchLanguages() returns empty list when current language is null and `() {
        val sampleLocaleList = listOf(
            Locale.US,
            Locale.UK,
            Locale.CANADA_FRENCH,
            Locale.CANADA,
            Locale("de", "CA")
        )
        mockkConstructor(GetLanguagesLocalesListExecutor::class)
        every { GetLanguagesLocalesListExecutor(middlewareComponent, emptyMap()) } returns mockk {
            coEvery { GetLanguagesLocalesListExecutor(middlewareComponent, emptyMap()).execute() } returns Success(
                sampleLocaleList
            )
        }
        runBlocking {
            val result = executor.fetchLanguages("IN")
            assertEquals(emptyList<String>(), result)
        }
    }

    @Test
    fun `test params() returns CheckTermsLinkInput`() {
        val expectedOutput = CheckTermsLinkInput("", "")
        val executor = CheckTermsLinkExecutor<String>(middlewareComponent)
        assertEquals(expectedOutput, executor.params())
        val executor1 = CheckTermsLinkExecutor(middlewareComponent, type = String::class.java)
        assertEquals(expectedOutput, executor1.params())
    }
}
