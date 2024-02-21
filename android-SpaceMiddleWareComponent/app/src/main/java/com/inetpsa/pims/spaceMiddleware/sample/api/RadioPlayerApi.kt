package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_RP_ON_AIR
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_RP_RECOMMENDATIONS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_RP_STATIONS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_COUNTRY
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_FACTORS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_LAT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_LNG
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_RPUID
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.RADIO_PLAYER
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SERVICE_NAME
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

@Deprecated("This should be replaced by the NewRadioPlayerApi")
class RadioPlayerApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "$SERVICE_NAME.$RADIO_PLAYER"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = Constants.PARAM_ACTION_TYPE,
            values = listOf(
                PARAM_ACTION_TYPE_RP_STATIONS,
                PARAM_ACTION_TYPE_RP_RECOMMENDATIONS,
                PARAM_ACTION_TYPE_RP_ON_AIR
            ),
            linkedInput = mapOf(
                PARAM_ACTION_TYPE_RP_STATIONS to listOf(
                    Input.FieldInput(
                        name = PARAM_COUNTRY,
                        placeHolder = PARAM_COUNTRY,
                        type = FieldType.STRING
                    )
                ),
                PARAM_ACTION_TYPE_RP_RECOMMENDATIONS to listOf(
                    Input.FieldInput(
                        name = PARAM_FACTORS,
                        placeHolder = PARAM_FACTORS,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAM_COUNTRY,
                        placeHolder = PARAM_COUNTRY,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAM_RPUID,
                        placeHolder = PARAM_RPUID,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAM_LAT,
                        placeHolder = PARAM_LAT,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = PARAM_LNG,
                        placeHolder = PARAM_LNG,
                        type = FieldType.DOUBLE
                    )
                ),
                PARAM_ACTION_TYPE_RP_ON_AIR to listOf(
                    Input.FieldInput(
                        name = PARAM_RPUID,
                        placeHolder = PARAM_RPUID,
                        type = FieldType.STRING
                    )
                )

            )
        )
    )

    /**
     * Input to generate with the SET method
     */
    override
    val setInput: List<Input>? = null

    /**
     * Input to generate with the SUBSCRIBE method
     */
    override
    val subscribeInput: List<Input>? = null

    /**
     * Input to generate with the UNSUBSCRIBE method
     */
    override
    val unSubscribeInput: List<Input>? = null
}
