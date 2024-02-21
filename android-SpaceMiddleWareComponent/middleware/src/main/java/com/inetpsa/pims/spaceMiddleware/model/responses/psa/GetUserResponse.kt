package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.DetailsResponse

internal data class GetUserResponse(
    @SerializedName("profile") val profile: ProfileResponse,
    @SerializedName("dealers") val dealers: DealersResponse,
    @SerializedName("vehicles") val vehicles: List<VehicleListResponse>,
    @SerializedName("selected_vehicle") val selectedVehicle: VehicleDetailsResponse,
    @SerializedName("settings_update") val lastUpdate: Long
) {

    internal data class DealersResponse(
        @SerializedName("apv") val apv: DetailsResponse?
    )
}
