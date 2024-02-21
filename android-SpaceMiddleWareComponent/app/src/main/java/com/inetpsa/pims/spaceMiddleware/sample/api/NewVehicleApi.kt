package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class NewVehicleApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.VEHICLE}"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> =
        listOf(
            Input.LinkedListInput(
                name = Constants.PARAM_ACTION_TYPE,
                values = listOf(
                    Constants.PARAM_ACTION_TYPE_LIST,
                    Constants.PARAM_ACTION_TYPE_DETAILS,
                    Constants.PARAM_ACTION_TYPE_CONTRACTS,
                    Constants.PARAM_ACTION_TYPE_SERVICES,
                    Constants.PARAM_ACTION_TYPE_CHECK,
                    Constants.PARAM_ACTION_TYPE_MANUAL
                ),
                linkedInput = mapOf(
                    Constants.PARAM_ACTION_TYPE_LIST to listOf(
                        Input.ListInput(
                            name = Constants.PARAM_KEY_ACTION,
                            values = listOf(Constants.PARAM_ACTION_GET, Constants.PARAM_ACTION_REFRESH)
                        )
                    ),
                    Constants.PARAM_ACTION_TYPE_DETAILS to listOf(
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
                    Constants.PARAM_ACTION_TYPE_DETAILS to listOf(
                        Input.LinkedListInput(
                            name = Constants.PARAM_KEY_TYPE,
                            values = listOf(
                                Constants.PARAM_TYPE_NORMAL,
                                Constants.PARAM_TYPE_ENCRYPTED,
                                Constants.PARAM_TYPE_LAST_CHARACTERS
                            ),
                            linkedInput = mapOf(
                                Constants.PARAM_TYPE_NORMAL to listOf(
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
                                Constants.PARAM_TYPE_LAST_CHARACTERS to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    )
                                ),
                                Constants.PARAM_TYPE_ENCRYPTED to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    )
                                )
                            )
                        )
                    ),
                    Constants.PARAM_ACTION_TYPE_CONTRACTS to listOf(
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
                    Constants.PARAM_ACTION_TYPE_SERVICES to listOf(
                        Input.LinkedListInput(
                            name = Constants.PARAM_KEY_ACTION,
                            values = listOf(
                                Constants.PARAM_ACTION_GET,
                                Constants.PARAM_ACTION_REFRESH
                            ),
                            linkedInput = mapOf(
                                Constants.PARAM_ACTION_GET to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    ),
                                    Input.FieldInput(
                                        name = Constants.PARAM_SCHEMA,
                                        placeHolder = Constants.PARAM_SCHEMA,
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
                    ),
                    Constants.PARAM_ACTION_TYPE_CHECK to listOf(
                        Input.FieldInput(
                            name = Constants.PARAM_VIN,
                            placeHolder = Constants.PARAM_VIN,
                            type = FieldType.STRING
                        )
                    ),
                    Constants.PARAM_ACTION_TYPE_MANUAL to listOf(
                        Input.FieldInput(
                            name = Constants.PARAM_VIN,
                            placeHolder = Constants.PARAM_VIN,
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
                Constants.PARAM_ACTION_TYPE_VEHICLE_REMOVE,
                Constants.PARAM_ACTION_TYPE_VEHICLE_ADD,
                Constants.PARAM_ACTION_TYPE_UPDATE
            ),
            linkedInput = mapOf(
                Constants.PARAM_ACTION_TYPE_VEHICLE_REMOVE to listOf(
                    Input.FieldInput(
                        name = Constants.PARAM_VIN,
                        placeHolder = Constants.PARAM_VIN,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_REASON,
                        placeHolder = Constants.PARAM_REASON,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = Constants.PARAM_REASON_ID,
                        placeHolder = Constants.PARAM_REASON_ID,
                        type = FieldType.STRING
                    )
                ),
                Constants.PARAM_ACTION_TYPE_VEHICLE_ADD to listOf(
                    Input.FieldInput(
                        name = Constants.PARAM_VIN,
                        placeHolder = Constants.PARAM_VIN,
                        type = FieldType.STRING
                    ),
                    Input.BooleanInput(
                        name = Constants.PARAM_CONNECTED,
                        checked = false,
                        linkedInput = mapOf(
                            true to listOf(
                                Input.FieldInput(
                                    name = Constants.PARAM_PLATE_NUMBER,
                                    placeHolder = Constants.PARAM_PLATE_NUMBER,
                                    type = FieldType.STRING
                                ),
                                Input.MapInput(
                                    name = Constants.PARAM_TC_REGISTRATION,
                                    inputs = listOf(
                                        Input.FieldInput(
                                            name = Constants.PARAM_COUNTRY_CODE,
                                            placeHolder = Constants.PLACEHOLDER_TC_REGISTRATION_COUNTRY_CODE,
                                            type = FieldType.STRING
                                        ),
                                        Input.FieldInput(
                                            name = Constants.PARAM_STATUS,
                                            placeHolder = Constants.PLACEHOLDER_TC_REGISTRATION_STATUS,
                                            type = FieldType.STRING
                                        ),
                                        Input.FieldInput(
                                            name = Constants.PARAM_VERSION,
                                            placeHolder = Constants.PLACEHOLDER_TC_REGISTRATION_VERSION,
                                            type = FieldType.STRING
                                        )

                                    )
                                ),
                                Input.MapInput(
                                    name = Constants.PARAM_TC_ACTIVATION,
                                    inputs = listOf(
                                        Input.FieldInput(
                                            name = Constants.PARAM_COUNTRY_CODE,
                                            placeHolder = Constants.PLACEHOLDER_TC_ACTIVATION_COUNTRY_CODE,
                                            type = FieldType.STRING
                                        ),
                                        Input.FieldInput(
                                            name = Constants.PARAM_STATUS,
                                            placeHolder = Constants.PLACEHOLDER_TC_ACTIVATION_STATUS,
                                            type = FieldType.STRING
                                        ),
                                        Input.FieldInput(
                                            name = Constants.PARAM_VERSION,
                                            placeHolder = Constants.PLACEHOLDER_TC_ACTIVATION_VERSION,
                                            type = FieldType.STRING
                                        )

                                    )
                                ),
                                Input.MapInput(
                                    name = Constants.PARAM_PP_ACTIVATION,
                                    inputs = listOf(
                                        Input.FieldInput(
                                            name = Constants.PARAM_COUNTRY_CODE,
                                            placeHolder = Constants.PLACEHOLDER_PP_ACTIVATION_COUNTRY_CODE,
                                            type = FieldType.STRING
                                        ),
                                        Input.FieldInput(
                                            name = Constants.PARAM_STATUS,
                                            placeHolder = Constants.PLACEHOLDER_PP_ACTIVATION_STATUS,
                                            type = FieldType.STRING
                                        ),
                                        Input.FieldInput(
                                            name = Constants.PARAM_VERSION,
                                            placeHolder = Constants.PLACEHOLDER_PP_ACTIVATION_VERSION,
                                            type = FieldType.STRING
                                        )

                                    )
                                )
                                /*Input.MapInput(
                                    name = Constants.PARAM_EMERGENCY_CONTACT,
                                    inputs = listOf(
                                        Input.MapInput(
                                            name = Constants.PARAM_EMERGENCY_CONTACT,
                                            inputs = listOf(
                                                Input.FieldInput(
                                                    name = Constants.PARAM_EMERGENCY_CONTACT_NAME,
                                                    placeHolder = Constants.PARAM_EMERGENCY_CONTACT_NAME,
                                                    type = FieldType.STRING
                                                ),
                                                Input.FieldInput(
                                                    name = Constants.PARAM_EMERGENCY_CONTACT_PHONE,
                                                    placeHolder = Constants.PARAM_EMERGENCY_CONTACT_PHONE,
                                                    type = FieldType.STRING
                                                )
                                            )
                                        )
                                    )
                                )*/
                            ),

                            false to listOf(
                                Input.FieldInput(
                                    name = Constants.PARAM_MILEAGE,
                                    placeHolder = Constants.PARAM_MILEAGE,
                                    type = FieldType.STRING
                                )
                            )
                        )
                    )
                ),
                Constants.PARAM_ACTION_TYPE_UPDATE to listOf(
                    Input.LinkedListInput(
                        name = Constants.PARAM_KEY_ACTION,
                        values = listOf(Constants.PARAM_KEY_NICKNAME),
                        linkedInput = mapOf(
                            Constants.PARAM_KEY_NICKNAME to listOf(
                                Input.FieldInput(
                                    name = Constants.PARAM_VIN,
                                    placeHolder = Constants.PARAM_VIN,
                                    type = FieldType.STRING

                                ),
                                Input.FieldInput(
                                    name = Constants.PARAMS_KEY_NAME,
                                    placeHolder = Constants.PARAMS_KEY_NAME,
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
