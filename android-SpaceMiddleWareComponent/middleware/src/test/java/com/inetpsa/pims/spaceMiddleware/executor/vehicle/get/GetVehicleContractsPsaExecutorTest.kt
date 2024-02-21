package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.PsaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Get
import com.inetpsa.pims.spaceMiddleware.model.common.Action.Refresh
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.BaseItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.Bta
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.ClubItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.ContractServicesItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.Dscp
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.ExtraBaseItem.Validity
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.NacItem
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.RAccess
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.RemoteLev
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse.Tmts
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.contracts.ContractsOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.details.AccountVehicleInfoPsa
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Suppress("LargeClass")
internal class GetVehicleContractsPsaExecutorTest : PsaExecutorTestHelper() {

    private lateinit var executor: GetVehicleContractsPsaExecutor

    // region models
    private val contractsResponse = ContractResponse(
        flexiLease = listOf(
            BaseItem(
                code = "FlexiLeaseCode",
                type = "FlexiLeaseType",
                status = null,
                title = "FlexiLeaseTitle"
            ),
            BaseItem(
                code = "FlexiLeaseCode1",
                type = "FlexiLeaseType1",
                status = null,
                title = "FlexiLeaseTitle1"
            )
        ),
        nac = listOf(
            NacItem(
                code = "NACCode",
                validity = Validity(10, 20),
                type = "NACType",
                title = "NACTitle",
                category = "NACCategory",
                status = ContractResponse.STATUS_ACTIVE,
                isExtensible = true,
                statusReason = "testStatusReason",
                topMainImage = "testTopMainImage",
                hasFreeTrial = true,
                subscriptionTechCreationDate = "testSubscriptionTechCreationDate",
                urlSso = "testUrlSso",
                associationId = "testAssociationId"
            ),
            NacItem(
                code = "NACCode1",
                validity = Validity(30, 40),
                type = "NACType1",
                title = "NACTitle1",
                category = "NACCategory",
                status = ContractResponse.STATUS_DEACTIVATED,
                isExtensible = true,
                statusReason = "testStatusReason",
                topMainImage = "testTopMainImage",
                hasFreeTrial = true,
                subscriptionTechCreationDate = "testSubscriptionTechCreationDate",
                urlSso = "testUrlSso",
                associationId = "testAssociationId"
            )
        ),
        remoteLev = listOf(
            RemoteLev(
                code = "RemoteLevCode",
                validity = Validity(10, 20),
                type = "RemoteLevType",
                title = "RemoteLevTitle",
                category = "RemoteLevCategory",
                status = ContractResponse.STATUS_DEACTIVATED,
                isExtensible = false,
                associationId = "testAssociationID",
                statusReason = "testStatusReason",
                fds = listOf("testFDS")
            ),
            RemoteLev(
                code = "RemoteLevCode1",
                validity = Validity(30, 40),
                type = "RemoteLevType1",
                title = "RemoteLevTitle1",
                category = "RemoteLevCategory",
                status = ContractResponse.STATUS_ACTIVE,
                isExtensible = false,
                associationId = "testAssociationID",
                statusReason = "testStatusReason",
                fds = listOf("testFDS")
            )
        ),
        club = listOf(
            ClubItem(
                code = "ClubItemCode",
                validity = Validity(10, 20),
                type = "ClubItemType",
                title = "ClubItemTitle",
                category = "ClubItemCategory",
                status = ContractResponse.STATUS_CANCELLED,
                isExtensible = null
            ),
            ClubItem(
                code = "ClubItemCode1",
                validity = Validity(30, 40),
                type = "ClubItemType1",
                title = "ClubItemTitle1",
                category = "ClubItemCategory",
                status = ContractResponse.STATUS_ACTIVE,
                isExtensible = null
            )
        ),
        services = listOf(
            ContractServicesItem(
                code = "ContractServicesItemCode",
                validity = Validity(10, 20),
                type = "ContractServicesItemType",
                title = "ContractServicesItemTitle",
                category = "ContractServicesItemCategory",
                label = "ContractServicesItemLabel",
                status = ContractResponse.STATUS_PENDING_ACTIVATION,
                isExtensible = true,
                products = listOf("testProducts")
            ),
            ContractServicesItem(
                code = "ContractServicesItemCode1",
                validity = Validity(30, 40),
                type = "ContractServicesItemType1",
                title = "ContractServicesItemTitle1",
                category = "ContractServicesItemCategory",
                label = "ContractServicesItemLabel",
                status = ContractResponse.STATUS_PENDING_ASSOCIATION,
                isExtensible = true,
                products = listOf("testProducts")
            )
        ),
        bta = listOf(
            Bta(
                code = "BtaCode",
                validity = Validity(10, 20),
                type = "BtaType",
                title = "BtaTitle",
                category = "BtaCategory",
                level = 1,
                secureCode = "BtaSecureCode",
                status = ContractResponse.STATUS_PENDING_ASSOCIATION,
                isExtensible = false
            )
        ),
        tmts = listOf(
            Tmts(
                code = "TmtsCode",
                validity = Validity(10, 20),
                type = "TmtsType",
                title = "TmtsTitle",
                category = "TmtsCategory",
                status = ContractResponse.STATUS_PENDING_SUBSCRIPTION,
                isExtensible = null,
                statusReason = "testStatusReason",
                topMainImage = "testTopMainImage",
                hasFreeTrial = true,
                urlSso = "testUrlSSO",
                associationId = "testAssociationId",
                subscriptionTechCreationDate = "testSubscriptionTechCreationDate"
            )
        ),
        dscp = listOf(
            Dscp(
                code = "DscpCode",
                validity = Validity(10, 20),
                type = "DscpType",
                title = "DscpTitle",
                category = "DscpCategory",
                status = ContractResponse.STATUS_TERMINATED,
                isExtensible = null,
                statusReason = null
            )
        ),
        raccess = listOf(
            RAccess(
                code = "RaccessCode",
                validity = Validity(10, 20),
                type = "RaccessType",
                title = "RaccessTitle",
                category = "RaccessCategory",
                fds = listOf("testFDS"),
                status = ContractResponse.STATUS_TERMINATED_EXPIRED,
                isExtensible = true,
                associationId = "testAssociationId",
                statusReason = "testStatusReason"
            )
        )
    )

    private val contractsOutput: ContractsOutput = ContractsOutput(
        listOf(
            ContractsOutput.NacItem(
                code = "NACType",
                validity = ContractsOutput.ExtraBaseItem.Validity(10, 20),
                title = "NACTitle",
                category = "NACCategory",
                status = ContractsOutput.Status.Active,
                isExtensible = true,
                statusReason = "testStatusReason",
                topMainImage = "testTopMainImage",
                hasFreeTrial = true,
                subscriptionTechCreationDate = "testSubscriptionTechCreationDate",
                urlSso = "testUrlSso",
                associationId = "testAssociationId"
            ),
            ContractsOutput.NacItem(
                code = "NACType1",
                validity = ContractsOutput.ExtraBaseItem.Validity(30, 40),
                title = "NACTitle1",
                category = "NACCategory",
                status = ContractsOutput.Status.Deactivated,
                isExtensible = true,
                statusReason = "testStatusReason",
                topMainImage = "testTopMainImage",
                hasFreeTrial = true,
                subscriptionTechCreationDate = "testSubscriptionTechCreationDate",
                urlSso = "testUrlSso",
                associationId = "testAssociationId"
            ),
            ContractsOutput.RemoteLev(
                code = "RemoteLevType",
                validity = ContractsOutput.ExtraBaseItem.Validity(10, 20),
                title = "RemoteLevTitle",
                category = "RemoteLevCategory",
                status = ContractsOutput.Status.Deactivated,
                isExtensible = false,
                associationId = "testAssociationID",
                statusReason = "testStatusReason",
                fds = listOf("testFDS")
            ),
            ContractsOutput.RemoteLev(
                code = "RemoteLevType1",
                validity = ContractsOutput.ExtraBaseItem.Validity(30, 40),
                title = "RemoteLevTitle1",
                category = "RemoteLevCategory",
                status = ContractsOutput.Status.Active,
                isExtensible = false,
                associationId = "testAssociationID",
                statusReason = "testStatusReason",
                fds = listOf("testFDS")
            ),
            ContractsOutput.BaseItem(
                code = "FlexiLeaseCode",
                status = null,
                title = "FlexiLeaseTitle"
            ),
            ContractsOutput.BaseItem(
                code = "FlexiLeaseCode1",
                status = null,
                title = "FlexiLeaseTitle1"
            ),
            ContractsOutput.ClubItem(
                code = "ClubItemType",
                validity = ContractsOutput.ExtraBaseItem.Validity(10, 20),
                title = "ClubItemTitle",
                category = "ClubItemCategory",
                status = ContractsOutput.Status.Cancelled,
                isExtensible = null
            ),
            ContractsOutput.ClubItem(
                code = "ClubItemType1",
                validity = ContractsOutput.ExtraBaseItem.Validity(30, 40),
                title = "ClubItemTitle1",
                category = "ClubItemCategory",
                status = ContractsOutput.Status.Active,
                isExtensible = null
            ),
            ContractsOutput.ServicesItem(
                code = "ContractServicesItemType",
                validity = ContractsOutput.ExtraBaseItem.Validity(10, 20),
                title = "ContractServicesItemTitle",
                category = "ContractServicesItemCategory",
                label = "ContractServicesItemLabel",
                status = ContractsOutput.Status.PendingActivation,
                isExtensible = true,
                products = listOf("testProducts")
            ),
            ContractsOutput.ServicesItem(
                code = "ContractServicesItemType1",
                validity = ContractsOutput.ExtraBaseItem.Validity(30, 40),
                title = "ContractServicesItemTitle1",
                category = "ContractServicesItemCategory",
                label = "ContractServicesItemLabel",
                status = ContractsOutput.Status.PendingAssociation,
                isExtensible = true,
                products = listOf("testProducts")
            ),
            ContractsOutput.Bta(
                code = "BtaType",
                validity = ContractsOutput.ExtraBaseItem.Validity(10, 20),
                title = "BtaTitle",
                category = "BtaCategory",
                level = 1,
                secureCode = "BtaSecureCode",
                status = ContractsOutput.Status.PendingAssociation,
                isExtensible = false
            ),
            ContractsOutput.Tmts(
                code = "TmtsType",
                validity = ContractsOutput.ExtraBaseItem.Validity(10, 20),
                title = "TmtsTitle",
                category = "TmtsCategory",
                status = ContractsOutput.Status.PendingSubscription,
                isExtensible = null,
                statusReason = "testStatusReason",
                topMainImage = "testTopMainImage",
                hasFreeTrial = true,
                urlSso = "testUrlSSO",
                associationId = "testAssociationId",
                subscriptionTechCreationDate = "testSubscriptionTechCreationDate"
            ),
            ContractsOutput.Dscp(
                code = "DscpType",
                validity = ContractsOutput.ExtraBaseItem.Validity(10, 20),
                title = "DscpTitle",
                category = "DscpCategory",
                status = ContractsOutput.Status.Terminated,
                isExtensible = null,
                statusReason = null
            ),
            ContractsOutput.RAccess(
                code = "RaccessType",
                validity = ContractsOutput.ExtraBaseItem.Validity(10, 20),
                title = "RaccessTitle",
                category = "RaccessCategory",
                fds = listOf("testFDS"),
                status = ContractsOutput.Status.TerminatedExpired,
                isExtensible = true,
                associationId = "testAssociationId",
                statusReason = "testStatusReason"
            )
        ).sortedBy { (it as? ContractsOutput.ExtraBaseItem)?.validity?.start }
    )
    // endregion

    @Before
    override fun setup() {
        super.setup()
        every { middlewareComponent.dataManager } returns dataManager
        every { userSession.customerId } returns "testCustomerId"
        every { userSessionManager.getUserSession() } returns userSession
        every { middlewareComponent.userSessionManager } returns userSessionManager
        executor = spyk(GetVehicleContractsPsaExecutor(baseCommand))
    }

    @Test
    fun `when execute params with missing action then throw missing parameter`() {
        val input = emptyMap<String, String>()
        val exception = PIMSFoundationError.missingParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid action then throw invalid parameter`() {
        val paramsId = 123
        val input = mapOf(Constants.Input.ACTION to paramsId)
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with missing vin then throw missing parameter`() {
        val input = mapOf(Constants.Input.ACTION to Constants.Input.Action.GET)
        val exception = PIMSFoundationError.missingParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid vin then throw invalid parameter`() {
        val vin = 123
        val input = mapOf(
            Constants.Input.ACTION to Constants.Input.Action.GET,
            Constants.Input.VIN to vin
        )
        val exception = PIMSFoundationError.invalidParameter(Constants.Input.VIN)
        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute transformToContractsOutput then return ContractsOutput`() {
        val result = executor.transformToContractsOutput(contractsResponse)
        Assert.assertEquals(contractsOutput, result)
        Assert.assertEquals(contractsOutput.hashCode(), result.hashCode())
        Assert.assertEquals(contractsOutput.toString(), result.toString())
    }

    @Test
    fun `when execute readFromCache in available case then return cache`() {
        every { dataManager.read(any(), any()) } returns contractsResponse.toJson()
        val cache = executor.readFromCache("testVin")
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_contracts_testVin"), eq(APPLICATION)) }
        Assert.assertEquals(contractsResponse, cache)
    }

    @Test
    fun `when execute readFromCache in empty case then return null`() {
        every { dataManager.read(any(), any()) } returns "   "
        val cache = executor.readFromCache("testVin")
        verify { dataManager.read(eq("PEUGEOT_PREPROD_testCustomerId_contracts_testVin"), eq(APPLICATION)) }
        Assert.assertEquals(null, cache)
    }

    @Test
    fun `when execute with action get and there is not a valid cache then make a network call`() {
        val vin = "testVin"
        every { executor.readFromCache(any()) } returnsMany listOf(null, contractsResponse)
        coEvery { executor.makeRequest(eq(vin)) } returns NetworkResponse.Success(contractsOutput)
        runTest {
            executor.execute(UserInput(Get, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 1) { executor.readFromCache(eq(vin)) }
            coVerify(exactly = 1) { executor.makeRequest(any()) }
        }
    }

    @Test
    fun `when execute with action get and there is a valid cache then return response from cache`() {
        val vin = "testVin"
        every { executor.readFromCache(vin) } returns contractsResponse

        runTest {
            val response = executor.execute(UserInput(Get, vin))
            verify(exactly = 1) { executor.readFromCache(eq(vin)) }
            coVerify(exactly = 0) { executor.makeRequest(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(contractsOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with success response`() {
        val vin = "testVin"
        every { executor.readFromCache(vin) } returns contractsResponse
        coEvery { executor.makeRequest(eq(vin)) } returns NetworkResponse.Success(contractsOutput)

        runTest {
            val response = executor.execute(UserInput(Refresh, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readFromCache(eq(vin)) }
            coVerify(exactly = 1) { executor.makeRequest(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(contractsOutput, success.response)
        }
    }

    @Test
    fun `when execute with action refresh then make a network call with failure response`() {
        val vin = "testVin"
        val error = PimsErrors.serverError(null, "test-errors")
        coEvery { executor.makeRequest(any()) } returns NetworkResponse.Failure(error)

        runTest {
            val response = executor.execute(UserInput(Refresh, vin))
            // check the case only before the network call,
            // for the network call we check the behavior with refresh action
            verify(exactly = 0) { executor.readFromCache(eq(vin)) }
            coVerify(exactly = 1) { executor.makeRequest(any()) }

            Assert.assertEquals(true, response is NetworkResponse.Failure)
            val failure = response as NetworkResponse.Failure
            Assert.assertEquals(error.code, failure.error?.code)
            Assert.assertEquals(error.message, failure.error?.message)
            Assert.assertEquals(error.subError?.status, failure.error?.subError?.status)
            Assert.assertEquals(error.subError?.body, failure.error?.subError?.body)
        }
    }

    @Test
    fun `when execute saveContracts then save contracts in cache`() {
        every {
            dataManager.create(
                key = any(),
                data = any(),
                mode = any(),
                callback = captureLambda<(Boolean) -> Unit>()
            )
        } answers {
            lambda<(Boolean) -> Unit>().captured.invoke(true)
        }

        runTest { executor.saveContracts("testKey", contractsResponse) }

        verify {
            dataManager.create(
                key = eq("PEUGEOT_PREPROD_testCustomerId_contracts_testKey"),
                data = any(),
                mode = eq(APPLICATION),
                callback = any()
            )
        }
    }

    @Test
    fun `when makeRequest then make a get API call`() {
        val vin = "testVin"
        coEvery { communicationManager.get<ContractResponse>(any(), any()) } returns
            NetworkResponse.Success(contractsResponse)
        coEvery { executor.saveContracts(any(), any()) } returns true

        runTest {
            val response = executor.makeRequest(vin)

            verify {
                executor.request(
                    type = eq(object : TypeToken<ContractResponse>() {}.type),
                    urls = eq(arrayOf("/shop/v1/contracts/", vin)),
                    headers = eq(mapOf("refresh-sams-cache" to "1")),
                    queries = any(),
                    body = any()
                )
            }

            coVerify {
                communicationManager.get<AccountVehicleInfoPsa>(
                    request = any(),
                    tokenType = eq(MiddlewareCommunicationManager.MymToken)
                )
            }
            coVerify(exactly = 1) { executor.saveContracts(eq(vin), eq(contractsResponse)) }
            coVerify(exactly = 1) { executor.transformToContractsOutput(eq(contractsResponse)) }
            Assert.assertEquals(true, response is NetworkResponse.Success)
            val success = response as NetworkResponse.Success
            Assert.assertEquals(contractsOutput, success.response)
        }
    }

    @Test
    fun `when execute transformToStatus then return ContractsOutput-Status`() {
        var status: ContractsOutput.Status? = executor.transformToStatus(ContractResponse.STATUS_ACTIVE)
        Assert.assertEquals(ContractsOutput.Status.Active, status)

        status = executor.transformToStatus(ContractResponse.STATUS_TERMINATED_EXPIRED)
        Assert.assertEquals(ContractsOutput.Status.TerminatedExpired, status)

        status = executor.transformToStatus(ContractResponse.STATUS_SAMS_EXPIRED)
        Assert.assertEquals(ContractsOutput.Status.TerminatedExpired, status)

        status = executor.transformToStatus(ContractResponse.STATUS_SAMS_EXPIRED_IN)
        Assert.assertEquals(ContractsOutput.Status.TerminatedExpired, status)

        status = executor.transformToStatus(ContractResponse.STATUS_PENDING_ACTIVATION)
        Assert.assertEquals(ContractsOutput.Status.PendingActivation, status)

        status = executor.transformToStatus(ContractResponse.STATUS_PENDING_ASSOCIATION)
        Assert.assertEquals(ContractsOutput.Status.PendingAssociation, status)

        status = executor.transformToStatus(ContractResponse.STATUS_PENDING_SUBSCRIPTION)
        Assert.assertEquals(ContractsOutput.Status.PendingSubscription, status)

        status = executor.transformToStatus(ContractResponse.STATUS_CANCELLED)
        Assert.assertEquals(ContractsOutput.Status.Cancelled, status)

        status = executor.transformToStatus(ContractResponse.STATUS_SAMS_CANCELLED)
        Assert.assertEquals(ContractsOutput.Status.Cancelled, status)

        status = executor.transformToStatus(ContractResponse.STATUS_DEACTIVATED)
        Assert.assertEquals(ContractsOutput.Status.Deactivated, status)

        status = executor.transformToStatus(ContractResponse.STATUS_TERMINATED)
        Assert.assertEquals(ContractsOutput.Status.Terminated, status)

        status = executor.transformToStatus(1)
        Assert.assertEquals(null, status)

        status = executor.transformToStatus(null)
        Assert.assertEquals(null, status)
    }

    @Test
    fun `when execute transformToValidity then return ContractsOutput-Validity`() {
        var validityInput = ContractResponse.ExtraBaseItem.Validity(start = null, end = null)
        var validityOutput: ContractsOutput.ExtraBaseItem.Validity? = executor.transformToValidity(validityInput)
        Assert.assertEquals(null, validityOutput?.start)
        Assert.assertEquals(null, validityOutput?.end)

        validityInput = ContractResponse.ExtraBaseItem.Validity(start = 10, end = null)
        validityOutput = executor.transformToValidity(validityInput)
        Assert.assertEquals(10, validityOutput?.start)
        Assert.assertEquals(null, validityOutput?.end)

        validityInput = ContractResponse.ExtraBaseItem.Validity(start = null, end = 10)
        validityOutput = executor.transformToValidity(validityInput)
        Assert.assertEquals(null, validityOutput?.start)
        Assert.assertEquals(10, validityOutput?.end)

        validityInput = ContractResponse.ExtraBaseItem.Validity(start = 10, end = 10)
        validityOutput = executor.transformToValidity(validityInput)
        Assert.assertEquals(10, validityOutput?.start)
        Assert.assertEquals(10, validityOutput?.end)
        Assert.assertEquals(validityInput.hashCode(), validityOutput?.hashCode())
        Assert.assertEquals(validityInput.toString(), validityOutput?.toString())

        validityOutput = executor.transformToValidity(null)
        Assert.assertEquals(null, validityOutput)
    }

    @Test
    fun `when execute transformToNac then return ContractsOutput-NacItem`() {
        val item = contractsResponse.nac?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToNac(item!!)
        val expected = contractsOutput.contracts
            .filterIsInstance(ContractsOutput.NacItem::class.java)
            .firstOrNull { it.code.equals(item.type) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `when execute transformToRemoteLev then return ContractsOutput-RemoteLev`() {
        val item = contractsResponse.remoteLev?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToRemoteLev(item!!)
        val expected = contractsOutput.contracts
            .filterIsInstance(ContractsOutput.RemoteLev::class.java)
            .firstOrNull { it.code.equals(item.type) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `when execute transformToBaseItem then return ContractsOutput-BaseItem`() {
        val item = contractsResponse.flexiLease?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToBaseItem(item!!)
        val expected = contractsOutput.contracts
            .firstOrNull { it.code.equals(item.code) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `when execute transformToClubItem then return ContractsOutput-ClubItem`() {
        val item = contractsResponse.club?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToClubItem(item!!)
        val expected = contractsOutput.contracts
            .filterIsInstance(ContractsOutput.ClubItem::class.java)
            .firstOrNull { it.code.equals(item.type) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `when execute transformToService then return ContractsOutput-ServicesItem`() {
        val item = contractsResponse.services?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToService(item!!)
        val expected = contractsOutput.contracts
            .filterIsInstance(ContractsOutput.ServicesItem::class.java)
            .firstOrNull { it.code.equals(item.type) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `when execute transformToBta then return ContractsOutput-Bta`() {
        val item = contractsResponse.bta?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToBta(item!!)
        val expected = contractsOutput.contracts
            .filterIsInstance(ContractsOutput.Bta::class.java)
            .firstOrNull { it.code.equals(item.type) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `when execute transformToTmts then return ContractsOutput-Tmts`() {
        val item = contractsResponse.tmts?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToTmts(item!!)
        val expected = contractsOutput.contracts
            .filterIsInstance(ContractsOutput.Tmts::class.java)
            .firstOrNull { it.code.equals(item.type) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `when execute transformToDscp then return ContractsOutput-Dscp`() {
        val item = contractsResponse.dscp?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToDscp(item!!)
        val expected = contractsOutput.contracts
            .filterIsInstance(ContractsOutput.Dscp::class.java)
            .firstOrNull { it.code.equals(item.type) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }

    @Test
    fun `when execute transformToRAccess then return ContractsOutput-RAccess`() {
        val item = contractsResponse.raccess?.firstOrNull()
        Assert.assertNotNull(item)
        val response = executor.transformToRAccess(item!!)
        val expected = contractsOutput.contracts
            .filterIsInstance(ContractsOutput.RAccess::class.java)
            .firstOrNull { it.code.equals(item.type) }

        Assert.assertEquals(expected, response)
        Assert.assertEquals(expected.hashCode(), response.hashCode())
        Assert.assertEquals(expected.toString(), response.toString())
    }
}
