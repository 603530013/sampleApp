# space.middleware.assistance

## GET

### action-type = callCenters

## This api provides the call Centers information

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "callCenters",
    "vin"       : "FCASWF01000000005"
}
```

##### Output

```json
{
    "result": {
        "roadSide": {
          "primary": "+32460204667",
          "secondary": "+32460204667"
        },
        "brand": {
          "primary": "0080034280000,303",
          "secondary": "+390244412778,3023"
        },
        "emergency": {
            "primary": "+008003668903",
            "secondary": "+3246020468956"
        },
      "status": "SUCCEEDED"
    }
}
```
