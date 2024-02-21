# space.middleware.user

## SET

### action-type = delete

This API allow to deactivate the user account.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |

###### Example

```json
{
    "actionType": "delete"
}
```

##### Output

```json
{
    "result": "{}",
    "status": "SUCCEEDED"
}
```
