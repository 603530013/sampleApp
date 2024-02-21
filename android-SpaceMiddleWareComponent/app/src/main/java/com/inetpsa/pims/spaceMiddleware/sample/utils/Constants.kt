package com.inetpsa.pims.spaceMiddleware.sample.utils

object Constants {

    const val SERVICE_NAME = "pims.middleware"
    const val NEW_SERVICE_NAME = "space.middleware"

    // Apis
    const val VEHICLE = "vehicle"
    const val ASSISTANCE = "assistance"
    const val SETTINGS = "settings"
    const val ACCOUNT = "account"
    const val USER = "user"
    const val CONTRACTS = "contracts"
    const val LOG_INCIDENT = "logIncident"
    const val LOCATIONS = "location"
    const val DEALER = "dealer"
    const val DESTINATION = "destination"
    const val RADIO_PLAYER = "radioPlayer"
    const val CONFIGURATION = "configuration"
    const val ESERVICES = "eservices"
    const val FEATURES = "features"

    const val PARAM_KEY_ACTION = "action"
    const val PARAM_ACTION_GET = "get"
    const val PARAM_ACTION_ADD = "add"
    const val PARAM_ACTION_REMOVE = "remove"
    const val PARAM_ACTION_REFRESH = "refresh"
    const val PARAM_ACTION_AGENDA = "agenda"
    const val PARAM_ACTION_DETAILS = "details"
    const val PARAM_ACTION_SERVICES = "services"
    const val PARAM_ACTION_LIST = "list"
    const val PARAM_ACTION_CURRENT = "current"
    const val PARAM_ACTION_CHARGING_STATION = "chargingStation"
    const val PARAM_ACTION_DELETE = "delete"
    const val PARAM_ACTION_FILTERS = "filters"

    // list of param details
    const val PARAM_ID = "id"
    const val PARAM_VIN = "vin"
    const val PARAM_SCHEMA = "schema"
    const val PARAM_MILEAGE = "mileage"
    const val PARAM_MILEAGE_UNIT = "mileageUnit"
    const val PARAMS_KEY_PROFILE = "profile"
    const val PARAM_KEYWORD = "keyword"
    const val PARAM_COUNTRY = "country"
    const val PARAM_SDP = "sdp"
    const val PARAM_INCIDENT_ID = "incidentId"
    const val PARAM_RADIUS = "radius"
    const val PARAM_RPUID = "rpuid"
    const val PARAM_FACTORS = "factors"
    const val PARAM_PLACE_ID = "placeId"
    const val PARAM_ORIGIN_LATITUDE = "origin-latitude"
    const val PARAM_ORIGIN_LONGITUDE = "origin-longitude"
    const val PARAM_DIRECTION_LONGITUDE = "direction-longitude"
    const val PARAM_DIRECTION_LATITUDE = "direction-latitude"
    const val PARAM_REASON = "reason"
    const val PARAM_REASON_ID = "reasonId"
    const val PARAM_CONNECTED = "connected"
    const val PARAM_BOOKING_ID = "bookingId"
    const val PARAM_BOOKING_LOCATION = "bookingLocation"
    const val PARAM_START_DATE = "startDate"
    const val PLACEHOLDER_PARAM_KEY_START = "startDate (YYYY-MM-DD)"
    const val PARAM_TIME_FENCE = "timeFence"
    const val PLACEHOLDER_PARAM_TIME_FENCE = "month"
    const val PLACEHOLDER_PARAM_MILEAGE_UNIT = "km/miles"
    const val PARAM_DATE = "date"
    const val PARAM_COD_NATION = "codNation"
    const val PARAM_CONTACT_NAME = "contactName"
    const val PARAM_CONTACT_PHONE = "contactPhone"
    const val PARAM_SERVICES = "services"
    const val PARAM_SERVICES_PLACEHOLDER = "services separated by ;"
    const val PARAM_PLATE_NUMBER = "plateNumber"
    const val PARAM_TC_REGISTRATION = "tcRegistration"
    const val PARAM_COUNTRY_CODE = "countryCode"
    const val PARAM_STATUS = "status"
    const val PARAM_VERSION = "version"
    const val PARAM_TC_ACTIVATION = "tcActivation"
    const val PARAM_PP_ACTIVATION = "ppActivation"
    const val PLACEHOLDER_TC_REGISTRATION_COUNTRY_CODE = "tcRegistration - countryCode"
    const val PLACEHOLDER_TC_REGISTRATION_STATUS = "tcRegistration - status"
    const val PLACEHOLDER_TC_REGISTRATION_VERSION = "tcRegistration - version"
    const val PLACEHOLDER_TC_ACTIVATION_COUNTRY_CODE = "tcActivation - countryCode"
    const val PLACEHOLDER_TC_ACTIVATION_STATUS = "tcActivation - status"
    const val PLACEHOLDER_TC_ACTIVATION_VERSION = "tcActivation - version"
    const val PLACEHOLDER_PP_ACTIVATION_COUNTRY_CODE = "ppActivation - countryCode"
    const val PLACEHOLDER_PP_ACTIVATION_STATUS = "ppActivation - status"
    const val PLACEHOLDER_PP_ACTIVATION_VERSION = "ppActivation - version"
    const val PLACEHOLDER_PARAM_KEY_DATE_TIME = "Date (YYYY-MM-DDThh:mm:ss)"

    const val PARAMS_KEY_PROVIDER = "provider"
    const val PARAMS_KEY_LATITUDE = "latitude"
    const val PARAMS_KEY_LONGITUDE = "longitude"
    const val PARAMS_KEY_NAME = "name"
    const val PARAMS_KEY_DESCRIPTION = "description"
    const val PARAMS_KEY_URL = "url"
    const val PARAMS_KEY_PHONE_NUMBER = "phoneNumber"
    const val PARAMS_KEY_CITY_NAME = "cityName"
    const val PARAMS_KEY_COUNTRY_NAME = "countryName"
    const val PARAMS_KEY_COUNTRY_CODE = "countryCode"
    const val PARAMS_KEY_PROVINCE_NAME = "provinceName"
    const val PARAMS_KEY_PROVINCE_CODE = "provinceCode"
    const val PARAM_KEY_ROADSIDE = "roadSide"
    const val PARAM_KEY_NICKNAME = "nickname"
    const val PARAM_KEY_ECALL = "eCall"
    const val PARAM_KEY_TYPE = "type"
    const val PARAM_TYPE_NORMAL = "normal"
    const val PARAM_TYPE_LAST_CHARACTERS = "lastCharacters"
    const val PARAM_TYPE_ENCRYPTED = "encrypted"
    const val PARAMS_KEY_DELETE = "delete"
    const val PARAM_KEY_BCALL = "bCall"

    const val PARAM_LAT = "latitude"
    const val PARAM_LNG = "longitude"
    const val PARAM_SITE_GEO = "site_geo"
    const val PARAM_RESULT_MAX = "resultmax"
    const val PARAM_LIMIT = "limit"
    const val PARAM_MOBILITY = "mobility"
    const val PARAM_PREMIUM_SERVICE = "premiumService"
    const val PARAM_FILTERS = "filters"
    const val PARAM_ACCESS_TYPES = "accessTypes"
    const val PARAM_CONNECTOR_TYPES = "connectorTypes"
    const val PARAM_POWERTYPES = "powerTypes"
    const val PARAM_OPEN24HOURS = "open24Hours"
    const val PARAM_CHARGINGCABLEATTACHED = "chargingCableAttached"
    const val PARAM_FREE = "free"
    const val PARAM_INDOOR = "indoor"

    // region of Action Type
    const val PARAM_ACTION_TYPE = "actionType"
    const val PARAM_ACTION_TYPE_VEHICLE_ENROLLMENT = "enrollment"
    const val PARAM_ACTION_TYPE_VEHICLE_ADD = "add"
    const val PARAM_ACTION_TYPE_VEHICLE_IMAGE = "vehicleImage"
    const val PARAM_ACTION_TYPE_VEHICLE_LIST = "vehiclesList"
    const val PARAM_ACTION_TYPE_VEHICLE_REMOVE = "remove"
    const val PARAM_ACTION_TYPE_SETTINGS = "settings"
    const val PARAM_ACTION_TYPE_RP_STATIONS = "stations"
    const val PARAM_ACTION_TYPE_RP_RECOMMENDATIONS = "recommendations"
    const val PARAM_ACTION_TYPE_RP_ON_AIR = "onAir"
    const val PARAM_ACTION_TYPE_LIST = "list"
    const val PARAM_ACTION_TYPE_DETAILS = "details"
    const val PARAM_ACTION_TYPE_CONTRACTS = "contracts"
    const val PARAM_ACTION_TYPE_SERVICES = "services"
    const val PARAM_ACTION_TYPE_ASSISTANCE = "ask"
    const val PARAM_ACTION_TYPE_CHECK = "check"
    const val PARAM_ACTION_TYPE_CONTACT = "contact"
    const val PARAM_ACTION_TYPE_CHARGING_STATIONS = "chargingStation"
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
    const val PARAM_ACTION_TYPE_PAYMENT = "payment"
    const val PARAMS_KEY_PLACE_ID = "placeId"
    const val PARAMS_KEY_ROUTE_PREFERENCE = "routePreference"
    const val PARAM_ACTION_TYPE_FAVORITE = "favorite"
    const val PARAM_KEY_RP_STATION_ACTION = "onAir"
    const val PARAM_ACTION_TYPE_REVIEW = "review"
    const val PARAM_ACTION_TYPE_LANGUAGE = "language"
    const val PARAMS_KEY_LANGUAGE = "language"
    const val PARAM_ACTION_TYPE_UPDATE = "update"
    const val PARAM_ACTION_TYPE_APPOINTMENT = "appointment"
    const val PARAM_ACTION_TYPE_APP_TERMS = "appTerms"
    const val PARAM_ACTION_TYPE_WEB_TERMS = "webTerms"
    const val PARAM_ACTION_TYPE_CONNECTED_TERMS = "connectedTC"
    const val PARAM_ACTION_TYPE_CONNECTED_PRIVACY = "connectedPP"
    const val PARAM_ACTION_TYPE_APP_PRIVACY = "privacy"
    const val PARAM_ACTION_TYPE_MANUAL = "manual"
    const val PARAM_ACTION_TYPE_ESERVICES = "eServices"
    const val PARAM_ACTION_TYPE_FAQ = "faq"
    const val PARAM_ACTION_TYPE_CALL_CENTERS = "callCenters"
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
    const val BODY_PARAM_ZIP_CODE = "zipCode"
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
}
