package com.inetpsa.pims.spaceMiddleware.executor.vehicle.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.ContractResponse
import com.inetpsa.pims.spaceMiddleware.model.user.UserInput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.contracts.ContractsOutput
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.createSync
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.map
import com.inetpsa.pims.spaceMiddleware.util.readSync

@Suppress("TooManyFunctions")
internal class GetVehicleContractsPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<UserInput, ContractsOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): UserInput =
        UserInput(
            action = parameters hasEnum Constants.Input.ACTION,
            vin = parameters has Constants.Input.VIN
        )

    override suspend fun execute(input: UserInput): NetworkResponse<ContractsOutput> {
        requireNotNull(input.vin)

        if (input.action == Action.Get) {
            readFromCache(input.vin)
                ?.let { contracts ->
                    return NetworkResponse.Success(transformToContractsOutput(contracts))
                }
        }
        if (input.action == Action.Refresh || input.action == Action.Get) {
            return makeRequest(input.vin)
        }

        throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun makeRequest(vin: String): NetworkResponse<ContractsOutput> {
        val request = request(
            type = ContractResponse::class.java,
            urls = arrayOf("/shop/v1/contracts/", vin),
            headers = mapOf("refresh-sams-cache" to "1")
        )

        return communicationManager.get<ContractResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map {
                // save to cache
                saveContracts(vin, it)
                // map result
                transformToContractsOutput(it)
            }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun saveContracts(vin: String, data: ContractResponse): Boolean =
        middlewareComponent
            .createSync("${Constants.Storage.CONTRACTS}_$vin", data, StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(vin: String?): ContractResponse? =
        middlewareComponent
            .readSync("${Constants.Storage.CONTRACTS}_$vin", StoreMode.APPLICATION)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToContractsOutput(response: ContractResponse): ContractsOutput {
        val merge: List<ContractsOutput.BaseItem> = listOfNotNull(
            response.nac.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToNac(it) },
            response.remoteLev.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToRemoteLev(it) },
            response.flexiLease.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToBaseItem(it) },
            response.club.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToClubItem(it) },
            response.services.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToService(it) },
            response.bta.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToBta(it) },
            response.tmts.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToTmts(it) },
            response.dscp.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToDscp(it) },
            response.raccess.takeIf { !it.isNullOrEmpty() }?.filterNotNull()?.map { transformToRAccess(it) }
        ).flatten().sortedBy { (it as? ContractsOutput.ExtraBaseItem)?.validity?.start }
        return ContractsOutput(merge)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToStatus(status: Int?): ContractsOutput.Status? =
        when (status) {
            ContractResponse.STATUS_ACTIVE -> ContractsOutput.Status.Active
            ContractResponse.STATUS_TERMINATED_EXPIRED,
            ContractResponse.STATUS_SAMS_EXPIRED,
            ContractResponse.STATUS_SAMS_EXPIRED_IN -> ContractsOutput.Status.TerminatedExpired

            ContractResponse.STATUS_PENDING_ACTIVATION -> ContractsOutput.Status.PendingActivation
            ContractResponse.STATUS_PENDING_ASSOCIATION -> ContractsOutput.Status.PendingAssociation
            ContractResponse.STATUS_PENDING_SUBSCRIPTION -> ContractsOutput.Status.PendingSubscription
            ContractResponse.STATUS_CANCELLED,
            ContractResponse.STATUS_SAMS_CANCELLED -> ContractsOutput.Status.Cancelled

            ContractResponse.STATUS_DEACTIVATED -> ContractsOutput.Status.Deactivated
            ContractResponse.STATUS_TERMINATED -> ContractsOutput.Status.Terminated
            else -> null
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToValidity(validity: ContractResponse.ExtraBaseItem.Validity?):
        ContractsOutput.ExtraBaseItem.Validity? =
        validity?.let { ContractsOutput.ExtraBaseItem.Validity(it.start, it.end) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToNac(item: ContractResponse.NacItem): ContractsOutput.NacItem =
        ContractsOutput.NacItem(
            code = item.type,
            validity = transformToValidity(item.validity),
            title = item.title,
            category = item.category,
            status = transformToStatus(item.status),
            topMainImage = item.topMainImage,
            hasFreeTrial = item.hasFreeTrial,
            isExtensible = item.isExtensible,
            statusReason = item.statusReason,
            subscriptionTechCreationDate = item.subscriptionTechCreationDate,
            urlSso = item.urlSso,
            associationId = item.associationId
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToRemoteLev(item: ContractResponse.RemoteLev): ContractsOutput.RemoteLev =
        ContractsOutput.RemoteLev(
            code = item.type,
            validity = transformToValidity(item.validity),
            title = item.title,
            category = item.category,
            status = transformToStatus(item.status),
            isExtensible = item.isExtensible,
            statusReason = item.statusReason,
            associationId = item.associationId,
            fds = item.fds
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToBaseItem(item: ContractResponse.BaseItem): ContractsOutput.BaseItem =
        ContractsOutput.BaseItem(
            code = item.code,
            title = item.title,
            status = transformToStatus(item.status)
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToClubItem(item: ContractResponse.ClubItem): ContractsOutput.ClubItem =
        ContractsOutput.ClubItem(
            code = item.type,
            validity = transformToValidity(item.validity),
            title = item.title,
            category = item.category,
            status = transformToStatus(item.status),
            isExtensible = item.isExtensible
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToService(item: ContractResponse.ContractServicesItem): ContractsOutput.ServicesItem =
        ContractsOutput.ServicesItem(
            code = item.type,
            validity = transformToValidity(item.validity),
            title = item.title,
            category = item.category,
            status = transformToStatus(item.status),
            isExtensible = item.isExtensible,
            label = item.label,
            products = item.products
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToBta(item: ContractResponse.Bta): ContractsOutput.Bta =
        ContractsOutput.Bta(
            code = item.type,
            validity = transformToValidity(item.validity),
            title = item.title,
            category = item.category,
            status = transformToStatus(item.status),
            isExtensible = item.isExtensible,
            level = item.level,
            secureCode = item.secureCode
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToTmts(item: ContractResponse.Tmts): ContractsOutput.Tmts =
        ContractsOutput.Tmts(
            code = item.type,
            validity = transformToValidity(item.validity),
            title = item.title,
            category = item.category,
            status = transformToStatus(item.status),
            isExtensible = item.isExtensible,
            topMainImage = item.topMainImage,
            hasFreeTrial = item.hasFreeTrial,
            urlSso = item.urlSso,
            associationId = item.associationId,
            subscriptionTechCreationDate = item.subscriptionTechCreationDate,
            statusReason = item.statusReason
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDscp(item: ContractResponse.Dscp): ContractsOutput.Dscp =
        ContractsOutput.Dscp(
            code = item.type,
            validity = transformToValidity(item.validity),
            title = item.title,
            category = item.category,
            status = transformToStatus(item.status),
            isExtensible = item.isExtensible,
            statusReason = item.statusReason
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToRAccess(item: ContractResponse.RAccess): ContractsOutput.RAccess =
        ContractsOutput.RAccess(
            code = item.type,
            validity = transformToValidity(item.validity),
            title = item.title,
            category = item.category,
            status = transformToStatus(item.status),
            isExtensible = item.isExtensible,
            statusReason = item.statusReason,
            associationId = item.associationId,
            fds = item.fds
        )
}
