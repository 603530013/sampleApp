package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.LatamDealerServiceResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.LatamDealerServiceResponse.Service
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkConstructor
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class GetLatamServiceFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetLatamServiceFcaExecutor
    private lateinit var executorLatamDealer: GetLatamDealerServicesFcaExecutor
    private lateinit var executorLatamFactory: GetLatamFactoryServicesFcaExecutor
    private lateinit var executorLatamRepair: GetLatamRepairServicesFcaExecutor

    private val dealerServiceResponse = LatamDealerServiceResponse(
        services = listOf(
            Service(
                id = "10",
                name = "testName",
                price = 10.0f,
                description = "testDescription"
            )
        )
    )

    override fun setup() {
        super.setup()
        mockkConstructor(GetLatamDealerServicesFcaExecutor::class)
        coEvery { anyConstructed<GetLatamDealerServicesFcaExecutor>().execute(any()) } returns Success(
            dealerServiceResponse
        )
        executorLatamDealer = spyk(GetLatamDealerServicesFcaExecutor(middlewareComponent))

        mockkConstructor(GetLatamFactoryServicesFcaExecutor::class)
        coEvery { anyConstructed<GetLatamFactoryServicesFcaExecutor>().execute(any()) } returns Success(
            dealerServiceResponse
        )
        executorLatamFactory = spyk(GetLatamFactoryServicesFcaExecutor(middlewareComponent))

        mockkConstructor(GetLatamRepairServicesFcaExecutor::class)
        coEvery { anyConstructed<GetLatamRepairServicesFcaExecutor>().execute(any()) } returns Success(
            dealerServiceResponse
        )
        executorLatamRepair = spyk(GetLatamRepairServicesFcaExecutor(middlewareComponent))

        executor = spyk(GetLatamServiceFcaExecutor(middlewareComponent))
    }

    @Test
    fun `when execute params with the right input then return DealerServicesInput`() {
        val input = ServicesNaftaLatamInput(
            vin = "testVin",
            dealerId = "testDealerID",
            mileage = 120,
            unit = ServicesNaftaLatamInput.MileageUnit.KM
        )

        val params = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "testDealerID",
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.MILEAGE to 120,
            Constants.Input.Appointment.PARAM_MILEAGE_UNIT to "km"
        )
        val paramsInput = executor.params(params)

        Assert.assertEquals(input, paramsInput)
    }

    @Test
    fun `when execute params with missing dealerId then throw missing parameter`() {
        val input = mapOf(Constants.Input.VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(Constants.Input.Appointment.BOOKING_ID to "testDealerID")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing mileage then throw missing parameter`() {
        val input = mapOf(Constants.Input.Appointment.BOOKING_ID to "testDealerID", Constants.Input.VIN to "testVin")
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.MILEAGE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid mileage then throw invalid parameter`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "testDealerID",
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.MILEAGE to "testMileage"
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.MILEAGE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid dealerId then throw invalid parameter`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to 1,
            Constants.Input.VIN to "testVin",
            Constants.Input.Appointment.MILEAGE to 0
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.Appointment.BOOKING_ID)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw invalid parameter`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to "testDealerID",
            Constants.Input.VIN to 1,
            Constants.Input.Appointment.MILEAGE to 0
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
    fun `when execute with all failures then return factory response`() = runTest {
        // Arrange
        val input = ServicesNaftaLatamInput(
            vin = "testVin",
            dealerId = "testDealerID",
            mileage = 120,
            unit = ServicesNaftaLatamInput.MileageUnit.KM
        )
        val factoryResponse = NetworkResponse.Failure(PimsErrors.typeError("factory_error"))
        val dealerResponse = NetworkResponse.Failure(PimsErrors.typeError("dealer error"))
        val repairResponse = NetworkResponse.Failure(PimsErrors.typeError("repair error"))
        coEvery { executor.executeFactoryServices(input) } returns factoryResponse
        coEvery { executor.executeDealerServices(input) } returns dealerResponse
        coEvery { executor.executeRepairServices(input) } returns repairResponse
        val result = executor.execute(input)
        assertTrue(result is NetworkResponse.Failure)
        assertEquals(factoryResponse, result)
    }

    @Test
    @Suppress("LongMethod")
    fun `when execute with all successes then return combined response`() = runTest {
        // Arrange
        val input = ServicesNaftaLatamInput(
            vin = "testVin",
            dealerId = "testDealerID",
            mileage = 120,
            unit = ServicesNaftaLatamInput.MileageUnit.KM
        )
        val factoryResponse = Success(
            LatamDealerServiceResponse(
                listOf(
                    Service(
                        id = "1",
                        name = "factory service",
                        price = 10.0f,
                        description = "factory description"
                    )
                )
            )
        )
        val dealerResponse = Success(
            LatamDealerServiceResponse(
                listOf(
                    Service(
                        id = "2",
                        name = "dealer service",
                        price = 20.0f,
                        description = "dealer description"
                    )
                )
            )
        )
        val repairResponse = Success(
            LatamDealerServiceResponse(
                listOf(
                    Service(
                        id = "3",
                        name = "repair service",
                        price = 30.0f,
                        description = "repair description"
                    )
                )
            )
        )
        coEvery { executor.executeFactoryServices(input) } returns factoryResponse
        coEvery { executor.executeDealerServices(input) } returns dealerResponse
        coEvery { executor.executeRepairServices(input) } returns repairResponse

        val result = executor.execute(input)

        assertTrue(result is Success)
        assertEquals(
            listOf(
                Services(
                    id = "1",
                    title = "factory service",
                    type = ServiceType.Factory,
                    description = "factory description",
                    price = 10.0f,
                    packages = null
                ),
                Services(
                    id = "2",
                    title = "dealer service",
                    type = ServiceType.Dealer,
                    description = "dealer description",
                    price = 20.0f,
                    packages = null
                ),
                Services(
                    id = "3",
                    title = "repair service",
                    type = ServiceType.Repair,
                    description = "repair description",
                    price = 30.0f,
                    packages = null
                )
            ),
            (result as Success).response.services
        )
    }

    @Suppress("LongMethod")
    @Test
    fun `when execute with some failures then return combined response without failed services`() =
        runTest {
            // Arrange
            val input = ServicesNaftaLatamInput(
                vin = "testVin",
                dealerId = "testDealerID",
                mileage = 120,
                unit = ServicesNaftaLatamInput.MileageUnit.KM
            )
            val factoryResponse = Success(
                LatamDealerServiceResponse(
                    listOf(
                        Service(
                            id = "1",
                            name = "factory service",
                            price = 10.0f,
                            description = "factory description"
                        )
                    )
                )
            )
            val dealerResponse = NetworkResponse.Failure(PimsErrors.typeError("dealer error"))
            val repairResponse = Success(
                LatamDealerServiceResponse(
                    listOf(
                        Service(
                            id = "3",
                            name = "repair service",
                            price = 30.0f,
                            description = "repair description"
                        )
                    )
                )
            )
            coEvery { executor.executeFactoryServices(input) } returns factoryResponse
            coEvery { executor.executeDealerServices(input) } returns dealerResponse
            coEvery { executor.executeRepairServices(input) } returns repairResponse

            val result = executor.execute(input)
            assertTrue(result is Success)
            assertEquals(
                listOf(
                    Services(
                        id = "1",
                        title = "factory service",
                        type = ServiceType.Factory,
                        description = "factory description",
                        price = 10.0f,
                        packages = null
                    ),
                    Services(
                        id = "3",
                        title = "repair service",
                        type = ServiceType.Repair,
                        description = "repair description",
                        price = 30.0f,
                        packages = null
                    )
                ),
                (result as Success).response.services
            )
        }

    @Test
    fun `when executeFactoryServices with valid input then return LatamDealerServiceResponse`() {
        runTest {
            val input = ServicesNaftaLatamInput(
                vin = "testVin",
                dealerId = "testDealerID",
                mileage = 120,
                unit = ServicesNaftaLatamInput.MileageUnit.KM
            )
            val expectedResponse = Success(LatamDealerServiceResponse(services = emptyList()))
            coEvery {
                anyConstructed<GetLatamFactoryServicesFcaExecutor>().execute(input)
            } returns expectedResponse
            val response = executor.executeFactoryServices(input)
            assertEquals(expectedResponse, response)
        }
    }

    @Test
    fun `when executeDealerServices is called with valid input then return LatamDealerServiceResponse`() = runTest {
        val input = ServicesNaftaLatamInput(
            vin = "testVin",
            dealerId = "testDealerID",
            mileage = 120,
            unit = ServicesNaftaLatamInput.MileageUnit.KM
        )
        val expectedResponse = Success(
            LatamDealerServiceResponse(
                services = listOf(
                    Service(
                        id = "10",
                        name = "testName",
                        price = 10.0f,
                        description = "testDescription"
                    )
                )
            )
        )
        coEvery {
            anyConstructed<GetLatamDealerServicesFcaExecutor>().execute(input)
        } returns expectedResponse
        val actualResponse = executor.executeDealerServices(input)
        assertEquals(expectedResponse, actualResponse)
        coVerify { anyConstructed<GetLatamDealerServicesFcaExecutor>().execute(input) }
    }

    @Test
    fun `when executeRepairServices is called with valid input then return LatamDealerServiceResponse`() = runTest {
        val input = ServicesNaftaLatamInput(
            vin = "testVin",
            dealerId = "testDealerID",
            mileage = 120,
            unit = ServicesNaftaLatamInput.MileageUnit.KM
        )
        val expectedResponse = Success(
            LatamDealerServiceResponse(
                services = listOf(
                    Service(
                        id = "10",
                        name = "testName",
                        price = 10.0f,
                        description = "testDescription"
                    )
                )
            )
        )
        coEvery {
            anyConstructed<GetLatamRepairServicesFcaExecutor>().execute(input)
        } returns expectedResponse

        val actualResponse = executor.executeRepairServices(input)
        assertEquals(expectedResponse, actualResponse)
        coVerify { anyConstructed<GetLatamRepairServicesFcaExecutor>().execute(input) }
    }
}
