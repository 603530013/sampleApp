package com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.services

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants.Input
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.ServiceType
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesNaftaLatamInput
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.services.ServicesOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.LatamDealerServiceResponse
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.unwrapNullable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class GetLatamServiceFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseFcaExecutor<ServicesNaftaLatamInput, ServicesOutput>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?) = ServicesNaftaLatamInput(
        vin = parameters has Input.VIN,
        dealerId = parameters has Appointment.BOOKING_ID,
        mileage = parameters has Appointment.MILEAGE,
        unit = parameters hasEnum Appointment.PARAM_MILEAGE_UNIT
    )

    override suspend fun execute(input: ServicesNaftaLatamInput): NetworkResponse<ServicesOutput> =
        coroutineScope {
            val asyncFactory = async { executeFactoryServices(input) }
            val asyncDealer = async { executeDealerServices(input) }
            val asyncRepair = async { executeRepairServices(input) }
            val responses = awaitAll(asyncFactory, asyncDealer, asyncRepair)
            val factoryResponse = responses[0]
            val dealerResponse = responses[1]
            val repairResponse = responses[2]

            if (factoryResponse is NetworkResponse.Failure &&
                dealerResponse is NetworkResponse.Failure &&
                repairResponse is NetworkResponse.Failure
            ) {
                factoryResponse
            } else {
                val factory = factoryResponse.unwrapNullable()
                    ?.let { transformToDealerServiceOutput(it, ServiceType.Factory) }.orEmpty()
                val dealer = dealerResponse.unwrapNullable()
                    ?.let { transformToDealerServiceOutput(it, ServiceType.Dealer) }.orEmpty()
                val repair = repairResponse.unwrapNullable()
                    ?.let { transformToDealerServiceOutput(it, ServiceType.Repair) }.orEmpty()

                val values = factory + dealer + repair
                NetworkResponse.Success(ServicesOutput(values))
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun executeFactoryServices(input: ServicesNaftaLatamInput):
        NetworkResponse<LatamDealerServiceResponse> =
        GetLatamFactoryServicesFcaExecutor(middlewareComponent, params).execute(input)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun executeDealerServices(input: ServicesNaftaLatamInput):
        NetworkResponse<LatamDealerServiceResponse> =
        GetLatamDealerServicesFcaExecutor(middlewareComponent, params).execute(input)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun executeRepairServices(input: ServicesNaftaLatamInput):
        NetworkResponse<LatamDealerServiceResponse> =
        GetLatamRepairServicesFcaExecutor(middlewareComponent, params).execute(input)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToDealerServiceOutput(
        response: LatamDealerServiceResponse,
        type: ServiceType
    ): List<ServicesOutput.Services> =
        response.services?.map { item ->
            ServicesOutput.Services(
                id = item.id,
                title = item.name,
                type = type,
                description = item.description,
                price = item.price
            )
        }.orEmpty()
}
