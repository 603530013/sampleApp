package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class FeaturesApi : BaseApi() {

    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.FEATURES}"

    override val getInput: List<Input> =
        listOf(
            Input.LinkedListInput(
                name = Constants.PARAM_KEY_ACTION,
                values = listOf(Constants.PARAM_ACTION_GET, Constants.PARAM_ACTION_REFRESH),
                linkedInput = mapOf(
                    Constants.PARAM_ACTION_GET to listOf(
                        Input.FieldInput(
                            name = Constants.PARAM_VIN,
                            placeHolder = Constants.PARAM_VIN,
                            type = FieldType.STRING
                        )
                    ),
                    Constants.PARAM_ACTION_REFRESH to listOf(
                        Input.FieldInput(
                            name = Constants.PARAM_VIN,
                            placeHolder = Constants.PARAM_VIN,
                            type = FieldType.STRING
                        )
                    )
                )
            )
        )

    override val setInput: List<Input>? = null
    override val subscribeInput: List<Input>? = null
    override val unSubscribeInput: List<Input>? = null
}
