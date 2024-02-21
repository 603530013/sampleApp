package com.inetpsa.pims.spaceMiddleware.manager

import com.inetpsa.mmx.foundation.monitoring.IMonitoringManager

interface LoggerManager {

    fun configure(monitoringManager: IMonitoringManager, parameters: Map<String, Any?>?)
}
