package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_SETTINGS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SERVICE_NAME
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SETTINGS
import com.inetpsa.pims.uisample.views.models.Input

@Deprecated("This should be replaced by NewSettingsApi")
class SettingsApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "$SERVICE_NAME.$SETTINGS"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.ListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(PARAM_ACTION_TYPE_SETTINGS)
        )
    )

    /**
     * Input to generate with the SET method
     */
    override val setInput: List<Input>? = null

    /**
     * Input to generate with the SUBSCRIBE method
     */
    override val subscribeInput: List<Input>? = null

    /**
     * Input to generate with the UNSUBSCRIBE method
     */
    override val unSubscribeInput: List<Input>? = null
}
