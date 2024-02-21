package com.inetpsa.pims.spaceMiddleware.model.vehicles.list

import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.FcaVehicleItem

@Deprecated("Should switch to the new class VehiclesResponse")
internal data class AccountVehicleListFca(val vehicles: List<FcaVehicleItem>)
