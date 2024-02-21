package com.mobiledrivetech.external.sample.api

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
     * Generate the Api input to help test
     *
     * @return
     */
    override fun generate(): List<Api> = listOf(
        Api(
            name = apiName,
            getInput = getInput,
            setInput = setInput
        )
    )
}

fun interface ApiGenerator {
    fun generate(): List<Api>
}
