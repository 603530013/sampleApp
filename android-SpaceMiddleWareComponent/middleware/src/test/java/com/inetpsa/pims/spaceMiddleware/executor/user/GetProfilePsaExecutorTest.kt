package com.inetpsa.pims.spaceMiddleware.executor.user

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Storage
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Add
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Remove
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetProfilePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetProfilePsaExecutor
    private val profileOutput = ProfileOutput(
        uid = "testUid",
        email = "testEmail",
        firstName = "testFirstName",
        lastName = "testLastName",
        civility = "testCivility",
        civilityCode = "testMRS",
        locale = "fr",
        phones = mapOf(ProfileOutput.KEY_PHONE_MOBILE to "+33600000001"),
        address1 = "testAddress1",
        address2 = "testAddress2",
        zipCode = "testZipCode",
        city = "testCity",
        country = "testCountry"
    )

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetUserPsaExecutor::class)
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager

        executor = spyk(GetProfilePsaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with missing action then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid action then throw invalid parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.Input.ACTION to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with add action then throw invalid parameter`() {
        val input = mapOf(Constants.Input.ACTION to Action.Add)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with remove action then throw invalid parameter`() {
        val input = mapOf(Constants.Input.ACTION to Action.Remove)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then continue execution`() {
        val input = mapOf(Constants.Input.ACTION to Constants.Input.Action.GET)
        val output = executor.params(input)
        Assert.assertEquals(Action.Get, output.action)
        Assert.assertEquals(null, output.vin)
    }

    @Test
    fun `when execute params with invalid vin then throw invalid parameter`() {
        val vin = 123
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.VIN)
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
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readFromCache()
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_profile"), eq(APPLICATION)) }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute with action get and there is not a valid cache then make a network call`() {
        every { executor.readFromCache() } returns null
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            executor.execute(UserInput(Get, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 2) { executor.readFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }
        }
    }

    @Test
    fun `when execute with action add then throw invalid parameter`() {
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        runTest {
            try {
                executor.execute(UserInput(Add, null))
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when execute with action remove then throw invalid parameter`() {
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        runTest {
            try {
                executor.execute(UserInput(Remove, null))
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        every { executor.readFromCache() } returns profileOutput

        runTest {
            val response = executor.execute(UserInput(Get, null))
            verify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = 0) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(profileOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response`() {
        every { executor.readFromCache() } returns profileOutput
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            val response = executor.execute(UserInput(Refresh, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(profileOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with failure response`() {
        every { executor.readFromCache() } returns profileOutput
        val error = PimsErrors.serverError(null, "test-errors")
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute(UserInput(Refresh, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response but empty cache`() {
        every { executor.readFromCache() } returns null
        coEvery { anyConstructed<GetUserPsaExecutor>().execute(any()) } returns NetworkResponse.Success(Unit)

        runTest {
            val response = executor.execute(UserInput(Refresh, null))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = 1) { anyConstructed<GetUserPsaExecutor>().execute(any()) }

            val error = PIMSFoundationError.invalidReturnParam(Storage.PROFILE)
            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }
}
