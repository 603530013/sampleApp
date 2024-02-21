# space.middleware.vehicle

## SET

### action-type = add

This api add connected vehicle

##### Input

| Name                      | Type    | Mandatory |
|---------------------------|---------|-----------|
| actionType                | String  | x         |
| vin                       | String  | x         |
| connected                 | Boolean | x         |
| plateNumber               | String  | x         |
| tcRegistrationCountryCode | String  | x         |
| tcRegistrationStatus      | String  | x         |
| tcRegistrationVersion     | String  | x         |
| tcActivationCountryCode   | String  | x         |
| tcActivationStatus        | String  | x         |
| tcActivationVersion       | String  | x         |
| ppActivationCountryCode   | String  | x         |
| ppActivationStatus        | String  | x         |
| ppActivationVersion       | String  | x         |

###### Example

```json
{
"pp": {
"activation": {
"countryCode": "GB",
"status": "AGREE",
"version": "1.0.0"
}
},
"role": "VEHICLE_OWNER",
"tc": {
"registration": {
"countryCode": "GB",
"status": "AGREE",
"version": "1.0.1"
}
}
}
```

##### Output

```json
{
 "result": {
    "make": "FIAT",
    "market": "GB",
    "name": "",
    "picture": "https://lb.assets.fiat.com/vl-picker-service/rest/getPngImage?source=connectivity&consumer=responsive&market=3112&brand=00&model=3320H&mmvs=003324120000&body=278&opt=011,023,026,03E,044,050,051,065,070,07N,0MF,0QU,0R3,112,132,140,198,1CY,1ES,1GD,1H5,1H7,1J2,1J6,228,264,2PZ,316,320,347,392,396,410,412,441,499,4DK,4JA,505,508,511,53Y,5BH,5CE,5DE,5GY,5JK,5M1,5QX,5YY,602,631,6C0,6DC,6HQ,784,78N,78P,7DF,856,8FZ,8H7,947,976,9U7,9UP,9XW,9YT,BCS,BGG,BNP,BNS,BNT,CDW,CFW,CGW,CJ2,CKT,CL5,CSD,JAJ,JJB,JJJ,JLN,JSK,JTM,LAY,LFF,LHA,LMS,LMU,LNJ,MDE,MMF,MRB,MWE,MXU,NHR,RCG,RDG,RF7,RS9,RSF,RTK,RTR,XAN,XEJ,XEV,XGM,XTX&view=EXT&angle=1&width=300&height=300&raiseError=true",
    "regTimeStamp": 1.692978680287E12,
    "sdp": "IGNITE",
    "subMake": "FIAT",
    "type": "ELECTRIC",
    "vin": "ZFAEJ0PP0LPK09347",
    "year": 2022.0
  },
  "status": "SUCCEEDED"
}

```

### action-type = add

This api add non connected vehicle

##### Input

{
"actionType": "add",
"connected": "false",
"vin": "VR7ATTENTKL127249"
}

##### Output

```json
{
  "attributes": [
    "DCD07CD",
    "DCX01CD",
    "DJY02CD",
    "DMW10CD",
    "DRCNNCD",
    "DRE07CD",
    "DVQ64CD",
    "DXD03CD"
  ],
  "eligibility": [
    "electric",
    "device_nac",
    "cea",
    "update_software_nac",
    "update_carto_nac",
    "iAP2",
    "raccess",
    "tmts",
    "remotelev",
    "navcozar",
    "nac"
  ],
  "lastUpdate": "1.693842454E9",
  "lcdv": "1CCESYUNKAB0A030M0VLOXFT",
  "name": "C5 AIRCROSS HYBRID",
  "picture": "https://visuel3d-secure.citroen.com/V3DImage.ashx?client:MyMarque&format:png&color:0MM00NVL&trim:0POX0RFT&back:0&width:1000&version:1CCESYUNKAB0A030&view:001&OPT1:LZ02&OPT2:WLT2",
  "regTimeStamp": "0.0",
  "type": "HYBRID",
  "vin": "VR7ATTENTKL127249"
}
```
