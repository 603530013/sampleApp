package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class NewSettingsApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.SETTINGS}"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = Constants.PARAM_ACTION_TYPE,
            values = listOf(
                Constants.PARAM_ACTION_TYPE_CONTACT,
                Constants.PARAM_ACTION_TYPE_APP_TERMS,
                Constants.PARAM_ACTION_TYPE_WEB_TERMS,
                Constants.PARAM_ACTION_TYPE_APP_PRIVACY,
                Constants.PARAM_ACTION_TYPE_CONNECTED_TERMS,
                Constants.PARAM_ACTION_TYPE_CONNECTED_PRIVACY,
                Constants.PARAM_ACTION_TYPE_LANGUAGE,
                Constants.PARAM_ACTION_TYPE_PAYMENT
            ),
            linkedInput = mapOf(
                Constants.PARAM_ACTION_TYPE_CONTACT to listOf(
                    Input.ListInput(
                        name = Constants.PARAM_KEY_ACTION,
                        values = listOf(Constants.PARAM_KEY_ROADSIDE)
                    )
                ),
                Constants.PARAM_ACTION_TYPE_PAYMENT to listOf(
                    Input.ListInput(
                        name = Constants.PARAM_KEY_ACTION,
                        values = listOf(Constants.PARAM_ACTION_LIST)
                    )
                ),
                Constants.PARAM_ACTION_TYPE_CONTACT to listOf(
                    Input.ListInput(
                        name = Constants.PARAM_KEY_ACTION,
                        values = listOf(
                            Constants.PARAM_KEY_ROADSIDE,
                            Constants.PARAM_KEY_ECALL,
                            Constants.PARAM_KEY_BCALL
                        ),
                        linkedInput = mapOf(
                            Constants.PARAM_KEY_ROADSIDE to Input.FieldInput(
                                name = Constants.PARAM_VIN,
                                placeHolder = Constants.PARAM_VIN,
                                type = FieldType.STRING
                            ),
                            Constants.PARAM_KEY_ECALL to Input.FieldInput(
                                name = Constants.PARAM_VIN,
                                placeHolder = Constants.PARAM_VIN,
                                type = FieldType.STRING
                            ),
                            Constants.PARAM_KEY_BCALL to Input.FieldInput(
                                name = Constants.PARAM_VIN,
                                placeHolder = Constants.PARAM_VIN,
                                type = FieldType.STRING
                            )
                        )
                    )
                ),

                Constants.PARAM_ACTION_TYPE_LANGUAGE to listOf(
                    Input.ListInput(
                        name = Constants.PARAM_KEY_ACTION,
                        values = listOf(Constants.PARAM_ACTION_LIST, Constants.PARAM_ACTION_CURRENT)
                    )
                ),

                Constants.PARAM_ACTION_TYPE_APP_TERMS to listOf(
                    Input.FieldInput(
                        name = Constants.PARAM_SDP,
                        placeHolder = Constants.PARAM_SDP,
                        type = FieldType.STRING
                    )
                ),
                Constants.PARAM_ACTION_TYPE_WEB_TERMS to listOf(
                    Input.FieldInput(
                        name = Constants.PARAM_SDP,
                        placeHolder = Constants.PARAM_SDP,
                        type = FieldType.STRING
                    )
                ),
                Constants.PARAM_ACTION_TYPE_APP_PRIVACY to listOf(
                    Input.FieldInput(
                        name = Constants.PARAM_SDP,
                        placeHolder = Constants.PARAM_SDP,
                        type = FieldType.STRING
                    )
                ),
                Constants.PARAM_ACTION_TYPE_CONNECTED_TERMS to listOf(
                    Input.FieldInput(
                        name = Constants.PARAM_SDP,
                        placeHolder = Constants.PARAM_SDP,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_COUNTRY,
                        placeHolder = Constants.PARAM_COUNTRY,
                        type = FieldType.STRING
                    )
                ),
                Constants.PARAM_ACTION_TYPE_CONNECTED_PRIVACY to listOf(
                    Input.FieldInput(
                        name = Constants.PARAM_SDP,
                        placeHolder = Constants.PARAM_SDP,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_COUNTRY,
                        placeHolder = Constants.PARAM_COUNTRY,
                        type = FieldType.STRING
                    )
                )
            )
        )

    )

    /**
     * Input to generate with the SET method
     */
    override val setInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = Constants.PARAM_ACTION_TYPE,
            values = listOf(
                Constants.PARAM_ACTION_TYPE_LANGUAGE
            ),
            linkedInput = mapOf(
                Constants.PARAM_ACTION_TYPE_LANGUAGE to listOf(
                    Input.FieldInput(
                        name = Constants.PARAMS_KEY_LANGUAGE,
                        placeHolder = Constants.PARAMS_KEY_LANGUAGE,
                        type = FieldType.STRING
                    )
                )
            )
        )
    )

    /**
     * Input to generate with the SUBSCRIBE method
     */
    override val subscribeInput: List<Input>? = null

    /**
     * Input to generate with the UNSUBSCRIBE method
     */
    override val unSubscribeInput: List<Input>? = null
}
