package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.model.destination.Destination
import com.inetpsa.pims.spaceMiddleware.model.destination.Destination.Location
import com.inetpsa.pims.spaceMiddleware.model.destination.Destination.Location.Address
import com.inetpsa.pims.spaceMiddleware.model.destination.DestinationInput
import com.inetpsa.pims.spaceMiddleware.model.destination.DestinationOutput
import com.inetpsa.pims.spaceMiddleware.model.destination.VehicleDestinationResponseFca
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.asJson
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class SetDestinationFcaExecutor(command: BaseCommand) :
    BaseFcaExecutor<DestinationInput, DestinationOutput>(command) {

    override fun params(parameters: Map<String, Any?>?): DestinationInput =
        DestinationInput(
            vin = parameters has Constants.PARAM_VIN,
            provider = parameters hasEnum Constants.PARAMS_KEY_PROVIDER,
            destination = Destination(
                location = Location(
                    placeId = parameters has Constants.PARAMS_KEY_PLACE_ID,
                    latitude = parameters has Constants.PARAMS_KEY_LATITUDE,
                    longitude = parameters has Constants.PARAMS_KEY_LONGITUDE,
                    name = parameters has Constants.PARAMS_KEY_NAME,
                    description = parameters has Constants.PARAMS_KEY_DESCRIPTION,
                    url = parameters has Constants.PARAMS_KEY_URL,
                    phoneNumber = parameters has Constants.PARAMS_KEY_PHONE_NUMBER,
                    address = Address(
                        streetName = parameters has Constants.PARAMS_KEY_STREET_NAME,
                        houseNumber = parameters has Constants.PARAMS_KEY_HOUSE_NUMBER,
                        postalNumber = parameters has Constants.PARAMS_KEY_POSTAL_NUMBER,
                        cityName = parameters has Constants.PARAMS_KEY_CITY_NAME,
                        countryName = parameters has Constants.PARAMS_KEY_COUNTRY_NAME,
                        countryCode = parameters has Constants.PARAMS_KEY_COUNTRY_CODE,
                        provinceName = parameters has Constants.PARAMS_KEY_PROVINCE_NAME,
                        provinceCode = parameters has Constants.PARAMS_KEY_PROVINCE_CODE
                    )
                ),
                routePreference = parameters has Constants.PARAMS_KEY_ROUTE_PREFERENCE
            )
        )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun transformInputToBody(input: DestinationInput): String {
        val destination = input.destination

        val mapLocation = mapOf(
            "placeid" to destination.location.placeId,
            "latitude" to destination.location.latitude,
            "longitude" to destination.location.longitude,
            "address" to destination.location.address,
            "name" to destination.location.name,
            "description" to destination.location.description,
            "url" to destination.location.url,
            "phoneNumber" to destination.location.phoneNumber
        )

        val mapParams = mapOf(
            "location" to mapLocation,
            "routePreference" to destination.routePreference,
            "poiInfoProvider" to input.provider.name
        )

        val mapCommand = mapOf(
            Constants.BODY_PARAM_CMD to "SDV",
            Constants.BODY_PARAM_PARAMS to mapParams
        )

        return mapCommand.asJson()
    }

    override suspend fun execute(input: DestinationInput): NetworkResponse<DestinationOutput> {
        val request = request(
            VehicleDestinationResponseFca::class.java,
            arrayOf("/v2/accounts/", uid, "/vehicles/", input.vin, "/sdv/location/"),
            body = transformInputToBody(input)
        )

        return communicationManager.post<VehicleDestinationResponseFca>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .transform { response ->
                when (response.error?.code?.isNotEmpty()) {
                    true -> NetworkResponse.Failure(PimsErrors.serverError(null, response.error.message))
                    else -> NetworkResponse.Success(
                        DestinationOutput(
                            command = response.command,
                            correlationId = response.correlationId,
                            responseStatus = response.responseStatus,
                            statusTimestamp = response.statusTimestamp
                        )
                    )
                }
            }
    }
}
