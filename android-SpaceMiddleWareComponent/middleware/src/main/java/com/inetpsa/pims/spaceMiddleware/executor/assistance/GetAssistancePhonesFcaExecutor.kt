package com.inetpsa.pims.spaceMiddleware.executor.assistance

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetSettingsFCAExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.CachedVehicles
import com.inetpsa.pims.spaceMiddleware.model.assistance.CallCenter
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.SettingsCallCenterItemResponse.CallCenterSettingFca
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse.SDP
import com.inetpsa.pims.spaceMiddleware.util.map
import java.util.Locale

@Suppress("TooManyFunctions")
internal class GetAssistancePhonesFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<String, Map<String, CallCenter>>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.Input.VIN

    override suspend fun execute(input: String): NetworkResponse<Map<String, CallCenter>> {
        val vehicle = CachedVehicles.getOrThrow(middlewareComponent, input, Action.Get)
        val servicesName = vehicle.services.orEmpty().map { it.service }
        return GetSettingsFCAExecutor(middlewareComponent, params)
            .execute(input)
            .map { transformSettings(it, vehicle, servicesName) }
            .map { it.embeddedCallCenterForSprint(vehicle) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformSettings(
        settings: List<SettingsCallCenterItemResponse>,
        vehicle: VehicleResponse,
        services: List<String>
    ) = mutableMapOf<String, CallCenter>().apply {
        settings.firstOrNull()?.settings?.map { callCenterSettings ->
            val isServiceContained = hasService(vehicle.sdpEnum, services, callCenterSettings)
            fillPhonesFromSettings(callCenterSettings, isServiceContained)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun hasService(sdp: SDP, services: List<String>, callCenterSettings: CallCenterSettingFca) =
        when {
            sdp == SDP.SXM && callCenterSettings.category == CallCenterSettingFca.Category.B_CALL ->
                services.any { callCenterSettings.callType.equals(it, true) }

            sdp == SDP.IGNITE ->
                services.any { callCenterSettings.settingCategory.equals(it, true) }

            else -> false
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun MutableMap<String, CallCenter>.fillPhonesFromSettings(
        callCenterSettings: CallCenterSettingFca,
        hasService: Boolean
    ) = apply {
        if (hasService) {
            val callCenter = CallCenter(
                callCenterSettings.primaryNumber,
                callCenterSettings.secondaryNumber
            )
            val key = callCenterTypeFilter(callCenterSettings.callCenterTypeEnum)
            if (key != CallCenterSettingFca.CallCenterType.None) {
                put(key.output, callCenter)
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun callCenterTypeFilter(type: CallCenterSettingFca.CallCenterType?) =
        when (type) {
            CallCenterSettingFca.CallCenterType.RoadSide -> CallCenterSettingFca.CallCenterType.RoadSide
            CallCenterSettingFca.CallCenterType.UConnect -> CallCenterSettingFca.CallCenterType.UConnect
            CallCenterSettingFca.CallCenterType.Brand -> CallCenterSettingFca.CallCenterType.Brand
            else -> CallCenterSettingFca.CallCenterType.None
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun embeddedCallCenter(
        make: String?,
        market: String?
    ): Map<String, CallCenter> =
        mutableMapOf<String, CallCenter>()
            .embeddedCallCenterForBrand(make)
            .embeddedCallCenterForUConnect()
            .embeddedCallCenterForRoadSide(market)
            .toMap()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun MutableMap<String, CallCenter>.embeddedCallCenterForBrand(make: String?) =
        apply {
            make.takeIf { !make.isNullOrEmpty() }
                ?.let {
                    val callCenter = CallCenter(
                        primary = when (it.uppercase()) {
                            // hard code here until we get the json files
                            Brand.RAM.name -> "+18667264636" // CL_31
                            Brand.JEEP.name -> "+18774265337" // CL_32
                            Brand.FIAT.name -> "+18882426342" // CL_33
                            Brand.DODGE.name -> "+18004236343" // CL_34
                            Brand.CHRYSLER.name -> "+18002479753" // CL_35
                            else -> null
                        },
                        secondary = null
                    )
                    if (!callCenter.primary.isNullOrBlank() || !callCenter.secondary.isNullOrBlank()) {
                        put(
                            CallCenterSettingFca.CallCenterType.Brand.output,
                            callCenter
                        )
                    }
                }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun MutableMap<String, CallCenter>.embeddedCallCenterForUConnect() =
        apply {
            put(
                CallCenterSettingFca.CallCenterType.UConnect.output,
                CallCenter(
                    primary = "+18557924241", // CL_36
                    secondary = null
                )
            )
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun MutableMap<String, CallCenter>.embeddedCallCenterForRoadSide(market: String?) =
        apply {
            market?.takeIf { market.isNotBlank() }
                ?.let {
                    val callCenter = CallCenter(
                        primary = when (it.uppercase()) {
                            // hard code here until we get the json files
                            Locale.CANADA.country -> "+18003634869" // CL_37
                            Locale.US.country -> "+18005212779" // CL_38
                            else -> null
                        },
                        secondary = null
                    )

                    if (!callCenter.primary.isNullOrBlank() || !callCenter.secondary.isNullOrBlank()) {
                        put(
                            CallCenterSettingFca.CallCenterType.RoadSide.output,
                            callCenter
                        )
                    }
                }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun MutableMap<String, CallCenter>.embeddedCallCenterForSprint(vehicle: VehicleResponse) =
        apply {
            if (vehicle.sdpEnum == SDP.SPRINT) {
                putAll(embeddedCallCenter(make = vehicle.make, market = vehicle.market))
            }
        }
}
