# space.middleware.vehicle

## GET

### action-type = services

### action = "refresh" provides the services list from api endpoint

### action = "get" provides the services list coming from cache

This api provides the list of vehicle services detailed information

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| vin        | String | x         |
| schema     | String |           |

###### Example

```json
{
    "actionType": "services",
    "action": "get/refresh",
    "vin": "VR7ATTENTKL127249",
    "schema": "test"
}
```

##### Output

```json
{
    "result": {
        "services": [
            {
                "category": "connected_services",
                "currency": "EUR",
                "description": "Voyagez en toute tranquillité avec l’information Trafic en temps réel by TomTom Services ainsi que des alertes à l’approche des zones de danger présentes sur vos trajets.",
                "id": "navcozar",
                "offer": {
                    "fromPrice": 0.0,
                    "isFreeTrial": 0.0,
                    "price": {
                        "currency": "EUR",
                        "periodType": "Month",
                        "price": 9.9,
                        "typeDiscount": "FlatFee"
                    },
                    "pricingModel": "Periodical"
                },
                "price": 109.0,
                "title": "Pack Navigation connectée",
                "url": "https://services-store.peugeot.fr/login-redirect?xcsrf=EMd1pYrPX54T3Y6Da/BViJCqP+SPV819Bth94PFLMZmJTjZowfRy+DIzWuanpPu5uaqbf9haMB+2\n3fnc8kUImg==\n&jwt=c59df522-d236-4b7b-a292-c9b9b019c1fc&inboundApplication=testing&redirect-url=https://services-store.peugeot.fr/store/pack-navigation-connectee-0"
            }
        ]
    },
    "status": "SUCCEEDED"
}
```
