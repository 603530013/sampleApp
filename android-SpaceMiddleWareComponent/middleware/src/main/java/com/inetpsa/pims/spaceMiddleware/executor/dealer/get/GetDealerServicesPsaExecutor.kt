package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services.Packages
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput.Services.Packages.Validity
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.map
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

internal class GetDealerServicesPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<ServicesNaftaLatamInput, ServicesOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): ServicesNaftaLatamInput =
        ServicesNaftaLatamInput(
            dealerId = parameters has Appointment.BOOKING_ID,
            vin = parameters has Input.VIN
        )

    override suspend fun execute(input: ServicesNaftaLatamInput): NetworkResponse<ServicesOutput> {
        val queries = mapOf(Constants.QUERY_PARAM_KEY_LANGUAGE to configurationManager.locale.language)

        val request = request(
            type = PackageResponse::class.java,
            urls = arrayOf("/car/v1/vehicle/", input.vin, "/packages/", input.dealerId),
            queries = queries

        )
        return communicationManager.get<PackageResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { response ->
                BookingOnlineCache.psaServices = response
                transformToDealerPackageOutput(response)
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDealerPackageOutput(it: PackageResponse): ServicesOutput =
        ServicesOutput(
            services = it.operations.orEmpty()
                .filter { !it.code.isNullOrBlank() }
                .map { operation ->
                    Services(
                        id = operation.code!!,
                        title = operation.title,
                        type = transformServiceType(operation.type),
                        packages = operation.packages?.map { packageItem ->
                            Packages(
                                reference = packageItem.reference,
                                title = packageItem.title?.filterNot { it == '\n' },
                                price = packageItem.price,
                                type = packageItem.type,
                                description = packageItem.description,
                                validity = packageItem.let {
                                    Validity(
                                        transformTimeStamp(it.validity?.start),
                                        transformTimeStamp(it.validity?.end)
                                    )
                                }
                            )
                        }
                    )
                }
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformTimeStamp(timestamp: Long?): String? =
        timestamp?.let {
            Instant.ofEpochSecond(it).atZone(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformServiceType(type: Int?): ServiceType? =
        when (type) {
            PackageResponse.Operations.TYPE_PACKAGE -> ServiceType.Package
            PackageResponse.Operations.TYPE_MAINTENANCE -> ServiceType.Maintenance
            PackageResponse.Operations.TYPE_INTERVENTION -> ServiceType.Intervention
            PackageResponse.Operations.TYPE_ALL -> ServiceType.Generic
            else -> null
        }
}
