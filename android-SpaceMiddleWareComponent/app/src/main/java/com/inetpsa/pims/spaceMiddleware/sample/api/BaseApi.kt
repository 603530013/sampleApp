package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.uisample.views.models.Api
import com.inetpsa.pims.uisample.views.models.Input

abstract class BaseApi : ApiGenerator {

    /**
     * Api name
     */
    abstract val apiName: String

    /**
     * Input to generate with the GET method
     */
    abstract val getInput: List<Input>?

    /**
     * Input to generate with the SET method
     */
    abstract val setInput: List<Input>?

    /**
     * Input to generate with the SUBSCRIBE method
     */
    abstract val subscribeInput: List<Input>?

    /**
     * Input to generate with the UNSUBSCRIBE method
     */
    abstract val unSubscribeInput: List<Input>?

    /**
     * Generate the Api input to help test
     *
     * @return
     */
    override fun generate(): List<Api> = listOf(
        Api(
            name = apiName,
            getInput = getInput,
            setInput = setInput,
            subscribeInput = subscribeInput,
            unSubscribeInput = unSubscribeInput
        )
    )
}
