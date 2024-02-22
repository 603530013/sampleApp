package com.mobiledrivetech.external.sample.framework.datasource

import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.sample.data.model.ApiName
import com.mobiledrivetech.external.sample.data.model.PARAMS_KEY_ACTION_TYPE
import com.mobiledrivetech.external.sample.data.model.PARAMS_VALUE_TEST_ACTION
import com.mobiledrivetech.external.sample.data.model.RESULT_KEY
import com.mobiledrivetech.external.sample.data.model.RESULT_KEY_TEST
import com.mobiledrivetech.external.sample.domain.models.Commands
import com.mobiledrivetech.external.sample.providers.FacadeDataProvider
import io.mockk.clearAllMocks
import io.mockk.coEvery
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
class TestDataSourceImplTest {
    private val facadeDataProvider: FacadeDataProvider = mockk()
    private lateinit var testDataSourceImpl: TestDataSourceImpl

    @Before
    fun setUp() {
        testDataSourceImpl = spyk(TestDataSourceImpl(facadeDataProvider))
        mockkObject(MDLog)
        justRun { MDLog.debug(any(), any(), any()) }
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when call initialize then initialize`() = runTest {
        // Arrange
        val params = mapOf("key" to "value")
        val expect: Map<String, Any?> = mockk()
        coEvery {
            facadeDataProvider.fetch(
                any(),
                FacadeDataProvider.Method.INITIALIZE,
                any()
            )
        } returns expect

        // Act
        val result = testDataSourceImpl.initialize(params)

        // Assert
        coVerify {
            facadeDataProvider.fetch(
                ApiName.Middleware.Initialize,
                FacadeDataProvider.Method.INITIALIZE,
                emptyMap()
            )
        }
        Assert.assertEquals(expect, result)
    }

    @Test
    fun `when call getTestCommandResult then get result for test command`() = runTest {
        // Arrange
        val expectedMap = mapOf("result" to mapOf("test" to "test_action_type"))
        coEvery {
            facadeDataProvider.fetch(
                any(),
                FacadeDataProvider.Method.GET,
                any()
            )
        } returns expectedMap

        // Act
        val result = testDataSourceImpl.getTestCommandResult()

        // Assert
        coVerify {
            facadeDataProvider.fetch(
                ApiName.Middleware.Test,
                FacadeDataProvider.Method.GET,
                parameter = mapOf(PARAMS_KEY_ACTION_TYPE to PARAMS_VALUE_TEST_ACTION)
            )
        }
        Assert.assertEquals(Result.success(expectedMap[RESULT_KEY]?.get(RESULT_KEY_TEST)), result)
    }

    @Test
    fun `when call executeCommand then get correct result`() = runTest {
        // Arrange
        val command = Commands.TestCommand
        val expectedMap = mapOf("result" to mapOf("test" to "test_action_type"))
        coEvery {
            facadeDataProvider.fetch(
                any(),
                FacadeDataProvider.Method.GET,
                any()
            )
        } returns expectedMap

        // Act
        val result = testDataSourceImpl.executeCommand(command)

        // Assert
        coVerify {
            facadeDataProvider.fetch(
                command.commandName,
                command.commandType,
                command.commandParams
            )
        }
        Assert.assertEquals(Result.success(expectedMap), result)
    }
}
