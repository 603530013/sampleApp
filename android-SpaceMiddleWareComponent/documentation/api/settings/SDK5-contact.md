# space.middleware.settings

## GET

### action-type = contact

### action = roadSide

This api provides the roadSide contact information

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "contact",
    "action": "roadSide",
    "vin": "VR7ATTENTKL127249"
}
```

##### Output

```json
{
    "result": {
        "phones": {
            "primary": "0549163547",
            "secondary": "0549163547"
        }
    }
}
```

### action = eCall

This api provides the ecall contact information (support only EMEA region)

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "contact",
    "action": "eCall",
    "vin": "1C4BU0PP0LPK08967"
}
```

##### Output

```json
{
    "result": {
        "phones": {
            "primary": "0080025320000,403",
            "secondary": "+390244412778,4031"
        }
    }
}
```

### action = bCall

This api provides the BRAND call contact information EMEA region similarly
UCONNECT call contact information for other regions(NAFTA & LATAM).

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "contact",
    "action": "bCall",
    "vin": "ZFAEU0PP0LPK02721"
}
```

##### Output for EMEA

```json
{
    "result": {
        "phones": {
           "primaryNumber": "+18449681617",
           "secondaryNumber": "+18449681617"
        }
    }
}
```

##### Output for NAFTA & LATAM

```json
{
    "result": {
        "phones": {
            "primaryNumber": "+18449681619",
            "secondaryNumber": "+18449681619"
        }
    }
}
```

## SET

NA
