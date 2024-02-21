# space.middleware.dealer

## SET

### action-type = favorite

### action = add

This api sets the favorite dealer

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| site_geo   | String | x         |

###### Example

```json
{
    "actionType": "favorite",
    "action": "add",
    "id": "0000039622"
}
```

##### Output

```json
{
    "result": "{}",
    "status": "SUCCEEDED"
}
```

### action-type = favorite

### action = remove

This api removes the favorite dealer

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |

###### Example

```json
{
    "actionType": "favorite",
    "action": "remove"
}
```

##### Output

```json
{
    "result": "{}",
    "status": "SUCCEEDED"
}
```

## GET

### action-type = favorite

### action = "refresh" provides the favorite dealers calling from api endpoint

### action = "get" provides the favorite dealers calling from cache

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| vin        | Stirng | x         |

###### Example

```json
{
    "actionType": "favorite",
    "action": "refresh",
    "vin": "FCASWF01000000005"
}
```

##### Output

```json
{
  "result": {
    "dealers": [
      {
        "address": "ROUTE DE NEUILLY, CHAUMONT, FRANCIA, 52000",
        "bookable": true,
        "bookingId": 20213,
        "bookingLocation": 0,
        "id": "0020213|000",
        "latitude": 48.08645,
        "longitude": 5.14675,
        "name": "LINGON GARAGE CHAUMONT",
        "phones": {
          "DEFAULT": 325353590
        },
        "preferred": true
      }
    ]
  },
  "status": "SUCCEEDED"
}
```
