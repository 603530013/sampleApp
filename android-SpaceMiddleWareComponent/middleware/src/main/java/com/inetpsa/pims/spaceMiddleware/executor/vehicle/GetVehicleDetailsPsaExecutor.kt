package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import androidx.annotation.VisibleForTesting
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ProfileResponse
import com.inetpsa.pims.spaceMiddleware.model.user.ProfileOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.AccountVehicleInfoPsa
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.VehiclePsaParam
import com.inetpsa.pims.spaceMiddleware.model.vehicles.list.Vehicles.Vehicle
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.transform

@Deprecated("try to switch to use this class GetVehicleDetailsPsaExecutor")
internal class GetVehicleDetailsPsaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>?
) : BasePsaExecutor<VehiclePsaParam, Vehicle>(middlewareComponent, params) {

    constructor(command: BaseCommand) : this(command.middlewareComponent, command.parameters)

    companion object {

        internal const val VEHICLE_THERMIC: Int = 0
        internal const val VEHICLE_HYBRID_A: Int = 2
        internal const val VEHICLE_HYBRID_B: Int = 3
        internal const val VEHICLE_ELECTRIC: Int = 4
        internal const val VEHICLE_HYBRID_C: Int = 5
    }

    override fun params(parameters: Map<String, Any?>?): VehiclePsaParam {
        return VehiclePsaParam(
            vin = parameters has Constants.PARAM_VIN,
            saveProfile = (parameters hasOrNull Constants.PARAM_SAVE_PROFILE) ?: true
        )
    }

    override suspend fun execute(input: VehiclePsaParam): NetworkResponse<Vehicle> {
        val request = request(
            type = object : TypeToken<AccountVehicleInfoPsa>() {}.type,
            urls = arrayOf("/me/v1/user"),
            queries = mapOf(Constants.PARAM_VIN to input.vin),
            headers = mapOf("refresh-sams-cache" to "1")
        )

        val response = communicationManager.get<AccountVehicleInfoPsa>(request, MiddlewareCommunicationManager.MymToken)

        if (response is NetworkResponse.Success) {
            // save profile on database
            if (input.saveProfile) {
                response.response.profile?.let { saveProfile(it) }
            }
            // save dealers on database
            response.response.dealers?.let { saveDealers(it) }
            // save vehicle on database
            saveVehicle(response.response.selectedVehicle)
        }

        return response.transform { vehicleInfo ->
            asVehicleResponseItem(vehicleInfo)?.let { NetworkResponse.Success(it) }
                ?: NetworkResponse.Failure(PIMSFoundationError.invalidReturnParam("type"))
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun asVehicleResponseItem(item: AccountVehicleInfoPsa): Vehicle? {
        val vin = item.selectedVehicle.vin

        val type = when (item.selectedVehicle.type) {
            VEHICLE_THERMIC -> Vehicle.Type.THERMIC
            VEHICLE_ELECTRIC -> Vehicle.Type.ELECTRIC
            VEHICLE_HYBRID_A,
            VEHICLE_HYBRID_B,
            VEHICLE_HYBRID_C -> Vehicle.Type.HYBRID
            else -> null
        } ?: return null

        return Vehicle(
            vin = vin,
            lcdv = item.selectedVehicle.lcdv ?: "",
            eligibility = item.selectedVehicle.eligibility,
            attributes = item.selectedVehicle.attributes,
            servicesConnected = item.selectedVehicle.servicesConnected.orEmpty().toJson(),
            preferredDealer = item.dealers,
            type = type,
            name = item.selectedVehicle.shortName.orEmpty(),
            regTimeStamp = 0,
            nickname = "",
            year = 1900,
            settingsUpdate = item.settingsUpdate
        )
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
            locale = null,
            phones = phones,
            address1 = profile.address1,
            address2 = profile.address2,
            zipCode = profile.zipCode,
            city = profile.city,
            country = profile.country
        )

        middlewareComponent.createSync(
            Constants.Storage.PROFILE,
            transformedProfile.toJson(),
            StoreMode.APPLICATION
        )
    }

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveVehicle(vehicle: AccountVehicleInfoPsa.SelectedVehicle) {
        // already done on the new implementation
    }

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveDealers(dealers: AccountVehicleInfoPsa.Dealers) {
        // already done on the new implementation
    }

    @Suppress("UnusedPrivateMember")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveDealer(dealers: AccountVehicleInfoPsa.Dealer) {
        // already done on the new implementation
    }
}
