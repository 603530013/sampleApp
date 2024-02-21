package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_ENVIRONMENT
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment.DEV
import com.inetpsa.mmx.foundation.tools.Environment.GMA_PREPROD
import com.inetpsa.mmx.foundation.tools.Environment.PREPROD
import com.inetpsa.mmx.foundation.tools.Environment.PROD
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.LocalExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TermCondition
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TermCondition.Mode
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput.Type.AppTerms
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class ReadAssetsTermConditionFcaExecutorTest : LocalExecutorTestHelper() {

    private lateinit var executor: ReadAssetsTermConditionFcaExecutor

    private val termCondition = TermCondition(
        mode = Mode.LOCAL,
        url = "https://aemstagems4-connectivity.alfaromeo.com/fr/fr/application-terms-of-use/latest.jsonview",
        market = "fr"
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(ReadAssetsTermConditionFcaExecutor(middlewareComponent))
    }

    @Test
    fun `when execute params with the right input then return TermsConditionsInput`() {
        val sdp = "testSDP"
        val country = "testCountry"
        val type = TermsConditionsInput.Type.AppTerms
        val termsConditionsInput = TermsConditionsInput(
            type = type,
            sdp = sdp,
            country = country
        )

        val input = mapOf(
            Constants.Input.Settings.SDP to sdp,
            Constants.Input.Settings.COUNTRY to country,
            Constants.Input.TYPE to type.name

        )
        val output = executor.params(input)
        assertEquals(termsConditionsInput.sdp, output.sdp)
        assertEquals(termsConditionsInput.country, output.country)
        assertEquals(AppTerms, output.type)
    }

    @Test
    fun `when execute params with missing type then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.TYPE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid type then throw missing parameter`() {
        val type = 111
        val input = mapOf(Constants.Input.TYPE to type)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.TYPE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params missing country then throw missing parameter`() {
        val type = TermsConditionsInput.Type.AppTerms
        val input = mapOf(Constants.Input.TYPE to type.name)
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Settings.COUNTRY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid country then throw missing parameter`() {
        val paramsCountry = 123
        val type = TermsConditionsInput.Type.AppTerms
        val input = mapOf(
            Constants.Input.TYPE to type.name,
            Constants.Input.Settings.COUNTRY to paramsCountry
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Settings.COUNTRY)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params missing sdp then throw missing parameter`() {
        val type = TermsConditionsInput.Type.AppTerms
        val paramsCountry = "testCountry"
        val input = mapOf(
            Constants.Input.TYPE to type.name,
            Constants.Input.Settings.COUNTRY to paramsCountry
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Settings.SDP)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid sdp then throw missing parameter`() {
        val type = TermsConditionsInput.Type.AppTerms.name
        val paramsCountry = "testCountry"
        val paramsSDP = 123
        val input = mapOf(
            Constants.Input.TYPE to type,
            Constants.Input.Settings.COUNTRY to paramsCountry,
            Constants.Input.Settings.SDP to paramsSDP
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Settings.SDP)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            assertEquals(exception.code, ex.code)
            assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `test execute() returns success response TermCondition when MODE is USER`() {
        val termsConditionsInput = TermsConditionsInput(
            type = TermsConditionsInput.Type.AppTerms,
            sdp = "testSDP",
            country = "testCountry"
        )
        coEvery { executor.fetchTermsConditions(any(), any(), any(), any()) } returns termCondition
        every { configurationManager.locale.country } returns "fr"
        every { configurationManager.environment } returns PREPROD
        runBlocking {
            val response = executor.execute(termsConditionsInput)
            assertEquals(true, response is Success)
            coVerify { executor.fetchTermsConditions(PREPROD, "TESTSDP", "fr", termsConditionsInput.type.value) }
            assertEquals(termCondition.copy(market = "fr"), (response as Success).response)
        }
    }

    @Test
    fun `test execute()  returns success response when Mode is VEHICALE `() {
        val termsConditionsInput = TermsConditionsInput(
            type = TermsConditionsInput.Type.ConnectedTC,
            sdp = "testSDP",
            country = "testCountry"
        )
        coEvery { executor.fetchTermsConditions(any(), any(), any(), any()) } returns termCondition
        every { configurationManager.locale.country } returns "fr"
        every { configurationManager.environment } returns PREPROD
        runBlocking {
            val response = executor.execute(termsConditionsInput)
            assertEquals(true, response is Success)
            coVerify {
                executor.fetchTermsConditions(
                    PREPROD,
                    "TESTSDP",
                    termsConditionsInput.country ?: "testCountry",
                    termsConditionsInput
                        .type.value
                )
            }
            assertEquals(termCondition.copy(market = "testCountry"), (response as Success).response)
        }
    }

    @Test
    fun `test fetchTermsConditions calls readTermConditionFile with proper file path for PREPROD`() {
        coEvery { executor.readTermConditionFile(any(), any(), any(), any()) } returns termCondition
        runBlocking {
            val result = executor.fetchTermsConditions(
                PREPROD,
                "TESTSDP",
                "fr",
                TermsConditionsInput.Type.AppTerms
                    .name
            )
            coVerify {
                executor.readTermConditionFile(
                    "fca/termcondition/tnc_preprod_mock.json",
                    "TESTSDP",
                    "fr",
                    TermsConditionsInput.Type.AppTerms.name
                )
                assertEquals(termCondition, result)
            }
        }
    }

    @Test
    fun `test fetchTermsConditions calls readTermConditionFile with proper file path for PROD`() {
        coEvery { executor.readTermConditionFile(any(), any(), any(), any()) } returns termCondition
        runBlocking {
            val result = executor.fetchTermsConditions(
                PROD,
                "TESTSDP",
                "fr",
                TermsConditionsInput.Type.AppTerms
                    .name
            )
            coVerify {
                executor.readTermConditionFile(
                    "fca/termcondition/tnc_prod_mock.json",
                    "TESTSDP",
                    "fr",
                    TermsConditionsInput.Type.AppTerms.name
                )
                assertEquals(termCondition, result)
            }
        }
    }

    @Test
    fun `test fetchTermsConditions calls readTermConditionFile with proper file path for DEV`() {
        coEvery { executor.readTermConditionFile(any(), any(), any(), any()) } returns termCondition
        runBlocking {
            val result = executor.fetchTermsConditions(
                DEV,
                "TESTSDP",
                "fr",
                TermsConditionsInput.Type.AppTerms
                    .name
            )
            coVerify {
                executor.readTermConditionFile(
                    "fca/termcondition/tnc_int_mock.json",
                    "TESTSDP",
                    "fr",
                    TermsConditionsInput.Type.AppTerms.name
                )
                assertEquals(termCondition, result)
            }
        }
    }

    @Test
    fun `test fetchTermsConditions throws PIMSError when readTermConditionFile returns null`() {
        val error = PIMSFoundationError.invalidReturnParam(TermsConditionsInput.Type.AppTerms.name)
        coEvery { executor.readTermConditionFile(any(), any(), any(), any()) } returns null
        try {
            executor.fetchTermsConditions(
                DEV,
                "TESTSDP",
                "fr",
                TermsConditionsInput.Type.AppTerms
                    .name
            )
        } catch (ex: PIMSError) {
            assertEquals(error.code, ex.code)
            assertEquals(error.message, ex.message)
        }
    }

    @Test
    fun `test fetchTermsConditions throws PIMSError when the ENVIRONMENT is not valid`() {
        val error = PIMSFoundationError.invalidParameter(CONTEXT_KEY_ENVIRONMENT)
        try {
            executor.fetchTermsConditions(
                GMA_PREPROD,
                "TESTSDP",
                "fr",
                TermsConditionsInput.Type.AppTerms
                    .name
            )
        } catch (ex: PIMSError) {
            assertEquals(error.code, ex.code)
            assertEquals(error.message, ex.message)
        }
    }

    @Test
    fun `when execute transformSdp then return Sdp value`() {
        val result = executor.transformSdp("IGNITE")
        assertEquals("GLOBAL", result)
    }

    @Test
    fun `when execute transformBrand then return brand`() {
        var result = executor.transformBrand(Brand.ALFAROMEO)
        assertEquals("alfa", result)

        Brand.values().filter { it != Brand.ALFAROMEO }.forEach { brand ->
            result = executor.transformBrand(brand)
            assertEquals(brand.name.lowercase(), result)
        }
    }
}
