package com.mobiledrivetech.external.middleware.foundation.monitoring.logger

import android.util.Log
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.models.SpecificLogType


// Todo: An API to save log into files and has recycle mechanism like android logs

object MDLog : ILogger {
    override fun debug(tag: String, type: SpecificLogType?, message: String?) {
        Log.d(tag, message ?: "")
    }

    override fun inform(tag: String, type: SpecificLogType?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun warning(tag: String, type: SpecificLogType?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun error(tag: String, type: SpecificLogType?, message: String?) {
        TODO("Not yet implemented")
    }
}