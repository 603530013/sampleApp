# space.middleware.dealer

## GET

### action-type = appointment

### action = details

This api provides the dealer appointment details status

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| id         | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "appointment",
    "action": "details",
    "id": "93720328",
    "vin": "FCASWF01000000002"
}
```

#### **EMEA**

##### Output

```json
{
    "bookingId": "0772000",
    "bookingLocation": "000",
    "comment": "test",
    "id": "93720328",
    "mileage": "6",
    "phone": "1234567891",
    "reminders": [],
    "scheduledTime": "2022-12-22T18:30Z",
    "services": [
        {
            "id": "FLAG10",
            "type": "generic"
        },
        {
            "id": "FLAG11",
            "type": "generic"
        }
    ],
    "status": "requested",
    "vin": "FCASWF01000000002"
}
```

#### **MASERATI**

##### Output

```json
{
    "bookingId": "063905",
    "comment": "-.-",
    "id": "1221",
    "phone": "3491371517",
    "reminders": [],
    "scheduledTime": "2022-09-11T12:00Z",
    "services": [
        {
            "id": "Maintenance",
            "name": "Maintenance",
            "type": "generic"
        }
    ],
    "status": "requested",
    "vin": "TESTOPSP0LPK11565"
}
```

#### **LATAM**

#### Output

```json
{
    "amount": 0.0,
    "bookingId": "790651",
    "email": "marelliduv@mailinator.com",
    "id": "1682",
    "mileage": "35693.0 km",
    "reminders": [],
    "scheduledTime": "2023-11-16T18:30+05:30",
    "services": [
        {
            "id": "29",
            "name": "REVISÃO PROGRAMADA",
            "opCode": "1001"
        }
    ],
    "vin": "988671173MKN00051"
}
```

##### **NAFTA**

//TODO not getting response for NAFTA yet
