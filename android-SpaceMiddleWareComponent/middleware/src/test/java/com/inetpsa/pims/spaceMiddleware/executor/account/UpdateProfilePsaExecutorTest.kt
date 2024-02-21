package com.inetpsa.pims.spaceMiddleware.executor.account

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.profile.ProfileParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.asJson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Deprecated("try to switch to use this class SetProfilePsaExecutorTest")
internal class UpdateProfilePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: UpdateProfilePsaExecutor
    private val profile = ProfileParams(
        email = "testEmail",
        firstName = "testFirstName",
        lastName = "testLastName",
        civility = null,
        address1 = "",
        address2 = "",
        zipCode = "",
        city = "",
        country = "",
        phone = "",
        mobile = "",
        mobilePro = ""
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(UpdateProfilePsaExecutor(baseCommand))
    }

    @Test
    fun `when execute and communicationManager update Success then return Success response`() {
        coEvery {
            communicationManager.update<String>(any(), any())
        } returns NetworkResponse.Success(Constants.RESPONSE_RESULT_PROFILE_UPDATE_SUCCESSFULLY)

        runTest {
            val response = executor.execute(profile)
            verify {
                executor.request(
                    type = eq(String::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/profile")),
                    body = eq(profile.asJson())
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
            val response = executor.execute(profile)
            verify {
                executor.request(
                    type = eq(String::class.java),
                    urls = eq(arrayOf("/me/v1/user_data/profile")),
                    body = eq(profile.asJson())
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
            Constants.PARAMS_KEY_PROFILE_EMAIL to "testEmail",
            Constants.PARAMS_KEY_PROFILE_FIRST_NAME to "testFirstName",
            Constants.PARAMS_KEY_PROFILE_LAST_NAME to "testLastName"
        )
        val input = mapOf(Constants.PARAMS_KEY_PROFILE to prof)

        val param = executor.params(input)
        Assert.assertEquals(profile, param)
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
}
