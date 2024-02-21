package com.inetpsa.pims.spaceMiddleware.executor.assistance

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.AssistanceResponse
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

@Deprecated("try to switch to use this class GetAssistancePSAExecutorTest")
internal class GetInfoForAssistancePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetInfoForAssistancePsaExecutor

    private val assistanceResponse = AssistanceResponse(
        id = "id",
        caseNumber = "caseNumber",
        status = "status",
        driverFirstname = null,
        driverLastname = null,
        driverPhoneNumber = null,
        vehicleLicensePlate = null,
        vehicleLocationAddressStreet = null,
        vehicleLocationAddressStreetNumber = null,
        vehicleLocationAddressPostalCode = null,
        vehicleLocationAddressCity = null,
        vehicleLocationCoordinatesLatitude = null,
        vehicleLocationCoordinatesLongitude = null,
        breakdownCategory = null,
        patrolCompanyName = null,
        patrolCallCenterPhoneNumber = null,
        patrolLatitude = null,
        patrolLongitude = null,
        estimatedArrivalTime = null,
        lastUpdate = null
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetInfoForAssistancePsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val paramsId = "testID"
        every { executor.params(any()) } returns paramsId
        coEvery { communicationManager.get<AssistanceResponse>(any(), any()) } returns NetworkResponse.Success(
            assistanceResponse
        )

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(AssistanceResponse::class.java),
                    urls = eq(arrayOf("/car/v1/rsa/assistance/", paramsId)),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<AssistanceResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(assistanceResponse, success.response)
        }
    }

    @Test
    fun `when execute params with the right paramID then return paramId`() {
        val paramsId = "testID"
        val input = mapOf(Constants.PARAM_ID to paramsId)
        val param = executor.params(input)

        Assert.assertEquals(paramsId, param)
    }

    @Test
    fun `when execute params with missing paramID then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid paramID then throw invalid parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.PARAM_ID to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
