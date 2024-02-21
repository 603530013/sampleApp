# space.middleware.eservices

## GET

### action-type = chargingStation

#### action = list

This API allows to locate the nearest charging stations to ensure convenient access to charging facilities.

##### Input

| Name       | Type             | Mandatory |
|------------|------------------|-----------|
| actionType | String           | x         |
| action     | String           | x         |
| vin        | String           | x         |
| lat        | Double           | x         |
| lon        | Double           | x         |
| filters    | Map<String, Any> | x         |

###### Example

```json
{
    "actionType": "chargingStation",
    "action": "list",
    "vin": "TESTOPSP0LPK11565",
    "latitude": 43.5,
    "longitude": 5.3,
    "filters": {
        "onlyAvailable": true,
        "chargingCableAttached": true,
        "free": true,
        "indoor": true,
        "open24Hours": true,
        "access": [
            "APP"
        ],
        "powerTypes": [
            "REGULAR_CHARGE"
        ],
        "connectorTypes": [
            "TYPE_2_CCS"
        ]
    }
}
```

##### Output EMEA

```json
{
    "chargingStations": [
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via privata Anton Francesco Grazzini 14, 20158, Milano",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 0.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 1.0,
                            "regularCharge": 0.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Fast",
                                "powerKw": 50.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "CHADEMO"
                }
            ],
            "id": "71e13b0b-54f6-4635-8ad9-f7ab9c77233d-IT*F2M*E28920449*1002",
            "link": "https://publiccpdetail?id=IT*F2M*E28920449*1002&from=GMA",
            "name": "f2mes",
            "openHours": [],
            "position": {
                "latitude": 45.506,
                "longitude": 9.16
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+39800920155",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "removed"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via privata Anton Francesco Grazzini 14, 20158, Milano",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 0.0
                    },
                    "compatible": true,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 1.0,
                            "regularCharge": 0.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Fast",
                                "powerKw": 50.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_CCS"
                }
            ],
            "id": "71e13b0b-54f6-4635-8ad9-f7ab9c77233d-IT*F2M*E28920449*1004",
            "link": "https://publiccpdetail?id=IT*F2M*E28920449*1004&from=GMA",
            "name": "f2mes",
            "openHours": [],
            "position": {
                "latitude": 45.506,
                "longitude": 9.16
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+39800920155",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "removed"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "4a, 20141, Milano MI",
            "connectors": [
                {
                    "availability": {
                        "available": 1.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 0.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 0.0,
                            "unknown": 1.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Unknown",
                                "powerKw": 0.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                }
            ],
            "id": "cfa4eaf4-449f-4f62-8091-e776250d9326-CH*ECUE4F4ADQN24LXVWRHHMY52BKNVWX",
            "link": "https://publiccpdetail?id=CH*ECUE4F4ADQN24LXVWRHHMY52BKNVWX&from=GMA",
            "name": "eCarUp AG",
            "openHours": [],
            "position": {
                "latitude": 45.4373545,
                "longitude": 9.1769745
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41415101717",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "available"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "4a, 20141, Milano MI",
            "connectors": [
                {
                    "availability": {
                        "available": 1.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 0.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 0.0,
                            "unknown": 1.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Unknown",
                                "powerKw": 0.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                }
            ],
            "id": "cfa4eaf4-449f-4f62-8091-e776250d9326-CH*ECUE86DBXKRWEMKDDG4DACQ54PACMQ",
            "link": "https://publiccpdetail?id=CH*ECUE86DBXKRWEMKDDG4DACQ54PACMQ&from=GMA",
            "name": "eCarUp AG",
            "openHours": [],
            "position": {
                "latitude": 45.4373545,
                "longitude": 9.1769745
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41415101717",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "available"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via Achille Grandi 12, 20090, Caleppio (MI)",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 0.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                }
            ],
            "id": "a231b27e-de51-45c7-9784-ed8d29660e6e-IT*F2M*E18A4030A*1001",
            "link": "https://publiccpdetail?id=IT*F2M*E18A4030A*1001&from=GMA",
            "name": "f2mes",
            "openHours": [],
            "position": {
                "latitude": 45.444213,
                "longitude": 9.377045
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+39800920155",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "removed"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via Carlo Cattaneo, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                }
            ],
            "id": "58d3472c-ef95-43c1-a78e-faa73b39e2b1-CHEVPE37244",
            "link": "https://publiccpdetail?id=CHEVPE37244&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.83143,
                "longitude": 9.02681
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via Carlo Cattaneo, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                }
            ],
            "id": "58d3472c-ef95-43c1-a78e-faa73b39e2b1-CHEVPE37245",
            "link": "https://publiccpdetail?id=CHEVPE37245&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.83143,
                "longitude": 9.02681
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Corso San Gottardo 16, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 0.0,
                            "unknown": 1.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Unknown",
                                "powerKw": 3.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "DOMESTIC_PLUG_GENERIC"
                }
            ],
            "id": "2a8c2d0f-51a7-46d3-918d-264185dd2219-CHEVPE36911",
            "link": "https://publiccpdetail?id=CHEVPE36911&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.8335,
                "longitude": 9.031
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via degli Albrici 1, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                }
            ],
            "id": "51282ca9-a487-4e45-82a4-79a33a79ed56-CHEVPE2081",
            "link": "https://publiccpdetail?id=CHEVPE2081&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.834082,
                "longitude": 9.033676999999999
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via degli Albrici 1, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                }
            ],
            "id": "51282ca9-a487-4e45-82a4-79a33a79ed56-CHEVPE2082",
            "link": "https://publiccpdetail?id=CHEVPE2082&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.834082,
                "longitude": 9.033676999999999
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via degli Albrici 1, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                }
            ],
            "id": "51282ca9-a487-4e45-82a4-79a33a79ed56-CHEVPE2080",
            "link": "https://publiccpdetail?id=CHEVPE2080&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.834082,
                "longitude": 9.033676999999999
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via degli Albrici 1, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                }
            ],
            "id": "51282ca9-a487-4e45-82a4-79a33a79ed56-CHEVPE2083",
            "link": "https://publiccpdetail?id=CHEVPE2083&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.834082,
                "longitude": 9.033676999999999
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via degli Albrici 1, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                }
            ],
            "id": "51282ca9-a487-4e45-82a4-79a33a79ed56-CHEVPE2078",
            "link": "https://publiccpdetail?id=CHEVPE2078&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.834082,
                "longitude": 9.033676999999999
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via degli Albrici 1, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                }
            ],
            "id": "51282ca9-a487-4e45-82a4-79a33a79ed56-CHEVPE2076",
            "link": "https://publiccpdetail?id=CHEVPE2076&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.834082,
                "longitude": 9.033676999999999
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "Via ai Crotti, 6830, Chiasso",
            "connectors": [
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 7.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_1_YAZAKI"
                },
                {
                    "availability": {
                        "available": 0.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 1.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 1.0,
                            "unknown": 0.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Regular",
                                "powerKw": 22.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                }
            ],
            "id": "18ec9255-756b-42e7-9e0c-9c400ef105c2-CHEVPE36968",
            "link": "https://publiccpdetail?id=CHEVPE36968&from=GMA",
            "name": "Green Motion SA",
            "openHours": [],
            "position": {
                "latitude": 45.83256,
                "longitude": 9.0258
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41582219660",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "unknown"
                }
            }
        },
        {
            "acceptablePayments": [
                "ELECTRONIC_PURSE",
                "ELECTRONIC_TOLL_COLLECTION",
                "SERVICE_PROVIDER_PAYMENT_METHOD",
                "FUEL_CARD"
            ],
            "address": "79, 20154, Milano MI",
            "connectors": [
                {
                    "availability": {
                        "available": 1.0,
                        "occupied": 0.0,
                        "outOfService": 0.0,
                        "reserved": 0.0,
                        "unknown": 0.0
                    },
                    "compatible": false,
                    "powerLevel": {
                        "chargeTypeAvailability": {
                            "fastCharge": 0.0,
                            "regularCharge": 0.0,
                            "unknown": 1.0
                        },
                        "chargingCapacities": [
                            {
                                "chargingMode": "Unknown",
                                "powerKw": 0.0
                            }
                        ]
                    },
                    "total": 1.0,
                    "type": "TYPE_2_MENNEKES"
                }
            ],
            "id": "c659d8d5-a93f-4b05-b550-5fa893a210de-CH*ECUETRT4Y4VD6U8ZSS2SKAFP3STZRS",
            "link": "https://publiccpdetail?id=CH*ECUETRT4Y4VD6U8ZSS2SKAFP3STZRS&from=GMA",
            "name": "eCarUp AG",
            "openHours": [],
            "position": {
                "latitude": 45.4870385,
                "longitude": 9.158268
            },
            "providers": {
                "f2m": {
                    "access": [
                        "noAuthentication"
                    ],
                    "canBeReserved": false,
                    "hotline": "+41415101717",
                    "indoor": false,
                    "open24Hours": true,
                    "renewableEnergy": false,
                    "status": "available"
                }
            }
        }
    ]
}
```
