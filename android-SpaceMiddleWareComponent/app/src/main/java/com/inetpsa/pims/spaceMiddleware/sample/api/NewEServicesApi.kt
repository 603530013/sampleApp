package com.inetpsa.pims.spaceMiddleware.sample.api

import com.inetpsa.pims.spaceMiddleware.sample.utils.Constants
import com.inetpsa.pims.uisample.views.models.FieldType
import com.inetpsa.pims.uisample.views.models.Input

class NewEServicesApi : BaseApi() {

    private val accessTypes = listOf("PUBLIC", "RESIDENTIAL", "PRIVATE")

    private val connectorTypes = listOf(
        "DOMESTIC_PLUG_GENERIC",
        "NEMA_5_20",
        "TYPE_1_YAZAKI",
        "TYPE_1_CCS",
        "TYPE_2_MENNEKES",
        "TYPE_2_CCS",
        "TYPE_3",
        "CHADEMO",
        "GBT_PART_2",
        "GBT_PART_3",
        "INDUSTRIAL_BLUE",
        "INDUSTRIAL_RED",
        "INDUSTRIAL_WHITE"
    )

    private val powerType = listOf("SLOW_CHARGE", "REGULAR_CHARGE", "FAST_CHARGE")

    override val apiName: String = "${Constants.NEW_SERVICE_NAME}.${Constants.ESERVICES}"
    override val getInput: List<Input> =
        listOf(
            Input.LinkedListInput(
                name = Constants.PARAM_ACTION_TYPE,
                values = listOf(Constants.PARAM_ACTION_TYPE_CHARGING_STATIONS),
                linkedInput = mapOf(
                    Constants.PARAM_ACTION_TYPE_CHARGING_STATIONS to listOf(
                        Input.LinkedListInput(
                            name = Constants.PARAM_KEY_ACTION,
                            values = listOf(Constants.PARAM_ACTION_LIST, Constants.PARAM_ACTION_FILTERS),
                            linkedInput = mapOf(
                                Constants.PARAM_ACTION_LIST to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    ),
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
                                    Input.MapInput(
                                        name = Constants.PARAM_FILTERS,
                                        multiple = false,
                                        inputs = listOf(
                                            Input.ListInput(
                                                name = Constants.PARAM_POWERTYPES,
                                                values = powerType,
                                                multipleSelection = true
                                            ),
                                            Input.ListInput(
                                                name = Constants.PARAM_ACCESS_TYPES,
                                                values = accessTypes,
                                                multipleSelection = true
                                            ),
                                            Input.ListInput(
                                                name = Constants.PARAM_CONNECTOR_TYPES,
                                                values = connectorTypes,
                                                multipleSelection = true
                                            ),
                                            Input.BooleanInput(
                                                name = Constants.PARAM_CHARGINGCABLEATTACHED,
                                                checked = false
                                            ),
                                            Input.BooleanInput(
                                                name = Constants.PARAM_FREE,
                                                checked = false
                                            ),
                                            Input.BooleanInput(
                                                name = Constants.PARAM_INDOOR,
                                                checked = false
                                            ),
                                            Input.BooleanInput(
                                                name = Constants.PARAM_OPEN24HOURS,
                                                checked = false
                                            )

                                        )
                                    )
                                ),
                                Constants.PARAM_ACTION_FILTERS to listOf(
                                    Input.FieldInput(
                                        name = Constants.PARAM_VIN,
                                        placeHolder = Constants.PARAM_VIN,
                                        type = FieldType.STRING
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

    override val setInput: List<Input>? = null
    override val subscribeInput: List<Input>? = null
    override val unSubscribeInput: List<Input>? = null
}
