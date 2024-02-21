package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import okhttp3.logging.HttpLoggingInterceptor

internal class PimsOkHttpLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        PIMSLogger.d(message)
    }
}
