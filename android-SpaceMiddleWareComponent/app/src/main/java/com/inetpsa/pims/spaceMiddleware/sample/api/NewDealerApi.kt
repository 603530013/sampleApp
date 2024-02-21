package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class NewDealerApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.DEALER}"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> =
        listOf(
            Input.LinkedListInput(
                name = Constants.PARAM_ACTION_TYPE,
                values = listOf(
                    Constants.PARAM_ACTION_TYPE_LIST,
                    Constants.PARAM_ACTION_TYPE_FAVORITE,
                    Constants.PARAM_ACTION_TYPE_REVIEW,
                    Constants.PARAM_ACTION_TYPE_APPOINTMENT
                ),
                linkedInput = mapOf(
                    Constants.PARAM_ACTION_TYPE_LIST to listOf(
                        Input.FieldInput(
                            name = Constants.PARAM_LAT,
                            placeHolder = Constants.PARAM_LAT,
                            type = FieldType.DOUBLE
                        ),
                        Input.FieldInput(
                            name = Constants.PARAM_LNG,
                            placeHolder = Constants.PARAM_LNG,
                            type = FieldType.DOUBLE
                        ),
                        Input.FieldInput(
                            name = Constants.PARAM_VIN,
                            placeHolder = Constants.PARAM_VIN,
                            type = FieldType.STRING
                        ),
                        Input.FieldInput(
                            name = Constants.PARAM_LIMIT,
                            placeHolder = Constants.PARAM_LIMIT,
                            type = FieldType.INT
                        )
                    ),
                    Constants.PARAM_ACTION_TYPE_FAVORITE to listOf(
                        Input.ListInput(
                            name = Constants.PARAM_KEY_ACTION,
                            values = listOf(Constants.PARAM_ACTION_GET, Constants.PARAM_ACTION_REFRESH),
                            linkedInput = mapOf(
                                Constants.PARAM_ACTION_GET to Input.FieldInput(
                                    name = Constants.PARAM_VIN,
                                    placeHolder = Constants.PARAM_VIN,
                                    type = FieldType.STRING
                                ),
                                Constants.PARAM_ACTION_REFRESH to Input.FieldInput(
                                    name = Constants.PARAM_VIN,
                                    placeHolder = Constants.PARAM_VIN,
                                    type = FieldType.STRING
                                )
                            )
                        )
                    ),
                    Constants.PARAM_ACTION_TYPE_REVIEW to listOf(
                        Input.FieldInput(
                            name = Constants.PARAM_VIN,
                            placeHolder = Constants.PARAM_VIN,
                            type = FieldType.STRING
                        ),
                        Input.FieldInput(
                            name = Constants.BODY_PARAM_SERVICE_TYPE,
                            placeHolder = Constants.BODY_PARAM_SERVICE_TYPE,
                            type = FieldType.STRING
                        ),
                        Input.FieldInput(
                            name = Constants.BODY_PARAM_VEHICLE_ID_TYPE,
                            placeHolder = Constants.BODY_PARAM_VEHICLE_ID_TYPE,
                            type = FieldType.STRING
                        )
                    ),

                    Constants.PARAM_ACTION_TYPE_APPOINTMENT to listOf(
                        Input.LinkedListInput(
                            name = Constants.PARAM_KEY_ACTION,
                            values = listOf(
                                Constants.PARAM_ACTION_AGENDA,
                                Constants.PARAM_ACTION_SERVICES,
                                Constants.PARAM_ACTION_DETAILS,
                                Constants.PARAM_ACTION_LIST
                            ),
                            linkedInput = mapOf(
                                Constants.PARAM_ACTION_AGENDA to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_BOOKING_ID,
                                        placeHolder = Constants.PARAM_BOOKING_ID,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_BOOKING_LOCATION,
                                        placeHolder = Constants.PARAM_BOOKING_LOCATION,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_START_DATE,
                                        placeHolder = Constants.PLACEHOLDER_PARAM_KEY_START,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_TIME_FENCE,
                                        placeHolder = Constants.PLACEHOLDER_PARAM_TIME_FENCE,
                                        type = FieldType.STRING
                                    )
                                ),
                                Constants.PARAM_ACTION_SERVICES to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_BOOKING_ID,
                                        placeHolder = Constants.PARAM_BOOKING_ID,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_BOOKING_LOCATION,
                                        placeHolder = Constants.PARAM_BOOKING_LOCATION,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_MILEAGE,
                                        placeHolder = Constants.PARAM_MILEAGE,
                                        type = FieldType.INT
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_MILEAGE_UNIT,
                                        placeHolder = Constants.PLACEHOLDER_PARAM_MILEAGE_UNIT,
                                        type = FieldType.STRING
                                    )
                                ),

                                Constants.PARAM_ACTION_DETAILS to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_ID,
                                        placeHolder = Constants.PARAM_ID,
                                        type = FieldType.STRING
                                    )
                                ),
                                Constants.PARAM_ACTION_LIST to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_BOOKING_ID,
                                        placeHolder = Constants.PARAM_BOOKING_ID,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_BOOKING_LOCATION,
                                        placeHolder = Constants.PARAM_BOOKING_LOCATION,
                                        type = FieldType.STRING
                                    )
                                )
                            )
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
                Constants.PARAM_ACTION_TYPE_FAVORITE,
                Constants.PARAM_ACTION_TYPE_APPOINTMENT
            ),
            linkedInput = mapOf(
                Constants.PARAM_ACTION_TYPE_FAVORITE to listOf(
                    Input.LinkedListInput(
                        name = Constants.PARAM_KEY_ACTION,
                        values = listOf(Constants.PARAM_ACTION_ADD, Constants.PARAM_ACTION_REMOVE),
                        linkedInput = mapOf(
                            Constants.PARAM_ACTION_ADD to listOf(
                                Input.FieldInput(
                                    name = Constants.PARAM_VIN,
                                    placeHolder = Constants.PARAM_VIN,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_ID,
                                    placeHolder = Constants.PARAM_ID,
                                    type = FieldType.STRING
                                )
                            )
                        )

                    )
                ),
                Constants.PARAM_ACTION_TYPE_APPOINTMENT to listOf(
                    Input.LinkedListInput(
                        name = Constants.PARAM_KEY_ACTION,
                        values = listOf(
                            Constants.PARAM_ACTION_DELETE,
                            Constants.PARAM_ACTION_ADD
                        ),
                        linkedInput = mapOf(
                            Constants.PARAM_ACTION_DELETE to listOf(
                                Input.FieldInput(
                                    name = Constants.PARAM_VIN,
                                    placeHolder = Constants.PARAM_VIN,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_ID,
                                    placeHolder = Constants.PARAM_ID,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_BOOKING_ID,
                                    placeHolder = Constants.PARAM_BOOKING_ID,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_BOOKING_LOCATION,
                                    placeHolder = Constants.PARAM_BOOKING_LOCATION,
                                    type = FieldType.STRING
                                )
                            ),
                            Constants.PARAM_ACTION_ADD to listOf(
                                Input.FieldInput(
                                    name = Constants.PARAM_VIN,
                                    placeHolder = Constants.PARAM_VIN,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_DATE,
                                    placeHolder = Constants.PLACEHOLDER_PARAM_KEY_DATE_TIME,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_MILEAGE,
                                    placeHolder = Constants.PARAM_MILEAGE,
                                    type = FieldType.INT
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_COD_NATION,
                                    placeHolder = Constants.PARAM_COD_NATION,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.BODY_PARAM_COMMENT,
                                    placeHolder = Constants.BODY_PARAM_COMMENT,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_BOOKING_ID,
                                    placeHolder = Constants.PARAM_BOOKING_ID,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_BOOKING_LOCATION,
                                    placeHolder = Constants.PARAM_BOOKING_LOCATION,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_CONTACT_NAME,
                                    placeHolder = Constants.PARAM_CONTACT_NAME,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_CONTACT_PHONE,
                                    placeHolder = Constants.PARAM_CONTACT_PHONE,
                                    type = FieldType.STRING
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_SERVICES,
                                    placeHolder = Constants.PARAM_SERVICES_PLACEHOLDER,
                                    type = FieldType.ARRAY
                                ),
                                Input.BooleanInput(
                                    name = Constants.PARAM_MOBILITY,
                                    checked = false
                                ),
                                Input.FieldInput(
                                    name = Constants.PARAM_PREMIUM_SERVICE,
                                    placeHolder = Constants.PARAM_PREMIUM_SERVICE,
                                    type = FieldType.STRING
                                )
                            )
                        )
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
