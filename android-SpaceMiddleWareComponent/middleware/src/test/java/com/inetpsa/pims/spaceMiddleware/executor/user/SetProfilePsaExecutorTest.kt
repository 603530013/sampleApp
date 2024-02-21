package com.inetpsa.pims.spaceMiddleware.executor.user

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileInput
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.asJson
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class SetProfilePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: SetProfilePsaExecutor
    private val profileInput = ProfileInput(
        firstName = "testFirstName",
        lastName = "testLastName",
        civility = null,
        address1 = null,
        address2 = null,
        zipCode = null,
        city = null,
        country = null,
        phone = null,
        mobile = null,
        mobilePro = null
    )
    private val profileOutput = ProfileOutput(
        uid = "",
        email = "testEmail",
        firstName = "testFirstName",
        lastName = "testLastName",
        phones = mapOf(),
        civility = null,
        address1 = null,
        address2 = null,
        zipCode = null,
        city = null,
        country = null,
        civilityCode = null,
        locale = null
    )

    @Before
    override fun setup() {
        super.setup()
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager

        executor = spyk(SetProfilePsaExecutor(baseCommand))
    }

    @Test
    fun `when execute and communicationManager update Success then return Success response`() {
        coEvery {
            communicationManager.update<String>(any(), any())
        } returns NetworkResponse.Success(SetProfilePsaExecutor.RESPONSE_SUCCESSFULLY)

        runTest {
            val response = executor.execute(profileInput)
            verify {
                executor.request(
                    type = eq(String::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/profile")),
                    body = eq(profileInput.asJson())
                )
            }
            coVerify {
                communicationManager.update<String>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            Assert.assertTrue(response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute and communicationManager update Failure then return updateProfileFailed response`() {
        coEvery {
            communicationManager.update<String>(any(), any())
        } returns NetworkResponse.Failure(PimsErrors.zeroResults("test_params"))

        runTest {
            val response = executor.execute(profileInput)
            verify {
                executor.request(
                    type = eq(String::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/profile")),
                    body = eq(profileInput.asJson())
                )
            }
            coVerify {
                communicationManager.update<String>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            Assert.assertTrue(response is NetworkResponse.Failure)
        }
    }

    @Test
    fun `when execute params with the right profile then return profile`() {
        val prof = mapOf(
            Constants.Input.Profile.EMAIL to "testEmail",
            Constants.Input.Profile.FIRST_NAME to "testFirstName",
            Constants.Input.Profile.LAST_NAME to "testLastName"
        )
        val input = mapOf(Constants.PARAMS_KEY_PROFILE to prof)

        val param = executor.params(input)
        Assert.assertEquals(profileInput, param)
    }

    @Test
    fun `when execute params with missing profile then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAMS_KEY_PROFILE)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with empty profile then throw missing parameter`() {
        val exception = PIMSFoundationError.missingParameter(Constants.PARAMS_KEY_PROFILE)
        val profile = emptyMap<String, String>()
        val input = mapOf(Constants.PARAMS_KEY_PROFILE to profile)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid profile then throw missing parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.PARAMS_KEY_PROFILE to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAMS_KEY_PROFILE)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute readFromCache in available case then return cache`() {
        every { dataManager.read(any(), any()) } returns profileOutput.toJson()
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_profile"), eq(APPLICATION)) }
        Assert.assertEquals(profileOutput, cache)
    }

    @Test
    fun `when execute readFromCache in empty case then return null`() {
        every { dataManager.read(any(), any()) } returns "  "
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_profile"), eq(APPLICATION)) }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute saveOnCache then save profile in cache`() {
        every {
            dataManager.create(
                key = any(),
                data = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }

        runTest { executor.saveOnCache(profileOutput) }

        verify {
            dataManager.create(
                key = eq("PEUGEOT_PREPROD_testCustomerId_profile"),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }

    @Test
    fun `when update cache then read from cache and save new data`() {
        every { dataManager.read(any(), any()) } returns profileOutput.toJson()
        coJustRun { executor.saveOnCache(any()) }
        runTest {
            executor.updateCache(profileInput)
            verify { executor.readFromCache() }
            coVerify { executor.saveOnCache(any()) }
        }
    }
}
