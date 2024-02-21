package com.inetpsa.pims.spaceMiddleware.executor.radioplayer.get

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.radioplayer.OnAirOutput
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.RadioPlayerOnAirResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.map

internal class GetOnAirPsaExecutor(command: BaseCommand) :
    BasePsaExecutor<String, OnAirOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_ID

    override suspend fun execute(input: String): NetworkResponse<OnAirOutput> {
        val request = request(
            type = RadioPlayerOnAirResponse::class.java,
            urls = arrayOf("/shop/v1/stations/", input, "/onair")
        )
        return communicationManager.get<RadioPlayerOnAirResponse>(request, MiddlewareCommunicationManager.MymToken)
            .map { transformToStations(it) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToStation(response: RadioPlayerOnAirResponse.OnAirItem): OnAirOutput.OnAirItem =
        OnAirOutput.OnAirItem(
            showName = response.show?.name,
            songName = response.song?.name,
            artist = response.song?.artist
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformToStations(response: RadioPlayerOnAirResponse): OnAirOutput {
        val items = response.stations
            ?.map { transformToStation(it) }
            .orEmpty()
        return OnAirOutput(items)
    }
}
