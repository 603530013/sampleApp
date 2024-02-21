# space.middleware.user

## GET

### action-type = profile

### action = "refresh" provides the user profile from api endpoint

### action = "get" provides the user profile calling from cache

This api provides the user profile

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |

###### Example

```json
{
    "actionType": "profile",
    "action": "get/refresh"
}
```

##### Output

```json
{
    "result": {
        "uid": "9c7bb26987be416cbe4d180804f360d2",
        "firstName": "TestFirstName",
        "lastName": "TestAccount",
        "country": "IT",
        "email": "fcaswfactory01@mailinator.com",
        "civility": "MISS",
        "civilityCode": "MISS",
        "phones": {},
        "address1": "test address",
        "address2": "test address",
        "zipCode": "75001",
        "city": "Paris",
        "country": "France",
        "image": "image-test-link"
    },
    "status": "SUCCEEDED"
}
```

## SET for PSA only

### action-type = profile

## For FCA NA

This api add the user profile and save the data in local cache

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| email      | String | x         |
| first_name | String | x         |
| last_name  | String | x         |
| civility   | String | x         |
| address1   | String | x         |
| address2   | String | x         |
| zip_code   | String | x         |
| city       | String | x         |
| country    | String | x         |
| phone      | String | x         |
| mobile     | String | x         |
| mobile_pro | String | x         |

###### Example

```json
{
    "actionType": "profile",
    "profile": {
        "email": "Test@yopmail.com",
        "first_name": "Test",
        "last_name": "Testss",
        "civility": "MISS",
        "address1": "Paris",
        "address2": "Paris",
        "zip_code": "00664790",
        "city": "FR",
        "country": "FR",
        "phone": "8123069041",
        "mobile": "8123069041",
        "mobile_pro": "8123069041"
    }
}
```

##### Output

```json
{
    "status": "Successfully Updated"
}
```
