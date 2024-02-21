# space.middleware.vehicle

## GET

### action-type = details

### type = normal

### action = "refresh" provides the details from api endpoint

### action = "get" provides the details calling from cache

This api provides the details of particular vehicle with normal vin

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| type       | String | x         |
| action     | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "details",
    "type": "normal",
    "action": "get/refresh",
    "vin": "FCASWF01000000005"
}
```

##### Output

```json
{
  "result": {
    "make": "FIAT",
    "market": "IT",
    "name": "King",
    "picture": "https://lb.assets.fiat.com/vl-picker-service/rest/getPngImage?source=connectivity&consumer=responsive&market=1000&brand=00&model=3320C&mmvs=003322210000&body=205&opt=011,023,026,03E,044,050,051,065,070,07N,0MF,0QU,0R3,112,132,140,198,1CY,1ES,1GD,1H5,1H7,1J2,1J6,228,264,2PZ,316,320,347,392,396,410,412,441,499,4DK,4JA,505,508,511,53Y,5BH,5CE,5DE,5GY,5JK,5M1,5QX,5YY,602,631,6C0,6DC,6HQ,784,78N,78P,7DF,856,8FZ,8H7,947,976,9U7,9UP,9XW,9YT,BCS,BGG,BNP,BNS,BNT,CDW,CFW,CGW,CJ2,CKT,CL5,CSD,JAJ,JJB,JJJ,JLN,JSK,JTM,LAY,LFF,LHA,LMS,LMU,LNJ,MDE,MMF,MRB,MWE,MXU,NHR,RCG,RDG,RF7,RO2,RS9,RSF,RTK,RTR,XAN,XEJ,XEV,XGM,XTX&view=EXT&angle=1&width=300&height=300&raiseError=true",
    "regTimeStamp": 1581673706714,
    "sdp": "IGNITE",
    "subMake": "FIAT",
    "type": "ELECTRIC",
    "vin": "FCASWF01000000005",
    "year": 2020,
    "enrollmentStatus" : "COMPLETE",
    "connectorType": "TYPE_1_YAZAKI / TYPE_2_MENNEKES / GBT_PART_2 / TYPE_1_CCS / TYPE_2_CCS / TYPE_3 / CHADEMO / GBT_PART_3 /DOMESTIC_PLUG_GENERIC / NEMA_5_20 / INDUSTRIAL_BLUE / INDUSTRIAL_RED / INDUSTRIAL_WHITE / UNKNOWN"

  },
  "status": "SUCCEEDED"
}
```

### action-type = details

### type = encrypted

This api provides the details of particular vehicle with encrypted vin

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| type       | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "details",
    "type": "encrypted",
    "vin": "LPK11235"
}
```

##### Output

```json
{
    "result": {
        "description": "Renegade PHEV",
        "make": "Jeep",
        "picture": "https://lb.assets.fiat.com/vl-picker-service/rest/getPngImage?source=connectivity&consumer=responsive&market=1000&brand=57&model=6870&mmvs=57687H6V0000&body=268&opt=007,011,023,026,02S,03E,044,050,051,052,07C,07D,097,0AR,0MF,112,132,18N,18P,18R,18U,198,1BZ,1CN,1MP,222,228,264,320,347,392,400,410,412,441,452,4DK,4JA,4UE,505,508,511,53Y,5BH,5CK,5GY,5M1,602,623,631,6C0,6DC,6HQ,7DF,976,989,9UP,9XW,BCS,BGG,BNP,BNS,BNT,CDW,CGW,CJ2,CKT,CL5,CSD,GAM,JJB,JLN,JSK,JTM,LA6,LFF,LHA,LMS,LMU,MDE,MMF,MRB,MXU,NHR,NHS,RCG,RDG,RF7,RS9,RSF,RTK,SJA,UGM,WLA,XAN,XEJ,XEV,XGM,XNM,XNW&view=EXT&angle=1&width=300&height=300&raiseError=true",
        "sdp": "IGNITE",
        "subMake": "Jeep",
        "tcCountryCode": "IT",
        "tcuType": 2,
        "userid": "1a4081ba1dee41618e6a0c39b5f90095",
        "vin": "LPK11235",
        "year": 2022
    },
    "status": "SUCCEEDED"
}
```

### action-type = details

### type = lastCharacters

This api provides the details of particular vehicle with last8Characters vin

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| type       | String | x         |
| vin        | String | x         |

###### Example

```json
{
    "actionType": "details",
    "type": "lastCharacters",
    "vin": "CMM00000000000005"
}
```

##### Output

```json
{
    "result": {
        "description": 679,
        "make": "Jeep",
        "picture": "https://lb.assets.fiat.com/vl-picker-service/rest/getPngImage?source=connectivity&consumer=responsive&market=3112&brand=57&model=6094&mmvs=57609P224000&body=888&opt=1Q0,4N5,9S8,9YC,AB7,BCS,BCZ,BGE,BHC,BHD,BHG,BNB,BNG,BNP,BNS,BPT,BRG,CAB,CAJ,CAK,CAY,CBD,CBG,CG6,CGD,CGU,CGW,CGY,CJ1,CJ5,CJF,CKT,CSD,CSH,CSM,CSN,CSR,CU7,CUD,CUF,CVB,CXE,CYD,DFQ,DME,ECX,GAK,GAM,GAQ,GFA,GNC,GNK,GU4,GWJ,GX4,GXD,GXT,HAF,JAL,JDJ,JFH,JFJ,JHA,JHB,JHC,JJA,JJB,JJJ,JJM,JKA,JKD,JKJ,JKP,JLP,JLW,JMB,JPM,JPT,JPZ,JRC,JRN,JSB,JSE,LA2,LAC,LAS,LAU,LAX,LAY,LAZ,LBA,LBC,LBH,LCJ,LDB,LEB,LEC,LEE,LEP,LER,LES,LFN,LHD,LHL,LMG,LMS,LMZ,LNJ,LP1,LP5,LPG,LSB,LSE,LSU,LTM,MCA,MCD,MDX,MFP,MGG,MGU,MJB,MMH,MNF,MNK,MNT,MVC,MVK,MVL,MVM,MYV,NA1,NBQ,NEL,NFS,NHA,NHB,NHJ,NHS,NHZ,NZE,RCG,RDG,RDZ,RF7,RFL,RFP,RFR,RHC,RS4,RSU,RTF,RTZ,SBL,SCC,SDF,SUD,TBF,TWS,UKM,WK1,WLZ,XAC,XAT,XBM,XC4,XDQ,XEV,XFE,XGA,XGM,XGR,XH5,XJA,XJM,XK9,XLC,XNS,XRB,XSK,XWT,XXN,XXT,XZ2,Z1A&view=EXT&angle=1&width=300&height=300&raiseError=true",
        "sdp": "IGNITE",
        "subMake": "Jeep",
        "tcCountryCode": "GB",
        "tcuType": 2,
        "userid": "db83f3a6b0874afea85b4d1e789533ff",
        "vin": "CMM00000000000005",
        "year": 2019
    },
    "status": "SUCCEEDED"
}
```
