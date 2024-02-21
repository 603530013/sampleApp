package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.DESTINATION
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_SEND_DESTINATION
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SERVICE_NAME
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

@Deprecated("This should be replaced by the NewDestinationApi")
class DestinationApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "$SERVICE_NAME.$DESTINATION"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input>? = null

    /**
     * Input to generate with the SET method
     */
    override val setInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = Constants.PARAM_ACTION_TYPE,
            values = listOf(PARAM_ACTION_SEND_DESTINATION),
            linkedInput = mapOf(
                PARAM_ACTION_SEND_DESTINATION to listOf(
                    Input.FieldInput(
                        Constants.PARAM_VIN,
                        Constants.PARAM_VIN,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_PROVIDER,
                        Constants.PARAMS_KEY_PROVIDER,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_PLACE_ID,
                        Constants.PARAMS_KEY_PLACE_ID,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_LATITUDE,
                        Constants.PARAMS_KEY_LATITUDE,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_LONGITUDE,
                        Constants.PARAMS_KEY_LONGITUDE,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_NAME,
                        Constants.PARAMS_KEY_NAME,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_DESCRIPTION,
                        Constants.PARAMS_KEY_DESCRIPTION,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_URL,
                        Constants.PARAMS_KEY_URL,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_PHONE_NUMBER,
                        Constants.PARAMS_KEY_PHONE_NUMBER,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_CITY_NAME,
                        Constants.PARAMS_KEY_CITY_NAME,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_COUNTRY_NAME,
                        Constants.PARAMS_KEY_COUNTRY_NAME,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_COUNTRY_CODE,
                        Constants.PARAMS_KEY_COUNTRY_CODE,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_PROVINCE_NAME,
                        Constants.PARAMS_KEY_PROVINCE_NAME,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_PROVINCE_CODE,
                        Constants.PARAMS_KEY_PROVINCE_CODE,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        Constants.PARAMS_KEY_ROUTE_PREFERENCE,
                        Constants.PARAMS_KEY_ROUTE_PREFERENCE,
                        FieldType.STRING
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
