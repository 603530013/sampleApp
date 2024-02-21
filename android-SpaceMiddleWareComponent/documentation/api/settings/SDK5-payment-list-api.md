# space.middleware.settings

## GET

### action-type = payment

### action = list

This api provides the list of payment information.

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |

###### Example

```json
{
    "actionType": "payment",
    "action": "list"
}
```

##### Output

```json
{
    "result": {
        "url": "https://idpcvs.citroen.com/am/oidc/sso?jwt=eyAidHlwIjogIkpXVCIsICJraWQiOiAiU3lsTEM2Tmp0MUtHUWt0RDlNdCswemNlUVNVPSIsICJhbGciOiAiUlMyNTYiIH0.eyAiYXRfaGFzaCI6ICJJNTcyaWVQSzIwS1Vza0hoYUxVSkR3IiwgInN1YiI6ICJBQy1BQ05UMjAwMDA1Njc5OTY1IiwgImlzcyI6ICJodHRwczovL2lkcGN2cy5jaXRyb2VuLmNvbTo0NDMvYW0vb2F1dGgyIiwgInRva2VuTmFtZSI6ICJpZF90b2tlbiIsICJnaXZlbl9uYW1lIjogIlJ3IiwgIm5vbmNlIjogIjRyazdGbENkbTBwZTdFbG1PMHFhdFEiLCAiYXVkIjogWyAiNTM2NGRlZmMtODBlNi00NDdiLWJlYzYtNGFmOGQxNTQyY2FlIiBdLCAiY19oYXNoIjogIjRpZ0E3R0w4eVN1OWY3TXdVZ1BENlEiLCAib3JnLmZvcmdlcm9jay5vcGVuaWRjb25uZWN0Lm9wcyI6ICI2ZjA2YTdiMy00NDRiLTRiMmMtOTg5ZC02ZDQyNWQ2OWM2Y2UiLCAidXBkYXRlZF9hdCI6ICIxNjk4Mjk1OTE1IiwgImF6cCI6ICI1MzY0ZGVmYy04MGU2LTQ0N2ItYmVjNi00YWY4ZDE1NDJjYWUiLCAiYXV0aF90aW1lIjogMTY5ODM5MTQzMSwgInJlYWxtIjogIi9jbGllbnRzQjJDQ2l0cm9lbiIsICJleHAiOiAxNjk4MzkyMDM0LCAidG9rZW5UeXBlIjogIkpXVFRva2VuIiwgImlhdCI6IDE2OTgzOTE0MzQsICJmYW1pbHlfbmFtZSI6ICJSd2UiLCAiZW1haWwiOiAidGVzdF95YW1pbmFAeW9wbWFpbC5jb20iIH0.ZJQWMGJFwTv-TJ8D-5JkgSU3y6xSeiguw3MpI1IMCobBp9LKqU7XpjQO0y8SnexeLC8xs-Dy9lI5eEbmEOm4ugSnU1HVasl6DOtLLtVTLApjo_aofGseVXnDq6u88cOLsllXmJix-e5DsmN72pHhct17DOI-Vi66gHPJpPVNL1M&url=https://services-store.citroen.fr/my-payment-method?expid=frFR"
    },
    "status": "SUCCEEDED"
}
```

## SET

NA
