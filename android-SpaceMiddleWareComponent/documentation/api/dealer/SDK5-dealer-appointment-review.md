# space.middleware.dealer

## GET

### action-type = appointment

### action = review

This api provides the appointment review details

##### Input

| Name          | Type   | Mandatory |
|---------------|--------|-----------|
| actionType    | String | x         |
| vin           | String | x         |
| serviceType   | String | x         |
| vehicleIdType | String | x         |

###### Example

```json
{
    "actionType": "review",
    "vin": "VR7ATTENTKL127249",
    "serviceType": "APV",
    "vehicleIdType": "VIN"
}
```

## Output

```json
{
    "result": {
        "success": true,
        "status": 200,
        "vehicle_id_type": "VIN",
        "VIN_mask": "^[A-Za-z0-9]{17}$",
        "review_max_date": "120",
        "review_max_month": "2",
        "review_min_delta": "72",
        "review_max_char": "500",
        "rating_negative_floor": "3",
        "CGU_link": "https://www.citroen-advisor.fr/conditions",
        "allowed": true
    },
    "status": "SUCCEEDED"
}
```
