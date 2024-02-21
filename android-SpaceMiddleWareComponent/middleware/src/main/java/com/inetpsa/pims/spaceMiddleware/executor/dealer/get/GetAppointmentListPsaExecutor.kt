package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.StoreMode
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.mapper.history.AppointmentHistoryXPsaMapper
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.create.CachedAppointmentsXPSA
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.history.HistoryOutput
import com.inetpsa.pims.spaceMiddleware.util.readSync

internal class GetAppointmentListPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<Unit, HistoryOutput>(command) {

    override fun params(parameters: Map<String, Any?>?) = Unit

    override suspend fun execute(input: Unit): NetworkResponse<HistoryOutput> {
        val cache = readFromCache()
        val transformation = AppointmentHistoryXPsaMapper().transformOutput(cache)
        return NetworkResponse.Success(transformation)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun readFromCache(): CachedAppointmentsXPSA =
        middlewareComponent.readSync(Constants.Storage.APPOINTMENT_xPSA, StoreMode.APPLICATION)
            ?: CachedAppointmentsXPSA(HashSet())
}
