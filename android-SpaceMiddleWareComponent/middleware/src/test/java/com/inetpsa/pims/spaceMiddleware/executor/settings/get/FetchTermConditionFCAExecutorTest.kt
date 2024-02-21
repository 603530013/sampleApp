package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.data.IDataManager
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.LocalApplicationTermsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TermCondition
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TermCondition.Mode.BROWSER
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.TermCondition.Mode.LOCAL
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.ApplicationTermsOutput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.CheckTermsLinkInput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput
import com.inetpsa.pims.spaceMiddleware.model.settings.termsandconditions.TermsConditionsInput.Type.AppTerms
import com.inetpsa.pims.spaceMiddleware.util.createSync
import io.mockk.coEvery
import io.mockk.coJustRun
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
import org.junit.Before
import org.junit.Test

internal class FetchTermConditionFCAExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: FetchTermConditionFCAExecutor
    private lateinit var readAssetsTermConditionFcaExecutor: ReadAssetsTermConditionFcaExecutor

    private val applicationTermsOutput = ApplicationTermsOutput(
        country = "testCountry",
        language = "testLanguage",
        url = "testUrl",
        content = "testContent",
        update = "testTimeStamp",
        version = "testVersion"
    )

    private val termConditionLocal = TermCondition(
        mode = LOCAL,
        url = "testUrl",
        market = "testMarket"
    )

    private val termsResponse = LocalApplicationTermsResponse(
        country = "testCountry",
        copyHU = "testCopyHu",
        webURL = "https://aemstagems4-connect.alfaromeo.com/us/en/application-terms-of-use/1-0-0",
        contentId = "testContentId",
        language = "testLanguage",
        copy = "testCopy",
        contentType = "testContentType",
        version = "testVersion",
        timestamp = "testTimeStamp"
    )

    private val finalUrl = "https://aemstagems4-connectivity.fiat.com/it/it/application-terms-of-use/latest.jsonview"

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(CheckTermsLinkExecutor::class)
        mockkConstructor(ReadAssetsTermConditionFcaExecutor::class)
        coEvery { anyConstructed<ReadAssetsTermConditionFcaExecutor>().execute(any()) } returns Success(
            termConditionLocal
        )
        executor = spyk(FetchTermConditionFCAExecutor(middlewareComponent))
        readAssetsTermConditionFcaExecutor = spyk(ReadAssetsTermConditionFcaExecutor(middlewareComponent))
    }

    @After
    fun tearDown() {
        unmockkAll()
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
        Assert.assertEquals(termsConditionsInput.sdp, output.sdp)
        Assert.assertEquals(termsConditionsInput.country, output.country)
        Assert.assertEquals(AppTerms, output.type)
    }

    @Test
    fun `when execute params with missing type then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.TYPE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
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
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
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
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
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
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
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
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid sdp then throw missing parameter`() {
        val type = TermsConditionsInput.Type.AppTerms
        val paramsCountry = "testCountry"
        val paramsSDP = 123
        val input = mapOf(
            Constants.Input.TYPE to type.name,
            Constants.Input.Settings.COUNTRY to paramsCountry,
            Constants.Input.Settings.SDP to paramsSDP
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
    fun `test execute() when mode is LOCAL calls transformFromLocal() and retunrs success response`() {
        val params = TermsConditionsInput(
            type = TermsConditionsInput.Type.AppTerms,
            sdp = "testSDP",
            country = "testCountry"
        )
        mockkConstructor(ReadAssetsTermConditionFcaExecutor::class)
        every { executor.params(any()) } returns params
        coEvery {
            anyConstructed<ReadAssetsTermConditionFcaExecutor>()
                .execute(params)
        } returns Success(termConditionLocal)
        coEvery { executor.transformFromBrowser(any(), any()) } returns Success(applicationTermsOutput)
        coEvery { executor.transformFromLocal(any(), any(), any()) } returns Success(applicationTermsOutput)
        runBlocking {
            val response = executor.execute(params)
            Assert.assertEquals(applicationTermsOutput, (response as Success).response)
            coVerify {
                executor.transformFromLocal(
                    params.type.value,
                    market = termConditionLocal.market.orEmpty(),
                    url = termConditionLocal.url
                )
            }
        }
    }

    @Test
    fun `test execute() when mode is BROWSER calls transformFromBrowser() and retunrs success response`() {
        val termConditionLocal = TermCondition(
            mode = BROWSER,
            url = "testUrl",
            market = "testMarket"
        )
        val params = TermsConditionsInput(
            type = TermsConditionsInput.Type.AppTerms,
            sdp = "testSDP",
            country = "testCountry"
        )
        mockkConstructor(ReadAssetsTermConditionFcaExecutor::class)
        every { executor.params(any()) } returns params
        coEvery {
            anyConstructed<ReadAssetsTermConditionFcaExecutor>()
                .execute(params)
        } returns Success(termConditionLocal)
        coEvery { executor.transformFromBrowser(any(), any()) } returns Success(applicationTermsOutput)
        coEvery { executor.transformFromLocal(any(), any(), any()) } returns Success(applicationTermsOutput)
        runBlocking {
            val response = executor.execute(params)
            Assert.assertEquals(applicationTermsOutput, (response as Success).response)
            coVerify {
                executor.transformFromBrowser(
                    market = termConditionLocal.market.orEmpty(),
                    url = termConditionLocal.url
                )
            }
        }
    }

    @Test
    fun `when execute transformFromLocal then return ApplicationTermsOutput`() {
        coEvery { anyConstructed<CheckTermsLinkExecutor<*>>().execute(any()) } returns
            Success(termsResponse)

        coEvery { executor.transformResponse(any(), any()) } returns applicationTermsOutput

        runTest {
            val response = executor.transformFromLocal(
                "ENDPOINT_APPLICATION_TERMS",
                "https://aemstagems4-connectivity.fiat.com/it/it/application-terms-of-use/latest.jsonview",
                "fr"
            )
            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(applicationTermsOutput, success.response)
        }
    }

    @Test
    fun `when execute transformResponse then return ApplicationTermsOutput`() {
        mockkStatic(android.util.Base64::class)
        every { android.util.Base64.decode("testCopy", android.util.Base64.DEFAULT) } returns "testCopy".toByteArray()
        val expectedOutput = ApplicationTermsOutput(
            content = "testCopy",
            version = "testVersion",
            update = "testVersion",
            language = "testLanguage",
            country = "testCountry"
        )
        runBlocking {
            coEvery { executor.checkVersion("testKey", "testVersion") } returns
                "testVersion"
            val response = executor.transformResponse("testKey", termsResponse)
            Assert.assertEquals(expectedOutput, response)
        }
    }

    @Test
    fun testCheckVersion_FirstTime() {
        val storageKey = "testKey"
        val version = "1.0"
        runTest {
            coJustRun { executor.save(any(), any()) }
            coEvery { executor.read(storageKey + Constants.Storage.CGU_LAST_UPDATE) } returns ""
            coEvery { executor.read(storageKey + Constants.Storage.CGU_VERSION) } returns version

            val result = executor.checkVersion(storageKey, version)
            coVerify(atLeast = 1) {
                executor.save(
                    eq("${storageKey}_${Constants.Storage.CGU_LAST_UPDATE}"),
                    eq(formattedClock)
                )
            }
            coVerify(atLeast = 1) { executor.save(eq("${storageKey}_${Constants.Storage.CGU_VERSION}"), eq(version)) }
            Assert.assertEquals(formattedClock, result)
        }
    }

    @Test
    fun testCheckVersion_SameVersion() {
        val storageKey = "testKey"
        val lastUpdate = "2022-01-01T00:00:00Z"
        val version = "1.2.0"
        coJustRun { executor.save(any(), any()) }
        runTest {
            coEvery {
                executor.read(storageKey + Constants.Storage.CGU_LAST_UPDATE)
            } returns lastUpdate
            coEvery { executor.read(storageKey + Constants.Storage.CGU_VERSION) } returns version

            val result = executor.checkVersion(storageKey, version)
            coVerify(atLeast = 0) { executor.save(any(), any()) }
            Assert.assertEquals(formattedClock, result)
        }
    }

    @Test
    fun testCheckVersion_NewVersion() {
        val storageKey = "testKey"
        val lastUpdate = "2022-01-01T00:00:00Z"
        val lastVersion = "1.2.0"
        val version = "1.3.0"
        runTest {
            coJustRun { executor.save(any(), any()) }
            coEvery {
                executor.read(storageKey + Constants.Storage.CGU_LAST_UPDATE)
            } returns lastUpdate
            coEvery { executor.read(storageKey + Constants.Storage.CGU_VERSION) } returns lastVersion

            val result = executor.checkVersion(storageKey, version)
            coVerify(atLeast = 1) {
                executor.save(
                    eq("${storageKey}_${Constants.Storage.CGU_LAST_UPDATE}"),
                    eq(formattedClock)
                )
            }
            coVerify(atLeast = 1) { executor.save(eq("${storageKey}_${Constants.Storage.CGU_VERSION}"), eq(version)) }
            Assert.assertEquals(formattedClock, result)
        }
    }

    @Test
    fun `test method baseUrl()`() {
        val result = executor.baseUrl(arrayOf("https://www.google.com", "https://www.google.it"))
        Assert.assertEquals("https://www.google.com", result)
        val result1 = executor.baseUrl(arrayOf())
        Assert.assertEquals("", result1)
    }

    @Test
    fun `test transformFromBrowser()`() {
        coEvery { anyConstructed<CheckTermsLinkExecutor<*>>().execute(any()) } returns
            Success("testLink")
        runBlocking {
            val result = executor.transformFromBrowser(finalUrl, "IT")
            Assert.assertEquals("testLink", (result as Success).response.url)
            coVerify { anyConstructed<CheckTermsLinkExecutor<*>>().execute(CheckTermsLinkInput("IT", finalUrl)) }
        }
    }

    @Test
    fun `test save() method calls createSync method `() {
        val dataManager: IDataManager = mockk(relaxed = true)
        mockkStatic(MiddlewareComponent::createSync)
        every { middlewareComponent.dataManager } returns dataManager
        coEvery { middlewareComponent.createSync(any(), any(), any()) } returns true
        runBlocking {
            executor.save("testKey", "testValue")
            coVerify { middlewareComponent.createSync(eq("testKey"), eq("testValue"), eq(StoreMode.APPLICATION)) }
        }
    }
}
