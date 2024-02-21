package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_DIRECTIONS_ROUTE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_NEARBY_SEARCH
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_PLACE_DETAILS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TEXT_SEARCH
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_DIRECTION_LATITUDE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_DIRECTION_LONGITUDE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_KEYWORD
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_LAT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_LNG
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ORIGIN_LATITUDE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ORIGIN_LONGITUDE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_PLACE_ID
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_RADIUS
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class NewLocationsApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.LOCATIONS}"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(
                PARAM_ACTION_TEXT_SEARCH,
                PARAM_ACTION_NEARBY_SEARCH,
                PARAM_ACTION_PLACE_DETAILS,
                PARAM_ACTION_DIRECTIONS_ROUTE
            ),
            linkedInput = mapOf(
                PARAM_ACTION_TEXT_SEARCH to listOf(
                    Input.FieldInput(
                        name = PARAM_KEYWORD,
                        placeHolder = PARAM_KEYWORD,
                        type = FieldType.STRING
                    )
                ),
                PARAM_ACTION_NEARBY_SEARCH to listOf(
                    Input.FieldInput(
                        name = PARAM_LAT,
                        placeHolder = PARAM_LAT,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = PARAM_LNG,
                        placeHolder = PARAM_LNG,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = PARAM_RADIUS,
                        placeHolder = PARAM_RADIUS,
                        type = FieldType.INT
                    ),
                    Input.FieldInput(
                        name = PARAM_KEYWORD,
                        placeHolder = PARAM_KEYWORD,
                        type = FieldType.STRING
                    )
                ),
                PARAM_ACTION_PLACE_DETAILS to listOf(
                    Input.FieldInput(
                        name = PARAM_PLACE_ID,
                        placeHolder = PARAM_PLACE_ID,
                        type = FieldType.STRING
                    )
                ),
                PARAM_ACTION_DIRECTIONS_ROUTE to listOf(
                    Input.FieldInput(
                        name = PARAM_ORIGIN_LATITUDE,
                        placeHolder = PARAM_ORIGIN_LATITUDE,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = PARAM_ORIGIN_LONGITUDE,
                        placeHolder = PARAM_ORIGIN_LONGITUDE,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = PARAM_DIRECTION_LATITUDE,
                        placeHolder = PARAM_DIRECTION_LATITUDE,
                        type = FieldType.DOUBLE
                    ),
                    Input.FieldInput(
                        name = PARAM_DIRECTION_LONGITUDE,
                        placeHolder = PARAM_DIRECTION_LONGITUDE,
                        type = FieldType.DOUBLE
                    )
                )

            )

        )
    )

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
