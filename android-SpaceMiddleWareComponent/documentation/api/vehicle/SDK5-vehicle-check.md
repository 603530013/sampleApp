# space.middleware.vehicle

## GET

### action-type = check

This api checks whether the input vin is exist or not

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "check",
    "action": "get/refresh",
    "vin": "VR7ATTENTKL127249"
}
```

##### Output

```json
{
  "label": "vin already exist"
}
```
