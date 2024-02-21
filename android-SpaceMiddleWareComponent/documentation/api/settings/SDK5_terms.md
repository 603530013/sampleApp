# space.middleware.settings

## GET

### action-type = appTerms

This api provides the information about the app terms URL and the last update date for app terms content change.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| sdp        | String | x         |

###### Example

```json
{
    "actionType": "appTerms",
    "sdp": "IGNITE"
}
```

##### Output

```json
{
    "url": "https://microservices.mym.awsmpsa.com/cms/webview/cgu?brand=AC&source=Middleware&v=1.2.1&site_code=AC_FR_ESP&culture=fr_FR&language=fr&os=and",
    "content": "",
    "country": "GB",
    "language": "en",
    "update": "2023-07-19T14:01:48.464Z",
    "version": "1.0.0"
}
```

### action-type = connectedTC

This api provides the information about the connected vehicle terms.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| country    | String | x         |
| sdp        | String | x         |

###### Example

```json
{
    "actionType": "connectedTC",
    "country": "IT",
    "sdp": "IGNITE"
}
```

##### Output

```json
{
    "url": "https://microservices.mym.awsmpsa.com/cms/webview/cgu?brand=AC&source=Middleware&v=1.2.1&site_code=AC_FR_ESP&culture=fr_FR&language=fr&os=and",
    "content": "",
    "country": "IT",
    "language": "it",
    "update": "2023-07-19T14:01:48.464Z",
    "version": "1.0.0"
}
```

### action-type = connectedPP

This api provides the information about the connected vehicle privacy.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| country    | String | x         |
| sdp        | String | x         |

###### Example

```json
{
    "actionType": "connectedPP",
    "country": "IT",
    "sdp": "IGNITE"
}
```

##### Output

```json
{
    "url": "https://microservices.mym.awsmpsa.com/cms/webview/cgu?brand=AC&source=Middleware&v=1.2.1&site_code=AC_FR_ESP&culture=fr_FR&language=fr&os=and",
    "content": "",
    "country": "IT",
    "language": "it",
    "update": "2023-07-19T14:01:48.464Z",
    "version": "1.0.0"
}
```

### action-type = privacy

This api provides the information about the user privacy.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| sdp        | String | x         |

###### Example

```json
{
    "actionType": "privacy",
    "sdp": "IGNITE"
}
```

##### Output

```json
{
    "url": "https://microservices.mym.awsmpsa.com/cms/webview/cgu?brand=AC&source=Middleware&v=1.2.1&site_code=AC_FR_ESP&culture=fr_FR&language=fr&os=and",
    "content": "",
    "country": "IT",
    "language": "it",
    "update": "2023-07-19T14:01:48.464Z",
    "version": "1.0.0"
}
```

### action-type = webTerms

This api provides the information about the web terms.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| sdp        | String | x         |

###### Example

```json
{
    "actionType": "webTerms",
    "sdp": "IGNITE"
}
```

##### Output

```json
{
    "url": "https://microservices.mym.awsmpsa.com/cms/webview/cgu?brand=AC&source=Middleware&v=1.2.1&site_code=AC_FR_ESP&culture=fr_FR&language=fr&os=and",
    "content": "",
    "country": "IT",
    "language": "it",
    "update": "2023-07-19T14:01:48.464Z",
    "version": "1.0.0"
}
```

## SET

NA
