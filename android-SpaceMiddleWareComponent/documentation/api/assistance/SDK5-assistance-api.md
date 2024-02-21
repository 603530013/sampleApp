# space.middleware.assistance

## SET

### action-type = ask

This api provides id and that id we need to provide as an input for assistance GET operation

##### Input

| Name        | Type   | Mandatory |
|-------------|--------|-----------|
| actionType  | String | x         |
| vin         | String | x         |
| category    | String | x         |
| latitude    | Double | x         |
| longitude   | Double | x         |
| phoneNumber | String | x         |
| country     | String | x         |

###### Example

```json

{
    "actionType": "ask",
    "vin": "VR7ATTENTK5005541",
    "category": "Test",
    "latitude": "44.0",
    "longitude": "4.0",
    "phoneNumber": "08065004634",
    "country": "FR"
}
```

##### Output

```json
{
    "result": {
        "breakdownCategory": "Test",
        "driver": {
            "firstname": "Rw",
            "lastname": "Rwe",
            "phoneNumber": 8123069041
        },
        "id": "78b58afc-8421-42e0-802e-7b77ae68cd3f",
        "licensePlate": "VF7SXHNZTHT554082",
        "status": "CREATED",
        "vehicleLocation": {
            "latitude": 44,
            "longitude": 5
        }
    },
    "status": "SUCCEEDED"
}
```

## GET

### action-type = details

This api provides the radioPlayer recommendations

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| id         | String | x         |

###### Example

```json
{
"actionType": "details",
"id": "78b58afc-8421-42e0-802e-7b77ae68cd3f"
}
```

##### Output

```json
{
  "result": {
    "breakdownCategory": "Test",
    "driver": {
      "firstname": "Rw",
      "lastname": "Rwe",
      "phoneNumber": 8123069041
    },
    "id": "78b58afc-8421-42e0-802e-7b77ae68cd3f",
    "licensePlate": "VF7SXHNZTHT554082",
    "status": "CREATED",
    "vehicleLocation": {
      "latitude": 44,
      "longitude": 5
    }
  },
  "status": "SUCCEEDED"
}
```
