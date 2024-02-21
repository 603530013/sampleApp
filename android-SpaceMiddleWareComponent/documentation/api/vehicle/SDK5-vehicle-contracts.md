# space.middleware.vehicle

## GET

### action-type = contracts

### action = "refresh" provides the list of vehicle contracts from api endpoint

### action = "get" provides the list of vehicle contracts coming from cache

This api provides the list of vehicle contracts

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "contracts",
    "action": "get/refresh",
    "vin": "FCASWF01000000005"
}
```

##### Output

Status can be active, terminatedExpired, pendingAssociation, pendingActivation, pendingSubscription,
cancelled,deactivated, terminated

```json
{
  "result": {
    "contracts": [
      {
        "code": "RDL",
        "status": "active",
        "title": "RDL"
      },
      {
        "code": "RDU",
        "status": "active",
        "title": "RDU"
      },
      {
        "code": "ROSECUREDELIVERY",
        "status": "active",
        "title": "ROSECUREDELIVERY"
      },
      {
        "code": "SQDF",
        "status": "active",
        "title": "SQDF"
      },
      {
        "code": "SDV",
        "status": "active",
        "title": "SDV"
      },
      {
        "code": "ASSIST",
        "status": "deactivated",
        "title": "ASSIST"
      },
      {
        "code": "ROC",
        "status": "active",
        "title": "ROC"
      },
      {
        "code": "ASOS",
        "status": "deactivated",
        "title": "ASOS"
      },
      {
        "code": "VF",
        "status": "active",
        "title": "VF"
      },
      {
        "code": "SVLA",
        "status": "deactivated",
        "title": "SVLA"
      },
      {
        "code": "ROCRANKINHIBITON",
        "status": "active",
        "title": "ROCRANKINHIBITON"
      },
      {
        "code": "ROCRANKINHIBITOFF",
        "status": "active",
        "title": "ROCRANKINHIBITOFF"
      },
      {
        "code": "ROLIGHTS",
        "status": "active",
        "title": "ROLIGHTS"
      },
      {
        "code": "TA",
        "status": "active",
        "title": "TA"
      },
      {
        "code": "VRC",
        "status": "active",
        "title": "VRC"
      },
      {
        "code": "DTC",
        "status": "active",
        "title": "DTC"
      },
      {
        "code": "HUC",
        "status": "deactivated",
        "title": "HUC"
      },
      {
        "code": "AOTAC",
        "status": "active",
        "title": "AOTAC"
      },
      {
        "code": "UBI",
        "status": "active",
        "title": "UBI"
      },
      {
        "code": "WIFIHOTSPOT",
        "status": "active",
        "title": "WIFIHOTSPOT"
      },
      {
        "code": "IVA",
        "status": "active",
        "title": "IVA"
      },
      {
        "code": "IVM",
        "status": "active",
        "title": "IVM"
      },
      {
        "code": "SVCSUBMSSG",
        "status": "active",
        "title": "SVCSUBMSSG"
      },
      {
        "code": "FTD",
        "status": "active",
        "title": "FTD"
      },
      {
        "code": "PARENT",
        "status": "active",
        "title": "PARENT"
      },
      {
        "code": "BCALL",
        " status": "deactivated",
        "title": "BCALL"
      },
      {
        "code": "FOTA",
        "status": "active",
        "title": "FOTA"
      },
      {
        "code": "SVLAAPP",
        "status": "deactivated",
        "title": "SVLAAPP"
      },
      {
        "code": "PHEV",
        "status": "active",
        "title": "PHEV"
      },
      {
        "code": "CSL",
        "status": "active",
        "title": "CSL"
      },
      {
        "code": "CSTOP",
        "status": "active",
        "title": "CSTOP"
      },
      {
        "code": "EVNOT",
        "status": "active",
        "title": "EVNOT"
      },
      {
        "code": "VHS",
        "status": "active",
        "title": "VHS"
      },
      {
        "code": "CNOW",
        "status": "active",
        "title": "CNOW"
      },
      {
        "code": "DEEPREFRESH",
        "status": "active",
        "title": "DEEPREFRESH"
      },
      {
        "code": "NOAOTA",
        "status": "active",
        "title": "NOAOTA"
      },
      {
        "code": "LASTMILE",
        "status": "active",
        "title": "LASTMILE"
      },
      {
        "code": "NAV_OB_WEATHER",
        "status": "active",
        "title": "NAV_OB_WEATHER"
      },
      {
        "code": "NAV_OB_SPEED_CAMERA",
        "status": "active",
        "title": "NAV_OB_SPEED_CAMERA"
      },
      {
        "code": "NAV_OB_SEARCH",
        "status": "active",
        "title": "NAV_OB_SEARCH"
      },
      {
        "code": "CPPLUS",
        "status": "active",
        "title": "CPPLUS"
      },
      {
        "code": "NAV_OB_FUEL_FINDER",
        "status": "active",
        "title": "NAV_OB_FUEL_FINDER"
      },
      {
        "code": "NAV_OB_PARKING_FINDER",
        "status": "active",
        "title": "NAV_OB_PARKING_FINDER"
      },
      {
        "code": "DUMMY",
        "status": "active",
        "title": "DUMMY"
      },
      {
        "code": "NAV_OB_MOTA_WIFI",
        "status": "active",
        "title": "NAV_OB_MOTA_WIFI"
      },
      {
        "code": "NAV_OB_MOTA_CELL",
        "status": "active",
        "title": "NAV_OB_MOTA_CELL"
      },
      {
        "code": "NAV_OB_MOTA_WIFI_CELL",
        "status": "active",
        "title": "NAV_OB_MOTA_WIFI_CELL"
      },
      {
        "code": "ELECPAGES",
        "status": "active",
        "title": "ELECPAGES"
      },
      {
        "code": "FLEET",
        "status": "active",
        "title": "FLEET"
      },
      {
        "code": "NAV_OB_ROUTE_DRM",
        "status": "active",
        "title": "NAV_OB_ROUTE_DRM"
      },
      {
        "code": "WIFIP",
        "status": "active",
        "title": "WIFIP"
      },
      {
        "code": "ECOSERVICE",
        "status": "active",
        "title": "ECOSERVICE"
      },
      {
        "code": "ALEXAVA",
        "status": "active",
        "title": "ALEXAVA"
      },
      {
        "code": "ECOCOACHINGR1",
        "status": "active",
        "title": "ECOCOACHINGR1"
      },
      {
        "code": "DASS",
        "status": "active",
        "title": "DASS"
      },
      {
        "code": "GSKILLL",
        "status": "active",
        "title": "GSKILLL"
      },
      {
        "code": "RACEOPTIONS",
        "status": "active",
        "title": "RACEOPTIONS"
      },
      {
        "code": "MYECHG",
        "status": "active",
        "title": "MYECHG"
      },
      {
        "code": "TA_SUP",
        "status": "active",
        "title": "TA_SUP"
      },
      {
        "code": "ROLIFTGATELOCK",
        "status": "active",
        "title": "ROLIFTGATELOCK"
      },
      {
        "code": "ROLIFTGATEUNLOCK",
        "status": "active",
        "title": "ROLIFTGATEUNLOCK"
      },
      {
        "code": "EVROUT",
        "status": "active",
        "title": "EVROUT"
      },
      {
        "code": "TRIPREPORT",
        "status": "active",
        "title": "TRIPREPORT"
      },
      {
        "code": "Not_Op_Test",
        "status": "deactivated",
        "title": "Not_Op_Test"
      }
    ]
  },
  "status": "SUCCEEDED"
}
```
