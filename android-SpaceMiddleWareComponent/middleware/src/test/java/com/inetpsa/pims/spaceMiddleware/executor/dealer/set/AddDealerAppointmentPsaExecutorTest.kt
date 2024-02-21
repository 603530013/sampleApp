package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.Constants.Storage
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.AppointmentInputXPSAMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateXPSAInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.DealerRdvOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DealerRdvConfirmResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DealerRdvConfirmResponse.Operations
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month.OCTOBER

internal class AddDealerAppointmentPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: AddDealerAppointmentPsaExecutor
    private val date = LocalDateTime.of(2023, OCTOBER, 3, 10, 30, 40, 50000)
    private val createXPSAInput = CreateXPSAInput(
        vin = "testVin",
        date = date,
        bookingId = "testDealerID",
        services = listOf("FCT0305"),
        mobility = false,
        comment = "test",
        phone = "123456789",
        contact = "test",
        premiumService = CreateXPSAInput.Service.NONE
    )

    private val rdvConfirmResponse = DealerRdvConfirmResponse(
        rid = "1080",
        accountId = "ACNT200005679965",
        vin = "testVin",
        siteGeo = "0000038471",
        rrdi = "020052W01F",
        day = "2023-07-20",
        hour = "11:15",
        contact = 0,
        mobility = 0,
        total = 0,
        discount = 0,
        created = "1689680483",
        basketId = "1245910",
        operations = listOf(
            Operations(
                reference = "FCT0305",
                title = "Batterie",
                icon = "test.png",
                type = 0,
                isPackage = 0,
                interventionLabel = "Batterie"
            )
        )
    )
    private val rdvConfirmOutput = DealerRdvOutput(
        vin = "testVin",
        bookingId = "0000038471",
        day = "2023-07-20",
        hour = "11:15",
        contact = 0,
        mobility = 0,
        discount = 0,
        appointmentId = "1245910",
        operations = listOf(
            DealerRdvOutput.Operations(
                reference = "FCT0305",
                title = "Batterie",
                type = 0,
                isPackage = 0,
                interventionLabel = "Batterie"
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
        executor = spyk(AddDealerAppointmentPsaExecutor(baseCommand))
        mockkConstructor(AppointmentInputXPSAMapper(createXPSAInput, Brand.PEUGEOT)::class)
        every { anyConstructed<AppointmentInputXPSAMapper>().transformBodyRequest(createXPSAInput) } returns
            rdvConfirmOutput.toString()
    }

    @Test
    fun `when execute then make a post API call`() {
        every { executor.params(any()) } returns createXPSAInput
        coEvery {
            communicationManager.post<DealerRdvConfirmResponse>(
                any(),
                any()
            )
        } returns Success(rdvConfirmResponse)
        every { anyConstructed<AppointmentInputXPSAMapper>().transformBodyRequest(createXPSAInput) } returns
            rdvConfirmOutput.toString()
        every { executor.generateOutput(rdvConfirmResponse) } returns rdvConfirmOutput
        coJustRun { executor.saveOnCache(any()) }

        runTest {
            val response = executor.execute()
            verify {
                executor.request(
                    DealerRdvConfirmResponse::class.java,
                    arrayOf("/shop/v1/user/rdv/confirm/", createXPSAInput.bookingId, "/", createXPSAInput.vin),
                    body = any()
                )
            }

            coVerify {
                communicationManager.post<DealerRdvConfirmResponse>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            coVerify(exactly = 1) { executor.saveOnCache(rdvConfirmResponse) }
            coVerify(exactly = 1) { executor.generateOutput(rdvConfirmResponse) }
            coVerify(exactly = 1) {
                anyConstructed<AppointmentInputXPSAMapper>().transformBodyRequest(createXPSAInput)
                (createXPSAInput)
            }

            Assert.assertEquals(true, response is Success)
            val success = response as Success
            Assert.assertEquals(rdvConfirmOutput, success.response)
        }
    }

    @Test
    fun `when execute params with the right input then return CreateAppointmentInput`() {
        val bookingId = "123"
        val vin = "testVin"
        val day = LocalDateTime.of(2023, OCTOBER, 3, 10, 30, 40, 50000)
        val services = listOf("FCT0305")
        val mobility = false
        val comment = "test"
        val phones = "123456789"
        val contact = "test"
        val premiumServices = CreateXPSAInput.Service.NONE

        val input = mapOf(
            Input.VIN to vin,
            Appointment.DATE to day.toString(),
            Appointment.BOOKING_ID to bookingId,
            Appointment.SERVICES to services,
            Appointment.MOBILITY to mobility,
            Appointment.COMMENT to comment,
            Input.CONTACT_PHONE to phones,
            Input.CONTACT_NAME to contact,
            Appointment.PARAM_PREMIUM_SERVICE to premiumServices
        )
        val createXPSAInput = CreateXPSAInput(
            vin = vin,
            date = day,
            bookingId = bookingId,
            services = services,
            mobility = mobility,
            comment = comment,
            phone = "123456789",
            contact = contact,
            premiumService = premiumServices
        )
        val output = executor.params(input)
        Assert.assertEquals(createXPSAInput, output)
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid id then throw invalidParameter`() {
        val id = "123"
        val vin = null
        val date = "2023-07-20"
        val hour = "11:15"
        val servicesId = listOf("FCT0305")
        val comment = "test"
        val phones = "123456789"
        val contact = "test"
        val premiumServices = CreateXPSAInput.Service.NONE

        val input = mapOf(
            Input.VIN to vin,
            Input.ID to id,
            Appointment.DATE to date,
            Appointment.HOUR to hour,
            Appointment.SERVICES_ID to servicesId,
            Appointment.MOBILITY to false,
            Appointment.COMMENT to comment,
            Input.CONTACT_PHONE to phones,
            Input.CONTACT_NAME to contact,
            Appointment.PARAM_PREMIUM_SERVICE to premiumServices
        )

        val exception = PIMSFoundationError.invalidParameter(Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when generateOutput then return DealerOutput`() {
        every {
            anyConstructed<AppointmentInputXPSAMapper>().transformBodyRequest(createXPSAInput)
        } returns rdvConfirmOutput.toString()
        val result = executor.generateOutput(rdvConfirmResponse)
        Assert.assertEquals(rdvConfirmOutput, result)
    }

    @Test
    fun `when execute save then save in cache`() {
        every {
            dataManager.create(
                key = any(),
                data = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }

        runTest { executor.saveOnCache(rdvConfirmResponse) }

        verify {
            dataManager.create(
                key = eq(
                    "PEUGEOT_PREPROD_testCustomerId_${Storage.APPOINTMENT_xPSA}"
                ),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }
}
