package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Failure
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.NaftaDealerServicesResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.NaftaDealerServicesResponse.Service
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.mockkConstructor
import io.mockk.spyk
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

internal class GetNaftaServiceFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetNaftaServiceFcaExecutor
    private lateinit var executorDealerServices: GetNaftaDealerServicesFcaExecutor
    private lateinit var executorFactoryServices: GetNaftaFactoryServicesFcaExecutor
    private lateinit var executorRepairServices: GetNaftaRepairServicesFcaExecutor

    private val dealerServiceResponse = NaftaDealerServicesResponse(
        services = listOf(
            Service(
                id = "testId",
                name = "testName",
                price = 10.0f,
                description = "testDescription"
            )
        )
    )

    override fun setup() {
        super.setup()
        mockkConstructor(GetNaftaDealerServicesFcaExecutor::class)
        coEvery { anyConstructed<GetNaftaDealerServicesFcaExecutor>().execute(any()) } returns Success(
            dealerServiceResponse
        )
        executorDealerServices = spyk(GetNaftaDealerServicesFcaExecutor(middlewareComponent))

        mockkConstructor(GetNaftaFactoryServicesFcaExecutor::class)
        coEvery { anyConstructed<GetNaftaFactoryServicesFcaExecutor>().execute(any()) } returns Success(
            dealerServiceResponse
        )
        executorFactoryServices = spyk(GetNaftaFactoryServicesFcaExecutor(middlewareComponent))

        mockkConstructor(GetNaftaRepairServicesFcaExecutor::class)
        coEvery { anyConstructed<GetNaftaRepairServicesFcaExecutor>().execute(any()) } returns Success(
            dealerServiceResponse
        )
        executorRepairServices = spyk(GetNaftaRepairServicesFcaExecutor(middlewareComponent))

        executor = spyk(GetNaftaServiceFcaExecutor(middlewareComponent))
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
            Constants.Input.Appointment.BOOKING_LOCATION to "testLocation",
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
    fun `when execute params with invalid bookingId then throw invalid parameter`() {
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
    fun `when execute with all failures then return factory response`() =
        runTest {
            val input = ServicesNaftaLatamInput(
                vin = "testVin",
                dealerId = "testDealerID",
                mileage = 120,
                unit = ServicesNaftaLatamInput.MileageUnit.KM
            )
            val factoryResponse = NetworkResponse.Failure(PimsErrors.typeError("factory_error"))
            val dealerResponse = NetworkResponse.Failure(PimsErrors.typeError("dealer error"))
            val repairResponse = NetworkResponse.Failure(PimsErrors.typeError("repair error"))
            coEvery { executor.executeFactoryServices(any()) } returns factoryResponse
            coEvery { executor.executeDealerServices(any()) } returns dealerResponse
            coEvery { executor.executeRepairServices(any()) } returns repairResponse

            val response = executor.execute(input)
            TestCase.assertTrue(response is Failure)
            TestCase.assertEquals(factoryResponse, response)
        }

    @Test
    @Suppress("LongMethod")
    fun `when execute with all successes then return combined response`() =
        runTest {
            // Arrange
            val input = ServicesNaftaLatamInput(
                vin = "testVin",
                dealerId = "testDealerID",
                mileage = 120,
                unit = ServicesNaftaLatamInput.MileageUnit.KM
            )
            val factoryResponse = Success(
                NaftaDealerServicesResponse(
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
                NaftaDealerServicesResponse(
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
                NaftaDealerServicesResponse(
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

            TestCase.assertTrue(result is Success)
            TestCase.assertEquals(
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
    fun `when execute with some failures then return combined response`() =
        runTest {
            // Arrange
            val input = ServicesNaftaLatamInput(
                vin = "testVin",
                dealerId = "testDealerID",
                mileage = 120,
                unit = ServicesNaftaLatamInput.MileageUnit.KM
            )
            val factoryResponse = Success(
                NaftaDealerServicesResponse(
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
                NaftaDealerServicesResponse(
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

            TestCase.assertTrue(result is Success)
            TestCase.assertEquals(
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
    fun `when executeFactoryServices with success then return factory response`() =
        runTest {
            // Arrange
            val input = ServicesNaftaLatamInput(
                vin = "testVin",
                dealerId = "testDealerID",
                mileage = 120,
                unit = ServicesNaftaLatamInput.MileageUnit.KM
            )
            val expectedResponse = Success(NaftaDealerServicesResponse(services = emptyList()))
            coEvery {
                anyConstructed<GetNaftaFactoryServicesFcaExecutor>().execute(input)
            } returns expectedResponse
            val response = executor.executeFactoryServices(input)
            TestCase.assertEquals(expectedResponse, response)
        }

    @Test
    fun `when executeRepairServices with success then return repair response`() =
        runTest {
            // Arrange
            val input = ServicesNaftaLatamInput(
                vin = "testVin",
                dealerId = "testDealerID",
                mileage = 120,
                unit = ServicesNaftaLatamInput.MileageUnit.KM
            )
            val expectedResponse = Success(NaftaDealerServicesResponse(services = emptyList()))
            coEvery {
                anyConstructed<GetNaftaRepairServicesFcaExecutor>().execute(input)
            } returns expectedResponse
            val response = executor.executeRepairServices(input)
            TestCase.assertEquals(expectedResponse, response)
        }

    @Test
    fun `when executeDealerServices with success then return dealer response`() =
        runTest {
            // Arrange
            val input = ServicesNaftaLatamInput(
                vin = "testVin",
                dealerId = "testDealerID",
                mileage = 120,
                unit = ServicesNaftaLatamInput.MileageUnit.KM
            )
            val expectedResponse = Success(NaftaDealerServicesResponse(services = emptyList()))
            coEvery {
                anyConstructed<GetNaftaDealerServicesFcaExecutor>().execute(input)
            } returns expectedResponse
            val response = executor.executeDealerServices(input)
            TestCase.assertEquals(expectedResponse, response)
        }
}
