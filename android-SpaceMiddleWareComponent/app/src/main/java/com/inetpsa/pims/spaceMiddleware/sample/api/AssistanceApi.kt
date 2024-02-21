package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_CATEGORY
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_COUNTRY
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_LATITUDE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_LONGITUDE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_PHONE_NUMBER
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_VIN
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_ASSISTANCE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_VEHICLE_DETAILS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ID
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

@Deprecated("This should be replaced by NewAssistanceApi")
class AssistanceApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "${Constants.SERVICE_NAME}.${Constants.ASSISTANCE}"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.ListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(PARAM_ACTION_VEHICLE_DETAILS),
            linkedInput = mapOf(
                PARAM_ACTION_VEHICLE_DETAILS to Input.FieldInput(
                    PARAM_ID,
                    PARAM_ID,
                    FieldType.STRING
                )
            )
        )
    )

    /*  */
    /**
     * Input to generate with the SET method
     */

    override val setInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(PARAM_ACTION_TYPE_ASSISTANCE),
            linkedInput = mapOf(
                PARAM_ACTION_TYPE_ASSISTANCE to listOf(
                    Input.FieldInput(
                        name = BODY_PARAM_VIN,
                        placeHolder = BODY_PARAM_VIN,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_CATEGORY,
                        placeHolder = BODY_PARAM_CATEGORY,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_LATITUDE,
                        placeHolder = BODY_PARAM_LATITUDE,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_LONGITUDE,
                        placeHolder = BODY_PARAM_LONGITUDE,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_PHONE_NUMBER,
                        placeHolder = BODY_PARAM_PHONE_NUMBER,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_COUNTRY,
                        placeHolder = BODY_PARAM_COUNTRY,
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
