# space.middleware.dealer

## GET

### action-type = list

This api provides the list of dealers

##### Input

| Name       | Type    | Mandatory |
|------------|---------|-----------|
| actionType | String  | x         |
| latitude   | Double  | x         |
| longitude  | Double  | x         |
| vin        | String  | x         |
| limit      | Integer |           |

###### Example

```json
{
    "actionType": "list",
    "latitude": 44.0,
    "longitude": 5.0,
    "vin": "VR7ATTENTKL127249",
    "limit": 10
}
```

##### Output

```json
{
    "dealers": [
        {
            "address": "33 RUE DE REUILLY\n75012 PARIS",
            "emails": {
                "APV": "ggedufaubourg@wanadoo.fr",
                "DEFAULT": "ggedufaubourg@wanadoo.fr"
            },
            "id": "0000040609",
            "latitude": "48.8473358",
            "longitude": "2.38679576",
            "name": "GARAGE DU FAUBOURG",
            "preferred": true,
            "bookable": false,
            "openingHours": {
                "GENERAL": {
                    "MONDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "TUESDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "WEDNESDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "THURSDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "FRIDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "SATURDAY": {
                        "closed": true
                    },
                    "SUNDAY": {
                        "closed": true
                    }
                },
                "APV": {
                    "MONDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "TUESDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "WEDNESDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "THURSDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "FRIDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "SATURDAY": {
                        "closed": true
                    },
                    "SUNDAY": {
                        "closed": true
                    }
                },
                "VN": {
                    "MONDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "TUESDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "WEDNESDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "THURSDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "FRIDAY": {
                        "closed": false,
                        "time": [
                            "07:30-12:00",
                            "13:30-19:00"
                        ]
                    },
                    "SATURDAY": {
                        "closed": true
                    },
                    "SUNDAY": {
                        "closed": true
                    }
                }
            },
            "phones": {
                "DEFAULT": "01 43 72 70 76"
            },
            "website": "reseau.citroen.fr/reparateur-paris-03"
        }
    ]
}
```
