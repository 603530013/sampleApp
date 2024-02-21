package com.inetpsa.pims.spaceMiddleware.executor.user

import androidx.annotation.VisibleForTesting
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.GetUserResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ProfileResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleListResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.deleteSync
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync

internal class GetUserPsaExecutor : BasePsaExecutor<String?, Unit> {

    constructor(command: BaseCommand) : super(command)

    constructor(
        middlewareComponent: MiddlewareComponent,
        parameters: Map<String, Any?>?
    ) : super(middlewareComponent, parameters)

    override fun params(parameters: Map<String, Any?>?): String? =
        parameters hasOrNull Constants.PARAM_VIN

    override suspend fun execute(vin: String?): NetworkResponse<Unit> {
        val request = request(
            type = object : TypeToken<GetUserResponse>() {}.type,
            urls = arrayOf("/me/v1/user"),
            queries = vin?.let { mapOf(Constants.PARAM_VIN to it) } ?: emptyMap(),
            headers = mapOf("refresh-sams-cache" to "1")
        )

        return communicationManager.get<GetUserResponse>(request, MiddlewareCommunicationManager.MymToken)
            .ifSuccess { userResponses ->
                // save profile on database
                saveProfile(userResponses.profile)

                // save preferred dealer on database
                userResponses.dealers.apv?.let { savePreferredDealer(it) }

                // save vehicle list on database
                userResponses.vehicles.takeIf { it.isNotEmpty() }?.let { saveVehicles(it) }

                // save selected vehicle on database
                saveVehicle(userResponses.selectedVehicle)

                // save last update time on database
                saveLastUpdate(userResponses.lastUpdate)
            }
            .map { }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveProfile(profile: ProfileResponse) {
        val phones = mutableMapOf<String, String>()
        profile.phone?.takeIf { it.isNotBlank() }?.let { phones[ProfileOutput.KEY_PHONE_DEFAULT] = it }
        profile.mobile?.takeIf { it.isNotBlank() }?.let { phones[ProfileOutput.KEY_PHONE_MOBILE] = it }
        profile.mobilePro?.takeIf { it.isNotBlank() }?.let { phones[ProfileOutput.KEY_PHONE_MOBILE_PRO] = it }

        val transformedProfile = ProfileOutput(
            uid = profile.idClient,
            email = profile.email,
            firstName = profile.firstName,
            lastName = profile.lastName,
            civility = profile.civility,
            civilityCode = profile.civilityCode,
            phones = phones,
            address1 = profile.address1,
            address2 = profile.address2,
            zipCode = profile.zipCode,
            city = profile.city,
            country = profile.country
        )

        save(Constants.Storage.PROFILE, transformedProfile)
    }

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveLastUpdate(lastUpdate: Long) {
        val savedLastUpdate: Long = read<Long>(Constants.Storage.LAST_UPDATE) ?: 0L
        if (savedLastUpdate < lastUpdate) {
            delete(Constants.Storage.SETTINGS)
            save(Constants.Storage.LAST_UPDATE, lastUpdate)
        }
    }

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveVehicle(vehicle: VehicleDetailsResponse) {
        save("${Constants.Storage.VEHICLE}_${vehicle.vin}", vehicle)
    }

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveVehicles(vehicles: List<VehicleListResponse>) {
        save(Constants.Storage.VEHICLES, vehicles.toJson())
    }

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun savePreferredDealer(dealer: DetailsResponse) {
        save(Constants.Storage.PREFERRED_DEALER, dealer)
        save(Constants.Storage.PREFERRED_DEALER_ID, dealer.siteGeo)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun save(key: String, data: Any): Boolean =
        middlewareComponent.createSync(key = key, data = data, mode = StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal inline fun <reified T : Any?> read(key: String): T? =
        middlewareComponent.readSync<T>(key = key, mode = StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun delete(key: String): Boolean =
        middlewareComponent.deleteSync(key = key, mode = StoreMode.APPLICATION)
}
