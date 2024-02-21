package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.uisample.views.models.Input

class ConfigurationApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.CONFIGURATION}"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input>? = null

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
