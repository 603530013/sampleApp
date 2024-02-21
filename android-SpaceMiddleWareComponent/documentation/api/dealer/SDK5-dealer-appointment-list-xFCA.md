# space.middleware.dealer

## GET

### action-type = appointment

### action = list

This api provides the dealer appointment history list

##### Input

| Name            | Type   | Mandatory  |
|-----------------|--------|------------|
| actionType      | String | x          |
| action          | String | x          |
| vin             | String | x          |
| bookingId       | String | Nafta only |
| bookingLocation | String | Nafta only |

###### Example

```json
{
    "actionType": "appointment",
    "action": "list",
    "vin": "FCASWF01000000002"
}
```

##### Output

```json
{
  "appointments": [
    {
      "appointmentId": "93731328",
      "scheduledTime": "2024-07-24T10:00Z",
      "status": "requested"
    },
    {
      "appointmentId": "93739448",
      "scheduledTime": "2023-11-17T10:45Z",
      "status": "requested"
    },
    {
      "appointmentId": "93738107",
      "scheduledTime": "2023-11-10T09:00Z",
      "status": "requested"
    }
  ]
}
```
