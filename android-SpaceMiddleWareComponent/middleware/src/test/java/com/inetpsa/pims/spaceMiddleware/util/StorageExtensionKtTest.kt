package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Environment
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
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StorageExtensionKtTest {

    private val configurationManager: ConfigurationManager = spyk()
    private val middlewareComponent: MiddlewareComponent = mockk()
    private val command: BaseCommand = mockk()

    @Before
    fun setup() {
        every { middlewareComponent.configurationManager } returns configurationManager
        every { command.middlewareComponent } returns middlewareComponent
        every { command.parameters } returns mapOf()
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when generateStorageKey and customerID is null then return key`() {
        val userSessionManager: UserSessionManager = mockk()
        val userSession: UserSession = mockk()
        every { middlewareComponent.userSessionManager } returns userSessionManager
        every { userSessionManager.getUserSession() } returns userSession
        every { userSession.customerId } returns null
        val key = "key"
        val actual = middlewareComponent.generateStorageKey(key)
        Assert.assertEquals("null_null_$key", actual)
    }

    @Test
    fun `when generateStorageKey and customerID is not null then return key`() {
        val userSessionManager: UserSessionManager = mockk()
        val userSession: UserSession = mockk()
        every { middlewareComponent.userSessionManager } returns userSessionManager
        every { userSessionManager.getUserSession() } returns userSession
        every { configurationManager.brand } returns Brand.PEUGEOT
        every { configurationManager.environment } returns Environment.PREPROD
        every { userSession.customerId } returns "test_uid"
        val key = "key"
        val actual = middlewareComponent.generateStorageKey(key)
        Assert.assertEquals("PEUGEOT_PREPROD_test_uid_$key", actual)
    }
}
