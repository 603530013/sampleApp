package com.inetpsa.pims.spaceMiddleware.executor.features.mapper

import androidx.annotation.VisibleForTesting
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config.Engine
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse

internal class FeaturesFcaOutputMapper(
    @get:VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val vehicle: VehicleResponse
) {

    private val codes = hashMapOf(
        "CP" to listOf("CHARGE_SCHEDULING"),
        "CPPLUS" to listOf("CHARGE_SCHEDULING", "CLIMATE_SCHEDULING"),
        "CPPLUSBEV" to listOf("CHARGE_SCHEDULING", "CLIMATE_SCHEDULING"),
        "CPPLUSPHEV" to listOf("CHARGE_SCHEDULING", "CLIMATE_SCHEDULING"),
        "CPCHRG2" to listOf("CHARGE_SCHEDULING"),
        "BCPPLUS" to listOf("CHARGE_SCHEDULING", "CLIMATE_SCHEDULING"),
        "CNOW" to listOf("CHARGE_NOW"),
        "ROPRECOND" to listOf("PRECONDIONNING_ON"),
        "ROPRECOND_OFF" to listOf("PRECONDIONNING_OFF"),
        "ROHVACON" to listOf("AIR_CONDITIONNING_ON"),
        "ROHVACOFF" to listOf("AIR_CONDITIONNING_OFF"),
        "ROCOMFORTON" to listOf("SEAT_HEAT_ON"),
        "ROCOMFORTOFF" to listOf("SEAT_HEAT_OFF"),
        "ROTRUNKLOCK" to listOf("TRUNK_LOCK"),
        "ROTRUNKUNLOCK" to listOf("TRUNK_UNLOCK"),
        "ROLIFTGATELOCK" to listOf("LIFT_GATE_LOCK"),
        "ROLIFTGATEUNLOCK" to listOf("LIFT_GATE_UNLOCK"),
        "REON" to listOf("ENGINE_ON"),
        "REOFF" to listOf("ENGINE_OFF"),
        "HBLF" to listOf("HORN"),
        "RDU" to listOf("DOOR_UNLOCK"),
        "RDL" to listOf("DOOR_LOCK"),
        "ROLIGHTS" to listOf("LIGHTS"),
        "VF" to listOf("VEHICLE_LOCATOR"),
        "VHSG" to listOf("VEHICLE_INFO"),
        "VHS" to listOf("VEHICLE_INFO"),
        "VRC" to listOf("VEHICLE_HEALTH_ALERTS"),
        "PRIVACY" to listOf("VEHICLE_PRIVACY"),
        "TA" to listOf("THEFT_ALARM"),
        "PARENT" to listOf("VEHICLE_DRIVE_ALERTS"),
        "SDV" to listOf("SEND_TO_NAV"),
        "LMN" to listOf("LAST_MILE_GUIDANCE"),
        "NAV_OB_ROUTE_DRM" to listOf("DYNAMIC_RANGE_MAP"),
        "NAV_OB_ROUTE_DRM15" to listOf("DYNAMIC_RANGE_MAP"),
        "NAV_OB_ROUTE_DRM20" to listOf("DYNAMIC_RANGE_MAP"),
        "DEEPREFRESH" to listOf("VEHICLE_DEEP_REFRESH"),
        "BCALL" to listOf("BREAKDOWN_CALL"),
        "ASSIST" to listOf("ASSISTANCE_CALL"),
        "IVA" to listOf("IVA_CALL"),
        "ECALL" to listOf("EMERGENCY_CALL"),
        "SVLA" to listOf("STOLEN_CALL"),
        "CSL" to listOf("CHARGE_STATION_LOCATOR"),
        "CSL_2" to listOf("CHARGE_STATION_LOCATOR"),
        "CPA" to listOf("VEHICLE_CONNECTED_ACCOUNT"),
        "ECODRIVE" to listOf("ECO_COACHING"),
        "ECOCOACHINGR1" to listOf("ECO_COACHING"),
        "ECOCOACHING20" to listOf("ECO_COACHING"),
        "TRIPREPORT" to listOf("TRIPS"),
        "TRAILS" to listOf("TRAILS"),
        "JTRAILS2" to listOf("TRAILS"),
        "RECALL" to listOf("RECALL_CAMPAIGN"),
        "ORPPLUS" to listOf("OFF_ROAD"),
        "ORPPLUS21" to listOf("OFF_ROAD")

    )

    private val configs = hashMapOf(
        "CP_CHARGE_SCHEDULING" to Feature.Config(
            schedule = 3,
            repeat = true,
            shared = true,
            daysType = listOf(Feature.Config.DaysType.WORK_DAYS, Feature.Config.DaysType.WEEKEND)
        ),

        "CPPLUS_CHARGE_SCHEDULING" to Feature.Config(
            schedule = 3,
            repeat = true,
            shared = true,
            daysType = listOf(Feature.Config.DaysType.CHOOSE_DAYS)
        ),

        "CPPLUS_CLIMATE_SCHEDULING" to Feature.Config(
            schedule = 3,
            repeat = true,
            shared = true,
            daysType = listOf(Feature.Config.DaysType.CHOOSE_DAYS)
        ),

        "CPPLUSBEV_CHARGE_SCHEDULING" to Feature.Config(
            schedule = 2,
            repeat = true,
            daysType = listOf(Feature.Config.DaysType.CHOOSE_DAYS)
        ),
        "CPPLUSBEV_CLIMATE_SCHEDULING" to Feature.Config(
            schedule = 2,
            repeat = true,
            daysType = listOf(Feature.Config.DaysType.CHOOSE_DAYS)
        ),

        "CPPLUSPHEV_CHARGE_SCHEDULING" to Feature.Config(
            schedule = 2,
            repeat = true,
            daysType = listOf(Feature.Config.DaysType.CHOOSE_DAYS)
        ),

        "CPPLUSPHEV_CLIMATE_SCHEDULING" to Feature.Config(
            schedule = 2,
            repeat = true,
            daysType = listOf(Feature.Config.DaysType.CHOOSE_DAYS)
        ),

        "CPCHRG2_CHARGE_SCHEDULING" to Feature.Config(
            schedule = 2,
            repeat = true,
            daysType = listOf(Feature.Config.DaysType.CHOOSE_DAYS)
        ),

        "BCPPLUS_CHARGE_SCHEDULING" to Feature.Config(
            schedule = 1,
            repeat = false,
            daysType = listOf(Feature.Config.DaysType.DAILY)
        ),

        "BCPPLUS_CLIMATE_SCHEDULING" to Feature.Config(
            schedule = 4,
            repeat = false,
            daysType = listOf(Feature.Config.DaysType.CHOOSE_DAYS)
        ),

        "REON_ENGINE_ON" to Feature.Config(
            version = 1F // if ROC or 2 if ROC2
        ),

        "REOFF_ENGINE_OFF" to Feature.Config(
            version = 1F // if ROC or 2 if ROC2
        ),

        "HBLF_HORN" to Feature.Config(
            version = 1F // if ROC or 2 if ROC2
        ),

        "RDU_DOOR_UNLOCK" to Feature.Config(
            version = 1F // if ROC or 2 if ROC2
        ),

        "RDL_DOOR_LOCK" to Feature.Config(
            version = 1F // if ROC or 2 if ROC2
        ),

        "ROLIGHTS_LIGHTS" to Feature.Config(
            version = 1F // if ROC or 2 if ROC2
        ),

        "VHSG_VEHICLE_INFO" to Feature.Config(
            engine = Engine.ICE
        ),

        "VHS_VEHICLE_INFO" to Feature.Config(
            engine = when (vehicle.fuelType) {
                "E" -> Engine.BEV
                "H" -> Engine.PHEV
                "G" -> Engine.ICE
                else -> Engine.UNKNONW
            }
        ),

        "NAV_OB_ROUTE_DRM_DYNAMIC_RANGE_MAP" to Feature.Config(
            version = 1F
        ),

        "NAV_OB_ROUTE_DRM15_DYNAMIC_RANGE_MAP" to Feature.Config(
            version = 1.5F
        ),

        "NAV_OB_ROUTE_DRM20_DYNAMIC_RANGE_MAP" to Feature.Config(
            version = 2F
        ),

        "CSL_CHARGE_STATION_LOCATOR" to Feature.Config(
            type = FeaturesOutput.Feature.Config.Type.INTERNAL // "internal/external/partner"
        ),

        "CSL_2_CHARGE_STATION_LOCATOR" to Feature.Config(
            type = FeaturesOutput.Feature.Config.Type.INTERNAL // "internal/external/partner"
        ),

        "ECODRIVE_ECO_COACHING" to Feature.Config(
            version = 1F
        ),

        "ECOCOACHINGR1_ECO_COACHING" to Feature.Config(
            version = 1F
        ),

        "ECOCOACHING20_ECO_COACHING" to Feature.Config(
            version = 2F
        ),

        "TRIPREPORT_TRIPS" to Feature.Config(
            protocol = FeaturesOutput.Feature.Config.Protocol.NETWORK
        ),

        "TRAILS_TRAILS" to Feature.Config(
            version = 1F
        ),

        "JTRAILS2_TRAILS" to Feature.Config(
            version = 2F
        ),

        "ORPPLUS_OFF_ROAD" to Feature.Config(
            version = 1F
        ),

        "ORPPLUS21_OFF_ROAD" to Feature.Config(
            version = 2F
        )
    )

    fun transformFeatureOutput(): FeaturesOutput {
        val features = vehicle.services.orEmpty()
            .filter { it.vehicleCapable }
            .mapNotNull { transformOutput(it) }
            .flatten()
        val sortedList = features.sortedBy { it.code }
        return FeaturesOutput(sortedList)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformOutput(service: VehicleResponse.Service): List<Feature>? =
        codes[service.service]?.map { code ->
            val name = service.service
            Feature(
                code = code,
                value = name,
                status = if (service.serviceEnabled) Feature.Status.ENABLE else Feature.Status.CAPABLE,
                config = configs["${name}_$code"]
            )
        }
}
