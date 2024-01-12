package com.mobiledrivetech.external.middleware.foundation.monitoring.logger

import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.models.SpecificLogType


//Log.Error(TAG, SpecificLogType, Texts)
//12/25 08:08:08:888 SeverityLevel TAG | ClassName | Method Name | Line Number [SpecificLogType] Texts
/**
 * Support TAG
 * Define log severity level by APIs
 * Define SpecificLogType log type by enum. Include suspicious keyword
 * Get class/method/line name information and fill in log
 * Support timestamp & process id in log for Vera/QNX platform.
 * One mechanism to filter logs with level by outer property.
 */
interface ILogger {

    /** Log a debug message with optional format args. */
    fun debug(tag: String, type: SpecificLogType? = null, message: String? = "")

    /** Log an info message with optional format args. */
    fun inform(tag: String, type: SpecificLogType? = null, message: String? = "")

    /** Log a warning message with optional format args. */
    fun warning(tag: String, type: SpecificLogType? = null, message: String? = "")


    /** Log a error message with optional format args. */
    fun error(tag: String, type: SpecificLogType? = null, message: String? = "")
}