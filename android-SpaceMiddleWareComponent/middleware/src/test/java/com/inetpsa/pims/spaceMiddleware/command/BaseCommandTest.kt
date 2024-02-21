package com.inetpsa.pims.spaceMiddleware.command

import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.genericComponent.GenericComponentInterface
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseExecutor
import com.inetpsa.pims.spaceMiddleware.util.MockWeakReference
import io.mockk.clearAllMocks
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

class BaseCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: TestCommand

    @Before
    fun setup() {
        command = spyk(TestCommand())
        every { command.middlewareComponent } returns middlewareComponent
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
            command.execute { }
        }
        coVerify { command.getExecutor() }
    }

    private class TestCommand : BaseCommand() {

        override suspend fun getExecutor(): BaseExecutor<*, *> = TestPsaExecutor()

        private inner class TestPsaExecutor : BaseExecutor<Unit, Unit>(this) {

            override fun params(parameters: Map<String, Any?>?) = Unit

            override suspend fun execute(input: Unit): NetworkResponse<Unit> = NetworkResponse.Success(Unit)
            override val communicationManager: ICommunicationManager
                get() = mockk()

            override fun baseUrl(args: Array<String>): String = "https://base.test.com/"

            override fun baseHeaders(): Map<String, String> = emptyMap()

            override fun baseQueries(): Map<String, String> = emptyMap()
        }
    }
}
