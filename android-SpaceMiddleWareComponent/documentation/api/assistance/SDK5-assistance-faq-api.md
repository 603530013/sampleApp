# space.middleware.assistance

## GET

### action-type = faq

This api provides the FAQ information

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |

###### Example

```json
{
    "actionType": "faq"
}
```

##### Output

```json
{
    "result": {
        "url": "https://citroen.my-customerportal.com/citroen/s/?language=fr",
        "phone": "09 69 39 18 18",
        "email": "test@faq.com"
    },
    "status": "SUCCEEDED"
}
```
