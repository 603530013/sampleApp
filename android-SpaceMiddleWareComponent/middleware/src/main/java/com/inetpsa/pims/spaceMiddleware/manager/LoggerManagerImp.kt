package com.inetpsa.pims.spaceMiddleware.manager

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.IMonitoringManager
import com.inetpsa.mmx.foundation.monitoring.MAX_CACHED_LOG_FILE
import com.inetpsa.mmx.foundation.monitoring.MAX_TIME_DURATION
import com.inetpsa.mmx.foundation.monitoring.MIN_TIME_DURATION
import com.inetpsa.mmx.foundation.tools.LogLevel
import com.inetpsa.mmx.foundation.tools.NewLogFile
import com.inetpsa.pims.spaceMiddleware.util.hasEnumNullable
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull

internal class LoggerManagerImp : LoggerManager {

    internal companion object {

        const val LOG_LEVEL = "logLevel"
        const val MONITORING = "monitoring"
        const val NEW_LOG_FILE = "newLogFile"
        const val MAX_LOG_FILES = "maxLogFiles"
        const val NEW_LOG_TIME_DURATION = "newLogTimeDuration"
    }

    private var monitoringData: MonitoringData = MonitoringData(
        logLevel = LogLevel.High,
        newLogFile = NewLogFile.AppLaunch,
        maxLogFiles = MAX_CACHED_LOG_FILE,
        newLogTimeDuration = MAX_TIME_DURATION
    )

    /**
     * Configures PIMS logger
     *
     * @param parameters [Map] contains all data required to configure PimsLogger
     */
    override fun configure(monitoringManager: IMonitoringManager, parameters: Map<String, Any?>?) {
        monitoringData = fetchParams(parameters)
        monitoringManager.configurePIMSLogger(
            logLevel = monitoringData.logLevel,
            newLogFile = monitoringData.newLogFile,
            newLogTimeDuration = monitoringData.newLogTimeDuration,
            maxLogFiles = monitoringData.maxLogFiles
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun fetchParams(parameters: Map<String, Any?>?): MonitoringData {
        val monitoring: Map<String, Any?>? = parameters?.get(MONITORING) as? Map<String, Any?>
        val logLevel = (monitoring.hasEnumNullable<LogLevel>(LOG_LEVEL)) ?: monitoringData.logLevel
        val newLogFile = (monitoring.hasEnumNullable<NewLogFile>(NEW_LOG_FILE)) ?: monitoringData.newLogFile
        val maxLogFiles: Int = (monitoring hasOrNull MAX_LOG_FILES) ?: monitoringData.maxLogFiles
        val newLogTimeDuration: Int = (monitoring.hasOrNull<Int>(NEW_LOG_TIME_DURATION))
            ?.run {
                when {
                    this < MIN_TIME_DURATION -> MIN_TIME_DURATION
                    this > MAX_TIME_DURATION -> MAX_TIME_DURATION
                    else -> this
                }
            } ?: monitoringData.newLogTimeDuration

        return MonitoringData(
            logLevel = logLevel,
            newLogFile = newLogFile,
            maxLogFiles = maxLogFiles,
            newLogTimeDuration = newLogTimeDuration
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal data class MonitoringData(
        val logLevel: LogLevel,
        val newLogFile: NewLogFile,
        val maxLogFiles: Int,
        val newLogTimeDuration: Int
    )
}
