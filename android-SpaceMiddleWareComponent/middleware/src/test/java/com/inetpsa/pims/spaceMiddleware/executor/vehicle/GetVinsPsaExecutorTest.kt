package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.VehicleListPsa.VehiclePsa
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetVinsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVinsPsaExecutor

    private val vehicles = listOf(
        VehiclePsa(command = null, vin = "testVin1", lcdv = null, shortLabel = "label1"),
        VehiclePsa(command = null, vin = "testVin2", lcdv = null, shortLabel = "label2"),
        VehiclePsa(command = null, vin = "testVin3", lcdv = null, shortLabel = "label3")
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetVinsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        coEvery { communicationManager.get<List<VehiclePsa>>(any(), any()) } returns
            NetworkResponse.Success(vehicles)

        runTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(object : TypeToken<List<VehiclePsa>>() {}.type),
                    urls = eq(arrayOf("/me/v1/vehicles")),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<List<VehiclePsa>>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(vehicles, success.response.vehicleList)
        }
    }
}
