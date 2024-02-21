package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.uisample.views.models.Api

fun interface ApiGenerator {

    /**
     * Generate the Api input to help test
     *
     * @return
     */
    fun generate(): List<Api>
}
