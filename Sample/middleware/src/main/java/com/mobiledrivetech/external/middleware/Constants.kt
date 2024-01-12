package com.mobiledrivetech.external.middleware

internal object Constants {

    const val API_PREFIX = "mobiledrivetech.middleware"
    const val SERVICE_NAME = "middleware"
    const val COMPONENT_NAME = "MiddlewareComponent"
    const val APIS = "apis"

    internal object API {
        const val CONFIGURATION = "configuration"
        const val TEST = "test"
    }

    internal object Input {

        const val ACTION_TYPE = "actionType"
        const val ACTION = "action"

        object Configuration {

            const val LOCALE = "locale"
            const val SITE_CODE = "siteCode"
            const val LANGUAGE_PATH = "languagePath"
        }
    }

    const val PARAMS_KEY_CREDENTIAL = "credential"
    const val PARAMS_KEY_PROFILE = "profile"
    const val PARAMS_KEY_GOOGLE_API_KEY = "googleApiKey"
    const val PARAMS_KEY_CODE = "code"
    const val PARAMS_KEY_LABEL = "label"
    const val PARAMS_KEY_SUB_CODE = "subCode"
    const val PARAMS_KEY_INFO = "info"

    const val CONTEXT_KEY_ENVIRONMENT = "environment"
    const val CONTEXT_KEY_MODE = "mode"
    const val CONTEXT_KEY_LOG_LEVEL = "logLevel"
    const val CONTEXT_KEY_CLIENT_ID = "clientID"
    const val CONTEXT_KEY_CLIENT_SECRET = "clientSecret"
    const val CONTEXT_KEY_BRAND = "brand"
    const val CONTEXT_KEY_RETURN_TYPE = "returnType"
    const val CONTEXT_KEY_LOGIN = "Login"
    const val CONTEXT_KEY_PHONE_MODEL = "phoneModel"
    const val CONTEXT_KEY_OS_VERSION = "osVersion"
    const val CONTEXT_KEY_MARKET = "market"

    const val KEY_TRANSACTION_ID = "transactionId"
    const val KEY_NAME = "name"
    const val KEY_TYPE = "type"
    const val KEY_PARAMETERS = "parameters"
    const val KEY_RESULT = "result"
    const val KEY_SDK_VERSION = "sdkVersion"
    const val KEY_STATUS = "status"
    const val KEY_ERROR = "error"
    const val KEY_CONTEXT = "context"

}