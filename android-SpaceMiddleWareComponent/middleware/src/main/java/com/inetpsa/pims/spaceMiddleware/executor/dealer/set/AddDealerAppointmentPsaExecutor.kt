package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.AppointmentInputXPSAMapper
import com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Booked
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CachedAppointmentsXPSA
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CachedAppointmentsXPSA.CachedAppointmentXPSA
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateXPSAInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.DealerRdvOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.DealerRdvOutput.Operations
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DealerRdvConfirmResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.hasLocalDateTime
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

internal class AddDealerAppointmentPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<CreateXPSAInput, DealerRdvOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = CreateXPSAInput(
        vin = parameters has Input.VIN,
        date = parameters hasLocalDateTime Input.Appointment.DATE,
        bookingId = parameters has Input.Appointment.BOOKING_ID,
        services = parameters has Input.Appointment.SERVICES,
        mobility = parameters hasOrNull Input.Appointment.MOBILITY,
        comment = parameters hasOrNull Input.Appointment.COMMENT,
        phone = parameters hasOrNull Input.CONTACT_PHONE,
        contact = parameters hasOrNull Input.CONTACT_NAME,
        premiumService = parameters hasOrNull Input.Appointment.PARAM_PREMIUM_SERVICE

    )

    override suspend fun execute(input: CreateXPSAInput): NetworkResponse<DealerRdvOutput> {
        val mapper = AppointmentInputXPSAMapper(input, middlewareComponent.configurationManager.brand)

        val request = request(
            DealerRdvConfirmResponse::class.java,
            arrayOf("/shop/v1/user/rdv/confirm/", input.bookingId, "/", input.vin),
            body = mapper.transformBodyRequest(input)
        )
        return communicationManager.post<DealerRdvConfirmResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { response ->
                // clear the online manager of psaServices
                BookingOnlineCache.clear()
                saveOnCache(response)
                generateOutput(response)
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveOnCache(response: DealerRdvConfirmResponse) {
        val appointment = CachedAppointmentXPSA(
            appointmentId = response.basketId ?: "",
            status = Booked.name,
            date = transformDate(response)?.toString()
        )

        val cache = middlewareComponent.readSync<CachedAppointmentsXPSA>(
            Constants.Storage.APPOINTMENT_xPSA,
            StoreMode.APPLICATION
        ) ?: CachedAppointmentsXPSA(HashSet())

        cache.appointments.add(appointment)

        middlewareComponent.createSync(
            key = Constants.Storage.APPOINTMENT_xPSA,
            data = cache,
            mode = StoreMode.APPLICATION
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateOutput(response: DealerRdvConfirmResponse): DealerRdvOutput = DealerRdvOutput(
        vin = response.vin,
        bookingId = response.siteGeo,
        day = response.day,
        hour = response.hour,
        contact = response.contact,
        mobility = response.mobility,
        discount = response.discount,
        appointmentId = response.basketId,
        operations = response.operations?.map { operation ->
            Operations(
                reference = operation.reference,
                title = operation.title,
                type = operation.type,
                isPackage = operation.isPackage,
                interventionLabel = operation.interventionLabel
            )
        }
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformDate(response: DealerRdvConfirmResponse): OffsetDateTime? {
        val date = LocalDate.parse(response.day)
        val time = LocalTime.parse(response.hour)
        return LocalDateTime.of(date, time).atOffset(OffsetDateTime.now().offset)
    }
}
