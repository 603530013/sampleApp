package com.inetpsa.pims.spaceMiddleware.executor.user

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.user.GigyaAccountResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.user.GigyaAccountResponse.Profile
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.user.GigyaAccountResponse.Profile.Phone
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetProfileFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetProfileFcaExecutor
    private val gigyaResponse = mapOf(
        "UID" to "testUid",
        "profile" to mapOf(
            "address" to "testAddress",
            "city" to "testCity",
            "country" to "testCountry",
            "zip" to "testZipCode",
            "email" to "testEmail",
            "firstName" to "testFirstName",
            "lastName" to "testLastName",
            "phones" to listOf(mapOf("number" to "testNumber", "type" to "testType")),
            "photoURL" to "testPhoto"
        )
    )
    private val profileResponse = GigyaAccountResponse(
        uid = "testUid",
        uidSignature = null,
        apiVersion = null,
        createdDate = null,
        createdTimestamp = null,
        emails = null,
        isActive = null,
        isRegistered = null,
        isVerified = null,
        lastLoginDate = null,
        lastLoginTimestamp = null,
        lastUpdated = null,
        lastUpdatedTimestamp = null,
        loginProvider = null,
        oldestDataUpdated = null,
        oldestDataUpdatedTimestamp = null,
        profile = Profile(
            activities = null,
            address = "testAddress",

            city = "testCity",
            country = "testCountry",
            zip = "testZipCode",
            email = "testEmail",
            firstName = "testFirstName",
            lastName = "testLastName",
            phones = listOf(Phone(number = "testNumber", type = "testType")),
            photoURL = "testPhoto",
            age = null,
            bio = null,
            birthDay = null,
            birthMonth = null,
            birthYear = null,
            educationLevel = null,
            followersCounts = null,
            followingCount = null,
            gender = null,
            hometown = null,
            honors = null,
            industry = null,
            interestedIn = null,
            interests = null,
            languages = null,
            lastLoginLocation = null,
            locale = null,
            name = null,
            nickname = null,
            politicalView = null,
            professionalHeadline = null,
            profileURL = null,
            proxyEmail = null,
            relationshipStatus = null,
            religion = null,
            specialities = null,
            state = null,
            thumbnailURL = null,
            timezone = null,
            username = null,
            verified = null
        ),
        registered = null,
        registeredTimestamp = null,
        signatureTimestamp = null,
        socialProviders = null,
        verified = null,
        verifiedTimestamp = null
    )
    private val profileOutput = ProfileOutput(
        uid = "testUid",
        email = "testEmail",
        firstName = "testFirstName",
        lastName = "testLastName",
        civility = null,
        civilityCode = null,
        locale = null,
        phones = mapOf("testType" to "testNumber"),
        address1 = "testAddress",
        address2 = null,
        zipCode = "testZipCode",
        city = "testCity",
        country = "testCountry",
        image = "testPhoto"
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetProfileFcaExecutor(baseCommand), recordPrivateCalls = true)
    }

    @Test
    fun `when execute params with empty input then continue execution`() {
        val output = executor.params(null)
        Assert.assertEquals(Unit, output)
    }

    @Test
    fun `when execute readFromCache is available case then return cache`() {
        every { userSessionManager.getFCAUserProfile() } returns gigyaResponse
        val cache = executor.readFromCache()
        verify(exactly = 1) { userSessionManager.getFCAUserProfile() }
        Assert.assertEquals(profileResponse, cache)
    }

    @Test
    fun `when execute readFromCache in empty case then return null`() {
        every { userSessionManager.getFCAUserProfile() } returns null
        val cache = executor.readFromCache()
        verify(exactly = 1) { userSessionManager.getFCAUserProfile() }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        every { executor.readFromCache() } returns profileResponse
        every { executor.transformToProfileOutput(any()) } returns profileOutput

        runTest {
            val response = executor.execute()

            coVerify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = 1) { executor.transformToProfileOutput(eq(profileResponse)) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = (response as NetworkResponse.Success).response
            Assert.assertEquals(profileOutput, success)
        }
    }

    @Test
    fun `when execute then make a network API call with failure response`() {
        every { executor.readFromCache() } returns null

        runTest {
            val response = executor.execute()

            coVerify(exactly = 1) { executor.readFromCache() }
            coVerify(exactly = 0) { executor.transformToProfileOutput(eq(profileResponse)) }

            val error = PIMSFoundationError.invalidReturnParam(Constants.Storage.PROFILE)
            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = (response as NetworkResponse.Failure).error
            Assert.assertEquals(error.code, failure?.code)
            Assert.assertEquals(error.message, failure?.message)
            Assert.assertEquals(error.subError?.status, failure?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure?.subError?.body)
        }
    }

    @Test
    fun `when transformToProfileOutput then get profileOutput`() {
        val response = executor.transformToProfileOutput(profileResponse)
        Assert.assertEquals(profileOutput, response)
    }
}
