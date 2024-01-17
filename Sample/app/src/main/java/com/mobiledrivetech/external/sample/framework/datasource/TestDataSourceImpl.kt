package com.mobiledrivetech.external.sample.framework.datasource

import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.sample.data.datasource.TestDataSource
import com.mobiledrivetech.external.sample.data.model.ApiName
import com.mobiledrivetech.external.sample.data.model.PARAMS_KEY_ACTION_TYPE
import com.mobiledrivetech.external.sample.data.model.PARAMS_VALUE_TEST_ACTION
import com.mobiledrivetech.external.sample.data.model.RESULT_KEY
import com.mobiledrivetech.external.sample.data.model.RESULT_KEY_TEST
import com.mobiledrivetech.external.sample.providers.FacadeDataProvider

class TestDataSourceImpl(private val facadeDataProvider: FacadeDataProvider) : TestDataSource {
    /**
     * Initialize the middleware before executing other commands
     * @param parameter for now we can set it to emptyMap
     * @return
     */
    override suspend fun initialize(parameter: Map<String, Any>?): Map<String, Any?> =
        facadeDataProvider.fetch(
            api = ApiName.Middleware.Initialize,
            method = FacadeDataProvider.Method.INITIALIZE,
            parameter = emptyMap()
        )

    override suspend fun getTestCommandResult(): Result<String> =
        facadeDataProvider.fetch(
            api = ApiName.Middleware.Test,
            method = FacadeDataProvider.Method.GET,
            parameter = mapOf(PARAMS_KEY_ACTION_TYPE to PARAMS_VALUE_TEST_ACTION)
        ).let {
            MDLog.debug("getTestCommandResult $it")
            val result = it[RESULT_KEY] as? Map<*, *>
            Result.success(result?.get(RESULT_KEY_TEST) as String)
        }
}