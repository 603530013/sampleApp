package com.mobiledrivetech.external.sample.domain.models

import com.mobiledrivetech.external.sample.data.model.ApiName
import com.mobiledrivetech.external.sample.data.model.PARAMS_KEY_ACTION_TYPE
import com.mobiledrivetech.external.sample.data.model.PARAMS_VALUE_TEST_ACTION
import com.mobiledrivetech.external.sample.providers.FacadeDataProvider

sealed class Commands {
    abstract val commandName: ApiName
    abstract val commandType: FacadeDataProvider.Method
    abstract val commandParams: Map<String, Any>?

    object TestCommand : Commands() {
        override val commandName = ApiName.Middleware.Test
        override val commandType = FacadeDataProvider.Method.GET
        override val commandParams: Map<String, Any> = mapOf(
            PARAMS_KEY_ACTION_TYPE to PARAMS_VALUE_TEST_ACTION
        )
    }
    // TODO: Add more commands here and also add it into defaultCommandsList if needed
}

val defaultCommandsList = listOf(
    Commands.TestCommand
)
