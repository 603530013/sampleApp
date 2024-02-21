# space.middleware.dealer

## SET

### action-type = appointment

### action = delete

This api delete the previously booked dealer appointment

##### Input

| Name            | Type   | Mandatory |
|-----------------|--------|-----------|
| actionType      | String | x         |
| action          | String | x         |
| vin             | String | x         |
| id              | String | x         |
| bookingId       | String | x         |
| bookingLocation | String |           |

###### Example

```json
{
  "actionType": "appointment",
  "action": "list",
  "vin": "FCASWF01000000003",
  "id ": "93733607",
  "bookingId": "0072000"
}
```

##### Output

```json
{
    "result": "{}",
    "status": "SUCCEEDED"
}
```
