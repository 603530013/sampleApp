package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.Status.Booked
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.details.DetailsOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AppointmentDetailsResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoUnit

internal class GetAppointmentDetailsPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<DetailsInput, DetailsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): DetailsInput = DetailsInput(
        vin = parameters has Constants.PARAM_VIN,
        id = parameters has Constants.PARAM_ID
    )

    override suspend fun execute(input: DetailsInput): NetworkResponse<DetailsOutput> {
        val language = configurationManager.locale.language.toString()
        val country = configurationManager.locale.country.toString()
        val brand = configurationManager.brandCode

        val queries = mutableMapOf(
            Constants.PARAM_COUNTRY to country,
            Constants.QUERY_PARAM_KEY_LANGUAGE to language,
            Constants.QUERY_PARAM_KEY_BRAND to brand
        )

        val request = request(
            type = AppointmentDetailsResponse::class.java,
            urls = arrayOf("/shop/v1/rdv/detail/", input.id),
            queries = queries
        )

        return communicationManager
            .get<AppointmentDetailsResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { transformAppointment(it, input.vin) }
    }

    private fun transformAppointment(response: AppointmentDetailsResponse, vin: String): DetailsOutput =
        DetailsOutput(
            id = response.commandNumber?.filterNot { it.isWhitespace() },
            vin = vin,
            bookingId = response.dealer.geoId,
            bookingLocation = null,
            scheduledTime = transformTimeStamp(response.rdvDate),
            comment = response.customerComment?.trim(),
            mileage = response.customer?.mileage?.toString(),
            email = response.customer?.email,
            phone = null,
            status = Booked,
            services = null,
            amount = response.amount
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformTimeStamp(timestamp: Long?): String? =
        timestamp?.toString()?.let {
            val temporal = DateTimeFormatterBuilder()
                .appendPattern("yyyyMMddHHmm")
                .parseLenient()
                .toFormatter()
                .parse(it)

            val localDate = LocalDate.from(temporal)
            val localTime = LocalTime.from(temporal)

            OffsetDateTime.of(localDate, localTime, ZoneOffset.UTC)
                .withSecond(1)
                .withNano(0)
                .truncatedTo(ChronoUnit.SECONDS)
                .toString()
        }
}
