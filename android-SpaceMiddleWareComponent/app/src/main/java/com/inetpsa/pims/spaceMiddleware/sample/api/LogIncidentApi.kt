package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_COMMENT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_CULTURE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_DATA
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_FILE_NAME
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_FIRST_NAME
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_ID_CLIENT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_LAST_NAME
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_NATION_ID
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_OPTIN_ONE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_OPTIN_TWO
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_SITE_CODE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_TITLE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_VIN
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_ZIP_CODE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.LOG_INCIDENT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_VEHICLE_ENROLL
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_VEHICLE_UPLOAD
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_INCIDENT_ID
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SERVICE_NAME
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

@Deprecated("This should be replaced by the NewLogIncidentApi")
class LogIncidentApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "$SERVICE_NAME.$LOG_INCIDENT"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input>? = null

    /**
     * Input to generate with the SET method
     */
    override val setInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(
                PARAM_ACTION_VEHICLE_ENROLL,
                PARAM_ACTION_VEHICLE_UPLOAD
            ),
            linkedInput = mapOf(
                PARAM_ACTION_VEHICLE_ENROLL to listOf(
                    Input.FieldInput(
                        name = BODY_PARAM_SITE_CODE,
                        placeHolder = BODY_PARAM_SITE_CODE,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_SITE_CODE,
                        placeHolder = BODY_PARAM_SITE_CODE,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_VIN,
                        placeHolder = BODY_PARAM_VIN,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_FIRST_NAME,
                        placeHolder = BODY_PARAM_FIRST_NAME,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_LAST_NAME,
                        placeHolder = BODY_PARAM_LAST_NAME,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_NATION_ID,
                        placeHolder = BODY_PARAM_NATION_ID,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_ZIP_CODE,
                        placeHolder = BODY_PARAM_ZIP_CODE,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_ID_CLIENT,
                        placeHolder = BODY_PARAM_ID_CLIENT,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_CULTURE,
                        placeHolder = BODY_PARAM_CULTURE,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_TITLE,
                        placeHolder = BODY_PARAM_TITLE,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_COMMENT,
                        placeHolder = BODY_PARAM_COMMENT,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_OPTIN_ONE,
                        placeHolder = BODY_PARAM_OPTIN_ONE,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_OPTIN_TWO,
                        placeHolder = BODY_PARAM_OPTIN_TWO,
                        type = FieldType.STRING
                    )
                ),
                PARAM_ACTION_VEHICLE_UPLOAD to listOf(
                    Input.FieldInput(
                        name = PARAM_INCIDENT_ID,
                        placeHolder = PARAM_INCIDENT_ID,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_FILE_NAME,
                        placeHolder = BODY_PARAM_FILE_NAME,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_DATA,
                        placeHolder = BODY_PARAM_DATA,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_TITLE,
                        placeHolder = BODY_PARAM_TITLE,
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
