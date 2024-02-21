package com.inetpsa.pims.spaceMiddleware.executor.settings.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.util.map

@Deprecated("this was moved to GetAssistancePhonesFcaExecutor class")
internal class GetRoadSideContactFCAExecutor(command: BaseCommand) :
    BaseFcaExecutor<String, Map<String, Map<String, String>>?>(command) {

    companion object {

        const val CALL_CENTER_TYPE = "ROADSIDE"
    }

    override fun params(parameters: Map<String, Any?>?): String = parameters has Constants.PARAM_VIN

    override suspend fun execute(input: String): NetworkResponse<Map<String, Map<String, String>>?> {
        return GetSettingsFCAExecutor(middlewareComponent, params).execute(input)
            .map { items ->
                val roadSide = items.firstOrNull()
                    ?.settings
                    ?.firstOrNull { it?.callCenterType == CALL_CENTER_TYPE }

                val phones = mutableMapOf<String, String>()
                    .apply {
                        roadSide?.primaryNumber
                            .takeIf { !it.isNullOrBlank() }
                            ?.let { put(Constants.PRIMARY, it) }
                        roadSide?.secondaryNumber
                            .takeIf { !it.isNullOrBlank() }
                            ?.let { put(Constants.SECONDARY, it) }
                    }
                    .takeIf { it.isNotEmpty() }
                    .orEmpty()

                mapOf(Constants.Output.Common.PHONES to phones)
            }
    }
}
