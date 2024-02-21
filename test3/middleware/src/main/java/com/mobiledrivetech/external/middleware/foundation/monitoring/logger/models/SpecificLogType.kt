package com.mobiledrivetech.external.middleware.foundation.monitoring.logger.models

enum class SpecificLogType {
    DEPRECATED, // Deprecated
    CATCH_EXCEPTION, // Catch Exception
    OVERRIDE, // Must Override
    ILLEGAL_ARGUMENT, // IllegalArgument
    ILLEGAL_STATE, //IllegalState
    ILLEGAL_SETTING, //IllegalSetting
    UNLIKELY_HAPPEN, //Unlikely happen
    NEVER_HAPPEN, // Never happen
    POLLING, // Intentionally Poll
    RETRY, // Retry
    DELAY, // Force Delay
    WORKAROUND// Workaround
}