# space.middleware.features

## GET

### action = get or refresh

This api provides the list of features that includes code, config, status, value

##### Input

| Name            | Type                      | Mandatory |
|-----------------|---------------------------|-----------|
| action          | String                    | x         |
| vin             | String                    | x         |

###### Example

```json
{
    "action": "get",
    "vin": "FCASWF01000000005"
}
```

##### Output xPSA

```json
"result": {
    "features": [
      {
        "code": "DOOR_LOCK",
        "config": {
          "version": 1.0
        },
        "status": "enable",
        "value": "RDL"
      },
      {
        "code": "DOOR_UNLOCK",
        "config": {
          "version": 1.0
        },
        "status": "enable",
        "value": "RDU"
      },
      {
        "code": "SEND_TO_NAV",
        "status": "enable",
        "value": "SDV"
      },
      {
        "code": "ASSISTANCE_CALL",
        "status": "enable",
        "value": "ASSIST"
      },
      {
        "code": "VEHICLE_LOCATOR",
        "status": "enable",
        "value": "VF"
      },
      {
        "code": "STOLEN_CALL",
        "status": "enable",
        "value": "SVLA"
      },
      {
        "code": "LIGHTS",
        "config": {
          "version": 1.0
        },
        "status": "enable",
        "value": "ROLIGHTS"
      },
      {
        "code": "THEFT_ALARM",
        "status": "enable",
        "value": "TA"
      },
      {
        "code": "VEHICLE_HEALTH_ALERTS",
        "status": "enable",
        "value": "VRC"
      },
      {
        "code": "IVA_CALL",
        "status": "enable",
        "value": "IVA"
      },
      {
        "code": "VEHICLE_DRIVE_ALERTS",
        "status": "enable",
        "value": "PARENT"
      },
      {
        "code": "BREAKDOWN_CALL",
        "status": "enable",
        "value": "BCALL"
      },
      {
        "code": "CHARGE_STATION_LOCATOR",
        "config": {
          "type": "internal"
        },
        "status": "enable",
        "value": "CSL"
      },
      {
        "code": "VEHICLE_INFO",
        "config": {
          "engine": ""
        },
        "status": "enable",
        "value": "VHS"
      },
      {
        "code": "CHARGE_NOW",
        "status": "enable",
        "value": "CNOW"
      },
      {
        "code": "VEHICLE_DEEP_REFRESH",
        "status": "enable",
        "value": "DEEPREFRESH"
      },
      {
        "code": "CHARGE_SCHEDULING",
        "config": {
          "daysType": [
            "chooseDays"
          ],
          "repeat": true,
          "schedule": 3.0,
          "shared": true
        },
        "status": "enable",
        "value": "CPPLUS"
      },
      {
        "code": "CLIMATE_SCHEDULING",
        "config": {
          "daysType": [
            "chooseDays"
          ],
          "repeat": true,
          "schedule": 3.0,
          "shared": true
        },
        "status": "enable",
        "value": "CPPLUS"
      },
      {
        "code": "DYNAMIC_RANGE_MAP",
        "config": {
          "version": 1.0
        },
        "status": "enable",
        "value": "NAV_OB_ROUTE_DRM"
      },
      {
        "code": "ECO_COACHING",
        "config": {
          "version": 1.0
        },
        "status": "enable",
        "value": "ECOCOACHINGR1"
      },
      {
        "code": "LIFT_GATE_LOCK",
        "status": "enable",
        "value": "ROLIFTGATELOCK"
      },
      {
        "code": "LIFT_GATE_UNLOCK",
        "status": "enable",
        "value": "ROLIFTGATEUNLOCK"
      },
      {
        "code": "TRIPS",
        "config": {
          "protocol": "NETWORK"
        },
        "status": "enable",
        "value": "TRIPREPORT"
      }
    ]
  }
```
