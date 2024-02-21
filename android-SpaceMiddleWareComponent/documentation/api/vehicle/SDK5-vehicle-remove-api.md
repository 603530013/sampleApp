# space.middleware.vehicle

## SET

### action-type = remove

This API allow to remove a vehicle from his list of user’s vehicles (Garage)

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| vin        | String | x         |
| reason     | Stirng | x         |
| reasonId   | Stirng | x         |

###### Example

```json
{
    "actionType": "remove",
    "vin": "VR7ATTENTKL127249",
    "reason": "testReason",
    "reasonId": "testReasonId"
}
```

##### Output

```json
{
  "result": "{}",
  "status": "SUCCEEDED"
}
```
