# space.middleware.vehicle

## SET

### action-type = update

#### action = nickname

This API allow to API allows customers to update the nickname.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| vin        | String | x         |
| name       | String |           |

###### Example

```json
{
    "actionType": "update",
    "action": "nickname",
    "vin": "1C4BU0PP0LPK08967",
    "name": "testName"

}
```

##### Output

```json
{
    "result": "{}",
    "status": "SUCCEEDED"
}
```
