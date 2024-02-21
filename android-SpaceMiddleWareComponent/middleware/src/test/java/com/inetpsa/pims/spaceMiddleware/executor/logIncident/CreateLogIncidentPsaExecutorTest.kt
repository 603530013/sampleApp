package com.inetpsa.pims.spaceMiddleware.executor.logIncident

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.logincident.CreateLogIncident
import com.inetpsa.pims.spaceMiddleware.model.logincident.CreateLogIncidentPsaParams
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class CreateLogIncidentPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: CreateLogIncidentPsaExecutor

    private val createLogIncident = CreateLogIncident(
        incidentId = "test_incidentId"
    )

    @Before
    override fun setup() {
        super.setup()
        every { baseCommand.parameters } returns mapOf(
            Constants.PARAM_URL to "https://api-log-incident-rp.mym.awsmpsa.com/c1st"
        )
        executor = spyk(CreateLogIncidentPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a post API call`() {
        val input = CreateLogIncidentPsaParams(
            siteCode = "OP_BE_ESP",
            vin = "test_vin",
            firstName = "test_firstName",
            lastName = "test_lastName",
            nationalID = null,
            phone = "+32",
            zipCode = "273",
            city = "New Taipei City",
            email = "solarus.126.prod@yopmail.com",
            idClient = "test_idClient",
            culture = "fr_BE",
            title = "test",
            comment = "just for test",
            optin1 = false,
            optin2 = true
        )
        every { executor.params(any()) } returns input
        coEvery { communicationManager.post<CreateLogIncident>(any(), any()) } returns
            NetworkResponse.Success(createLogIncident)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(CreateLogIncident::class.java),
                    urls = eq(arrayOf("v1/logincident")),
                    headers = any(),
                    queries = any(),
                    body = eq(input.toJson())
                )
            }

            coVerify {
                communicationManager.post<CreateLogIncident>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(createLogIncident, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return LogIncidentPsaParams`() {
        val url = "https://api-log-incident-rp.mym.awsmpsa.com/c1st"

        val params = CreateLogIncidentPsaParams(
            siteCode = "OP_BE_ESP",
            vin = "test_vin",
            firstName = "test_firstName",
            lastName = "test_lastName",
            nationalID = null,
            phone = "+32",
            zipCode = "273",
            city = "New Taipei City",
            email = "solarus.126.prod@yopmail.com",
            idClient = "test_idClient",
            culture = "fr_BE",
            title = "test",
            comment = "just for test",
            optin1 = false,
            optin2 = true
        )

        val input = mapOf(
            Constants.PARAM_URL to url,
            Constants.BODY_PARAM_SITE_CODE to "OP_BE_ESP",
            Constants.BODY_PARAM_VIN to "test_vin",
            Constants.BODY_PARAM_FIRST_NAME to "test_firstName",
            Constants.BODY_PARAM_LAST_NAME to "test_lastName",
            Constants.BODY_PARAM_NATION_ID to null,
            Constants.BODY_PARAM_PHONE to "+32",
            Constants.BODY_PARAM_ZIP_CODE to "273",
            Constants.BODY_PARAM_CITY to "New Taipei City",
            Constants.BODY_PARAM_EMAIL to "solarus.126.prod@yopmail.com",
            Constants.BODY_PARAM_ID_CLIENT to "test_idClient",
            Constants.BODY_PARAM_CULTURE to "fr_BE",
            Constants.BODY_PARAM_TITLE to "test",
            Constants.BODY_PARAM_COMMENT to "just for test",
            Constants.BODY_PARAM_OPTIN_ONE to false,
            Constants.BODY_PARAM_OPTIN_TWO to true

        )

        val output = executor.params(input)

        Assert.assertEquals(params, output)
    }
}
