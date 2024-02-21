package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_COMMENT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_RATING
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_SEND_CONFIRM_EMAIL
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_SERVICE_DATE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_SERVICE_TYPE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.BODY_PARAM_VIN
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.DEALER
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_ADVISOR_DEALER_REVIEW_DETAILS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_DEALER_LIST_DETAILS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_DEALER_LIST_NEARBY_DETAILS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_PREFERRED_DEALER_DETAILS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_PREFERRED_DEALER_ENROLL
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_PREFERRED_DEALER_REMOVE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_SEND_ADVISOR_DEALER_REVIEW
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_LAT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_LNG
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_RESULT_MAX
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_SITE_GEO
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SERVICE_NAME
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input
@Deprecated("This should be replaced by the NewDealerAPI")
class DealerApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "$SERVICE_NAME.$DEALER"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(
                PARAM_ACTION_DEALER_LIST_DETAILS,
                PARAM_ACTION_DEALER_LIST_NEARBY_DETAILS,
                PARAM_ACTION_PREFERRED_DEALER_DETAILS,
                PARAM_ACTION_ADVISOR_DEALER_REVIEW_DETAILS
            ),
            linkedInput = mapOf(
                PARAM_ACTION_DEALER_LIST_DETAILS to listOf(
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
                        name = PARAM_RESULT_MAX,
                        placeHolder = PARAM_RESULT_MAX,
                        type = FieldType.STRING
                    )

                ),
                PARAM_ACTION_DEALER_LIST_NEARBY_DETAILS to listOf(
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
                        name = PARAM_RESULT_MAX,
                        placeHolder = PARAM_RESULT_MAX,
                        type = FieldType.STRING
                    )
                ),
                PARAM_ACTION_PREFERRED_DEALER_DETAILS to listOf(
                    Input.ListInput(
                        name = PARAM_ACTION_TYPE,
                        values = listOf(PARAM_ACTION_PREFERRED_DEALER_DETAILS)
                    )
                ),
                PARAM_ACTION_ADVISOR_DEALER_REVIEW_DETAILS to listOf(
                    Input.FieldInput(
                        name = PARAM_SITE_GEO,
                        placeHolder = PARAM_SITE_GEO,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_VIN,
                        placeHolder = BODY_PARAM_VIN,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_RATING,
                        placeHolder = PARAM_RESULT_MAX,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_COMMENT,
                        placeHolder = BODY_PARAM_COMMENT,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_SERVICE_TYPE,
                        placeHolder = BODY_PARAM_SERVICE_TYPE,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_SEND_CONFIRM_EMAIL,
                        placeHolder = BODY_PARAM_SEND_CONFIRM_EMAIL,
                        type = FieldType.STRING
                    ),
                    Input.FieldInput(
                        name = BODY_PARAM_SERVICE_DATE,
                        placeHolder = BODY_PARAM_SERVICE_DATE,
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
        Input.ListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(
                PARAM_ACTION_PREFERRED_DEALER_ENROLL,
                PARAM_ACTION_PREFERRED_DEALER_REMOVE,
                PARAM_ACTION_SEND_ADVISOR_DEALER_REVIEW
            ),
            linkedInput = mapOf(
                PARAM_ACTION_PREFERRED_DEALER_ENROLL to Input.FieldInput(
                    PARAM_SITE_GEO,
                    PARAM_SITE_GEO,
                    FieldType.STRING
                ),
                PARAM_ACTION_PREFERRED_DEALER_REMOVE to Input.ListInput(
                    name = PARAM_ACTION_TYPE,
                    values = listOf(PARAM_ACTION_PREFERRED_DEALER_REMOVE)
                ), // no params
                PARAM_ACTION_SEND_ADVISOR_DEALER_REVIEW to Input.LinkedListInput(
                    name = PARAM_ACTION_TYPE,
                    values = listOf(PARAM_ACTION_SEND_ADVISOR_DEALER_REVIEW),
                    linkedInput = mapOf(
                        PARAM_ACTION_SEND_ADVISOR_DEALER_REVIEW to listOf(
                            Input.FieldInput(
                                name = PARAM_SITE_GEO,
                                placeHolder = PARAM_SITE_GEO,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = BODY_PARAM_VIN,
                                placeHolder = BODY_PARAM_VIN,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = BODY_PARAM_RATING,
                                placeHolder = BODY_PARAM_RATING,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = BODY_PARAM_COMMENT,
                                placeHolder = BODY_PARAM_COMMENT,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = BODY_PARAM_SERVICE_TYPE,
                                placeHolder = BODY_PARAM_SERVICE_TYPE,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = BODY_PARAM_SEND_CONFIRM_EMAIL,
                                placeHolder = BODY_PARAM_SEND_CONFIRM_EMAIL,
                                type = FieldType.STRING
                            ),
                            Input.FieldInput(
                                name = BODY_PARAM_SERVICE_DATE,
                                placeHolder = BODY_PARAM_SERVICE_DATE,
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
