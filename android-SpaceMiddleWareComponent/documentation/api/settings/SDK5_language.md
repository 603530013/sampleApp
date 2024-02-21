# space.middleware.settings

## GET

### action-type = language

#### action = list

This api provides the information about the list of supported languages/country.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |

###### Example

```json
{
    "actionType": "language",
    "action": "list"
}
```

##### Output

```json
{
    "languages": [
        "en-IE",
        "nb-NO",
        "hr-HR",
        "en-IN",
        "en-AE",
        "fr-TN",
        "ar-AE",
        "sl-SI",
        "nl-BE",
        "en-IS",
        "de-AT",
        "el-GR",
        "nl-NL",
        "fr-MQ",
        "fr-CH",
        "he-IL",
        "cs-CZ",
        "de-CH",
        "ja-JP",
        "de-DE",
        "hu-HU",
        "fr-MA",
        "pt-BR",
        "fr-DZ",
        "es-UY",
        "sk-SK",
        "es-ES",
        "es-CL",
        "es-CO",
        "it-IT",
        "fa-IR",
        "pl-PL",
        "ru-RU",
        "pt-PT",
        "es-AR",
        "ro-RO",
        "fr-BE",
        "sv-SE",
        "it-CH",
        "da-DK",
        "tr-TR",
        "fr-LU",
        "fr-FR",
        "en-GB"
    ]
}
```

### action-type = language

#### action = current

This api provides the currently selected languages/country.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |

###### Example

```json
{
  "actionType": "language",
  "action": "current"
}
```

##### Output

```json
{
    "language": "en-GB"
}
```

## SET

### action-type = language

This api help configure the languages/country.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| language   | String | x         |

###### Example

```json
{
    "actionType": "language",
    "language": "en-GB"
}
```

##### Output

```json
{}
```
