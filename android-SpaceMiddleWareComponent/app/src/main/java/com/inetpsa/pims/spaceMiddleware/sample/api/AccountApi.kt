package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.ACCOUNT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE_ADDRESS_1
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE_ADDRESS_2
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE_CITY
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE_CIVILITY
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE_COUNTRY
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE_FIRST_NAME
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE_LAST_NAME
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAMS_KEY_PROFILE_ZIP_CODE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SERVICE_NAME
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

@Deprecated("This should be replaced by NewUserApi")
class AccountApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "$SERVICE_NAME.$ACCOUNT"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.ListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(PARAMS_KEY_PROFILE)
        )
    )

    /**
     * Input to generate with the SET method
     */
    override val setInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(PARAMS_KEY_PROFILE),
            linkedInput = mapOf(
                PARAMS_KEY_PROFILE to listOf(
                    Input.FieldInput(
                        name = PARAMS_KEY_PROFILE_CIVILITY,
                        placeHolder = PARAMS_KEY_PROFILE_CIVILITY,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAMS_KEY_PROFILE_LAST_NAME,
                        placeHolder = PARAMS_KEY_PROFILE_LAST_NAME,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAMS_KEY_PROFILE_FIRST_NAME,
                        placeHolder = PARAMS_KEY_PROFILE_FIRST_NAME,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAMS_KEY_PROFILE_ADDRESS_1,
                        placeHolder = PARAMS_KEY_PROFILE_ADDRESS_1,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAMS_KEY_PROFILE_ADDRESS_2,
                        placeHolder = PARAMS_KEY_PROFILE_ADDRESS_2,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAMS_KEY_PROFILE_CITY,
                        placeHolder = PARAMS_KEY_PROFILE_CITY,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAMS_KEY_PROFILE_COUNTRY,
                        placeHolder = PARAMS_KEY_PROFILE_COUNTRY,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAMS_KEY_PROFILE_ZIP_CODE,
                        placeHolder = PARAMS_KEY_PROFILE_ZIP_CODE,
                        FieldType.STRING
                    )
                    /*, Input.FieldInput(
                        name = PARAMS_KEY_PHONE,
                        placeHolder = PARAMS_KEY_PHONE, FieldType.STRING
                    ), Input.FieldInput(
                        name = PARAMS_KEY_MOBILE,
                        placeHolder = PARAMS_KEY_MOBILE, FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = PARAMS_KEY_MOBILE_PRO,
                        placeHolder = PARAMS_KEY_MOBILE_PRO, FieldType.STRING
                    )*/
                )
            )

        )
    )

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
