package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.CONTRACTS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_ACTION_VEHICLE_DETAILS
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.PARAM_VIN
import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants.SERVICE_NAME
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class ContractsApi : BaseApi() {

    /**
     * Api name
     */
    override val apiName: String = "$SERVICE_NAME.$CONTRACTS"

    /**
     * Input to generate with the GET method
     */
    override val getInput: List<Input> = listOf(
        Input.ListInput(
            name = Constants.PARAM_ACTION_TYPE,
            values = listOf(PARAM_ACTION_VEHICLE_DETAILS),
            linkedInput = mapOf(
                PARAM_ACTION_VEHICLE_DETAILS to Input.FieldInput(
                    PARAM_VIN,
                    PARAM_VIN,
                    FieldType.STRING
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
