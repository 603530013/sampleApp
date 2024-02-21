package com.inetpsa.pims.spaceMiddleware.executor.vehicle.set

import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.enrollment.AddVehicleConnectedFcaInput
import com.inetpsa.pims.spaceMiddleware.model.enrollment.ProfilePostBodyRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class AddVehicleConnectedFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: AddVehicleConnectedFcaExecutor

    private val addVehicleConnectedFcaInput = AddVehicleConnectedFcaInput(
        vin = "testVin",
        licencePlateNumber = "testLicencePlateNumber",
        contacts = listOf(
            AddVehicleConnectedFcaInput.Contact("testName", "testPhone")
        ),
        tcRegistration = AddVehicleConnectedFcaInput.LegalContent(
            "IT",
            AddVehicleConnectedFcaInput
                .LegalContent.Status.AGREE,
            "1.23"
        ),
        tcActivation = AddVehicleConnectedFcaInput.LegalContent(
            "IT",
            AddVehicleConnectedFcaInput.LegalContent.Status.AGREE,
            "1.23"
        ),
        ppActivation = AddVehicleConnectedFcaInput.LegalContent(
            "IT",
            AddVehicleConnectedFcaInput.LegalContent.Status.AGREE,
            "1.23"
        )
    )

    private val profilePostBodyRequest = ProfilePostBodyRequest(
        role = ProfilePostBodyRequest.UserRole.VEHICLE_OWNER,
        licencePlateNumber = "testLicencePlateNumber",
        emergencyContacts = listOf(
            ProfilePostBodyRequest.EmergencyContact("testName", "testPhone")
        ),
        tc = ProfilePostBodyRequest.TermsAndConditions(
            registration = ProfilePostBodyRequest
                .LegalContent("IT", "AGREE", "1.23"),
            activation = ProfilePostBodyRequest.LegalContent(
                "IT",
                "AGREE",
                "1.23"
            )
        ),
        pp = ProfilePostBodyRequest.PrivacyPolicy(
            activation = ProfilePostBodyRequest.LegalContent(
                "IT",
                "AGREE",
                "1.23"
            )
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(AddVehicleConnectedFcaExecutor(middlewareComponent, emptyMap()))
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute then make a network API call with success response`() {
        every { executor.params(any()) } returns addVehicleConnectedFcaInput
        coEvery { communicationManager.post<Unit>(any(), any()) } returns
            Success(Unit)
        runTest {
            val response = executor.execute()
            val expectedJson = profilePostBodyRequest.toJson()
            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(
                        arrayOf(
                            "/v4/accounts/",
                            "testCustomerId",
                            "/vehicles/",
                            addVehicleConnectedFcaInput.vin
                        )
                    ),
                    body = expectedJson
                )
            }
            coVerify {
                communicationManager.post<Unit>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(Unit, success.response)
        }
    }

    @Test
    fun `when generateBodyRequest then return a json string`() {
        val json = executor.generateBodyRequest(addVehicleConnectedFcaInput)
        val expectedJson = profilePostBodyRequest.toJson()
        Assert.assertEquals(expectedJson, json)
    }

    @Test
    fun `when execute params with right inputs then return an AddVehicleConnectedFcaInput`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcRegistration.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcActivation?.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcActivation?.status?.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcActivation?.version
            ),
            Constants.Input.PARAM_PP_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.ppActivation.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.ppActivation.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.ppActivation.version
            ),
            Constants.Input.PARAM_CONTACTS to listOf(
                mapOf(
                    Constants.Input.PARAM_NAME to "testName",
                    Constants.Input.PARAM_PHONE to "testPhone"
                )
            )
        )

        val param = executor.params(input)
        Assert.assertEquals(addVehicleConnectedFcaInput, param)
    }

    @Test
    fun `when params with missing vin then throw missing parameter`() {
        val input = mapOf(
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_CONTACTS to addVehicleConnectedFcaInput.contacts,
            Constants.Input.PARAM_TC_REGISTRATION to addVehicleConnectedFcaInput.tcRegistration,
            Constants.Input.PARAM_TC_ACTIVATION to addVehicleConnectedFcaInput.tcActivation,
            Constants.Input.PARAM_PP_ACTIVATION to addVehicleConnectedFcaInput.ppActivation
        )
        val exception = PIMSFoundationError.missingParameter(Constants.BODY_PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Tc Registration then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_ACTIVATION to addVehicleConnectedFcaInput.tcActivation,
            Constants.Input.PARAM_PP_ACTIVATION to addVehicleConnectedFcaInput.ppActivation
        )
        val exception =
            PIMSFoundationError.missingParameter(Constants.Input.PARAM_TC_REGISTRATION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Tc Registration country code then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcRegistration.status,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to addVehicleConnectedFcaInput.tcActivation,
            Constants.Input.PARAM_PP_ACTIVATION to addVehicleConnectedFcaInput.ppActivation
        )
        val exception =
            PIMSFoundationError.missingParameter(Constants.Input.PARAM_COUNTRY_CODE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Tc Registration status then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to addVehicleConnectedFcaInput.tcActivation,
            Constants.Input.PARAM_PP_ACTIVATION to addVehicleConnectedFcaInput.ppActivation
        )
        val exception =
            PIMSFoundationError.missingParameter(Constants.Input.PARAM_STATUS)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Tc Registration version then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_STATUS to
                    addVehicleConnectedFcaInput.tcRegistration.status.name
            ),
            Constants.Input.PARAM_TC_ACTIVATION to addVehicleConnectedFcaInput.tcActivation,
            Constants.Input.PARAM_PP_ACTIVATION to addVehicleConnectedFcaInput.ppActivation
        )
        val exception =
            PIMSFoundationError.missingParameter(Constants.Input.PARAM_VERSION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Tc Activation country code then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcRegistration.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to mapOf(
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcActivation?.status?.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcActivation?.version
            ),
            Constants.Input.PARAM_PP_ACTIVATION to addVehicleConnectedFcaInput.ppActivation
        )
        val exception =
            PIMSFoundationError.invalidParameter(Constants.Input.PARAM_PP_ACTIVATION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Tc Activation status then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcRegistration.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to addVehicleConnectedFcaInput.tcActivation?.countryCode,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcActivation?.version
            ),
            Constants.Input.PARAM_PP_ACTIVATION to addVehicleConnectedFcaInput.ppActivation
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.PARAM_PP_ACTIVATION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Tc Activation version then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcRegistration.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcActivation?.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcActivation?.status?.name
            ),
            Constants.Input.PARAM_PP_ACTIVATION to addVehicleConnectedFcaInput.ppActivation
        )
        val exception =
            PIMSFoundationError.invalidParameter(Constants.Input.PARAM_PP_ACTIVATION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Pp Activation country code then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcRegistration.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcActivation?.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcActivation?.status?.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcActivation?.version
            ),
            Constants.Input.PARAM_PP_ACTIVATION to mapOf(
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.ppActivation.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.ppActivation.version
            )
        )
        val exception =
            PIMSFoundationError.missingParameter(Constants.Input.PARAM_COUNTRY_CODE)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Pp Activation status then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcRegistration.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcActivation?.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcActivation?.status?.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcActivation?.version
            ),
            Constants.Input.PARAM_PP_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.ppActivation.countryCode,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.ppActivation.version
            )
        )
        val exception =
            PIMSFoundationError.missingParameter(Constants.Input.PARAM_STATUS)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when params with missing Pp Activation version then throw missing parameter`() {
        val input = mapOf(
            Constants.BODY_PARAM_VIN to addVehicleConnectedFcaInput.vin,
            Constants.PARAM_PLATE_NUMBER to addVehicleConnectedFcaInput.licencePlateNumber,
            Constants.Input.PARAM_TC_REGISTRATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcRegistration.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcRegistration.status.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcRegistration.version
            ),
            Constants.Input.PARAM_TC_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.tcActivation?.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.tcActivation?.status?.name,
                Constants.Input.PARAM_VERSION to addVehicleConnectedFcaInput.tcActivation?.version
            ),
            Constants.Input.PARAM_PP_ACTIVATION to mapOf(
                Constants.Input.PARAM_COUNTRY_CODE to
                    addVehicleConnectedFcaInput.ppActivation.countryCode,
                Constants.Input.PARAM_STATUS to addVehicleConnectedFcaInput.ppActivation.status.name
            )
        )
        val exception =
            PIMSFoundationError.missingParameter(Constants.Input.PARAM_VERSION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
