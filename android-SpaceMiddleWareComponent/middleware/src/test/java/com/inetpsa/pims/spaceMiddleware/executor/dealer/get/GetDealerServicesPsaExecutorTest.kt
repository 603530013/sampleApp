package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput.MileageUnit.KM
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services.Packages
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services.Packages.Validity
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class GetDealerServicesPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetDealerServicesPsaExecutor

    private val servicesInput = ServicesNaftaLatamInput(
        dealerId = "testId",
        vin = "testVin",
        mileage = 120,
        unit = KM
    )
    private val packageResponse = PackageResponse(
        vin = "testVin",
        rrdi = "532811M01F",
        operations = listOf(
            PackageResponse.Operations(
                code = "testCode",
                icon = "testIcon",
                title = "testTitle",
                type = 0,
                maintenance = 0,
                security = 0,
                packages = listOf(
                    PackageResponse.Operations.Packages(
                        reference = "testReference",
                        referenceTp = "testReferenceTp",
                        referenceCt = "testReferenceCt",
                        title = "testTitle",
                        nature = "testNature",
                        typeBtob = true,
                        typeFdz = "testTypeFdz",
                        typeDi = "testTypeDi",
                        price = 100f,
                        isAnyPgInBrr = true,
                        description = listOf("testDescription"),
                        type = 0,
                        validity = PackageResponse.Operations.Packages.Validity(
                            start = 1688487950,
                            end = 1735748750
                        ),
                        subPackages = listOf("testSubPackages")
                    )
                )
            )
        )
    )

    private val result = ServicesOutput(
        services = listOf(
            Services(
                id = "testCode",
                title = "testTitle",
                type = ServiceType.Package,
                packages = listOf(
                    Packages(
                        reference = "testReference",
                        title = "testTitle",
                        price = 100f,
                        description = listOf("testDescription"),
                        type = 0,
                        validity = Validity(
                            start = "2023-07-04T16:25:50Z",
                            end = "2025-01-01T16:25:50Z"
                        )
                    )
                )
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        userSessionManager.getUserSession()?.customerId
        executor = spyk(GetDealerServicesPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val queries = mapOf(Constants.QUERY_PARAM_KEY_LANGUAGE to configurationManager.locale.language)
        every { executor.params(any()) } returns servicesInput
        coEvery { communicationManager.get<PackageResponse>(any(), any()) } returns
            NetworkResponse.Success(packageResponse)

        runTest {
            val responseExecutor = executor.execute()

            coVerify {
                executor.request(
                    type = eq(PackageResponse::class.java),
                    urls = eq(
                        arrayOf(
                            "/car/v1/vehicle/",
                            servicesInput.vin,
                            "/packages/",
                            servicesInput.dealerId.orEmpty()
                        )
                    ),
                    queries = queries
                )
            }

            coVerify {
                communicationManager.get<PackageResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, responseExecutor is Success)
            val success = responseExecutor as Success
            Assert.assertEquals(result, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return DealerPackageInput`() {
        val vin = "testVin"
        val id = "testId"

        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to servicesInput.dealerId,
            Constants.Input.VIN to servicesInput.vin,
            Constants.Input.Appointment.MILEAGE to 120,
            Constants.Input.Appointment.PARAM_MILEAGE_UNIT to "km"
        )
        val output = ServicesNaftaLatamInput(dealerId = id, vin = vin)

        val result = executor.params(input)

        Assert.assertEquals(output, result)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to servicesInput.dealerId,
            Constants.Input.VIN to " "
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing id param then throw missing parameter`() {
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to " ",
            Constants.Input.VIN to servicesInput.vin
        )
        val exception = PIMSFoundationError.missingParameter(Constants.Input.Appointment.BOOKING_ID)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw invalid parameter`() {
        val vin = 123
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to servicesInput.dealerId,
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
    fun `when execute params with invalid id then throw invalid parameter`() {
        val id = 123789
        val input = mapOf(
            Constants.Input.Appointment.BOOKING_ID to id,
            Constants.Input.VIN to servicesInput.vin
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
    fun testTransformTimeStampPositive() {
        val timestamp = 1688487950L
        val expected = "2023-07-04T16:25:50Z"
        val result = executor.transformTimeStamp(timestamp)
        Assert.assertEquals(expected, result)
    }

    @Test
    fun testTransformTimeStampNegative() {
        val timestamp = null
        val result = executor.transformTimeStamp(timestamp)
        Assert.assertNull(result)
    }
}
