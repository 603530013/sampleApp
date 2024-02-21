package com.inetpsa.pims.spaceMiddleware.executor.eservices.mapper

import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorFCARequest.Location
import com.inetpsa.pims.spaceMiddleware.model.vehicles.chargestation.ChargeStationLocatorInput
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChargingStationNoneMapperTest {

    private lateinit var chargingStationNoneMapper: ChargingStationNoneMapper

    @Before
    fun setUp() {
        chargingStationNoneMapper = ChargingStationNoneMapper()
    }

    @Test
    fun `test transformToBodyRequest`() {
        val chargeStationLocatorInput = ChargeStationLocatorInput(
            vin = "vin",
            longitude = 1.0,
            latitude = 2.0,
            filters = null
        )
        val chargeStationLocatorFCARequest = ChargeStationLocatorFCARequest(
            location = Location(
                latitude = 2.0,
                longitude = 1.0
            )
        )
        val result = chargingStationNoneMapper.transformToBodyRequest(chargeStationLocatorInput)
        assertEquals(chargeStationLocatorFCARequest, result)
    }

    @Test
    fun `test transformParamsToInput`() {
        assertEquals(null, chargingStationNoneMapper.transformParamsToInput(emptyMap()))
    }
}
