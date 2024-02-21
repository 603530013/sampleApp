package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class UserApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.USER}"

    /**
     * Input to generate with the GET method
     */
    // {actionType=profile, action=xxx}
    override val getInput: List<Input> = listOf(
        Input.ListInput(
            name = Constants.PARAM_ACTION_TYPE,
            values = listOf(Constants.PARAMS_KEY_PROFILE),
            linkedInput = mapOf(
                Constants.PARAMS_KEY_PROFILE to Input.ListInput(
                    name = Constants.PARAM_KEY_ACTION,
                    values = listOf(Constants.PARAM_ACTION_GET, Constants.PARAM_ACTION_REFRESH)
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
            values = listOf(Constants.PARAMS_KEY_PROFILE, Constants.PARAMS_KEY_DELETE),
            linkedInput = mapOf(
                Constants.PARAMS_KEY_PROFILE to listOf(
                    Input.MapInput(
                        name = Constants.PARAMS_KEY_PROFILE,
                        inputs = listOf(
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_EMAIL,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_EMAIL,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_FIRST_NAME,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_FIRST_NAME,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_LAST_NAME,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_LAST_NAME,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_CIVILITY,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_CIVILITY,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_ADDRESS_1,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_ADDRESS_1,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_ADDRESS_2,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_ADDRESS_2,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_ZIP_CODE,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_ZIP_CODE,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_CITY,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_CITY,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PROFILE_COUNTRY,
                                placeHolder = Constants.PARAMS_KEY_PROFILE_COUNTRY,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_PHONE,
                                placeHolder = Constants.PARAMS_KEY_PHONE,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_MOBILE,
                                placeHolder = Constants.PARAMS_KEY_MOBILE,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = Constants.PARAMS_KEY_MOBILE_PRO,
                                placeHolder = Constants.PARAMS_KEY_MOBILE_PRO,
                                type = FieldType.STRING
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
