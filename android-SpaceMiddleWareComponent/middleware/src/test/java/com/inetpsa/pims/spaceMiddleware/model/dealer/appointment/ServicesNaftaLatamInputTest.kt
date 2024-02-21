package com.inetpsa.pims.spaceMiddleware.model.dealer.appointment

import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import org.junit.Assert
import org.junit.Test

class ServicesNaftaLatamInputTest {

    @Test
    fun `when mileage is null then mileageKm is null`() {
        val servicesInput = ServicesNaftaLatamInput(
            dealerId = "testDealerId",
            vin = "testVin",
            mileage = null,
            unit = ServicesNaftaLatamInput.MileageUnit.KM
        )
        Assert.assertNull(servicesInput.mileageKm)
    }

    @Test
    fun `when mileage is 100 then mileageKm is 100`() {
        val servicesInput = ServicesNaftaLatamInput(
            dealerId = "testDealerId",
            vin = "testVin",
            mileage = 100,
            unit = ServicesNaftaLatamInput.MileageUnit.KM
        )
        Assert.assertEquals(100, servicesInput.mileageKm)
    }

    @Test
    fun `when mileage is 100 with different unit then mileageKm is 100`() {
        val servicesInput = ServicesNaftaLatamInput(
            dealerId = "testDealerId",
            vin = "testVin",
            mileage = 100,
            unit = ServicesNaftaLatamInput.MileageUnit.Miles
        )
        Assert.assertEquals(160, servicesInput.mileageKm)
    }

    @Test
    fun `when mileage is null then mileageMiles is null`() {
        val servicesInput = ServicesNaftaLatamInput(
            dealerId = "testDealerId",
            vin = "testVin",
            mileage = null,
            unit = ServicesNaftaLatamInput.MileageUnit.Miles
        )
        Assert.assertNull(servicesInput.mileageMiles)
    }

    @Test
    fun `when mileage is 100 then mileageMile is 100`() {
        val servicesInput = ServicesNaftaLatamInput(
            dealerId = "testDealerId",
            vin = "testVin",
            mileage = 100,
            unit = ServicesNaftaLatamInput.MileageUnit.Miles
        )
        Assert.assertEquals(100, servicesInput.mileageMiles)
    }

    @Test
    fun `when mileage is 100 with different unit then mileageMile is 100`() {
        val servicesInput = ServicesNaftaLatamInput(
            dealerId = "testDealerId",
            vin = "testVin",
            mileage = 100,
            unit = ServicesNaftaLatamInput.MileageUnit.KM
        )
        Assert.assertEquals(62, servicesInput.mileageMiles)
    }
}
