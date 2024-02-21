package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.data.IDataManager
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.mmx.foundation.tools.UserSession
import com.inetpsa.mmx.foundation.userSession.UserSessionManager
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.manager.ConfigurationManager
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DataManagerExtensionsKtTest {

    private val configurationManager: ConfigurationManager = spyk()
    private val middlewareComponent: MiddlewareComponent = mockk()
    private val command: BaseCommand = mockk()
    private val userSessionManager: UserSessionManager = mockk()
    private val userSession: UserSession = mockk()
    private val dataManager: IDataManager = mockk(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    @Before
    fun setup() {
        every { middlewareComponent.configurationManager } returns configurationManager
        every { command.middlewareComponent } returns middlewareComponent
        every { command.parameters } returns mapOf()
        every { middlewareComponent.userSessionManager } returns userSessionManager
        every { userSessionManager.getUserSession() } returns userSession
        every { userSession.customerId } returns "test_uid"
        every { middlewareComponent.dataManager } returns dataManager
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when createSync and key is null then return false`() {
        val key: String? = null
        val data = "data"
        val mode = StoreMode.APPLICATION
        runTest {
            val actual = key?.let { middlewareComponent.createSync(it, data, mode) }
            actual?.let { assertFalse(it) }
        }
    }

    @Test
    fun `when deleteSync and key is null then return false`() {
        val key: String? = null
        val mode = StoreMode.APPLICATION
        runTest {
            val actual = key?.let { middlewareComponent.deleteSync(it, mode) }
            actual?.let { assertFalse(it) }
        }
    }

    @Test
    fun `when deleteSync and key is not null then return true`() {
        val key = "key"
        val mode = StoreMode.APPLICATION
        testScope.launch {
            val actual = key.let { middlewareComponent.deleteSync(it, mode) }
            assertTrue(actual)
        }
    }

    @Test
    fun `when createSync and key is not null then return true`() {
        val key = "key"
        val data = "data"
        val mode = StoreMode.APPLICATION
        testScope.launch {
            val actual = key.let { middlewareComponent.createSync(it, data, mode) }
            assertTrue(actual)
        }
    }

    @Test
    fun `when updateSync and key is null then return false`() {
        val key: String? = null
        val data = "data"
        val mode = StoreMode.APPLICATION
        runTest {
            val actual = key?.let { middlewareComponent.updateSync(it, data, mode) }
            actual?.let { assertFalse(it) }
        }
    }

    @Test
    fun `when updateSync and key is not null then return true`() {
        val key = "key"
        val data = "data"
        val mode = StoreMode.APPLICATION
        testScope.launch {
            val actual = key.let { middlewareComponent.updateSync(it, data, mode) }
            assertTrue(actual)
        }
    }

    @Test
    fun `when readSync and key is null then return null`() {
        val key: String? = null
        val mode = StoreMode.APPLICATION
        runTest {
            val actual = key?.let { middlewareComponent.readSync<String>(it, mode) }
            actual?.let { assertTrue(it.isBlank()) }
        }
    }

    @Test
    fun `when readSync and key is not null then return data`() {
        val key = "key"
        val data = "data"
        val mode = StoreMode.APPLICATION
        every { dataManager.read(key = key, mode = mode) } returns data
        testScope.launch {
            val actual = key.let { middlewareComponent.readSync<String>(it, mode) }
            assertTrue(actual == data)
        }
    }

    @Test
    fun `when readSync and key is not null then return null`() {
        val key = "key"
        val data = ""
        val mode = StoreMode.APPLICATION
        every { dataManager.read(key = key, mode = mode) } returns data
        testScope.launch {
            val actual = key.let { middlewareComponent.readSync<String>(it, mode) }
            assertTrue(actual.isNullOrBlank())
        }
    }

    @Test
    fun `when readSync and key is not null then return data from json`() {
        val key = "key"
        val data = "test"
        val mode = StoreMode.APPLICATION
        every { dataManager.read(key = key, mode = mode) } returns data
        testScope.launch {
            val actual = key.let { middlewareComponent.readSync<String>(it, mode) }
            assertTrue(actual == data)
        }
    }

    @Test
    fun `when readSync and key is not null then return null from json`() {
        val key = "key"
        val data = ""
        val mode = StoreMode.APPLICATION
        every { dataManager.read(key = key, mode = mode) } returns data
        testScope.launch {
            val actual = key.let { middlewareComponent.readSync<String>(it, mode) }
            assertTrue(actual.isNullOrBlank())
        }
    }

    @Test
    fun `when readSync and data is null then return null from json`() {
        val key = "key"
        val data = null
        val mode = StoreMode.APPLICATION
        every { dataManager.read(key = key, mode = mode) } returns data
        testScope.launch {
            val actual = key.let { middlewareComponent.readSync<String>(it, mode) }
            assertTrue(actual.isNullOrBlank())
        }
    }

    @Test
    fun `when readSync and data is null then return null`() {
        val key = "key"
        val data = null
        val mode = StoreMode.APPLICATION
        every { dataManager.read(key = key, mode = mode) } returns data
        testScope.launch {
            val actual = key.let { middlewareComponent.readSync<String>(it, mode) }
            assertTrue(actual.isNullOrBlank())
        }
    }
}
