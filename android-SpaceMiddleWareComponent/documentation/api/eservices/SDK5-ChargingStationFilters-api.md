# space.middleware.eservices

## GET

### action-type = chargingStation

#### action = filters

This API allows get list oe filters according to vin provided.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "chargingStation",
    "action": "filters",
    "vin": "TESTOPSP0LPK11565"

}
```

##### Output F2M

```json
{ "filters": [
        {
            "data": [
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
            ],
            "key": "connectorTypes",
            "type": "list"
        },
        {
            "key": "chargingCableAttached",
            "type": "boolean"
        },
        {
            "key": "free",
            "type": "boolean"
        },
        {
            "key": "indoor",
            "type": "boolean"
        },
        {
            "data": [
                "SLOW_CHARGE",
                "REGULAR_CHARGE",
                "FAST_CHARGE"
            ],
            "key": "powerTypes",
            "type": "list"
        },
        {
            "data": [
                "APP",
                "CHARGING_CARD",
                "NO_AUTHENTICATION",
                "PLUG_AND_CHARGE"
            ],
            "key": "access",
            "type": "list"
        },
        {
            "key": "open24Hours",
            "type": "boolean"
        }
    ],
    "hasPartner": "true"
}
```
