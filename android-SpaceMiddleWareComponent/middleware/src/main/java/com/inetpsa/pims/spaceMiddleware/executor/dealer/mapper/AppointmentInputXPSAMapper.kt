package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.helpers.psa.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest.RDV
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest.RDV.Phone
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest.RDV.Services.CourtesyVehicle
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest.RDV.Services.Delivery
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest.RDV.Services.MobilityService
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest.RDV.Services.NONE
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest.RDV.Services.PickUp
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.BodyPSARequest.RDV.Services.PickUpAndDelivery
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateXPSAInput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse.Operations
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse.Operations.Packages

internal class AppointmentInputXPSAMapper(private val input: CreateInput, private val brand: Brand) :
    AppointmentInputMapper<CreateXPSAInput> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val selectedServices: Map<String?, Operations> =
        BookingOnlineCache.psaServices
            ?.takeIf { !input.services.isNullOrEmpty() }
            ?.operations
            ?.filter { !it.code.isNullOrBlank() && input.services?.contains(it.code) == true }
            ?.map { operation -> filterByOperation(operation, input.services) }
            ?.associate { it }
            .orEmpty()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun filterByOperation(
        operation: Operations,
        servicesId: List<String>?
    ): Pair<String?, Operations> =
        if (servicesId?.contains(operation.code) == true) {
            Pair(operation.code, operation)
        } else {
            filterByPackage(operation, operation.packages, listOf(operation.code))
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun filterByPackage(
        operation: Operations,
        packages: List<Packages>?,
        servicesId: List<String?>
    ): Pair<String?, Operations> =
        packages?.map { item ->
            if (servicesId.contains(item.reference)) {
                Pair(item.reference, operation)
            } else {
                Pair(null, operation)
            }
        }?.firstOrNull() ?: Pair(null, operation)

    override fun transformBodyRequest(input: CreateXPSAInput): String {
        val operations: List<Map<String, Any?>>? = input.services
            ?.mapNotNull { serviceId ->
                selectedServices[serviceId]
                    ?.let { operation -> generateRequestOperationItem(operation, serviceId) }
            }?.flatten()

        val valetServices = if (brand == Brand.DS && input.premiumService != null) {
            transformPremiumService(input.premiumService)
        } else {
            null
        }

        val plpPremiumService = if (brand == Brand.PEUGEOT && input.premiumService != null) {
            transformPremiumService(input.premiumService)
        } else {
            null
        }

        return BodyPSARequest(
            rdv = RDV(
                vin = input.vin,
                bookingId = input.bookingId,
                day = input.date.toLocalDate().toString(),
                hour = input.date.toLocalTime().toString(),
                discount = transformDiscount(BookingOnlineCache.psaAgenda),
                mobility = input.mobility ?: false,
                comment = input.comment,
                total = fetchTotalPrice(input.services?.firstOrNull()?.let { selectedServices[it] }),
                phones = input.phone.takeIf { !it.isNullOrBlank() }?.let { listOf(Phone(it)) },
                operation = operations,
                valetService = valetServices,
                plpPremiumService = plpPremiumService
            )
        ).toJson()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformDiscount(psaAgenda: AgendaResponse?): Float? =
        psaAgenda?.days
            ?.firstOrNull()
            ?.let { it.slots?.firstOrNull()?.discount }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun fetchTotalPrice(operation: Operations?): Float? {
        return operation?.packages?.map { packages ->
            val totalSubPackages = packages.subPackages
                ?.map { subPackage -> selectedServices[subPackage]?.packages?.firstOrNull()?.price ?: 0f }
                ?.sum() ?: 0f
            packages.price?.plus(totalSubPackages) ?: 0f
        }?.sum()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateRequestOperationItem(operation: Operations, serviceId: String):
        List<Map<String, Any?>>? =
        when (fetchIfHasPackage(serviceId, operation)) {
            true -> {
                operation.packages?.map { packages ->
                    packages.subPackages
                        ?.map { generateRequestPackage(operation, packages) }
                        .orEmpty() + generateRequestPackage(operation, packages)
                }.takeIf { !it.isNullOrEmpty() }?.flatten()
            }

            else -> listOf(generateRequestOperation(operation))
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformPremiumService(premiumService: CreateXPSAInput.Service): RDV.Services =
        when (premiumService) {
            CreateXPSAInput.Service.Delivery -> Delivery
            CreateXPSAInput.Service.PickUp -> PickUp
            CreateXPSAInput.Service.PickUpAndDelivery -> PickUpAndDelivery
            CreateXPSAInput.Service.CourtesyVehicle -> CourtesyVehicle
            CreateXPSAInput.Service.MobilityService -> MobilityService
            CreateXPSAInput.Service.NONE -> NONE
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun fetchIfHasPackage(
        serviceId: String,
        operation: Operations? = selectedServices[serviceId]
    ): Boolean = (!operation?.packages.isNullOrEmpty())

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateRequestPackage(
        operation: Operations,
        packages: Packages
    ): Map<String, Any?> {
        return mapOf(
            // package reference or operation reference if there is no packages
            Constants.Input.Appointment.REFERENCE to packages.reference,
            // package title or operation title if there is no packages
            Constants.Input.Appointment.TITLE to packages.title,
            Constants.Input.Appointment.PRICE to packages.price,
            Constants.Input.Appointment.TYPE to operation.type, // operation.type,
            Constants.Input.Appointment.PERIOD to 0, // ALWAYS 0 , not used for the moment: MYM
            Constants.Input.Appointment.IS_PACKAGE to 1, // // 0 if there is no package and 1 if there is a package
            Constants.Input.Appointment.INTERVENTION_LABEL to packages.title
        )
    }
}
