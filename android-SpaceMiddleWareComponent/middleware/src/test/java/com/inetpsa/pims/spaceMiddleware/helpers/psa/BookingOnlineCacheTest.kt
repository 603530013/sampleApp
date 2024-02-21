package com.inetpsa.pims.spaceMiddleware.helpers.psa

import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse.DaysItem.SlotsItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

internal class BookingOnlineCacheTest {

    private val manager = spyk<BookingOnlineCache>()

    @Test
    fun `when update psaAgenda then update value`() {
        val input = AgendaResponse(
            rrdi = "testRrdi",
            period = 1,
            from = "testFrom",
            to = "testTo",
            type = 2,
            days = listOf(
                AgendaResponse.DaysItem(
                    date = "testDate",
                    slots = listOf(
                        SlotsItem(
                            receptionTotal = 3,
                            start = "testStart",
                            discount = 4f,
                            end = "testEnd",
                            receptionAvailable = 1
                        )
                    )
                )
            )
        )
        manager.psaAgenda = input

        Assert.assertEquals(input, manager.psaAgenda)
    }

    @Test
    fun `when update psaServices then update value`() {
        val input = PackageResponse(
            vin = "testVin",
            rrdi = "testRrdi",
            operations = listOf(
                PackageResponse.Operations(
                    code = "testCode",
                    icon = "testIcon",
                    title = "testTitle",
                    type = 1,
                    maintenance = 2,
                    security = 3,
                    packages = listOf(
                        PackageResponse.Operations.Packages(
                            reference = "testReferences",
                            referenceTp = "testReferenceTp",
                            referenceCt = "testReferencesCt",
                            title = "testTitle",
                            nature = "testNature",
                            typeBtob = true,
                            typeFdz = "typeTypeFdz",
                            typeDi = "testTypeDi",
                            price = 200f,
                            isAnyPgInBrr = true,
                            type = 3,
                            validity = PackageResponse.Operations.Packages.Validity(
                                start = 1688487950,
                                end = 1735748750
                            ),
                            subPackages = listOf("testSubPackage"),
                            description = listOf("testDescription")
                        )
                    )
                )
            )
        )
        manager.psaServices = input

        Assert.assertEquals(input, manager.psaServices)
    }

    @Test
    fun clear_shouldSetPsaAgendaAndPsaServicesToNull() {
        // Arrange
        BookingOnlineCache.psaAgenda = AgendaResponse()
        BookingOnlineCache.psaServices = PackageResponse()
        // Act
        BookingOnlineCache.clear()
        // Assert
        Assert.assertEquals(null, BookingOnlineCache.psaAgenda)
        Assert.assertEquals(null, BookingOnlineCache.psaServices)
    }

    @Test
    fun clear_shouldNotThrowNullPointerExceptionWhenPsaAgendaIsNull() {
        // Arrange
        BookingOnlineCache.psaAgenda = null
        BookingOnlineCache.psaServices = PackageResponse()
        // Act
        BookingOnlineCache.clear()
        // Assert
        Assert.assertEquals(null, BookingOnlineCache.psaAgenda)
        Assert.assertEquals(null, BookingOnlineCache.psaServices)
    }

    @Test
    fun clear_shouldNotThrowNullPointerExceptionWhenPsaServicesIsNull() {
        // Arrange
        BookingOnlineCache.psaAgenda = AgendaResponse()
        BookingOnlineCache.psaServices = null
        // Act
        BookingOnlineCache.clear()
        // Assert
        Assert.assertEquals(null, BookingOnlineCache.psaAgenda)
        Assert.assertEquals(null, BookingOnlineCache.psaServices)
    }
}
