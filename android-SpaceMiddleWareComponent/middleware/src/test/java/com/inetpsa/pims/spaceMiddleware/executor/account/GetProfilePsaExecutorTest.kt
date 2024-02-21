package com.inetpsa.pims.spaceMiddleware.executor.account

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.profile.ProfileOutput
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Deprecated("try to switch to use this class GetProfilePsaExecutorTest")
internal class GetProfilePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetProfilePsaExecutor

    private val oldProfileOutput = ProfileOutput(
        uid = "testUid",
        email = "testEmail",
        firstName = "testFirstName",
        lastName = "testLastName",
        civility = "testCivility",
        civilityCode = "testMRS",
        locale = "fr",
        phones = listOf("+33600000001"),
        address1 = "testAddress1",
        address2 = "testAddress2",
        zipCode = "testZipCode",
        city = "testCity",
        country = "testCountry"
    )

    private val profileOutput = com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput(
        uid = "testUid",
        email = "testEmail",
        firstName = "testFirstName",
        lastName = "testLastName",
        civility = "testCivility",
        civilityCode = "testMRS",
        locale = "fr",
        phones = mapOf("mobile" to "+33600000001"),
        address1 = "testAddress1",
        address2 = "testAddress2",
        zipCode = "testZipCode",
        city = "testCity",
        country = "testCountry"
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetProfilePsaExecutor::class)
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        executor = spyk(GetProfilePsaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with empty input then continue execution`() {
        val output = executor.params(null)
        Assert.assertEquals(Unit, output)
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        every { dataManager.read(any(), any()) } returns profileOutput.toJson()

        runTest {
            val response = executor.execute()

            verify {
                dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_profile"), eq(APPLICATION))
            }
            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(oldProfileOutput.toJson(), success.response)
        }
    }

    @Test
    fun `when execute then make a network API call with failure response`() {
        every { dataManager.read(any(), any()) } returns null

        runTest {
            val response = executor.execute()

            val error = PIMSFoundationError.invalidReturnParam(Constants.Storage.PROFILE)
            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(error.code, failure?.code)
            Assert.assertEquals(error.message, failure?.message)
            Assert.assertEquals(error.subError?.status, failure?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure?.subError?.body)
        }
    }
}
