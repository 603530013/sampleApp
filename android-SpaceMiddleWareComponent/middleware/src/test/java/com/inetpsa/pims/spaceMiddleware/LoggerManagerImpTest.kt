package com.inetpsa.pims.spaceMiddleware

import com.inetpsa.mmx.foundation.monitoring.IMonitoringManager
import com.inetpsa.mmx.foundation.monitoring.MAX_CACHED_LOG_FILE
import com.inetpsa.mmx.foundation.monitoring.MAX_TIME_DURATION
import com.inetpsa.mmx.foundation.monitoring.MIN_TIME_DURATION
import com.inetpsa.mmx.foundation.tools.LogLevel
import com.inetpsa.mmx.foundation.tools.LogLevel.Low
import com.inetpsa.mmx.foundation.tools.NewLogFile
import com.inetpsa.mmx.foundation.tools.NewLogFile.Duration
import com.inetpsa.pims.spaceMiddleware.manager.LoggerManagerImp
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class LoggerManagerImpTest {

    private lateinit var loggerManagerImp: LoggerManagerImp
    private var monitoringManager: IMonitoringManager = mockk()

    @Before
    fun setup() {
        loggerManagerImp = spyk(LoggerManagerImp())
        monitoringManager = mockk(relaxed = true)
    }

    @Test
    fun `When valid params then initialize with params`() {
        runTest {
            val validMonitoringParams = mutableMapOf<String, Any>(
                Pair(
                    "monitoring",
                    hashMapOf<String, Any>(
                        Pair("logLevel", "low"),
                        Pair("newLogFile", "duration"),
                        Pair("newLogTimeDuration", 10)
                    )
                )
            )

            loggerManagerImp.configure(monitoringManager, validMonitoringParams)

            verify(exactly = 1) {
                monitoringManager.configurePIMSLogger(
                    logLevel = Low,
                    newLogFile = Duration,
                    newLogTimeDuration = 10,
                    maxLogFiles = MAX_CACHED_LOG_FILE
                )
            }
        }
    }

    @Test
    fun `When missing param newLogFile then initialize with default value`() {
        val noLogFile = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("logLevel", "high"),
                    Pair("newLogTimeDuration", 3)
                )
            )
        )
        loggerManagerImp.configure(monitoringManager, noLogFile)
        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.High,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = MIN_TIME_DURATION,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When missing param Monitoring then initialize with default value`() {
        val noMonitoring = mapOf<String, Any>()
        loggerManagerImp.configure(monitoringManager, noMonitoring)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.High,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = MAX_TIME_DURATION,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When missing param LogLevel then initialize with default value`() {
        val noLogLevel = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("newLogFile", "appLaunch"),
                    Pair("newLogTimeDuration", 3)
                )
            )
        )

        loggerManagerImp.configure(monitoringManager, noLogLevel)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.High,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = 5,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When missing param duration then initialize with default value`() {
        val noDuration = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("logLevel", "high"),
                    Pair("newLogFile", "appLaunch")
                )
            )
        )

        loggerManagerImp.configure(monitoringManager, noDuration)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.High,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = MAX_TIME_DURATION,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When log duration is less then 5 min then initialize with default value`() {
        val logDurationLessThanFiveMinutes = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("logLevel", "high"),
                    Pair("newLogFile", "appLaunch"),
                    Pair("newLogTimeDuration", 3)
                )
            )
        )

        loggerManagerImp.configure(monitoringManager, logDurationLessThanFiveMinutes)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.High,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = 5,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When log duration is more then 1hour then initialize with default value`() {
        val logDurationMoreThanHour = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("logLevel", "high"),
                    Pair("newLogFile", "appLaunch"),
                    Pair("newLogTimeDuration", 10000)
                )
            )
        )

        loggerManagerImp.configure(monitoringManager, logDurationMoreThanHour)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.High,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = MAX_TIME_DURATION,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When invalid loglevel then initialize with default value`() {
        val invalidLogLevel = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("logLevel", "INVALID"),
                    Pair("newLogFile", "appLaunch"),
                    Pair("newLogTimeDuration", 10)
                )
            )
        )

        loggerManagerImp.configure(monitoringManager, invalidLogLevel)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.High,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = 10,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When invalid newLogFilz then initialize with default value`() {
        val inValidNewLogFile = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("logLevel", "low"),
                    Pair("newLogFile", "INVALID"),
                    Pair("newLogTimeDuration", 10)
                )
            )
        )

        loggerManagerImp.configure(monitoringManager, inValidNewLogFile)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.Low,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = 10,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When mid log level then initialize with default value`() {
        val midLogLevel = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("logLevel", "medium"),
                    Pair("newLogFile", "appLaunch"),
                    Pair("newLogTimeDuration", 5)
                )
            )
        )

        loggerManagerImp.configure(monitoringManager, midLogLevel)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.Medium,
                newLogFile = NewLogFile.AppLaunch,
                newLogTimeDuration = 5,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }

    @Test
    fun `When mid log level then initialize with day value`() {
        val midloglevel = mutableMapOf<String, Any>(
            Pair(
                "monitoring",
                hashMapOf<String, Any>(
                    Pair("logLevel", "medium"),
                    Pair("newLogFile", "day"),
                    Pair("newLogTimeDuration", 5)
                )
            )
        )

        loggerManagerImp.configure(monitoringManager, midloglevel)

        verify(exactly = 1) {
            monitoringManager.configurePIMSLogger(
                logLevel = LogLevel.Medium,
                newLogFile = NewLogFile.Day,
                newLogTimeDuration = 5,
                maxLogFiles = MAX_CACHED_LOG_FILE
            )
        }
    }
}
