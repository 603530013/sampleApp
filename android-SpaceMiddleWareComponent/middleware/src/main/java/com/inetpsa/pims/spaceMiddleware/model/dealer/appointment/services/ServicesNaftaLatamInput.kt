package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.BookingIdField
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput.MileageUnit.KM
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput.MileageUnit.Miles

internal data class ServicesNaftaLatamInput(
    override var dealerId: String,
    val vin: String,
    private val mileage: Int? = null,
    private val unit: MileageUnit = KM
) : BookingIdField {

    enum class MileageUnit { KM, Miles }

    companion object {

        const val TRANSFORM_KM_TO_MILES = 0.621371192
        const val TRANSFORM_MILES_TO_KM = 1.60934f
    }

    val mileageKm: Int?
        get() = when (unit) {
            KM -> mileage
            Miles -> mileage?.times(TRANSFORM_MILES_TO_KM)?.toInt()
        }

    val mileageMiles: Int?
        get() = when (unit) {
            KM -> mileage?.times(TRANSFORM_KM_TO_MILES)?.toInt()
            Miles -> mileage
        }
}
