package com.inetpsa.pims.spaceMiddleware

import com.inetpsa.mmx.foundation.tools.Constants

internal object Constants {

    const val API_PREFIX = "space.middleware"
    const val SERVICE_NAME = "middleware"
    const val COMPONENT_NAME = "MiddlewareComponent"

    internal object API {

        const val USER = "user"
        const val VEHICLE = "vehicle"
        const val RADIO_PLAYER = "radioPlayer"
        const val ASSISTANCE = "assistance"
        const val DEALER = "dealer"
        const val SETTINGS = "settings"
        const val LOCATIONS = "location"
        const val LOG_INCIDENT = "logIncident"
        const val DESTINATION = "destination"
        const val CONFIGURATION = "configuration"
        const val ESERVICES = "eservices"
        const val FEATURES = "features"
    }

    internal object Input {

        const val ACTION_TYPE = "actionType"
        const val ACTION = "action"
        const val VIN = "vin"
        const val SCHEMA = "schema"
        const val LANGUAGE = "language"
        const val ID = "id"
        const val TYPE = "type"
        const val DATE = "date"
        const val COD_NATION = "codNation"
        const val CONTACT_NAME = "contactName"
        const val CONTACT_PHONE = "contactPhone"
        const val PARAM_SERVICES = "services"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val FILTERS = "filters"

        internal object Settings {

            const val COUNTRY = "country"
            const val SDP = "sdp"
        }

        const val PARAM_TC_REGISTRATION = "tcRegistration"
        const val PARAM_COUNTRY_CODE = "countryCode"
        const val PARAM_STATUS = "status"
        const val PARAM_VERSION = "version"
        const val PARAM_TC_ACTIVATION = "tcActivation"
        const val PARAM_PP_ACTIVATION = "ppActivation"
        const val PARAM_CONTACTS = "contacts"
        const val PARAM_NAME = "name"
        const val PARAM_PHONE = "phone"

        internal object ActionType {

            const val PROFILE = "profile"
            const val LIST = "list"
            const val DETAILS = "details"
            const val CONTRACTS = "contracts"
            const val SERVICES = "services"
            const val ASK = "ask"
            const val ADD = "add"
            const val REMOVE = "remove"
            const val CHECK = "check"
            const val FAVORITE = "favorite"
            const val CONTACT = "contact"
            const val STATIONS = "stations"
            const val RECOMMENDATIONS = "recommendations"
            const val REVIEW = "review"
            const val LANGUAGE = "language"
            const val UPDATE = "update"
            const val APPOINTMENT = "appointment"
            const val WEB_TERMS = "webTerms"
            const val E_SERVICES = "eServices"
            const val APP_TERMS = "appTerms"
            const val APP_PRIVACY = "privacy"
            const val CONNECTED_TERMS = "connectedTC"
            const val CONNECTED_PRIVACY = "connectedPP"
            const val MANUAL = "manual"
            const val DELETE = "delete"
            const val FAQ = "faq"
            const val CALL_CENTERS = "callCenters"
            const val CHARGING_STATION = "chargingStation"
            const val PAYMENT = "payment"
        }

        internal object Action {

            const val GET = "get"
            const val ROADSIDE = "roadSide"
            const val ON_AIR = "onAir"
            const val LANGUAGE = "language"
            const val NICKNAME = "nickname"
            const val AGENDA = "agenda"
            const val SERVICES = "services"
            const val DETAILS = "details"
            const val E_CALL = "eCall"
            const val B_CALL = "bCall"
            const val LIST = "list"
            const val CURRENT = "current"
            const val FILTERS = "filters"
        }

        object Configuration {

            const val LOCALE = "locale"
            const val SITE_CODE = "siteCode"
            const val LANGUAGE_PATH = "languagePath"
        }

        object Profile {

            const val EMAIL = "email"
            const val FIRST_NAME = "firstName"
            const val LAST_NAME = "lastName"
            const val CIVILITY = "civility"
            const val ADDRESS_1 = "address1"
            const val ADDRESS_2 = "address2"
            const val ZIP_CODE = "zipCode"
            const val CITY = "city"
            const val COUNTRY = "country"
            const val PHONE = "phone"
            const val MOBILE = "mobile"
            const val MOBILE_PRO = "mobilePro"
        }

        object Appointment {

            const val BOOKING_ID = "bookingId"
            const val BOOKING_LOCATION = "bookingLocation"
            const val START_DATE = "startDate"
            const val TIME_FENCE = "timeFence"
            const val SERVICES = "services"
            const val MILEAGE = "mileage"
            const val DEPARTMENT_ID = "departmentId"
            const val DATE = "date"
            const val HOUR = "hour"
            const val REFERENCE = "reference"
            const val COMMENT = "comment"
            const val CONTACT = "contact"
            const val SERVICES_ID = "servicesId"
            const val PARAM_PHONE = "phone"
            const val PRICE = "price"
            const val TYPE = "type"
            const val TITLE = "type"
            const val IS_PACKAGE = "is_package"
            const val INTERVENTION_LABEL = "interventionLabel"
            const val PERIOD = "period"
            const val PARAM_PREMIUM_SERVICE = "premiumService"
            const val MOBILITY = "mobility"
            const val OPERATION = "operation"
            const val PARAM_MILEAGE_UNIT = "mileageUnit"
        }
    }

    internal object Storage {

        const val PROFILE = "profile"
        const val PREFERRED_DEALER = "preferredDealer"
        const val PREFERRED_DEALER_ID = "preferredDealerId"
        const val VEHICLES = "vehicles"
        const val VEHICLE = "vehicle"
        const val LAST_UPDATE = "lastUpdate"
        const val CONTRACTS = "contracts"
        const val SETTINGS = "settings"
        const val LANGUAGE = "language"
        const val CGU_VERSION = "cguVersion"
        const val CGU_LAST_UPDATE = "cguLastUpdate"
        const val MP_PARTNERS = "mp_partners"
        const val APPOINTMENT_xPSA = "appointment_xPSA"
    }

    const val DEFAULT = "DEFAULT"

    internal object Output {

        internal object Common {

            const val PHONES = "phones"
        }

        internal object Phones {

            const val ROADSIDE = "roadSide"
        }

        const val LANGUAGE = "language"
    }

    // region ACTION TYPE
    const val PARAM_ACTION_TYPE = "actionType"
    const val PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT = "enrollment"
    const val PARAM_ACTION_TYPE_VEHICLE_IMAGE = "vehicleImage"
    const val PARAM_ACTION_TYPE_VEHICLE_LIST = "vehiclesList"
    const val PARAM_ACTION_TYPE_VEHICLE_REMOVE = "remove"
    const val PARAM_ACTION_TYPE_SETTINGS = "settings"

    const val PARAM_ACTION_VEHICLE_ENROLL = "enroll"
    const val PARAM_ACTION_VEHICLE_UPLOAD = "upload"
    const val PARAM_ACTION_VEHICLE_DETAILS = "details"
    const val PARAM_ACTION_TEXT_SEARCH = "textSearch"
    const val PARAM_ACTION_NEARBY_SEARCH = "nearbySearch"
    const val PARAM_ACTION_PLACE_DETAILS = "placeDetails"
    const val PARAM_ACTION_SEND_DESTINATION = "sendDestination"
    const val PARAM_ACTION_DIRECTIONS_ROUTE = "directionsRoute"
    const val PARAM_ACTION_PREFERRED_DEALER_DETAILS = "preferredDealerDetails"
    const val PARAM_ACTION_PREFERRED_DEALER_ENROLL = "enrollPreferredDealer"
    const val PARAM_ACTION_PREFERRED_DEALER_REMOVE = "removePreferredDealer"
    const val PARAM_ACTION_DEALER_LIST_DETAILS = "dealerListDetails"
    const val PARAM_ACTION_DEALER_LIST_NEARBY_DETAILS = "dealerListNearbyDetails"
    const val PARAM_ACTION_ADVISOR_DEALER_REVIEW_DETAILS = "advisorDealerReviewDetails"
    const val PARAM_ACTION_SEND_ADVISOR_DEALER_REVIEW = "sendAdvisorDealerReview"

    const val PARAM_ACTION_TYPE_RP_STATIONS = "stations"
    const val PARAM_ACTION_TYPE_RP_RECOMMENDATIONS = "recommendations"
    const val PARAM_ACTION_TYPE_RP_ON_AIR = "onAir"
    // endregion

    // region STORAGE KEY
    const val STORAGE_KEY_PIN_TOKEN = "pinToken"
    const val STORAGE_KEY_PIN_TOKEN_EXPIRY = "pinTokenExpiry"
    const val STORAGE_KEY_PROFILE = "profile"
    const val STORAGE_KEY_MYM_TOKEN = "mymToken"
    // endregion

    const val VEHICLE = "vehicle"
    const val ASSISTANCE = "assistance"
    const val SETTINGS = "settings"
    const val ACCOUNT = "account"
    const val CONTRACTS = "contracts"
    const val LOG_INCIDENT = "logIncident"
    const val LOCATIONS = "location"
    const val DEALER = "dealer"
    const val DESTINATION = "destination"
    const val CONFIGURATION = "configuration"
    const val RADIO_PLAYER = "radioPlayer"

    const val CONTEXT_KEY_MARKET = "market"
    const val PARAMS_KEY_CREDENTIAL = "credential"
    const val PARAMS_KEY_PROFILE = "profile"
    const val PARAMS_KEY_GOOGLE_API_KEY = "googleApiKey"
    const val PARAMS_KEY_PROVIDER = "provider"

    const val PARAMS_KEY_PLACE_ID = "placeId"
    const val PARAMS_KEY_ROUTE_PREFERENCE = "routePreference"
    const val PARAMS_KEY_LATITUDE = "latitude"
    const val PARAMS_KEY_LONGITUDE = "longitude"
    const val PARAMS_KEY_NAME = "name"
    const val PARAMS_KEY_DESCRIPTION = "description"
    const val PARAMS_KEY_URL = "url"
    const val PARAMS_KEY_PHONE_NUMBER = "phoneNumber"

    const val PARAMS_KEY_STREET_NAME = "streetName"
    const val PARAMS_KEY_HOUSE_NUMBER = "houseNumber"
    const val PARAMS_KEY_POSTAL_NUMBER = "postalNumber"
    const val PARAMS_KEY_CITY_NAME = "cityName"
    const val PARAMS_KEY_COUNTRY_NAME = "countryName"
    const val PARAMS_KEY_COUNTRY_CODE = "countryCode"
    const val PARAMS_KEY_PROVINCE_NAME = "provinceName"
    const val PARAMS_KEY_PROVINCE_CODE = "provinceCode"
    const val PARAMS_KEY_LANGUAGES = "languages"
    const val PARAMS_KEY_NICKNAME = "nickName"
    const val PARAMS_KEY_DEALER_ID = "dealerId"
    const val PARAMS_KEY_DATE = "date"
    const val PARAMS_KEY_VEHICLE_KM = "vehicleKm"
    const val PARAMS_KEY_COD_NATION = "codNation"
    const val PARAMS_KEY_FAULT_DESCRIPTION = "faultDescription"
    const val PARAMS_KEY_LOCATION = "location"
    const val PARAMS_KEY_CONTACT_NAME = "contactName"
    const val PARAMS_KEY_TELEPHONE = "telephone"
    const val PARAMS_KEY_SERVICES_LIST = "servicesList"

    const val PARAM_ID = "id"
    const val PARAM_CONNECTED = "connected"
    const val PARAM_VIN = "vin"
    const val PARAM_SAVE_PROFILE = "saveProfile"
    const val PARAM_WIDTH = "width"
    const val PARAM_HEIGHT = "height"
    const val PARAM_IMAGE_FORMAT = "imageFormat"
    const val PARAM_COUNTRY = "country"
    const val PARAM_MILEAGE = "mileage"
    const val PARAM_PIN_AUTH = "pinAuth"
    const val PARAM_REASON_ID = "reasonId"
    const val PARAM_REASON = "reason"
    const val PARAM_INCIDENT_ID = "incidentId"
    const val PARAM_URL = "url"
    const val PARAM_KEYWORD = "keyword"
    const val PARAM_PLACE_ID = "placeId"
    const val PARAM_ORIGIN_LONGITUDE = "origin-longitude"
    const val PARAM_ORIGIN_LATITUDE = "origin-latitude"
    const val PARAM_DIRECTION_LONGITUDE = "direction-longitude"
    const val PARAM_DIRECTION_LATITUDE = "direction-latitude"
    const val PARAM_RADIUS = "radius"
    const val PARAM_RPUID = "rpuid"
    const val PARAM_FACTORS = "factors"

    const val PARAM_LAT = "latitude"
    const val PARAM_LNG = "longitude"
    const val PARAM_MAX = "max"
    const val PARAM_RESULT_MAX = "resultmax"
    const val PARAM_SITE_GEO = "site_geo"
    const val PARAM_LIMIT = "limit"
    const val PARAM_PLATE_NUMBER = "plateNumber"

    const val LOCALE = "locale"
    const val PRIMARY = "primary"
    const val SECONDARY = "secondary"

    // region HEADER KEY
    const val HEADER_PARAM_CVS_TOKEN = "cvs-token"
    const val HEADER_PARAM_MYM_TOKEN = "mym-access-token"
    const val AWS_HEADER_KEY_SERVICE_NAME = Constants.OKHTTP_AWS_HEADER_KEY_SERVICE_NAME
    const val AWS_HEADER_VALUE_SERVICE_NAME = "execute-api"
    // endregion

    // region QUERY KEY
    const val QUERY_PARAM_KEY_LANGUAGE = "language"
    const val QUERY_PARAM_KEY_VERSION = "v"
    const val QUERY_PARAM_KEY_OS = "os"
    const val QUERY_PARAM_VALUE_OS = "and"
    const val QUERY_PARAM_KEY_BRAND = "brand"
    const val QUERY_PARAM_KEY_SOURCE = "source"
    const val QUERY_PARAM_KEY_CULTURE = "culture"
    const val QUERY_PARAM_KEY_SITE_CODE = "site_code"
    const val QUERY_PARAM_KEY_VIN = "vin"
    const val QUERY_PARAM_KEY_VIN_LAST_EIGHT = "vinLast8"
    const val QUERY_PARAM_KEY_EVIN = "evin"
    const val QUERY_PARAM_KEY_IMAGE_FORMAT = "resp"
    const val QUERY_PARAM_KEY_QUERY = "query"
    const val QUERY_PARAM_KEY_KEY = "key"
    const val QUERY_PARAM_KEY_PLACE_ID = "place_id"
    const val QUERY_PARAM_KEY_ORIGIN = "origin"
    const val QUERY_PARAM_KEY_DESTINATION = "destination"
    const val QUERY_PARAM_KEY_SDP = "sdp"
    const val QUERY_PARAM_KEY_STAGE = "stage"
    const val QUERY_PARAM_KEY_LOCATION = "location"
    const val QUERY_PARAM_KEY_FROM = "from"
    const val QUERY_PARAM_KEY_TO = "to"
    const val QUERY_PARAM_KEY_START_DATE = "startdate"
    const val QUERY_PARAM_KEY_END_DATE = "enddate"
    const val QUERY_PARAM_KEY_HINT_DEALER = "hintdealer"
    const val QUERY_PARAM_KEY_DEALER_ID = "dealerId"
    const val QUERY_PARAM_KEY_SERVICES_IDS = "servicesIds"
    const val QUERY_PARAM_KEY_DATE = "date"
    const val QUERY_PARAM_KEY_DATE_STRING = "dateString"
    const val QUERY_PARAM_TIME_FENCE = "timefence"

    const val QUERY_PARAM_KEY_LATAM_START_DATE = "startDate"
    const val QUERY_PARAM_KEY_LATAM_END_DATE = "endDate"

    const val QUERY_PARAM_KEY_MODEL = "model"
    const val QUERY_PARAM_KEY_YEAR = "year"

    const val AWS_QUERY_VALUE_ALL = "ALL"
    const val AWS_QUERY_VALUE_REST = "REST"
    // endregion

    // region BODY KEY
    const val BODY_PARAM_VIN = "vin"
    const val BODY_PARAM_CATEGORY = "category"
    const val BODY_PARAM_LATITUDE = "latitude"
    const val BODY_PARAM_LONGITUDE = "longitude"
    const val BODY_PARAM_PHONE_NUMBER = "phoneNumber"
    const val BODY_PARAM_COUNTRY = "country"
    const val BODY_PARAM_SITE_CODE = "siteCode"
    const val BODY_PARAM_FIRST_NAME = "firstName"
    const val BODY_PARAM_LAST_NAME = "lastName"
    const val BODY_PARAM_NATION_ID = "nationalID"
    const val BODY_PARAM_PHONE = "phone"
    const val BODY_PARAM_ZIP_CODE = "zipCode"
    const val BODY_PARAM_CITY = "city"
    const val BODY_PARAM_EMAIL = "email"
    const val BODY_PARAM_ID_CLIENT = "idClient"
    const val BODY_PARAM_CULTURE = "culture"
    const val BODY_PARAM_TITLE = "title"
    const val BODY_PARAM_COMMENT = "comment"
    const val BODY_PARAM_OPTIN_ONE = "optin1"
    const val BODY_PARAM_OPTIN_TWO = "optin2"
    const val BODY_PARAM_FILE_NAME = "filename"
    const val BODY_PARAM_DATA = "data"
    const val BODY_PARAM_RATING = "rating"
    const val BODY_PARAM_SEND_CONFIRM_EMAIL = "sendConfirmEmail"
    const val BODY_PARAM_SERVICE_DATE = "serviceDate"
    const val BODY_PARAM_SERVICE_TYPE = "serviceType"
    const val BODY_PARAM_VEHICLE_ID_TYPE = "vehicleIdType"
    const val BODY_PARAM_CMD = "command"
    const val BODY_PARAM_PARAMS = "params"
    const val BODY_PARAM_FILE_TYPE = "fileType"
    const val BODY_PARAM_QUALITY = "quality"
    // endregion

    // region Contract Id
    const val CONTRACT_ID_PARAM_NAC = "nac"
    const val CONTRACT_ID_PARAM_BTA = "bta"
    const val CONTRACT_ID_PARAM_CLUB = "club"
    const val CONTRACT_ID_PARAM_REMOTELEV = "remoteLev"
    const val CONTRACT_ID_PARAM_FLEXILEASE = "flexiLease"
    const val CONTRACT_ID_PARAM_SERVICES = "services"
    const val CONTRACT_ID_PARAM_TMTS = "tmts"
    const val CONTRACT_ID_PARAM_DSCP = "dscp"
    const val CONTRACT_ID_PARAM_RACCESS = "raccess"
    // endregion

    const val RESPONSE_RESULT_PROFILE_UPDATE_SUCCESSFULLY = "Successfully Updated"

    // region Profile
    const val PARAMS_KEY_PROFILE_EMAIL = "email"
    const val PARAMS_KEY_PROFILE_FIRST_NAME = "first_name"
    const val PARAMS_KEY_PROFILE_LAST_NAME = "last_name"
    const val PARAMS_KEY_PROFILE_CIVILITY = "civility"
    const val PARAMS_KEY_PROFILE_ADDRESS_1 = "address1"
    const val PARAMS_KEY_PROFILE_ADDRESS_2 = "address2"
    const val PARAMS_KEY_PROFILE_ZIP_CODE = "zip_code"
    const val PARAMS_KEY_PROFILE_CITY = "city"
    const val PARAMS_KEY_PROFILE_COUNTRY = "country"
    const val PARAMS_KEY_PHONE = "phone"
    const val PARAMS_KEY_MOBILE = "mobile"
    const val PARAMS_KEY_MOBILE_PRO = "mobile_pro"
    // endregion

    // region STATUS
    const val STATUS_OK = "OK"
    // endregion

    const val QUERY_PARAM_KEY_APP = "APP"

    const val QUERY_DEFAULT_IMAGE_WIDTH = 300
    const val QUERY_DEFAULT_IMAGE_HEIGHT = 300
    const val QUERY_DEFAULT_IMAGE_FORMAT = "png"
}
