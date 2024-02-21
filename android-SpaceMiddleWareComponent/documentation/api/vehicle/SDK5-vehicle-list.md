# space.middleware.vehicle

## GET

### action-type = list

### action = "refresh" provides the list of vehicle contracts from api endpoint

### action = "get" provides the list of vehicle contracts coming from cache

This api provides the list of vehicles

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |

###### Example

```json
{
    "actionType": "list",
   "action": "get/refresh"

}
```

##### Output

```json
{
  "result": {
    "vehicles": [
      {
        "modelDescription": "NEW 500 EME",
        "picture": "https://lb.assets.fiat.com/vl-picker-service/rest/getPngImage?source=connectivity&consumer=responsive&market=1000&brand=00&model=3320C&mmvs=003322210000&body=205&opt=011,023,026,03E,044,050,051,065,070,07N,0MF,0QU,0R3,112,132,140,198,1CY,1ES,1GD,1H5,1H7,1J2,1J6,228,264,2PZ,316,320,347,392,396,410,412,441,499,4DK,4JA,505,508,511,53Y,5BH,5CE,5DE,5GY,5JK,5M1,5QX,5YY,602,631,6C0,6DC,6HQ,784,78N,78P,7DF,856,8FZ,8H7,947,976,9U7,9UP,9XW,9YT,BCS,BGG,BNP,BNS,BNT,CDW,CFW,CGW,CJ2,CKT,CL5,CSD,JAJ,JJB,JJJ,JLN,JSK,JTM,LAY,LFF,LHA,LMS,LMU,LNJ,MDE,MMF,MRB,MWE,MXU,NHR,RCG,RDG,RF7,RO2,RS9,RSF,RTK,RTR,XAN,XEJ,XEV,XGM,XTX&view=EXT&angle=1&width=300&height=300&raiseError=true",
        "shortLabel": "King",
        "vin": "FCASWF01000000005"
      }
    ]
  },
  "status": "SUCCEEDED"
}
```
