package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.enrollvehicle.EnrollVehiclePsaParams
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

internal class EnrollVehiclePsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: EnrollVehiclePsaExecutor

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(EnrollVehiclePsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a set API call`() {
        val vin = "testVin"
        val mileage = 100
        val testAddVehicle = EnrollVehiclePsaParams(vin, mileage)

        every { executor.params(any()) } returns testAddVehicle

        coEvery { communicationManager.post<Unit>(any(), any()) } returns
            Success(Unit)

        runTest {
            val response = executor.execute()
            val body = testAddVehicle.toJson()
            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(arrayOf("/me/v1/user/vehicles/add")),
                    body = eq(body.toString())
                )
            }

            coVerify {
                communicationManager.post<Unit>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return AddVehiclePsaParams`() {
        val params = EnrollVehiclePsaParams(
            vin = "testVin",
            mileage = 100
        )

        val input = mapOf(
            Constants.BODY_PARAM_VIN to "testVin",
            Constants.PARAM_MILEAGE to 100
        )

        val output = executor.params(input)
        Assert.assertEquals(params, output)
    }
}
