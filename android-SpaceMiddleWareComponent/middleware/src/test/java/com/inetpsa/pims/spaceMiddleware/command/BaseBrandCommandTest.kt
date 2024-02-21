package com.inetpsa.pims.spaceMiddleware.command

import com.inetpsa.mmx.foundation.commandManager.CONTEXT_KEY_BRAND
import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.genericComponent.GenericComponentInterface
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommandTest.TestCommand.TestCommonExecutor
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommandTest.TestCommand.TestPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import com.inetpsa.pims.spaceMiddleware.model.BrandGroup
import com.inetpsa.pims.spaceMiddleware.util.MockWeakReference
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference

class BaseBrandCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private val configurationManager: ConfigurationManager = spyk()
    private val communicationManager: ICommunicationManager = mockk()
    private lateinit var command: TestCommand
    private lateinit var commandCommon: TestCommonCommand

    @Before
    fun setup() {
        command = spyk(TestCommand())
        commandCommon = spyk(TestCommonCommand())
        every { middlewareComponent.configurationManager } returns configurationManager
        every { command.middlewareComponent } returns middlewareComponent
        every { commandCommon.middlewareComponent } returns middlewareComponent
        every { middlewareComponent.communicationManager } returns communicationManager
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when actionType is missing then throw PimsError missing parameter`() {
        every { command.parameters } returns mapOf()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_ACTION_TYPE)

        try {
            command.actionType
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when actionType is invalid then throw PimsError invalid parameter`() {
        every { command.parameters } returns mapOf(Constants.PARAM_ACTION_TYPE to 123)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)

        try {
            command.actionType
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when middlewareComponent is null then throw PimsError component not configured`() {
        every { command.componentReference } returns null
        val exception = PIMSFoundationError.componentNotConfigured(Constants.COMPONENT_NAME)

        try {
            command.middlewareComponent
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when component is different from MiddlewareComponent then throw PimsError component not configured`() {
        val weakRef: WeakReference<GenericComponentInterface?> = MockWeakReference(mockk())
        every { command.componentReference } returns weakRef
        val exception = PIMSFoundationError.componentNotConfigured(Constants.COMPONENT_NAME)

        try {
            command.middlewareComponent
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with PSA brand then invoke getPsaExecutor`() {
        runTest {
            every { configurationManager.brandGroup } returns BrandGroup.PSA
            coEvery { command.getCommonExecutor() } returns null
            val executor = command.getExecutor()

            coVerify(exactly = 1) { command.getCommonExecutor() }
            coVerify(exactly = 1) { command.getPsaExecutor() }
            coVerify(exactly = 0) { command.getFcaExecutor() }
            Assert.assertEquals(true, executor is TestPsaExecutor)
            Assert.assertNull(command.getCommonExecutor())
        }
    }

    @Test
    fun `when execute with FCA brand with defined commonExecutor then invoke getCommonExecutor`() {
        runTest {
            every { configurationManager.brandGroup } returns BrandGroup.FCA
            coEvery { command.getCommonExecutor() } returns null

            val executor = command.getExecutor()
            coVerify(exactly = 1) { command.getCommonExecutor() }
            coVerify(exactly = 1) { command.getFcaExecutor() }
            coVerify(exactly = 0) { command.getPsaExecutor() }
            Assert.assertEquals(true, executor is BaseFcaExecutor)
            Assert.assertNull(command.getCommonExecutor())
        }
    }

    @Test
    fun `when execute with Local on FCA Brand then invoke getCommonExecutor`() {
        runTest {
            every { configurationManager.brandGroup } returns BrandGroup.FCA
            val executor = command.getExecutor()
            coVerify(exactly = 1) { command.getCommonExecutor() }
            coVerify(exactly = 0) { command.getPsaExecutor() }
            coVerify(exactly = 0) { command.getFcaExecutor() }
            Assert.assertEquals(true, executor is TestCommonExecutor)
        }
    }

    @Test
    fun `when execute with Local on PSA Brand then invoke getCommonExecutor`() {
        runTest {
            every { configurationManager.brandGroup } returns BrandGroup.PSA
            val executor = command.getExecutor()
            coVerify(exactly = 1) { command.getCommonExecutor() }
            coVerify(exactly = 0) { command.getPsaExecutor() }
            coVerify(exactly = 0) { command.getFcaExecutor() }
            Assert.assertEquals(true, executor is TestCommonExecutor)
        }
    }

    @Test
    fun `when execute with unknown brand then throw PimsError invalid Parameter`() {
        every { configurationManager.brandGroup } returns BrandGroup.UNKNOWN
        val exception = PIMSFoundationError.invalidParameter(CONTEXT_KEY_BRAND)

        runTest {
            try {
                command.getExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }

            coVerify(exactly = 0) { command.getCommonExecutor() }
            coVerify(exactly = 0) { command.getPsaExecutor() }
            coVerify(exactly = 0) { command.getFcaExecutor() }
        }
    }

    private class TestCommand : BaseBrandCommand() {

        override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = TestPsaExecutor()

        override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = TestFcaExecutor()

        override suspend fun getCommonExecutor(): BaseLocalExecutor<*, *>? = TestCommonExecutor()

        inner class TestPsaExecutor : BasePsaExecutor<Unit, Unit>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): NetworkResponse<Unit> = NetworkResponse.Success(Unit)

            override fun baseUrl(args: Array<String>): String = "https://base.test.com/"
        }

        inner class TestFcaExecutor : BaseFcaExecutor<Unit, String>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): NetworkResponse<String> = NetworkResponse.Success("test")
        }

        inner class TestCommonExecutor : BaseLocalExecutor<Unit, String>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): NetworkResponse<String> = NetworkResponse.Success("test")
        }
    }

    private class TestCommonCommand : BaseBrandCommand() {

        override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = TestPsaExecutor()

        override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = TestFcaExecutor()

        override suspend fun getCommonExecutor(): BaseLocalExecutor<*, *> = TestCommonExecutor()

        private inner class TestPsaExecutor : BasePsaExecutor<Unit, Unit>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): NetworkResponse<Unit> = NetworkResponse.Success(Unit)
        }

        private inner class TestFcaExecutor : BaseFcaExecutor<Unit, String>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): NetworkResponse<String> = NetworkResponse.Success("test")
        }

        private inner class TestCommonExecutor : BaseLocalExecutor<Unit, String>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): NetworkResponse<String> = NetworkResponse.Success("test")
        }
    }
}
