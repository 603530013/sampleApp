package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_VEHICLE_IMAGE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_VEHICLE_LIST
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_TYPE_VEHICLE_REMOVE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_MILEAGE
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_VIN
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SERVICE_NAME
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.VEHICLE
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

@Deprecated("This should be replaced by the new Vehicle API")
class VehicleApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "$SERVICE_NAME.$VEHICLE"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(PARAM_ACTION_TYPE_VEHICLE_IMAGE, PARAM_ACTION_TYPE_VEHICLE_LIST),
            linkedInput = mapOf(
                PARAM_ACTION_TYPE_VEHICLE_IMAGE to listOf(
                    Input.FieldInput(
                        PARAM_VIN,
                        PARAM_VIN,
                        FieldType.STRING
                    )
                ),
                PARAM_ACTION_TYPE_VEHICLE_LIST to listOf(
                    Input.LinkedListInput(
                        name = PARAM_ACTION_TYPE,
                        values = listOf(PARAM_ACTION_TYPE_VEHICLE_LIST)
                    )
                )
            ) // no params
        )
    )

    /**
     * Input to generate with the SET method
     */
    override val setInput: List<Input> = listOf(
        Input.LinkedListInput(
            name = PARAM_ACTION_TYPE,
            values = listOf(
                PARAM_ACTION_TYPE_VEHICLE_REMOVE,
                PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT
            ),
            linkedInput = mapOf(
                PARAM_ACTION_TYPE_VEHICLE_REMOVE to listOf(
                    Input.FieldInput(
                        PARAM_VIN,
                        PARAM_VIN,
                        FieldType.STRING
                    )
                ),
                PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT to listOf(
                    Input.FieldInput(
                        PARAM_VIN,
                        PARAM_VIN,
                        FieldType.STRING
                    ),
                    Input.FieldInput(
                        PARAM_MILEAGE,
                        PARAM_MILEAGE,
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
