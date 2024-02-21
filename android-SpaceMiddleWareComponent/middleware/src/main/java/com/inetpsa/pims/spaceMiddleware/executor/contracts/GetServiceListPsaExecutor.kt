package com.inetpsa.pims.spaceMiddleware.executor.contracts

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.servicelist.BaseItem
import com.inetpsa.pims.spaceMiddleware.model.servicelist.Contract
import com.inetpsa.pims.spaceMiddleware.model.servicelist.ServicesList
import com.inetpsa.pims.spaceMiddleware.model.servicelist.ServicesList.ServiceItem
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("this is moved to GetVehicleContractsPSAExecutor")
internal class GetServiceListPsaExecutor(command: BaseCommand) : BasePsaExecutor<String, ServicesList>(command) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun mergeContracts(contract: Contract): ServicesList {
        val listNac = asServiceItems(contract.nac, Constants.CONTRACT_ID_PARAM_NAC)
        val listBta = asServiceItems(contract.bta, Constants.CONTRACT_ID_PARAM_BTA)
        val listClub = asServiceItems(contract.club, Constants.CONTRACT_ID_PARAM_CLUB)
        val listRemoteLev = asServiceItems(contract.remoteLev, Constants.CONTRACT_ID_PARAM_REMOTELEV)
        val listFlexiLease = contract.flexiLease?.mapNotNull {
            ServiceItem(
                id = Constants.CONTRACT_ID_PARAM_FLEXILEASE,
                status = it?.status
            )
        }.orEmpty()
        val listServices = asServiceItems(contract.services, Constants.CONTRACT_ID_PARAM_SERVICES)
        val listTmts = asServiceItems(contract.tmts, Constants.CONTRACT_ID_PARAM_TMTS)
        val listDscp = asServiceItems(contract.dscp, Constants.CONTRACT_ID_PARAM_DSCP)
        val listRaccess = asServiceItems(contract.raccess, Constants.CONTRACT_ID_PARAM_RACCESS)

        val services: List<ServiceItem> = listNac + listBta + listClub + listRemoteLev +
            listFlexiLease + listServices + listTmts + listDscp + listRaccess
        return ServicesList(services.sortedBy { it.id })
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun asServiceItem(item: BaseItem, id: String): ServiceItem = ServiceItem(
        id = id,
        title = item.title,
        validity = item.validity,
        category = item.category,
        status = item.status,
        type = item.type
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun asServiceItems(items: List<BaseItem?>?, id: String) = items
        ?.mapNotNull { item -> item?.let { asServiceItem(it, id) } }
        .orEmpty()

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<ServicesList> {
        val request = request(
            type = Contract::class.java,
            urls = arrayOf("/shop/v1/contracts/", input),
            headers = mapOf("refresh-sams-cache" to "1")
        )

        return communicationManager.get<Contract>(request, MiddlewareCommunicationManager.MymToken).map {
            mergeContracts(it)
        }
    }
}
