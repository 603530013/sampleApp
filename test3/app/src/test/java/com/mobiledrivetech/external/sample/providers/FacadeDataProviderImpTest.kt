package com.mobiledrivetech.external.sample.providers

import com.mobiledrivetech.external.middleware.IMiddleware
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.sample.data.model.ApiName
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FacadeDataProviderImpTest {

    private lateinit var facadeDataProviderImp: FacadeDataProviderImp
    private val middleware: IMiddleware = mockk()

    @Before
    fun setUp() {
        facadeDataProviderImp = spyk(FacadeDataProviderImp(middleware))
        mockkObject(MDLog)
        justRun { MDLog.debug(any(), any(), any()) }
        justRun { MDLog.inform(any(), any(), any()) }
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when fetch with INITIALIZE then middleware initialized`() = runTest {
        // Arrange
        val parameter: Map<String, Any> = emptyMap()
        coEvery { middleware.initialize(parameter, any()) } coAnswers {
            secondArg<(Map<String, Any>) -> Unit>().invoke(emptyMap())
        }

        // act
        facadeDataProviderImp.fetch(
            ApiName.Middleware.Initialize,
            FacadeDataProvider.Method.INITIALIZE,
            parameter = parameter
        )

        // Assert
        coVerify { facadeDataProviderImp.initialize(parameter) }
        coVerify { middleware.initialize(parameter, any()) }
    }

    @Test
    fun `when fetch with GET then get response from middleware`() = runTest {
        // Arrange
        val apiName = ApiName.Middleware.Test
        val parameter = mapOf("param1" to "param")
        val expectedResponse = mapOf("key" to "value")
        coEvery { middleware.get(apiName.name, any(), any()) } coAnswers {
            val callback = thirdArg<(Map<String, Any?>) -> Unit>()
            callback(expectedResponse).toString()
        }

        // Act
        val result = facadeDataProviderImp.fetch(
            ApiName.Middleware.Test,
            FacadeDataProvider.Method.GET,
            parameter = parameter
        )

        // Assert
        coVerify { facadeDataProviderImp.get(ApiName.Middleware.Test, parameter) }
        coVerify { middleware.get(ApiName.Middleware.Test.name, parameter, any()) }
        Assert.assertEquals(expectedResponse, result)
    }

    @Test
    fun `when fetch with SET then get response from middleware`() = runTest {
        // Arrange
        val apiName = ApiName.Middleware.Test
        val parameter = mapOf("param1" to "param")
        val expectedResponse = mapOf("key" to "value")
        coEvery { middleware.set(apiName.name, any(), any()) } coAnswers {
            val callback = thirdArg<(Map<String, Any?>) -> Unit>()
            callback(expectedResponse).toString()
        }

        // Act
        val result = facadeDataProviderImp.fetch(
            ApiName.Middleware.Test,
            FacadeDataProvider.Method.SET,
            parameter = parameter
        )

        // Assert
        coVerify { facadeDataProviderImp.set(ApiName.Middleware.Test, parameter) }
        coVerify { middleware.set(ApiName.Middleware.Test.name, parameter, any()) }
        Assert.assertEquals(expectedResponse, result)
    }

    @Test
    fun `when fetch with RELEASE then get response from middleware`() = runTest {
        // Arrange
        coJustRun { middleware.release() }

        // Act
        facadeDataProviderImp.fetch(
            ApiName.Middleware.Test,
            FacadeDataProvider.Method.RELEASE,
            parameter = emptyMap()
        )

        // Assert
        coVerify { facadeDataProviderImp.release() }
        coVerify { middleware.release() }
    }
}
