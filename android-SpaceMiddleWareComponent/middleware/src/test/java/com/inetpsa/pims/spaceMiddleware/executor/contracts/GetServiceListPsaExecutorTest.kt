package com.inetpsa.pims.spaceMiddleware.executor.contracts

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.servicelist.BaseItem
import com.inetpsa.pims.spaceMiddleware.model.servicelist.Bta
import com.inetpsa.pims.spaceMiddleware.model.servicelist.ClubItem
import com.inetpsa.pims.spaceMiddleware.model.servicelist.Contract
import com.inetpsa.pims.spaceMiddleware.model.servicelist.ContractServicesItem
import com.inetpsa.pims.spaceMiddleware.model.servicelist.Dscp
import com.inetpsa.pims.spaceMiddleware.model.servicelist.FlexiLease
import com.inetpsa.pims.spaceMiddleware.model.servicelist.NacItem
import com.inetpsa.pims.spaceMiddleware.model.servicelist.Raccess
import com.inetpsa.pims.spaceMiddleware.model.servicelist.RemoteLev
import com.inetpsa.pims.spaceMiddleware.model.servicelist.ServicesList
import com.inetpsa.pims.spaceMiddleware.model.servicelist.ServicesList.ServiceItem
import com.inetpsa.pims.spaceMiddleware.model.servicelist.Tmts
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Deprecated("try to switch to use this class GetVehicleContractsPSAExecutorTest")
internal class GetServiceListPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetServiceListPsaExecutor
    private val contract = Contract(
        nac = listOf(
            NacItem(
                code = "NACCode",
                validity = null,
                type = "NACType",
                title = "NACTitle",
                category = "NACCategory",
                status = null,
                isExtensible = null,
                statusReason = null
            )
        ),
        remoteLev = listOf(
            RemoteLev(
                code = "RemoteLevCode",
                validity = null,
                type = "RemoteLevType",
                title = "RemoteLevTitle",
                category = "RemoteLevCategory",
                status = null,
                isExtensible = null,
                associationId = null,
                statusReason = null,
                fds = listOf()
            )
        ),
        flexiLease = listOf(FlexiLease(code = "FlexiLeaseCode", status = null)),
        club = listOf(
            ClubItem(
                code = "ClubItemCode",
                validity = null,
                type = "ClubItemType",
                title = "ClubItemTitle",
                category = "ClubItemCategory",
                status = null,
                isExtensible = null
            )
        ),
        services = listOf(
            ContractServicesItem(
                code = "ContractServicesItemCode",
                validity = null,
                type = "ContractServicesItemType",
                title = "ContractServicesItemTitle",
                category = "ContractServicesItemCategory",
                label = "ContractServicesItemLabel",
                status = null,
                isExtensible = null,
                products = listOf()
            )
        ),
        bta = listOf(
            Bta(
                code = "BtaCode",
                validity = null,
                type = "BtaType",
                title = "BtaTitle",
                category = "BtaCategory",
                level = null,
                secureCode = "BtaSecureCode",
                status = null,
                isExtensible = null
            )
        ),
        tmts = listOf(
            Tmts(
                code = "TmtsCode",
                validity = null,
                type = "TmtsType",
                title = "TmtsTitle",
                category = "TmtsCategory",
                status = null,
                isExtensible = null,
                statusReason = null
            )
        ),
        dscp = listOf(
            Dscp(
                code = "DscpCode",
                validity = null,
                type = "DscpType",
                title = "DscpTitle",
                category = "DscpCategory",
                status = null,
                isExtensible = null,
                statusReason = null
            )
        ),
        raccess = listOf(
            Raccess(
                code = "RaccessCode",
                validity = null,
                type = "RaccessType",
                title = "RaccessTitle",
                category = "RaccessCategory",
                fds = listOf(),
                status = null,
                isExtensible = null,
                associationId = null,
                statusReason = null
            )
        )
    )

    private val servicesList = ServicesList(
        services = listOf(
            ServiceItem(
                id = "bta",
                type = "BtaType",
                title = "BtaTitle",
                category = "BtaCategory"
            ),
            ServiceItem(
                id = "club",
                type = "ClubItemType",
                title = "ClubItemTitle",
                category = "ClubItemCategory"
            ),
            ServiceItem(
                id = "dscp",
                type = "DscpType",
                title = "DscpTitle",
                category = "DscpCategory"
            ),
            ServiceItem(
                id = "flexiLease"
            ),
            ServiceItem(
                id = "nac",
                type = "NACType",
                title = "NACTitle",
                category = "NACCategory"
            ),
            ServiceItem(
                id = "raccess",
                type = "RaccessType",
                title = "RaccessTitle",
                category = "RaccessCategory"
            ),
            ServiceItem(
                id = "remoteLev",
                type = "RemoteLevType",
                title = "RemoteLevTitle",
                category = "RemoteLevCategory"
            ),
            ServiceItem(
                id = "services",
                type = "ContractServicesItemType",
                title = "ContractServicesItemTitle",
                category = "ContractServicesItemCategory"
            ),
            ServiceItem(
                id = "tmts",
                type = "TmtsType",
                title = "TmtsTitle",
                category = "TmtsCategory"
            )
        )
    )

    private val expected = listOf(
        ServiceItem(
            id = "bta",
            type = "BtaType",
            title = "BtaTitle",
            category = "BtaCategory",
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        ),
        ServiceItem(
            id = "club",
            type = "ClubItemType",
            title = "ClubItemTitle",
            category = "ClubItemCategory",
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        ),
        ServiceItem(
            id = "dscp",
            type = "DscpType",
            title = "DscpTitle",
            category = "DscpCategory",
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        ),
        ServiceItem(
            id = "flexiLease",
            title = null,
            category = null,
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        ),
        ServiceItem(
            id = "nac",
            type = "NACType",
            title = "NACTitle",
            category = "NACCategory",
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        ),
        ServiceItem(
            id = "raccess",
            type = "RaccessType",
            title = "RaccessTitle",
            category = "RaccessCategory",
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        ),
        ServiceItem(
            id = "remoteLev",
            type = "RemoteLevType",
            title = "RemoteLevTitle",
            category = "RemoteLevCategory",
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        ),
        ServiceItem(
            id = "services",
            type = "ContractServicesItemType",
            title = "ContractServicesItemTitle",
            category = "ContractServicesItemCategory",
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        ),
        ServiceItem(
            id = "tmts",
            type = "TmtsType",
            title = "TmtsTitle",
            category = "TmtsCategory",
            description = null,
            url = null,
            urlSso = null,
            urlCvs = null,
            price = null,
            currency = null,
            offer = null,
            validity = null,
            status = null
        )
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(GetServiceListPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute then make a get API call`() {
        val paramsVin = "testVin"
        every { executor.params(any()) } returns paramsVin
        coEvery { communicationManager.get<Contract>(any(), any()) } returns NetworkResponse.Success(contract)

        runBlockingTest {
            val response = executor.execute()

            verify {
                executor.request(
                    type = eq(Contract::class.java),
                    urls = eq(arrayOf("/shop/v1/contracts/", paramsVin)),
                    headers = any(),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<Contract>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(servicesList, success.response)
        }
    }

    @Test
    fun `check transform BaseItem to ServiceItem`() {
        val id = "TEST_ID"
        val baseItem = BaseItem(
            code = "testCode",
            validity = null,
            type = "testType",
            title = "testTitle",
            category = "testCategory",
            status = null
        )
        val response = executor.asServiceItem(baseItem, id)
        Assert.assertEquals(id, response.id)
        Assert.assertEquals(baseItem.validity, response.validity)
        Assert.assertEquals(baseItem.title, response.title)
        Assert.assertEquals(baseItem.category, response.category)
        Assert.assertEquals(baseItem.status, response.status)
    }

    @Test
    fun `check transform list of BaseItem to ServiceItems`() {
        val id = "TEST_ID"
        val baseItem = BaseItem(
            code = "testCode",
            validity = null,
            type = "testType",
            title = "testTitle",
            category = "testCategory",
            status = null
        )
        val response = executor.asServiceItems(listOf(null, baseItem, null), id)
        Assert.assertEquals(1, response.size)
        val item = response.first()
        Assert.assertEquals(id, item.id)
        Assert.assertEquals(baseItem.validity, item.validity)
        Assert.assertEquals(baseItem.title, item.title)
        Assert.assertEquals(baseItem.category, item.category)
        Assert.assertEquals(baseItem.status, item.status)
    }

    @Test
    fun `check mergeContracts to get ServicesList`() {
        val response = executor.mergeContracts(contract)
        Assert.assertEquals(expected, response.services)
    }

    @Test
    fun `when execute params with the right paramsVin then return paramsVin`() {
        val paramsVin = "testVin"
        val input = mapOf(Constants.PARAM_VIN to paramsVin)
        val param = executor.params(input)

        Assert.assertEquals(paramsVin, param)
    }

    @Test
    fun `when execute params with missing paramsVin then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid paramsVin then throw missing parameter`() {
        val paramsVin = 123
        val input = mapOf(Constants.PARAM_VIN to paramsVin)
        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }
}
