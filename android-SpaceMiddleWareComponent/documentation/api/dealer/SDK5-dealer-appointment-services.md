# space.middleware.dealer

## GET

### action-type = appointment

### action = services

This api provides the list of dealer operations and interventions for PSA
And in case of FCA it provides the list of dealer services code.

##### Input

| Name            | Type   | Mandatory |
|-----------------|--------|-----------|
| actionType      | String | x         |
| action          | String | x         |
| vin             | String | x         |
| bookingId       | String | x         |
| bookingLocation | String |           |
| mileage         | Int    | x         |
| mileageUnit     | Enum   | x         |

##### In case of PSA

##### Example

```json
{
  "actionType": "appointment",
  "action": "services",
  "vin": "VR7ATTENTKL127249",
  "bookingId": "0000039622"
}
```
##### Output

```json
{
    "services": [
        {
            "code": "FCT0002",
            "packages": [],
            "title": "Révision Constructeur",
            "type": 2
        },
        {
            "code": "FCT0305",
            "title": "Batterie",
            "type": 0,
            "packages": [
                {
                    "price": 228.0,
                    "reference": "1101XX01FR0301",
                    "title": "Forfait B to B Remplacement batterie",
                    "validity": {
                        "end": "2023-07-04T16:25:50Z",
                        "start": "2025-01-01T16:25:50Z"
                    }
                },
                {
                    "price": 0.0,
                    "reference": "CO110101FR0601",
                    "title": "Forfait Carré Contrôle charge batterie offert.",
                    "validity": {
                        "end": "2023-07-04T16:25:50Z",
                        "start": "2025-01-01T16:25:50Z"
                    }
                }
            ]
        }
    ]
}
```

##### In case of EMEA xFCA
##### Example

```json
{
    "actionType": "appointment",
    "action": "services",
    "vin": "FCASWF01000000005",
    "bookingId": "0065470",
    "bookingLocation": "000"
}
```

##### Output

```json
{
    "services": [
        {
            "code": "FLAG8"
        },
        {
            "code": "FLAG5"
        },
        {
            "code": "FLAG7"
        },
        {
            "code": "FLAG6"
        },
        {
            "code": "FLAG1"
        },
        {
            "code": "FLAG3"
        }
    ]
}
```

##### In case of NAFTA xFCA
##### When mileageUnit input is km then the mileage value is converted into miles
##### Example

```json
{
    "actionType": "appointment",
    "action": "services",
    "vin": "1C6SRFPT8PN3017HT",
    "bookingId": "6414",
    "bookingLocation": "000",
    "mileage": 10,
    "mileageUnit": "km"
}
```

##### Output

```json
{
  "date": "2023-10-16T20:38:30Z",
  "type": "GET",
  "name": "space.middleware.dealer",
  "parameters": {
      "actionType": "appointment",
      "action": "services",
      "vin": "1C6SRFPT8PN3017HT",
      "bookingId": "6414",
      "bookingLocation": "000",
      "mileage": 10,
      "mileageUnit": "km"
  },
  "result": {
        "services": [
            {
                "id": "163157590",
                "price": 0.0,
                "title": "Replace synthetic engine oil and filter [7qt. 0W20]",
                "type": 100.0
            },
            {
                "id": "228965",
                "price": 0.0,
                "title": "Multi-point inspection (according to maintenance interval)",
                "type": 100.0
            },
            {
                "id": "228964",
                "price": 35.6,
                "title": "Rotate tires",
                "type": 100.0
            },
            {
                "id": "61547670",
                "price": 0.0,
                "title": "Inspect CV joints",
                "type": 100.0
            },
            {
                "id": "228798",
                "price": 24.95,
                "title": "Battery Test",
                "type": 101.0
            },
            {
                "id": "228803",
                "price": 0.0,
                "title": "Cooling System Service (Flush and Replace)",
                "type": 101.0
            }
        ]
    },
  "status": "SUCCEEDED"
}
```

##### In case of LATAM xFCA
##### When mileageUnit input is km then the mileage value is converted into miles
##### Example
```json
{
    "actionType": "appointment",
    "action": "services",
    "vin": "1C6SRFHT2PNLATAM7",
    "bookingId": "6414",
    "bookingLocation": "000",
    "mileage": 10,
    "mileageUnit": "km"
}
```

##### Output
```json
{
  "date": "2023-10-20T10:34:04Z",
  "type": "GET",
  "name": "space.middleware.dealer",
  "parameters": {
      "actionType": "appointment",
      "action": "services",
      "vin": "988671173MKN00051",
      "bookingId": "790331",
      "bookingLocation": "000",
      "mileage": 10,
      "mileageUnit": "km"
  },
  "result": {
        "services": [
            {
                "id": "17",
                "title": "ALINHAMENTO E BALANACEAMENTO",
                "type": 101.0
            }
        ]
    },
  "status": "SUCCEEDED"
}
```
