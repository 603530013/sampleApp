package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class NewRadioPlayerApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.RADIO_PLAYER}"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = Constants.PARAM_ACTION_TYPE,
            values = listOf(
                Constants.PARAM_ACTION_TYPE_RP_STATIONS,
                Constants.PARAM_ACTION_TYPE_RP_RECOMMENDATIONS
            ),
            linkedInput = mapOf(
                Constants.PARAM_ACTION_TYPE_RP_STATIONS to listOf(
                    Input.ListInput(
                        name = Constants.PARAM_KEY_ACTION,
                        values = listOf(Constants.PARAM_KEY_RP_STATION_ACTION),
                        linkedInput = mapOf(
                            Constants.PARAM_KEY_RP_STATION_ACTION to Input.FieldInput(
                                name = Constants.PARAM_ID,
                                placeHolder = Constants.PARAM_ID,
                                type = FieldType.STRING

                            )
                        )
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_COUNTRY,
                        placeHolder = Constants.PARAM_COUNTRY,
                        type = FieldType.STRING
                    )
                ),

                Constants.PARAM_ACTION_TYPE_RP_RECOMMENDATIONS to listOf(
                    Input.FieldInput(
                        name = Constants.PARAM_FACTORS,
                        placeHolder = Constants.PARAM_FACTORS,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_COUNTRY,
                        placeHolder = Constants.PARAM_COUNTRY,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_ID,
                        placeHolder = Constants.PARAM_ID,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_LAT,
                        placeHolder = Constants.PARAM_LAT,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_LNG,
                        placeHolder = Constants.PARAM_LNG,
                        type = FieldType.DOUBLE
                    )
                )
            )
        )
    )

    /**
     * Input to generate with the SET method
     */
    override val setInput: List<Input> = listOf()

    /**
     * Input to generate with the SUBSCRIBE method
     */
    override val subscribeInput: List<Input>? = null

    /**
     * Input to generate with the UNSUBSCRIBE method
     */
    override val unSubscribeInput: List<Input>? = null
}
