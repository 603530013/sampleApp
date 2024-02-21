package com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper

import com.inetpsa.mmx.foundation.extensions.asJson
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddDealerAppointmentPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateXPSAInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CreateXPSAInput.Service
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.details.AppointmentDetailsEmeaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.AgendaResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse.Operations.Packages
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer.PackageResponse.Operations.Packages.Validity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month

internal class AppointmentInputXPSAMapperTest {

    private val date = LocalDateTime.of(2023, Month.OCTOBER, 3, 10, 30, 40, 50000)

    private val createXPSAInput = CreateXPSAInput(
        vin = "testVin",
        date = date,
        bookingId = "testDealerID",
        services = listOf("FCT0305"),
        mobility = false,
        comment = "test",
        phone = "testPhone",
        contact = "test",
        premiumService = Service.NONE
    )

    private val agendaResponse = AgendaResponse(
        rrdi = "testRrdi",
        period = 2,
        from = "testFrom",
        to = "testTo",
        type = 1,
        days = listOf(
            AgendaResponse.DaysItem(
                date = "testDate",
                slots = listOf(
                    AgendaResponse.DaysItem.SlotsItem(
                        receptionTotal = 1,
                        start = "testStart",
                        discount = 1f,
                        end = "testEnd",
                        receptionAvailable = 1
                    )
                )
            )
        )
    )

    private val packageResponse = PackageResponse(
        vin = "testVin",
        rrdi = "532811M01F",
        operations = listOf(
            PackageResponse.Operations(
                code = "testServiceID",
                icon = "testIcon",
                title = "testTitle",
                type = 0,
                maintenance = 0,
                security = 0,
                packages = listOf(
                    Packages(
                        reference = "testReference",
                        referenceTp = "testReferenceTp",
                        referenceCt = "testReferenceCt",
                        title = "testTitle",
                        nature = "testNature",
                        typeBtob = true,
                        typeFdz = "testTypeFdz",
                        typeDi = "testTypeDi",
                        price = 100f,
                        isAnyPgInBrr = true,
                        description = listOf("testDescription"),
                        type = 0,
                        validity = Validity(
                            start = 1688487950,
                            end = 1735748750
                        ),
                        subPackages = listOf("testSubPackages")
                    )
                )
            )
        )
    )

    private val packagesList = PackageResponse.Operations(
        code = "testServiceID",
        icon = "testIcon",
        title = "testTitle",
        type = 0,
        maintenance = 0,
        security = 0,
        packages = listOf(
            Packages(
                reference = "testReference",
                referenceTp = "testReferenceTp",
                referenceCt = "testReferenceCt",
                title = "testTitle",
                nature = "testNature",
                typeBtob = true,
                typeFdz = "testTypeFdz",
                typeDi = "testTypeDi",
                price = 100f,
                isAnyPgInBrr = true,
                description = listOf("testDescription"),
                type = 0,
                validity = Validity(
                    start = 1688487950,
                    end = 1735748750
                ),
                subPackages = listOf("testSubPackages")
            )
        )
    )

    private val emptySubPackages = PackageResponse.Operations(
        code = "testCode",
        icon = "testIcon",
        title = "testTitle",
        type = 0,
        maintenance = 0,
        security = 0,
        packages = listOf(
            Packages(
                reference = "testReference",
                referenceTp = "testReferenceTp",
                referenceCt = "testReferenceCt",
                title = "testTitle",
                nature = "testNature",
                typeBtob = true,
                typeFdz = "testTypeFdz",
                typeDi = "testTypeDi",
                price = 100f,
                isAnyPgInBrr = true,
                description = listOf("testDescription"),
                type = 0,
                validity = Validity(
                    start = 1688487950,
                    end = 1735748750
                ),
                subPackages = null
            )
        )
    )

    private val emptyPackages = PackageResponse.Operations(
        code = "testCode",
        icon = "testIcon",
        title = "testTitle",
        type = 0,
        maintenance = 0,
        security = 0,
        packages = null
    )

    private val operations = listOf(
        mapOf(
            "reference" to "FCT0305",
            "title" to "Batterie",
            "icon" to "test.png",
            "type" to 0,
            "isPackage" to 0,
            "intervention_label" to "Batterie"
        )
    )

    private val packageOperation = PackageResponse.Operations(
        code = "testServiceID",
        icon = "testIcon",
        title = "testTitle",
        type = 0,
        maintenance = 0,
        security = 0,
        packages = listOf(
            Packages(
                reference = "testReference",
                referenceTp = "testReferenceTp",
                referenceCt = "testReferenceCt",
                title = "testTitle",
                nature = "testNature",
                typeBtob = true,
                typeFdz = "testTypeFdz",
                typeDi = "testTypeDi",
                price = 100.0f,
                isAnyPgInBrr = true, type = 0,
                validity =
                Validity(start = 1688487950, end = 1735748750),
                subPackages = listOf("testSubPackages"),
                description = listOf("testDescription")
            )
        )
    )

    private lateinit var mapper: AppointmentInputXPSAMapper

    @Before
    fun setUp() {
        mapper = spyk(AppointmentInputXPSAMapper(createXPSAInput, Brand.PEUGEOT), recordPrivateCalls = true)
        mockkConstructor(AddDealerAppointmentPsaExecutor::class)
        mockkConstructor(AppointmentDetailsEmeaResponse::class)
    }

    @Test
    fun `should return a valid body request with operation`() {
        val output = mapOf(
            "rdv" to mapOf(
                "vin" to "testVin",
                "sitegeo" to "testDealerID",
                "day" to "2023-10-03",
                "hour" to "10:30:40.000050",
                "discount" to 1.0,
                "total" to null,
                "comment" to "test",
                "mobility" to false,
                "phones" to listOf(mapOf("phone" to "testPhone")),
                "contact" to null,
                "valet_services" to null,
                "plp_premium_service" to Service.NONE,
                "operations" to emptyList<String>()
            )
        ).asJson()

        every { mapper.generateRequestOperationItem(any(), any()) } returns operations

        runBlocking {
            val result = mapper.transformBodyRequest(createXPSAInput)
            Assert.assertEquals(output, result)
        }
    }

    @Test
    fun `when execute transformDiscount with valid agenda then return discount`() {
        val discount = 1.0f
        coEvery { mapper.transformDiscount(agendaResponse) } returns discount
        runTest {
            val result = mapper.transformDiscount(agendaResponse)
            coVerify(exactly = 1) { mapper.transformDiscount(agendaResponse) }

            Assert.assertEquals(discount, result)
        }
    }

    @Test
    fun `when execute fetchTotalPrice with empty package return null`() {
        val result = mapper.fetchTotalPrice(emptyPackages)
        Assert.assertEquals(null, result)
    }

    @Test
    fun `when execute fetchTotalPrice with empty subPackage return null`() {
        val totalPrice = 100f
        val result = mapper.fetchTotalPrice(emptySubPackages)
        Assert.assertEquals(totalPrice, result)
    }

    @Test
    fun `when execute fetchTotalPrice with right package return sum price`() {
        val totalPrice = 100f
        val result = mapper.fetchTotalPrice(packageResponse.operations?.firstOrNull())
        Assert.assertEquals(totalPrice, result)
    }

    @Test
    fun `when execute fetchIfHasPackage with empty package return right value`() {
        val isPackage = false

        val result = mapper.fetchIfHasPackage("testServiceID", emptyPackages)
        Assert.assertEquals(isPackage, result)
    }

    @Test
    fun `when execute fetchIfHasPackage with  right package return right value`() {
        val isPackage = true
        val result = mapper.fetchIfHasPackage("testServiceID", packageResponse.operations?.firstOrNull())
        Assert.assertEquals(isPackage, result)
    }

    @Test
    fun `when execute fetchIfHasPackage with serviceID return right value`() {
        val isPackage = false
        val result = mapper.fetchIfHasPackage("testServiceID")
        Assert.assertEquals(isPackage, result)
    }

    @Test
    fun `when execute filterByOperation with serviceID return right value`() {
        val actual = Pair("testServiceID", packageResponse.operations?.firstOrNull())
        val result = mapper.filterByOperation(packagesList, listOf("testServiceID"))
        Assert.assertEquals(actual, result)
    }

    @Test
    fun `when execute filterByOperation with non serviceID return right value`() {
        val result = mapper.filterByOperation(packagesList, listOf("testServiceID123"))
        val check = mapper.filterByPackage(packagesList, packagesList.packages, listOf("testServiceID123"))
        Assert.assertEquals(check, result)
    }

    @Test
    fun `when execute filterByPackage with serviceID return right value`() {
        val serviceID = packagesList.packages?.firstOrNull()?.reference
        val actual = Pair(serviceID, packagesList)
        val result = mapper.filterByPackage(packagesList, packagesList.packages, listOf(serviceID))
        Assert.assertEquals(actual, result)
    }

    @Test
    fun `when execute generateRequestOperationItem with right value return package body response`() {
        every { mapper.fetchIfHasPackage(any(), any()) } returns true
        val map = mapOf(
            Constants.Input.Appointment.REFERENCE to "testReference",
            Constants.Input.Appointment.TITLE to "testTitle",
            Constants.Input.Appointment.PRICE to 100.0f,
            Constants.Input.Appointment.TYPE to 0,
            Constants.Input.Appointment.PERIOD to 0,
            Constants.Input.Appointment.IS_PACKAGE to 1,
            Constants.Input.Appointment.INTERVENTION_LABEL to "testTitle"

        )

        val actual = mapper.generateRequestOperationItem(packageOperation, "testServiceID")
        verify(exactly = 1) { mapper.fetchIfHasPackage("testServiceID", packagesList) }
        Assert.assertNotNull(actual)
        Assert.assertEquals(listOf(map, map), actual)
    }

    @Test
    fun `when execute generateRequestOperationItem with non serviceID return return operation body response `() {
        every { mapper.fetchIfHasPackage(any(), any()) } returns false
        val map = mapOf(
            Constants.Input.Appointment.REFERENCE to "testCode",
            Constants.Input.Appointment.TITLE to "testTitle",
            Constants.Input.Appointment.TYPE to 0,
            Constants.Input.Appointment.PERIOD to 0,
            Constants.Input.Appointment.IS_PACKAGE to 0
        )
        val result = mapper.generateRequestOperationItem(emptyPackages, "testServiceID")
        verify(exactly = 1) { mapper.fetchIfHasPackage("testServiceID", emptyPackages) }
        Assert.assertEquals(listOf(map), result)
    }
}
