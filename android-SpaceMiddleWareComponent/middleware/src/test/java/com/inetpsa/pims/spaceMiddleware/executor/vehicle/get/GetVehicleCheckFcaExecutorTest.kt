package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Activation
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ChannelFeature
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Consents
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.CslProvider.F2M
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.FCAProfiling
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Pp
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Svla
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.Tc.Registration
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.ThirdPartyProfiling
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.VehicleLegalDocuments
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.VehicleLegalDocumentsToReview
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehiclesResponse
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class GetVehicleCheckFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: GetVehicleCheckFcaExecutor
    private val today = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    //region vehicles Response
    private val vehiclesResponse = VehiclesResponse(
        userid = "testUserId",
        version = "testVersion",
        vehicles = listOf(
            VehicleResponse(
                activationSource = "testActivationSource",
                authorizedPartner = "testAuthorizedPartner",
                brandCode = "testBrandCode",
                channelFeatures = listOf(
                    ChannelFeature(
                        channels = listOf("testChannel"),
                        featureCode = "testFeatureCode"
                    )
                ),
                color = "testColor",
                company = "testCompany",
                connectorType = VehicleResponse.ConnectorType.TYPE_2_MENNEKES,
                consents = Consents(
                    thirdPartyProfiling = ThirdPartyProfiling(
                        status = "testStatus",
                        creationTimestamp = today,
                        updateTimestamp = today
                    ),
                    fcaProfiling = FCAProfiling(
                        status = "testStatus",
                        creationTimestamp = today,
                        updateTimestamp = today
                    )
                ),
                cslProvider = F2M,
                customerRegStatus = "testCustomerRegStatus",
                enrollmentStatus = "testEnrollmentStatus",
                fuelType = "testFuelType",
                isCompanyCar = false,
                language = "testLanguage",
                make = "testMake",
                market = "testMarket",
                model = "testModel",
                modelDescription = "testModelDescription",
                navEnabledHU = false,
                nickname = "testNickname",
                pp = Pp(Activation(status = "testStatus", version = "testVersion")),
                privacyMode = "testPrivacyMode",
                radio = "testRadio",
                regStatus = "testRegStatus",
                regTimestamp = today,
                sdp = "testSdp",
                services = listOf(
                    Service(service = "testService1", serviceEnabled = false, vehicleCapable = false),
                    Service(service = "testService2", serviceEnabled = true, vehicleCapable = true)
                ),
                soldRegion = "testSoldRegion",
                subMake = "testSubMake",
                svla = Svla(status = "testStatus", timestamp = today),
                tc = Tc(
                    activation = Activation(
                        status = "testStatus",
                        version = "testVersion"
                    ),
                    registration = Registration(
                        status = "testStatus",
                        version = "testVersion"
                    )
                ),
                tcuType = "testTcuType",
                tsoBodyCode = "testTsoBodyCode",
                tsoModelYear = "testTsoModelYear",
                vehicleLegalDocuments = listOf(
                    VehicleLegalDocuments(
                        documentType = "testDocumentType",
                        countryCode = "testCountryCode",
                        status = "testStatus",
                        version = "testVersion",
                        updatedAt = "testUpdateAt"
                    )
                ),
                vehicleLegalDocumentsToReview = listOf(
                    VehicleLegalDocumentsToReview(
                        documentType = "testDocument",
                        countryCode = "testCountryCode",
                        status = "testStatus",
                        version = "testVersion",
                        updatedAt = false
                    )
                ),
                vehicleType = "testVehicleType",
                vin = "testVin",
                year = 1986,
                imageUrl = "testImageUrl"
            )
        )
    )
    //endregion

    @Before
    override fun setup() {
        super.setup()
        mockkConstructor(GetVehiclesResponseFcaExecutor::class)
        val successResponse = NetworkResponse.Success(vehiclesResponse)
        coEvery { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(any()) } returns successResponse
        executor = spyk(GetVehicleCheckFcaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with the right vin then return vin`() {
        val vin = "testVin"
        val input = mapOf(Constants.PARAM_VIN to vin)
        val param = executor.params(input)

        Assert.assertEquals(vin, param)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw missing parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.PARAM_VIN to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute with no existing vin then return success response`() {
        val inputVin = "testVin1"
        every { executor.params(any()) } returns inputVin

        coEvery {
            anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(inputVin))
        } returns NetworkResponse.Success(vehiclesResponse)

        runTest {
            val response = executor.execute()
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(inputVin)) }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when execute with existing vin then return failure response`() {
        val inputVin = "testVin"
        every { executor.params(any()) } returns inputVin

        coEvery {
            anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(inputVin))
        } returns NetworkResponse.Success(vehiclesResponse)

        runTest {
            val response = executor.execute()
            coVerify(exactly = 1) { anyConstructed<GetVehiclesResponseFcaExecutor>().execute(eq(inputVin)) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val error = PimsErrors.alreadyExist(Constants.PARAM_VIN)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }
}
