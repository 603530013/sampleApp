package com.inetpsa.pims.spaceMiddleware.executor.dealer.set

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateEMEAInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.AddDealerAppointmentResponse
import com.inetpsa.pims.spaceMiddleware.util.hasLocalDateTime
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.map
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class AddDealerAppointmentFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<CreateEMEAInput, CreateOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): CreateEMEAInput {
        return CreateEMEAInput(
            vin = parameters has Input.VIN,
            date = parameters hasLocalDateTime Input.DATE,
            mileage = parameters has Constants.PARAM_MILEAGE,
            codNation = parameters has Input.COD_NATION,
            comment = parameters has Constants.BODY_PARAM_COMMENT,
            bookingId = parameters has Appointment.BOOKING_ID,
            bookingLocation = parameters hasOrNull Appointment.BOOKING_LOCATION,
            contactName = parameters has Input.CONTACT_NAME,
            contactPhone = parameters has Input.CONTACT_PHONE,
            services = parameters hasOrNull Input.PARAM_SERVICES
        )
    }

    override suspend fun execute(input: CreateEMEAInput): NetworkResponse<CreateOutput> {
        val bodyJson = mapOf(
            Constants.PARAMS_KEY_DATE to transformToMilliSeconds(input.date),
            Constants.PARAMS_KEY_VEHICLE_KM to input.mileage,
            Constants.PARAMS_KEY_COD_NATION to input.codNation,
            Constants.PARAMS_KEY_FAULT_DESCRIPTION to input.comment,
            Constants.PARAMS_KEY_DEALER_ID to input.bookingId,
            Constants.PARAMS_KEY_LOCATION to input.bookingLocation,
            Constants.PARAMS_KEY_CONTACT_NAME to input.contactName,
            Constants.PARAMS_KEY_TELEPHONE to input.contactPhone,
            Constants.PARAMS_KEY_SERVICES_LIST to input.services
        )

        val request = request(
            type = AddDealerAppointmentResponse::class.java,
            urls = arrayOf("/v1/accounts/", uid, "/vehicles/", input.vin, "/servicescheduler/appointment"),
            body = bodyJson.toJson()
        )
        return communicationManager
            .post<AddDealerAppointmentResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .map {
                BookingOnlineCache.clearAfterCreation()
                transformToCreateOutput(it)
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToCreateOutput(response: AddDealerAppointmentResponse): CreateOutput =
        CreateOutput(id = response.codRepairOrder)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToMilliSeconds(date: LocalDateTime): String =
        date.toInstant(ZoneOffset.UTC).toEpochMilli().toString()
}
