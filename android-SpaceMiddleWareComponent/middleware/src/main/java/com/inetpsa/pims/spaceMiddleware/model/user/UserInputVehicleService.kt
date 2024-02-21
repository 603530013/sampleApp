package com.inetpsa.pims.spaceMiddleware.model.user

import com.inetpsa.pims.spaceMiddleware.model.common.Action

internal data class UserInputVehicleService(val action: Action, val vin: String, val schema: String?)
