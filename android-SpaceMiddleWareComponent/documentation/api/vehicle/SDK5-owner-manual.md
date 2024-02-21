# space.middleware.vehicle

## GET

### action-type = manual

This returns Owner Manual Data

#### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| vin        | Stirng | x         |

##### Example

```json
{
    "actionType": "manual",
    "vin": "VR7ATTENTKL127249"
}
```

#### Output

```json
{
    "type": "web/sdk/pdf",
    "url": "https://public-servicebox.opel.com/OVddb/OV/index.html?VR7ATTENTKL127249"
}
```

##### Example

###### Brand = Peugeot/Citroen/DS

```json
{
    "type": "sdk"
}
```

###### Brand = Opel or Vauxhell

```json
{
    "type": "web",
    "url": "https://public-servicebox.opel.com/OVddb/OV/index.html?VR7ATTENTKL127249"
}
```

###### Brand = Maserati

```json
{
    "error": {
        "code": 2002,
        "label": "API not supported"
    }
}

```

###### Region = NAFTA and country = US

```json
{
    "type": "pdf",
    "url": "https://public-servicebox.opel.com/OVddb/OV/index.pdf"
}
```

###### Region = EMEA/LATAM/NAFTA

```json
{
    "type": "web",
    "url": "https://public-servicebox.opel.com/OVddb/OV/index.html?VR7ATTENTKL127249"
}
```
