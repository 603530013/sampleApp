# space.middleware.dealer

## SET

### action-type = appointment

### action = add

This api sets dealer appointment

##### Input

| Name            | Type           | Mandatory |
|-----------------|----------------|-----------|
| actionType      | String         | x         |
| action          | String         | x         |
| vin             | String         | x         |
| date            | String         | x         |
| mileage         | Number         | x         |
| codNation       | String         | x         |
| comment         | String         | x         |
| bookingId       | String         | x         |
| bookingLocation | String         |           |
| contactName     | String         |           |
| contactPhone    | String         | x         |
| services        | List of String |           |

###### Example

```json
{
    "actionType": "appointment",
    "action": "add",
    "vin": "FCASWF01000000005",
    "date": "2023-10-03T16:20:00",
    "mileage": 1073,
    "codNation": "IT",
    "comment": "Test",
    "bookingId": "0072042",
    "bookingLocation": "001",
    "contactName": "Roy",
    "contactPhone": "123456789",
    "services": [
        "FLAG1",
        "FLAG2"
    ]
}
```

##### Output

```json
{
    "result": {
        "id": "93733607"
    },
    "status": "SUCCEEDED"
}
```

### Incase of PSA

### action-type = appointment

#### action = add

This api provides the information about appointment confirm details like booking id etc

##### Input

| Name           | Type           | Mandatory |
|----------------|----------------|-----------|
| actionType     | String         | x         |
| action         | String         | x         |
| vin            | String         | x         |
| date           | String         | x         |
| comment        | String         |           |
| bookingId      | String         | x         |
| contactName    | String         |           |
| contactPhone   | String         |           |
| services       | List of String | x         |
| mobility       | Boolean        |           |
| premiumService | String         |           |

###### Example

```json
{
    "actionType": "appointment",
    "action": "add",
    "vin": "VR7ATTENTKL127249",
    "bookingId": "0000038471",
    "day": "2023-07-20",
    "hour": "11:15",
    "mileage": 1000,
    "codNation": "IT",
    "comment": "Test",
    "bookingLocation": "001",
    "contactName": "Swa",
    "contactPhone": "123456789",
    "services": [
        "FCT0305"
    ]
}
```

##### Output

```json
{
    "success": {
        "vin": "VR7ATTENTKL127249",
        "bookingId": "0000038471",
        "day": "2023-07-20",
        "hour": "11:15",
        "contact": 0,
        "mobility": 0,
        "discount": 0,
        "appointmentId": 1245910,
        "operations": [
            {
                "reference": "FCT0305",
                "title": "Batterie",
                "type": 0,
                "is_package": 0,
                "intervention_label": "Batterie"
            }
        ]
    }
}
```
