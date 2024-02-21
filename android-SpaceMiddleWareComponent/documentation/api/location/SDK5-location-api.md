# spacce.middleware.location

## GET

### action-type = textSearch

This api provides the location as per user textSearch

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| keyword    | String | x         |

###### Example

```json
{
    "actionType": "textSearch",
    "keyword": "text"
}
```

##### Output

```json
{
  "result": {
    "places": [
      {
        "formatted_address": "Milan",
        "geometry": {
          "location": {
            "lat": 45.4642035,
            "lng": 9.189982
          },
          "viewport": {
            "northeast": {
              "lat": 45.535689,
              "lng": 9.2903463
            },
            "southwest": {
              "lat": 45.3897787,
              "lng": 9.065118199999999
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
        "name": "Milan",
        "place_id": "ChIJ53USP0nBhkcRjQ50xhPN_zw",
        "rating": 0,
        "types": [
          "locality",
          "political"
        ],
        "user_ratings_total": 0
      }
    ]
  },
  "status": "SUCCEEDED"
}

```

## GET

### action-type = directionsRoute

This api provides the direction route

##### Input

| Name                | Type   | Mandatory |
|---------------------|--------|-----------|
| actionType          | String | x         |
| origin-latitude     | Double | x         |
| origin-longitude    | Double | x         |
| direction-latitude  | Double | x         |
| direction-longitude | Double | x         |

###### Example

```json
{
    "actionType": "directionsRoute",
    "origin-latitude": "46.2640959",
    "origin-longitude" : "10.5621413",
    "direction-latitude" : "46",
    "direction-longitude": "10"
}

```

##### Output

```json
{
  "result": {
    "directions": [
      {
        "bounds": {
          "northeast": {
            "lat": 46.2640959,
            "lng": 10.5621413
          },
          "southwest": {
            "lat": 45.47995299999999,
            "lng": 9.163639900000002
          }
        },
        "copyrights": "Map data ©2023 Google",
        "legs": [
          {
            "distance": {
              "text": "176 km",
              "value": 175882.0
            },
            "duration": {
              "text": "2 ore 54 min",
              "value": 10428.0
            },
            "end_address": "SS 42, 25056 Ponte di legno BS, Italia",
            "end_location": {
              "lat": 46.2571169,
              "lng": 10.5621413
            },
            "start_address": "Via Bramante, 16, 20154 Milano MI, Italia",
            "start_location": {
              "lat": 45.47995299999999,
              "lng": 9.179746699999999
            },
            "steps": [
              {
                "distance": {
                  "text": "0,5 km",
                  "value": 501.0
                },
                "duration": {
                  "text": "2 min",
                  "value": 118.0
                },
                "end_location": {
                  "lat": 45.4841079,
                  "lng": 9.177392
                },
                "html_instructions": "Procedi in direzione <b>nord</b> da <b>Via Bramante</b> verso <b>Via Giuseppe Giusti</b>",
                "polyline": {
                  "points": "uxqtGm|_w@aAVk@PcA^wAh@E@KDw@\\eBn@_Bl@a@NsAd@UJGFsBtB]^"
                },
                "start_location": {
                  "lat": 45.47995299999999,
                  "lng": 9.179746699999999
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,4 km",
                  "value": 440.0
                },
                "duration": {
                  "text": "2 min",
                  "value": 98.0
                },
                "end_location": {
                  "lat": 45.4856013,
                  "lng": 9.1820059
                },
                "html_instructions": "Alla rotonda prendi la <b>1ª</b> uscita e prendi <b>Piazzale Cimitero Monumentale</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "urrtGum_w@CKISEKCGCCACFm@DYFy@@K?M@O@G?I?U?K?MAo@AQSeCMw@Qe@EKCKEKISGOIMCEKSEGKKKKGEKIQOSQEC[SWOIGCACCCCCECEAE?AAKAE?C?E?E@Y"
                },
                "start_location": {
                  "lat": 45.4841079,
                  "lng": 9.177392
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,3 km",
                  "value": 296.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 55.0
                },
                "end_location": {
                  "lat": 45.4881392,
                  "lng": 9.1825301
                },
                "html_instructions": "Svolta a <b>sinistra</b> e prendi <b>Via Carlo Farini</b>",
                "maneuver": "turn-left",
                "polyline": {
                  "points": "_|rtGqj`w@?I?I?Ik@CeAKo@EKAYAa@Eu@GEAUAYCm@Ew@KK?KAg@E"
                },
                "start_location": {
                  "lat": 45.4856013,
                  "lng": 9.1820059
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,3 km",
                  "value": 292.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 41.0
                },
                "end_location": {
                  "lat": 45.48922169999999,
                  "lng": 9.1857869
                },
                "html_instructions": "Svolta leggermente a <b>destra</b> e prendi <b>Via Ugo Bassi</b>",
                "maneuver": "turn-slight-right",
                "polyline": {
                  "points": "{kstGym`w@_@WGGEEIEECCGCGAKIa@CKKm@GYGYKg@[gBKe@UmASmAI]@_A"
                },
                "start_location": {
                  "lat": 45.4881392,
                  "lng": 9.1825301
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,3 km",
                  "value": 332.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 67.0
                },
                "end_location": {
                  "lat": 45.4914043,
                  "lng": 9.1839818
                },
                "html_instructions": "Alla <b>Piazza Fidia</b> prendi la <b>5ª</b> uscita e prendi <b>Via Medardo Rosso</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "srstGebaw@?A@A?A?A?A?A@A?C?A?A?AAA?A?A?AAC?AAA?AAA?AA?AA?AA?AAAAA?A?AAA?A?A?A@A?A?A@A@A??@A@?@A??@A@?@A@?@?@A@?@?B?@?@?@?@?@?@?B?@@@?@?@@@?@?@QRkArAw@z@c@f@QPYZIJ[\\e@d@YZA@A?E@C?EAC?UKA?C?A?A?A?A@?D"
                },
                "start_location": {
                  "lat": 45.48922169999999,
                  "lng": 9.1857869
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,7 km",
                  "value": 699.0
                },
                "duration": {
                  "text": "3 min",
                  "value": 166.0
                },
                "end_location": {
                  "lat": 45.4974178,
                  "lng": 9.1865724
                },
                "html_instructions": "Svolta a <b>destra</b> e prendi <b>Via Carlo Farini</b>",
                "maneuver": "turn-right",
                "polyline": {
                  "points": "g`ttG{v`w@KEuBq@cF{AmBm@gCy@QGKGOGc@QyAc@iA]ICy@[QCSGa@OMG_DeAOGWK"
                },
                "start_location": {
                  "lat": 45.4914043,
                  "lng": 9.1839818
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,5 km",
                  "value": 488.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 89.0
                },
                "end_location": {
                  "lat": 45.50162479999999,
                  "lng": 9.188328199999999
                },
                "html_instructions": "Continua su <b>Via Valassina</b>",
                "polyline": {
                  "points": "{eutGagaw@WKUG_ASs@Sc@MuC}@i@Q[KMEeDeAQEQIMGMEGCKAsAe@EAIESIe@Q"
                },
                "start_location": {
                  "lat": 45.4974178,
                  "lng": 9.1865724
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "2,4 km",
                  "value": 2439.0
                },
                "duration": {
                  "text": "3 min",
                  "value": 185.0
                },
                "end_location": {
                  "lat": 45.5209828,
                  "lng": 9.175351
                },
                "html_instructions": "Svolta a <b>sinistra</b> e prendi <b>Viale Enrico Fermi</b>",
                "maneuver": "turn-left",
                "polyline": {
                  "points": "c`vtGaraw@a@b@o@r@aAfAs@x@w@z@{@~@e@h@kBrBWXuA|Au@x@y@~@[\\MLEFGFGHQPGHWVA?WXABsAvACB[\\CDUT_@`@YZk@n@}@bAk@l@SRUTED]VQLSNa@TIBQHYJ_@JKDSFeAPeAVMBw@PqBb@u@Pa@JsCl@yBf@iDr@MDIBuBd@i@LQDaATi@JMBiDp@M@I@UDg@JUDa@HWFSDk@JeARm@JWFo@Li@Ls@LUFUHSFUJUJy@`@QJe@ZOJSPe@`@s@v@]b@U\\Y`@U`@?@cB`DINKV"
                },
                "start_location": {
                  "lat": 45.50162479999999,
                  "lng": 9.188328199999999
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,9 km",
                  "value": 949.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 54.0
                },
                "end_location": {
                  "lat": 45.5271485,
                  "lng": 9.1669348
                },
                "html_instructions": "Continua su <b>Viale Rubicone</b>",
                "polyline": {
                  "points": "cyytG}`_w@eAnBAB_@n@QZKRe@v@GJILIL_@j@GHMPCFCDA@MNCFIJILMPORc@p@KL]f@KLKPQV{EdHs@dAs@dAo@z@EFUZCDABUXy@jAKNs@dAeBhCABY`@c@n@QTo@`A"
                },
                "start_location": {
                  "lat": 45.5209828,
                  "lng": 9.175351
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,5 km",
                  "value": 510.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 27.0
                },
                "end_location": {
                  "lat": 45.5309378,
                  "lng": 9.1635335
                },
                "html_instructions": "Mantieni la <b>sinistra</b> per restare su <b>Viale Rubicone</b>",
                "maneuver": "keep-left",
                "polyline": {
                  "points": "u_{tGil}v@y@lAaAvAOTiA`BA@A@W^Yb@MNKNSXKPMN[^GHKJMLGDKHMJQLMFGBMHQHOF_@NYH]HsAXG@u@N"
                },
                "start_location": {
                  "lat": 45.5271485,
                  "lng": 9.1669348
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "42,4 km",
                  "value": 42447.0
                },
                "duration": {
                  "text": "27 min",
                  "value": 1634.0
                },
                "end_location": {
                  "lat": 45.6657192,
                  "lng": 9.659830699999999
                },
                "html_instructions": "Svolta leggermente a <b>destra</b> per entrare nella <b>A4</b>/<wbr/><b>E64</b> verso <b>Venezia</b><div style=\"font-size:0.9em\">Strada a pedaggio</div>",
                "maneuver": "ramp-right",
                "polyline": {
                  "points": "kw{tGaw|v@c@SA?A?MAM?GAI?GACAKECAa@Wk@_@}AgAeAu@KGOEwA}@GECAy@g@AAU@WMQGKEICYIUCCASA]A_@Bi@B]DUDeARUIA?A?A?g@De@Bc@Da@DE?G@YAWC]IQIKGKEAAIEOMCCKKACMSKSGQCIc@eAWs@KWa@iAUo@KWIQM[GOCEEEKI}@kCQe@s@mBqCyJi@mBOo@Mo@o@eCMg@GUEOS_ASy@[}AS{@Q}@iAcGEUEQG]G[wAoHi@oCEWCKCKKq@Ow@G]GYUmA?CUoAACAECQES?CAC?AO{@G_@Ke@G[O_ACKKm@EWGWO_AQcA[iBKo@oA}HOaAYmBQkAMk@EYGYU{AE[UqAW{Ag@aDgAeG[oBG_@Ko@UaBESAGc@oCEUW{AQaAKo@UuAW{AO}@a@aCIe@aC_O{@kGCa@Cg@Go@Ee@OqBEw@C_@EcAEkAA[?IAUCm@EwBAg@?o@ASA}A?QAc@AeA?UC}CCmAAoAEsDAs@E}C?YAgACqA?YAgACiDAu@CmBC}ACoBCyB?QAo@?]C}BG{DAoA?OCuAAq@?aAA{@KyEC{@AIGuAA_@C_@GcAASC]Ci@AIC]?AAGCc@MwAGq@Go@I}@OgBW_DQcCOcBWuCAGGm@?CKeASyBGs@YyC?Gc@cF_@cEKyAc@uF[gEIiAy@cNMiB[wDEg@OeBQuBIcAKqAC[QuBSgCa@{EC_@Gq@UyCUqCKqAOgBKqAMcBGq@Ei@Ei@AEASGw@Em@C]AOC[OmBGo@KkAOkBEc@Ee@McBIeAGq@KmAIaAIw@O}AEi@Gk@McBGq@[aEMgBUsCOcBm@mHGq@ASSaCAKMwAYqDMwAU{CM{AS}BG{@QuBW{COaBAWIkAOcBKuAIgAMwACaAA_A?a@AK?_@?[?W?UKuAAMAUI_A?ICUAMEa@Ia@CYEW?AIe@MiAEYYeBKm@?CM{@?CAMCYAI?GC]Gm@QyBQyBUyCOcBMuBW_DEc@C_@Cc@ACIgAYmDS{BAKe@cGy@gKI_AEq@IcAScCGq@UuCIu@UsCC[CUu@yJIy@AMC[?ECYE]G{@CWCa@CSCYAMC[C_@Eg@CWWcDe@aGEm@[gECUGy@[}DKuA[_EOeBOgBIgAU}BMeBIw@[mDm@oGWsC?Ca@kEKqAGu@C]CYGy@a@iFGq@Gu@y@mK?AMcBGq@AW[wD]aEuAeQy@aLKuACUY{D?Co@wHe@eGm@iHOkBOkBUsCCU]mEM{AC]Ek@CYCSKoAGy@QuBUyCKkAC]m@wHEc@Ce@UiCMuAC[MeBs@}ISeCa@kFOsBCSScCWcDIiAAMSgCQqBGy@]kEMyAQ_CQ{BI_AMuAG}@KmAI}@QcC]mEKgAYuDU{CMmB[oESeCCc@ImA_@aFCQeAcNG}@AMQkBkAcOMcBCUOuB[sDK}Ai@wGa@wEC[IeAIkAW_DQqBU}CKkA[eEQuBs@gIIaAUiCEk@OaB]gEScC}@}K]mEc@uFEc@Gy@Eg@AOCUoBqVi@gHCQEi@I}@Cg@Ei@a@kFEm@YgDOsB[yDUyCc@sFAQi@oFC_@K_AGo@YeCGg@Gi@Is@E_@[mCI}@E_@eAaJK_AK{@Gk@?CIo@MmAKw@UsB[mC_@sDm@mFmAmKGg@i@}Ew@cHa@iDCYK_AUsBgBcPMiA{@gIk@}Fo@{Fe@gEq@cGQ_BK{@{A}LE][iCIq@Gg@K_Ag@gEg@iEGe@E_@OsAE_@gAwJCSScB_@gDUmBAQE]CSe@eE}@gICSW{BEWCWCY]sCGq@}BkS_@cDk@iFKy@gCcUAQMkAW{BQuAg@wEg@gE_@mD[sCMcA_@aDc@}Do@sFu@aHq@eGa@kDa@kDa@uDYgCUuBQ{AIu@a@iDw@eHSgBAKGe@g@oE]_DAAm@qFeAkJIq@E]CYKcACOGo@Ik@AKKeAQ{AOsA_@kD_@}CCUo@yF_@iDCWe@}DIu@{AgNg@sEGg@[oCMkAGm@S}AE_@MkAUoBE_@Iq@Iy@Ky@OsAGo@K_AYmCWcCYaCWwBE[QeBYeCSgBUwBw@_He@cEW_CGg@SgBk@cFq@iGGm@OsA]}CCMCQOmAS_BMmAO_AWkBm@sD[aB]eBWmAaAeE_@aB[sAq@oC_@{A[kAe@qB?AOk@AKKa@UaAYsAo@eCq@wCQo@[mAOu@Qu@Mg@mBcIOo@aBaHIY_A_E]wAsAwFACq@yCq@sCi@_CWkAeA{EMi@GYaAkEGYoAoFOm@EUa@eBGSy@iDAI_@}AcBgH?CS}@Qs@WiAGUOm@]yAQw@Om@GSAES{@K]GYU}@Mi@K]I_@I]k@mCS_AYoA]yAMe@AE_@{AOm@?ASu@Qo@I_@a@yAOq@wAuFQs@yCgMSu@Qw@Qs@I_@GUKc@uCaMOm@GWGWGWUcAIYI]YqAg@uBkB{HS}@c@mBg@qB{@uD{@qDS}@w@cD_A_E[sAu@_D_ByGWeAc@oBQw@u@iDc@sB_AmEWmA}@cEKc@YoA?Ae@qBS{@CO[sAs@_DGQCOCMi@wBcA{Dm@cC_@{AACMi@a@_Bi@uB_ByGGQ]{AOm@S{@gBuH?AeBmHw@_DqAiF{@iDs@uCo@kCe@oB}@}DOe@eAoEMi@?Cs@wCwAcGOk@AEMk@WeAm@kC}@oDs@iCe@gBQk@Uu@So@K[IUIYKWKYIWM[ISIYKWKUKWKY_@_AMYKWMYIUMUKWKUKWKUMYGMEGKUMWe@gA_AqB?CSa@i@kAe@eAyByEWk@eCqF_AuBg@gA[o@y@gBGQ_@u@Ug@[q@k@kAKUM[Ue@o@uAe@aAWk@]q@MW]u@iAaC[m@oAmC{AaDe@cAIS[q@{AgDiCwF}EmKgHuOyB{EIQcBuDaBmDMWc@cA]s@a@_Aa@}@c@_AUe@IQ}DwIEKUe@O]ACO]Ug@AACIS_@GM}@qBSa@Sc@Qa@]w@CGO[{ByEkAiCaAuBo@uAO_@g@eAe@eAgB{D}@kByBuEGOg@eAe@gAGKMYYo@qAuC_AsB{@kB]w@Q]a@_AMWSk@kAiCmAmCMUu@aBAA}@mBIU}@kB[o@sBoEsBoEeCoF{C{GqAoCCI}@oBUg@IMQa@[o@k@oAa@}@ISq@uA_@}@]q@MYACEKMWAC_BmDeBuDa@y@KWKUMYMWMYMWMYYm@MYYm@a@_AOYKUM[MUWk@M[a@}@KUIQSc@gByDAEEIUg@ACa@}@cAwBGMO_@gCsFc@{@cA}BQ_@CEcA}Bi@iAAAKWgA_CaAwB]s@O[k@qAwA_DWi@k@oAwAaDkAgCs@}A[s@O[s@}Ak@mA?Ak@qAyCuGkAiCO[u@aBWi@Wk@Wi@Q_@MYCG"
                },
                "start_location": {
                  "lat": 45.5309378,
                  "lng": 9.1635335
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "1,8 km",
                  "value": 1804.0
                },
                "duration": {
                  "text": "3 min",
                  "value": 170.0
                },
                "end_location": {
                  "lat": 45.6756039,
                  "lng": 9.6698471
                },
                "html_instructions": "Prendi l'uscita <b>Bergamo</b> verso <b>Bergamo</b>/<wbr/><b>Aeroporto Orio al Serio</b><div style=\"font-size:0.9em\">Strada a pedaggio</div>",
                "maneuver": "ramp-right",
                "polyline": {
                  "points": "wavuG}t}y@?W?A?CACO]AEYm@Yo@GMO[EIo@wAUi@O]M[O_@M]Wq@Uq@Sg@EKYs@Wk@IQSc@ISUc@qAqCUe@CESa@CImAgCO_@EKIYM]I[Mi@G]GU?EEQCWGg@E]CSASA[ASA_@Ac@?aA@YB[?GBKBMBGDGDIFGDEFGFCHCHAJAD@D@HBHB@@DBHHDFDFBFBFBF@F@F@J@B?F?B?F?LEZENCJEHCDEHGFEBEDGBGBG@E@I@O@YAm@IOCa@GIAk@KiDg@_@GGAi@Ic@GIAOC{@OA?]E]GSC{@OGA[GsAMQAc@AYAgAEMAC?C?aAIQAe@Ck@EQEICMCKEWQIKCCCEGIGIGMSa@"
                },
                "start_location": {
                  "lat": 45.6657192,
                  "lng": 9.659830699999999
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "0,3 km",
                  "value": 326.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 35.0
                },
                "end_location": {
                  "lat": 45.6754536,
                  "lng": 9.6740157
                },
                "html_instructions": "Alla rotonda prendi la <b>1ª</b> uscita e prendi lo svincolo <b>SS671</b> per <b>Lovere</b>/<wbr/><b>Cremona</b>/<wbr/><b>Brescia</b>/<wbr/><b>Crema</b>/<wbr/><b>Aeroporto</b>/<wbr/><b>Fiera</b>/<wbr/><b>Oriocenter</b>/<wbr/><b>Val Seriana</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "o_xuGqs_z@COAMB]@Q?C@}@?K?Y?c@?[?wA?yA?U@O?K?S?G@_@Ba@@QNeC@MBc@@OEo@"
                },
                "start_location": {
                  "lat": 45.6756039,
                  "lng": 9.6698471
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "7,4 km",
                  "value": 7446.0
                },
                "duration": {
                  "text": "6 min",
                  "value": 365.0
                },
                "end_location": {
                  "lat": 45.6806549,
                  "lng": 9.7526885
                },
                "html_instructions": "Entra in <b>SS671</b>",
                "maneuver": "merge",
                "polyline": {
                  "points": "q~wuGsm`z@Dm@@WB[Bc@Bi@^qGPcDJ{AD}@@_@Bc@@g@?E@c@?]?_@?S?S?QAe@?]AQ?QC]A]CYCc@AYC]E[CUC[Io@QiA?AUoAOo@EQGWI[Og@K]Qe@GQGQ_A_COc@]s@KWKWaBcEa@cAa@eASe@k@wA[y@Qe@}@}BKY_AcCQk@GU_@uAi@cCCMCM[_CGYEWGg@AMCQCe@IgACi@AMC]C{@EoB?{@?s@BqC?W@SDuFDaD?[?]HsG?S?UBeBDgE@U@uABs@?U@S@O?K@G?K@QBUB[?I@G@QBQBYBK?GBQDWBMFe@Lo@Nm@Nk@H[L_@FQFQRm@JW^}@f@oAl@{Af@kAt@mBFO`@aARi@Zu@Na@N]Ri@@ERi@DIL_@FOFQVs@Ro@Vs@`@sATs@d@{Ah@qBnAaFDOd@kBx@aDlCuKLe@\\wADO^uAT_APo@@C^uARq@BIHYt@gCRm@`@sADKTq@Z_A@C@E@EHWFS@CpA_Ep@qBBIh@_BdAeDPe@Ni@Pi@^kAd@sAr@{BPi@FUHSFOl@iB^iALc@L_@Ty@XeALm@Pw@TcAFc@P{@Hg@Lw@Fm@Hq@Jy@LuAHgAJcB@[@O@SDgA?EBq@@s@@}@?o@?i@?_A?yF?I?]@m@?U?A@Q?Q@OBY@O@SBQBYDWDUDUHg@@IBG@KBSBMBQ@[@I@_@@??G@]?K?A?G?O?G?QCg@AWAOCYE]AQCMIo@I_@Ke@IYQe@ACMYIOKSOUIOMOMOMOOMMKKKIEMIA?GEIEMGSIME[GA?_@IOEa@MOESIECGAGCIEA?OIQIGCUMc@UWM]QIGAAi@YEAqAk@GCe@WYOAAWQe@Yg@_@UQYSGIg@a@UU_@_@w@{@KQgAgAq@u@SSQQOMQMSOKISOOMOI_@SUMSKUIQIs@YaASqBe@m@MYGKCSGMC]K_@Oe@USIo@_@[S]WQQSSGECGMMUYOQKMAAIMQUS[QYQ[Qc@S_@GMKWIUQk@Oe@Qk@CMIUAGQu@Ia@I_@Ii@O_AIu@CKOkAAIMeAAIQ{A"
                },
                "start_location": {
                  "lat": 45.6754536,
                  "lng": 9.6740157
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "5,6 km",
                  "value": 5587.0
                },
                "duration": {
                  "text": "5 min",
                  "value": 287.0
                },
                "end_location": {
                  "lat": 45.6832691,
                  "lng": 9.820996
                },
                "html_instructions": "Continua su <b>Tangenziale Sud</b>/<wbr/><b>SS42</b> (indicazioni per <b>Val Camonica</b>/<wbr/><b>Lovere</b>/<wbr/><b>Sarnico</b>)<div style=\"font-size:0.9em\">Continua su SS42</div>",
                "polyline": {
                  "points": "a_yuGiyoz@G_@YeCWwBACg@qD?GUuAKw@Ie@EYE]]wBEUAMAKCMAIACEGK{@_@oC[cCCSEk@Ek@C[AUAe@Cq@AcA?W@k@B}@By@FkAFk@@ILmAFg@L}@Fe@n@{Ej@mE~@kHFg@BUDWh@cEfAiIb@eDdB{MHo@Hq@DWD[De@D_@Da@Be@@a@Be@?UB}@?o@AeAA_@C{@AYEo@E_@AWCSKu@Gc@My@Kk@Kg@EQMo@Qu@Oo@Om@Mm@Om@CMIa@Mi@AEEOi@cCMe@GWG_@Ic@M{@Iy@Ce@C[Cq@Aw@@a@?s@@]@c@Dm@Fw@Ju@TsA?ALk@?ANm@L]Pi@Rg@N]Zm@LWh@eA`@{@?AHSNa@JWFQFOH[Lc@BKDOPs@DU?CFYFYD[BMJy@J{@NoAJ{@Fs@LeAFg@Fq@D[@G@IFk@H{@Hm@BQB_@Jw@LoABWLgAJcAHcAF}@Bm@B{@?C?y@Au@Ai@E{@Eq@C[Io@Io@EYCMIa@E[ESEUu@sEYaBMu@Ik@c@gCKi@W{AYcBKu@I_@WgB_@{B]kBCQKm@SqACQCKGc@Kq@AMGa@MiACYCSI}@KaBEq@GiAA]G{BASASAaACs@Aa@?a@?AC]Aa@IgBIeAIaAOcB?AOmAKw@CU?AKq@AGOeAO_AQ_AKs@?ACMGa@YeBYgBCSI_@e@uCSoAQiAMu@Kq@OaAe@oCM{@c@kCCQYeBYaBIi@EU"
                },
                "start_location": {
                  "lat": 45.6806549,
                  "lng": 9.7526885
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "1,2 km",
                  "value": 1184.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 67.0
                },
                "end_location": {
                  "lat": 45.6827003,
                  "lng": 9.8360071
                },
                "html_instructions": "Mantieni la <b>sinistra</b>",
                "maneuver": "keep-left",
                "polyline": {
                  "points": "moyuGgd}z@SmAEUIi@Ks@Gk@Ge@E]ASGk@ASCg@?AAICi@AYA_@Ae@AU?Y?g@?M?S?K?w@@w@BkAB{A?EFsCToH?KBa@@Q@a@@O@a@BYLwC@M@M`@eHPyCBc@@ODq@Dg@NiCH}ABa@DiAB_A@u@@G?[Dk@@U@KBIBGFO"
                },
                "start_location": {
                  "lat": 45.6832691,
                  "lng": 9.820996
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "3,1 km",
                  "value": 3098.0
                },
                "duration": {
                  "text": "5 min",
                  "value": 272.0
                },
                "end_location": {
                  "lat": 45.702791,
                  "lng": 9.858774799999999
                },
                "html_instructions": "Alla rotonda prendi la <b>2ª</b> uscita e prendi <b>Via A. Gramsci</b>/<wbr/><b>SP89</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "{kyuGab`{@BAFEFGDGDI@C@GBS@K?MAI?AAKCKEKEGGGGEGEGAIAI@G@GBGDGFEHEHGR]`@IFGFGBI?G?MCOEMQ[]IMIEq@aAc@k@oAeBOWKQk@qACEm@kBYcAGQyB{HIUm@oBIU}@yByAsC}AuC_@s@aAyBeCyG}A{EQk@IUY}@Sc@IQKQKOKOMMQOUO[OWKwCu@}A_@CAq@Sk@QICw@]AA_@SAA_@U_@USMs@m@UQWU}@gAw@eAk@{@EEYc@Ya@GMQUs@eAOUe@m@SYEIeA_B[c@g@s@Ya@[a@IOW_@g@i@EEe@i@CCc@a@_@_@SU}@{@{@}@kAuA}@iAg@m@SW_AiAIK}@gAaAoAm@w@IIMQ[_@_@]g@[_@Oa@IA?o@AUAiABa@@a@@KBMBCDC?ABMHEHYd@"
                },
                "start_location": {
                  "lat": 45.6827003,
                  "lng": 9.8360071
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "3,2 km",
                  "value": 3151.0
                },
                "duration": {
                  "text": "4 min",
                  "value": 229.0
                },
                "end_location": {
                  "lat": 45.7188026,
                  "lng": 9.8892245
                },
                "html_instructions": "Alla rotonda prendi la <b>1ª</b> uscita e prendi <b>SS42</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "mi}uGipd{@CAC?C@C?A@C@C@ABC@ABABABABADAB?DKJOJSJOFA?I?E?UCSCe@M{Cu@KEKCUKOGOKKKCAIKMOIMIKACKSIUM]Sq@Oe@Ok@GUGUEWE[CUC]Ek@IgBa@yHKcBAWCo@GcAA[Ew@GgAKgBCc@Eg@AAI}@CSEc@AGQ_BK}@K}@YuB]yCK{@O_A?AIYI]ESQk@KYSi@GMUi@Uc@QW]i@g@s@e@s@IK]g@IMGM?Ag@_AYo@i@kAYo@cBqD[q@Sa@MWGOUa@IMGIEGCC_AsA]e@MOo@}@i@}@g@gAGKe@cAq@yAa@{@Ug@gBaEuByEUi@EI]s@MW_@s@KSACkAwBs@sAIQoA_CIMkBqDy@_BGKqAeCAAGMu@wAa@w@e@{@We@k@eAGOc@w@ACUa@Wa@cBsCAAQYEMACEQAK?K"
                },
                "start_location": {
                  "lat": 45.702791,
                  "lng": 9.858774799999999
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "14,6 km",
                  "value": 14581.0
                },
                "duration": {
                  "text": "17 min",
                  "value": 1041.0
                },
                "end_location": {
                  "lat": 45.7904066,
                  "lng": 10.0022188
                },
                "html_instructions": "Alla rotonda, prendi la <b>2ª</b> uscita e rimani su <b>SS42</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "om`vGsnj{@@C@A?A@A@A@A?C@A?C@A?C?A?C?C?A?C?A?C?CAA?CAA?AACAAAAAAAA?AAACAA?AAA?A?A?A?A?C?A?A@A?A@A@A@A@A@ABK?E?ECGEGISWUa@Wa@S]Q[]u@CGIUEKCICICGEOCMCGAECQACKg@CIACEWIe@AKI_@?GEOMw@Ki@m@aDSu@WiAEUG_@CMCQGg@E_@AIAOOeCCa@CSAOGi@CWCOG[?AGWCICMGUACGQGWKWKWGMKUKSKSOWIKIMMOQSWWWUYWKIIMi@a@s@o@oAeAYUgA}@k@e@SOiB}Am@c@QQ]Y]WOKUOc@QKC]EOESEQEEAUEM?SEMGMEIEKIOOKIIGIIIGWUEEYUQOQQGGECc@a@a@]WUKKCCOQUYIKCAEEIKIOcAiBs@sAEGa@q@oBuDEKs@oAS]UYOOOMQMSMSKYMA?WGWGUCaAGw@EMC]Ck@Cm@GC?u@E_AEy@AQ?Y@]DODGBOBQHUJSNc@`@Y^[^MPSTQLWRUJ[HS@O?wAESAeBGaBISAq@EKCIAKAQAEAGAKA[GKEKCSIIEa@S]Q_Ag@e@WWOQKAC?A?C?AAA?CAA?AAA?CCA?AAAAAA?AAA?AAA?A?AAA?A@A?A?A?A@A?A@A@A@MCIAIESIOGM?s@_@KGiAm@YOeA]{@UGCiAYi@O{@U]K{@UcBg@SEGCa@Kg@OWGMAc@Im@?a@?[@SBa@Dw@PA@SFcA^u@Vy@Zs@V?@WHYHYDK@KBO@Q@g@?sAA[?eACY?K?]Ac@EGAi@ECASCw@Ku@UICcA[OEmA]_AQg@MECkBg@q@Se@Mi@WMIm@g@e@g@OOOOaAkAy@aAOQi@o@[a@IMm@m@g@e@a@W[S[QYKYK}@Ui@M{@QOGqAi@e@W]WYUa@a@ECYYOSIMi@y@_@o@Qa@Sc@]_A]kAIUW_Ai@{By@uCGUWy@ISIMIOIKSW]SUKq@Yw@]KEOOII?A?A?A?A?AAA?A?A?A?AA??A?AAA?AAAAA?AA??AA?AAA??AA?A?A?A?A?A?A?A@A?A@A@A@A@YEOA][q@o@KOKSSa@M[[w@e@mASk@Oa@Sg@CGS_@OW[g@o@_AOSQYSYs@eAY_@EGm@y@aAuA_AuAc@m@a@k@[e@[e@Q]O]?CIYGUGYE]EYG[G]?AA?CQISKYISAAMQKMQOSMa@Uk@Ya@Qg@Ua@Si@WWMKE]OWMUMSOe@c@KMSSU]Ye@ACgAoBgAmBUc@AEYk@Os@Me@i@iC?CMk@COOk@M_@GOKWKSKSEGMQGGGISSECQSECo@]QI{@a@oBaAi@Wa@U]W][[a@Wc@Ug@Oe@AEK]Ki@I_@Ga@Ea@Ga@G]?CE[G]GSEOCIKSEKGIQYA?OSYSWMMEOEUCK?G?Q?I@SBA?MBQBs@Jg@D[@W?UAWCYCI?GAMAWAO@a@@[DUFSHSHMFEBKFGBGDKHC@QJYNUNSJUNYJUF[HYFk@Lg@Hk@Jc@Hu@Nk@JK@a@Fc@B_@AOCKEUIMIWOCCIIOOSYo@aAIMYc@_@i@]e@a@c@g@e@c@_@OKa@Uc@Qe@QQIYQ[[IISS_@c@WY]_@EEm@s@]i@?AWc@g@}@q@kAaAaBYi@Qc@I]ACEYEUAEC]?Y?o@FoABo@@e@?EAe@Cc@Gy@KiAQoAGUIUMYAEMWWg@m@aAa@o@g@q@UWm@o@aAeA{@{@s@q@][_@SWM]Ka@K]IUKWQMMOSOUGOGUEMiAmEGYQs@e@gBSy@Uq@Ug@Qa@iA_CYm@MYEQACEWEWAUCWAo@AwAA{ACoAAYC]CYAMAGCQEUMe@g@aB[aA]cAOk@GYGa@Ge@AUASAk@?Y?I?EDiANuDB]BS?GDY?EBID[Lo@J]J_@Tk@Rg@Tk@\\u@L[FOPa@Z{@?CPk@@CJe@F]NgABa@BY@k@?QBgC@_B@mB@gC?uA@_B?_@?_@?i@@_A?g@?M@gD?A@uB?[@gD@wC?K?]CgAAuACeAEoBCmAAmA@_A@eABeB?e@?M?a@?eAAYCkBCwBAuAB{CBQ@y@?yA@u@@k@B]@SB]HoBF_ADo@@E?E?SDm@FsABa@@w@AY?E?U?QASEiAOsCIcB?u@?a@@c@Bc@Fc@PqABUFo@?E@q@?W?O?UA_@Q_D@O?A?CAYCa@?W?[@Y?G@I@UBUBKBWBSJg@@GBIDQ`@wADMNi@@CF]v@kCHYh@eB`@wA\\eARs@Li@Ha@Jk@DYLiAJaAHo@T}BNeBDm@JwBJkCDg@N{CBi@@[?WAw@C]AOCKEUSi@aAqBs@wAs@{Ae@{@a@s@IMKKQQUOMIu@_@GGCCCCAACCACa@_A[e@"
                },
                "start_location": {
                  "lat": 45.7188026,
                  "lng": 9.8892245
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "1,8 km",
                  "value": 1781.0
                },
                "duration": {
                  "text": "2 min",
                  "value": 124.0
                },
                "end_location": {
                  "lat": 45.8000785,
                  "lng": 10.0187444
                },
                "html_instructions": "Alla rotonda, prendi la <b>2ª</b> uscita e rimani su <b>SS42</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "amnvG{p`|@@?@A@C@A@C?A@C@C?C@C?C?C?C?C?C?C?CAC?AACACACAAAAACAAAAAAC?AAA?CAA?C?A@A?C@C@A?A@ABA@A@ABA@AB?BAB?@a@AQ?KAIEIGKGAAUSGG[_@sDiEQSEE_@e@kBuBm@q@KOEG[e@Sc@IUEMAEGSCSCOEY?GAKCQ?SEu@KwCCq@Ce@AOCQCMCKOk@IQMYEIGKGIMQ]]q@o@YY][_@_@g@c@OQWWEE[_@QSm@u@m@w@Ya@A?Ya@g@s@KO[a@Ya@QWGIYc@IIWa@Wc@Ue@Qa@Oa@Qm@I[GSGSEUEOGWGUEMCOEMCOK]Qs@Mm@Kc@GUK]]uAQu@YkAc@iBOi@Qi@Og@Sk@WeAACG_@AAEI"
                },
                "start_location": {
                  "lat": 45.7904066,
                  "lng": 10.0022188
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "47,7 km",
                  "value": 47686.0
                },
                "duration": {
                  "text": "41 min",
                  "value": 2474.0
                },
                "end_location": {
                  "lat": 46.0882831,
                  "lng": 10.3193424
                },
                "html_instructions": "Alla rotonda, prendi la <b>2ª</b> uscita e rimani su <b>SS42</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "oipvGcxc|@?E@AAA?C?CMi@IWKYKMIOQSQUII][q@k@q@c@q@YWOKE{FmCa@UKO]WWUCEW[QSMSIMEGCGYi@KSKUk@oA[o@MUOU?CKOOQEIAAOSEEWWEEMMMI]]EEYYACSUSYSYKQCGWe@MUCEO_@O]Oc@Qk@Ka@GOESQw@O}@SyACYO{ACa@s@wMCs@AM?M?CAC?CAWCUCi@CUCi@Cm@Ek@AUAWCk@Ci@?EAs@?u@?{@?_@@g@FwADcABg@@QDe@Hw@NqADe@Hc@D_@Hm@NqAJq@F_@f@yDV{AToAJo@Hi@Ji@H_@j@_DXuAZeBP_AR_ABQHe@@EHi@?AHa@BSDUFk@NmA?ODa@?E@I?EB[?EDq@@s@?C@c@?m@AkAEiAAUAWEi@C[CYK_AQkASgAESEQGWOi@W_AQg@Sg@Qc@Sa@KUS_@e@w@e@s@AC?A}@oA_@w@GMmAwAYc@[W]e@a@c@Y_@SSAAWYCEOOi@k@QQKKy@i@i@k@c@g@IIIISSiAoA]a@]]y@}@[]KMSSIIk@o@]_@KM[_@]c@W[CEYc@We@Yc@Yc@We@We@We@Ye@We@o@kAWe@Yc@We@We@We@Ye@We@o@kAYe@o@kAo@kAYc@We@We@We@q@kAOYGKWe@iAqBWe@gAqBS_@CEiAsBWe@We@We@KSKQq@kAWc@We@MSc@w@We@o@kAWe@q@kAWe@CG{AqCWe@KQKSiAqBo@kAo@mAOWGMUg@k@gAAE{@qBEIUi@Si@Ui@CEOc@Qk@Qg@AC?CAAOg@Qk@Qk@Qk@IYGSg@wBUaAOm@Ko@EOG_@Mo@Mo@AEIi@Iq@Ie@AIUaBIq@Ii@?GIq@KmAKw@AOGi@?GEq@Es@Eq@AMCe@Cq@Cs@Ey@E_BEqA?SCs@As@As@Aq@Aw@Co@?OAc@Cq@?ICi@Ao@G{AAMIeAG]CKMk@MeAE_@Ca@CQESEQIYAEEOGQw@eBwA{CGQm@w@Y]c@m@GEi@g@AAm@e@OGWOGE[OKCy@YCAc@MCAc@K]E{@KcAMMAoC_@a@GMAUCc@Gc@E]Eg@MYEICc@IcA[c@Ma@M[KWIOGa@Sa@Qc@S]SKEUO_@W_@UKIQO_@Y_@Y]YGEUU][g@g@SSY]AA[_@IKQUOUIK[a@QSIKQWGKYc@KQKSWe@Yc@CIc@q@ACIMS[[i@We@Ye@IOMSYc@Yc@KOMUWc@q@iAIMMWYg@We@Q[]o@Wg@ACSc@Si@EIO]Si@A?Om@Om@}@yDEUKmAEq@QgCAMCs@Cy@Ak@Aq@Ai@BqA@]Bq@@U@]Dq@?EFk@H}@Jw@\\gBDOViADSDS@EDO@CJc@@CDQDM@C@ENm@Pk@FUHWr@kCBGV}@FWHYFS`@_CXaAd@}APi@Ni@Lg@DK?CDQJ[FYPm@Nm@@CLi@Nm@FOXiAPm@J_@DK@ELg@Ha@DM@GHc@?CDYFc@@WF}@Be@?A?K@o@Bw@?I?i@?QAS?M?]Am@AKAMASCk@Go@CYAME_@Ku@Gg@Gc@M}@kAcJW{BEYIo@G_@CQKo@YgBGg@UaBUgBCKM}@QaASgA[wAOk@?AMk@Og@YcASm@]aAEMYy@w@mBiAsCQe@AAQg@i@sAK]GMSi@Si@EIO_@Si@KYIOUg@Ui@CGQ]We@o@_AmAyAMK][][cBwAGGoAcA[Y{CiC_BsA{BmB[WqBeB{AoA?A}@u@US}@w@g@a@sAmAaAy@{@y@eAiAKMCCAAQScBuBc@m@EE]g@w@aA_AsA[a@Ya@CCW]]g@GGwBwC]e@CC]g@cIwKiHwJ{DmF{DmFMOACQSeCkDY_@w@cAw@gAe@o@s@aAe@o@[a@]e@AAU[KOSWCEU[SYGGKQY_@]e@_@e@[_@U[]_@m@q@w@y@a@_@WUUUWSoAeAo@e@q@e@s@e@k@_@WOmEeCcFuCmPkJGEIEaB}@_DkBs@a@MGeAm@uA{@YS_@[KKYYSUOQm@{@a@q@Q]Sa@Ys@g@uACIKc@I]Os@Mo@Ku@K}@Gq@IoAAUAWAo@AoA?OAs@@w@?w@BwABqA?CBw@PmHBm@@m@NsEDcBDaBDgBBq@@_@BaA@s@HeCB}@Bs@?cAD_CC_AImACc@CSIq@Im@Oy@Km@AAGYQu@I_@CGIYQk@]{@Uk@Se@We@cCoEuBuDw@sAUa@ACS_@}AqCaBwCoDqGQ]Q[eDaGa@s@k@}@a@s@m@w@w@{@GKSSKKKKKMaCeCc@a@a@_@KIs@q@qE_FoBsBi@k@W[U]QWOWIQKQAA]s@[s@M_@Ws@ISQk@AEg@{AY{@[}@gAgD[{@Wk@Q_@Q[Ya@CEi@u@_@_@YW[Wm@]g@WSI[OEA_EgBiFaCkCoA[OEAUKmCuAy@g@cAw@{@s@k@g@QS{@_AKKu@_A_@e@e@o@eAuAwK{NoAeBGIy@iAEGY_@gAcBi@y@kAmBO[wB_EYg@EGaAeBm@qAIMIOcAmBWe@eEgI}CaGm@kAWg@kCiFgGqLWg@[m@Ue@Ue@QYw@}AGKcAuBS_@w@}AyDqHsA}BeCmDIMGGGGkBsBy@y@EGUUAAmEuEMOOMiO{OcFgFKMKIoBsBgS}SOOACQQyC}C[]]]]]cKoKSUQQAAQSg@g@IIGIoAqAoBqBOO{ByBYYUSk@i@GImByA{E_ESOIIGEAAc@]uFcE[QQOw@m@eAy@_Aq@e@]UQIGOK_@Q_As@OM[U[We@a@MKEEWWUU[_@KMQUMQEIKOKOGKGIIMMQIMMWQa@Q_@CIWq@CGUi@Uu@e@yA?AIYM]e@_BOg@Oc@Sq@EOM]Qm@M_@Qi@Me@Ma@EKe@cBQk@Qk@Qk@Oc@a@qAEMEOKYMc@g@_BQk@AEy@iC]iAQm@Qk@M_@K]]eAUs@Kc@Og@AEOu@Qq@Qu@Oq@EUIWGW_@iBKk@Qy@c@yBe@_CQaACKIc@CKEOa@sBKm@e@oBOw@c@yBQ{@?AQ_AQ{@Ga@GWWmAMm@Mo@{@eEYwA_@iBUcAg@yBOs@Qw@?M?IG[I[G_@SaAOq@CSKe@{@eEw@wDY_BMo@Ia@AIACKo@Mo@ESCMY_BCICOCOI_@Ki@]}AMo@Mm@Oo@AEMo@Kg@AEKi@AEMo@s@cDCQ_@eBc@wBEM_@kBOs@?EOm@S_AGQEOEQAAIWGSGMCKGMKWMWS_@GOWa@Q[Q]EGCCm@y@W[a@_@EEe@]YOAAg@W{@[ICWKWECAA?KCCAo@KUGiBa@c@KOEQEc@IoDq@}H{A]IC?}H_BeASiB_@eASaASiA[eAWa@MkCq@c@Ma@Kc@Ma@Ic@IeASw@OMEkBu@_@QCC[Sm@c@OMYUCESYg@w@QYOWIMKQUa@KWUe@KSUg@Ug@ACSe@Si@GOQ_@CEIWsA}C{@uBo@yAQe@EGO_@Ug@i@sAGQMWUi@Sg@Ui@EKO]Si@c@eA{@uBEOACEOKWQk@ISK]EMo@{BCIQm@a@{AOm@Qm@I]EOMm@Oo@]}AMk@?A]}AOo@[}AOo@Mm@]_BKg@AEOo@y@}DG[EUGWm@mCc@kBa@iBq@}Cc@mBy@oDGWIc@_@_BMk@Ke@CIG[[uA]}ACIOm@Mo@m@kCeA}Eq@qCWiASaA_@{AKa@CKOm@a@{AIWQk@AASg@Si@Ug@Qa@Q]?AaAmBu@wAW_@We@AACES[EGWc@q@aAW_@[_@W]CEEG[_@IIo@s@i@m@i@i@WUAAWWSQy@q@YWYUoA{@aAq@MGa@UGEWOMIQKgIwEOI{A{@UMIEWOaB_AIE_@U_@Sw@c@IGWMi@[w@c@IEUOIEWMIE_Ak@WMIEUOIEWOi@YWOGEa@UUMa@UIEGESIQKcDiBAAeAm@m@a@q@i@CAu@o@YYKIi@o@_@c@c@g@e@u@_@k@Wc@Ua@w@wAQa@Q_@_AoBYk@Qa@Q]ACCIi@qA_@w@Wi@cAsBaAqBGOi@eAUg@Q[EIMUKUEKe@_AOUKSUa@OWGKIKOWMQMOEGm@y@AA]]OQk@g@CE_@[]YGCMIGE_Ai@[QCAa@SECMCi@SWIAAg@MA?uA]}A]kAWWGEACAOCOEC?gB]MCYI_Cq@CAOCUGGEA?MGIEWKi@WwAq@_@QMGQKa@QMGSKGC[QWK_@SEC[O]Oa@UWMaAg@a@SiAk@]OkB}@AAw@_@WMe@OSIKGQKA?_@WEAYU_@YKIQO{@y@]Y[a@W]CCYc@i@y@IMOYYg@Ue@[q@k@wASg@Ui@Ui@CIaBiESk@}@}BSi@i@uAUi@Si@Wg@We@CGS]Yc@CEMQGKY[MKIIIKQQUSUQYSa@Wa@UEA]M]MA?A?a@MGAe@Ke@GWAC?s@CO?y@?C@I?c@BS@O@_AHA@_AJ{@J{@LgALa@FUBm@J[FG?{@LmAPqAP_ANOBu@JQBOBYDI@YDc@Fe@HC?]Dc@FG@[B_@DC?G@Y@E@A?[@E?]?G?[@A?G?YAOAs@ECAc@EGAKAOC{@QICc@GGC[Ic@KIEUMa@Qg@Wk@c@KEo@[IEwAu@SKEAOIKGCAiAk@}CcBKEQI]O[KYK]IUGUEKCSAWE_@A[A]@a@BI@a@Bm@F}@Ji@Du@Fa@Dc@DC?_@Dc@D[Dk@BA?a@BcBHk@?c@?a@?A?a@Ac@Ac@A]AE?c@Ec@CCA_@Ec@GE?]Ga@GUEMCa@Kc@KGAYIc@KKCUIa@Mc@OKCUIa@Oa@MGC[Ka@Ma@OGAYKc@Ma@Ma@M_@MA?c@Ma@Mc@Ma@Ma@KYIICa@Ic@I_@Ie@Ic@G[GG?c@Ec@EOAQAc@EE?]Ac@CA?a@?g@?cA?Y@I?c@@]@E?c@Dc@BSBO@c@D_@DA?c@Fc@FIBYDa@Hc@HM@UF}@Pi@N[JG@a@Na@LSFMFa@Na@PGBYLUJKDWNGBa@RQJeAl@_BbAu@b@eA|@c@^A@k@f@?@]XGFSTKHEDKJIFSRC@YXKJgA~@eA`AA@a@^g@b@{@v@CB{AnA]Z]Z]Z{@v@KJ]\\w@t@CBy@x@A?]\\[ZGF]Z]\\[\\A?CBWVA@[ZA?[ZIH{@v@GF]ZWTEDWT]ZEDWTED}@x@UR]ZGFcA~@WNWNcB|@[PC@a@Pa@Pi@TYLc@RE@YJGBa@L{@ZG@WHKBiBX[FG@YDG@QBu@H[BG?[Dc@DG?c@DYBG@c@Dc@D[BG@c@Dc@D[Bg@DC?YBG?[Bk@B[@A?E@c@@[BG?[@G@c@B[@G?c@B{@DK@[@c@BgADa@BgAFkBHkBHc@Bc@BS@s@Dc@@kBJqDPc@@oCNgADwKf@{AHmI^kBJ_HZiBHgAFuERA?gAFaI^eDNyGZkCJkCZkCn@iCz@uGtBgPnFiPlFsGtBiCz@uAj@_@NA?GBURA?]V_@Xi@^SR]XA@]X]ZCBYV]Z_@Z]X]Z]Z_@ZWTED]Z]X]Z}@v@ONMLKJMNKHMNYXKJSTOPe@d@?@ON[\\a@Pa@N_@PA?c@LE@WFe@FKFMFGFCBGLGPABELEPANABATAT?@AR?T?H@J@V@N?B@HBNDPNl@Nn@Nl@Nl@DPF\\@@Fb@@JD^?P@X?X?PA`@?HCh@?@Il@[nCWlBIl@SjBIp@y@bH?DKp@Gj@ADCREPYl@ABMFQN"
                },
                "start_location": {
                  "lat": 45.8000785,
                  "lng": 10.0187444
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "5,8 km",
                  "value": 5827.0
                },
                "duration": {
                  "text": "6 min",
                  "value": 377.0
                },
                "end_location": {
                  "lat": 46.13316349999999,
                  "lng": 10.3336831
                },
                "html_instructions": "Alla rotonda, prendi la <b>1ª</b> uscita e rimani su <b>SS42</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "wrhxG{n~}@AAA?AAA?A?A?A?C?A?A?A@A?A@A??@A@A@A@A@?@AB?@A@?BA@?@?BABSd@INcBpAgA`AKL[^SVCFABe@nBKVEJQ\\GLa@`@k@d@A?EBOFK@WFK?W@K?W@K@gABW@E@eBDI@c@DK@M@E@WDCBa@Ju@ZMHE@a@Ra@RC@ULEBC@uCtAMHYNEBCBa@R_@Ts@^]NKDSHI?G?C?W@A?a@GKAIAE?GAc@GIAqAO}@KWCKAcD_@A?K@C?E@I@c@NULUPIFEBWZ]f@g@v@EFMLGHYPUHe@Ja@BG?S?WCUGWK_Ak@AA_@UMIIGGE_@UMIa@WWKSES?YHKFKNSXCBEJGNGLCFA@AFGHMVWR[R_@P]LMBC@MBIBI@OBq@NE?[DC@A?I?Q?G?O@A?a@AOAC?YEI?SCE?Y@M?UBI@YB}@HC?KAg@CEAQESGOGAAq@YQKaAi@UMIEa@U_@ScAi@i@[MEIEUIMCO?M@[JILg@fAs@vBK`@CHSj@GPQ`@QVA@GHOLWLOFUDU?O?I?ICA?YIECa@SCA[SOKQIOIWKaAe@ECy@a@{@a@IEgAk@]c@QSY]gBaCuBwCCEAA}BcDEEACW_@i@u@MQY]Ya@[a@AASYIKy@u@SQGEYSECAAgAw@w@c@SMKCUGOCMCQAe@Cu@?U?k@@E?YAM?KCIAMEECGAICKEICGCGCIEEAGEEEGAEEECGEIGY[YYCC_AcAY[OUCE]_@yA{Ay@{@QOIIKQa@g@c@g@qByB[YgBaBqCyCiDeEMMiBeBeBaBMM_@a@i@m@}DgEIISSGKIGe@i@oCwCyCaDSQGKy@{@]][]y@}@[]y@{@MMEEIIQSg@g@mDsD[]u@w@_@a@o@q@e@i@kAqAq@s@"
                },
                "start_location": {
                  "lat": 46.0882831,
                  "lng": 10.3193424
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "3,9 km",
                  "value": 3901.0
                },
                "duration": {
                  "text": "4 min",
                  "value": 235.0
                },
                "end_location": {
                  "lat": 46.164811,
                  "lng": 10.3503927
                },
                "html_instructions": "Alla rotonda, prendi la <b>1ª</b> uscita e rimani su <b>SS42</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "gkqxGoha~@?C?C?A?C?C?AAC?CAA?CAA?AACAAAAAAAAAAAAA?AAA?A?C?A?A?A?A?A@C@Y_@SUwB_C]]aBgBm@i@k@e@UOg@[oAq@KEkLyFa@Q_@SuKkFuBiAi@YuAy@qBwA}AgA_@WoE}CMKqAcA{EcDuCuAaAg@a@QAA]IAAWOi@a@s@o@_BwA{@k@i@[SGUGSEC?M?WBUD]BQ@E?SAe@Gi@Oc@IeAW_Ck@KCa@Ma@KiBi@UGKAE?W?WDuBf@SBO@C?K?OC{@a@kAe@iA[{CQm@Cc@Ca@M[SCC_@Yk@c@aAu@o@c@WQGCYQUQGICCAGCECIAKAK?C?K@[?g@AWAS?ECMGUGMAEEEGIECCAGCGAIAO@I@A?K@eAPE@y@JG@K@W@E@c@@[ASCUESIg@[KGaAk@gBkA_D}Bg@]]QWMYIKAa@GAASCC?[AQ?E?K?G@G?}@LSBs@L_C\\C?IAKBK@K@S?KEKEIKGI"
                },
                "start_location": {
                  "lat": 46.13316349999999,
                  "lng": 10.3336831
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "1,0 km",
                  "value": 988.0
                },
                "duration": {
                  "text": "1 min",
                  "value": 86.0
                },
                "end_location": {
                  "lat": 46.1715544,
                  "lng": 10.3432564
                },
                "html_instructions": "Alla rotonda, prendi la <b>2ª</b> uscita e rimani su <b>SS42</b><div style=\"font-size:0.9em\">Attraversa la rotonda</div>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "aqwxG}pd~@?CAA?A?AAA?C?AAAAAAAAA?AAAA?AAA?AAA?A?A?C?A?A?A?A@A?C@A@A@?@A@A@A@A@?@AB?@AB?@AB?@?DS^EDC@QJMHa@Rg@NcAPMBiAPmARo@Ne@NQHMJKFCBSTQRILABsAbCy@rAKN{@rAUZ_@VA?A??@A?A??@A?A@A@A@?@A@?@A@?@?@A@?@?@?@?@?@?@?@?@GZERIRm@fA[n@EN_@t@k@bAEHi@`Ag@fAQXq@jAIP[`@Wd@ILOTOT"
                },
                "start_location": {
                  "lat": 46.164811,
                  "lng": 10.3503927
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "1,4 km",
                  "value": 1413.0
                },
                "duration": {
                  "text": "2 min",
                  "value": 131.0
                },
                "end_location": {
                  "lat": 46.181542,
                  "lng": 10.3335036
                },
                "html_instructions": "Alla rotonda, prendi la <b>2ª</b> uscita e rimani su <b>SS42</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "e{xxGkdc~@I?KFEBEFGFIN?@EHAH?J@HOl@GRINWf@e@t@Yh@Yd@o@jAQZ_AfBq@pAa@t@}B~Do@hAw@tAGLU^U`@KR_@n@}@t@EFC@iAjACDUP?@UPQPIFq@n@C@gA~@SLMFOFKBk@HO@e@Gu@KQ?K@i@Ne@NaAZ_A\\EBkA`@w@XOFODKBWF[JG@_ANUFMFIDGDKHIFGFA@GDIHOLU|AAT"
                },
                "start_location": {
                  "lat": 46.1715544,
                  "lng": 10.3432564
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "5,2 km",
                  "value": 5151.0
                },
                "duration": {
                  "text": "7 min",
                  "value": 406.0
                },
                "end_location": {
                  "lat": 46.21894409999999,
                  "lng": 10.3565657
                },
                "html_instructions": "Svolta a <b>destra</b> per rimanere su <b>SS42</b>",
                "maneuver": "turn-right",
                "polyline": {
                  "points": "syzxGkga~@m@Ua@QA?c@Ws@q@oAmAIIIGMI}BuA_@IMCGCGEAAGGEGIKAEISAAGUACKe@G]CS?CAIAQ?KAC?G?GAKAE?CCKAGCMEICSKQIKGGKGOGMECASGMG[MSOsB}AaAu@o@_@QQSYSSKKOSe@m@y@_A[_@A?]c@YYKKAAMKA?OIYMGEa@QCA]OGCECGGGGAAKMKOAAWg@MWIMCGMQGE?AAAECKGMGQEIAE??AC?OAO?A?OCGAKEKEICIGKKSQWYECGIcA}@MMe@a@a@a@II]]WSEG]]OOKMEEQKWQo@[c@SYM{@a@MGa@QUI_Aa@OIUKUKEAc@Se@SAAOGIG[KOEIAI?MAO?K?C?I?K@C?G@A?[JMDUJA?_@RWLIBE@KBKAEACAKCEC?AIGCAGGGGUQi@o@ECSU[WCA]UECCCa@UOIOKSMEAAACACAGAIAA?A?A?A?A@C@A@A?ABAFABAL?B?F@D@DBBBBBBB?B?@@B?B?H?J@J@D@FDLJPLNF@?JBJAH@B?RBB@@@@B@@?@@B?B?F?B?D?BADABA@A?A?C@E?KC?AKEGEWQAAIGMKQMMGEAAAIAGAG?A?CAC?KCGCECCCCACAKIMQOS[_@GICCCEGCCAG?E?G@O@E?E?C?Q@GAC?CAGAYMYOCACAIGGCQGOAGAEAC?OCMAWCa@GGAEAAAA?QEe@QUCKAI?K?K?W?[AWAWESIWKCAYQECECMKOKQOAAIIOMMMMMWWOOEGYWOOGGIGEESOOIUKUKUIKEKC[KWIWAKAO?A@I?G@YHc@JODC@C?QDIBQBC@a@Dk@Fg@BI?a@?_@Ec@K]EEAICk@WUKSMCAKGMMAAAAOOU[g@s@[a@?AY_@GIGEEGGECC]KCAMIKIOIGGIKKKEEIMOYOYMYOQKMA?KMIGGC?AGAGCEAG?IAS@E@MBQHEB[RC@GDMDGBC@WHIBI@QDG@QBQ?S?m@Eu@IkAKs@IYCK?KA]AW@e@?C?c@@c@@M@]?y@?a@?G?OAO?ICICIGEECAKMKQIQIU?AGQSq@EQAAEIGMGMAAIMIISOKIg@UMGSKICA?EAIAGAKAE?_@?C?K@M@_@H[J[JM@O?KCKAAAIEGKEICI_@gAACACCEEKEKCCCCAA?AOKGGEAKIQEEAEAIAOEYEKAKAa@CAAiAKi@GOCOEKGIEAAIIMKIIIMIOAAAEKU?AEMEMI]GSGWAA?AIYEQAE?AEYGi@CU?AKiAMkACa@?G?M?IA]?A?A@??A?A?A@??A?A?A?A?A?A?A?AAA?A?AAA?AA??AAAAAGc@CKEGEI"
                },
                "start_location": {
                  "lat": 46.181542,
                  "lng": 10.3335036
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "9,6 km",
                  "value": 9614.0
                },
                "duration": {
                  "text": "11 min",
                  "value": 630.0
                },
                "end_location": {
                  "lat": 46.2494354,
                  "lng": 10.4640036
                },
                "html_instructions": "<b>SS42</b> fa una leggera curva a <b>destra</b> e diventa <b>SS42</b>",
                "polyline": {
                  "points": "kcbyGqwe~@EUMa@KWMYOSKQ[_@OO[]CCSUEEAAWa@Ya@GGSWEEWWKMIGGEOKOIOKOKQKOIg@[e@YMQKOWc@A?U_@CCYc@EIQ[Yc@MQe@w@GKOWWc@c@q@kAoBc@q@_@k@[g@EGq@iAa@o@CCAES[CC{@uAKUIOMYAEA?EKKWGMM[AEACQe@Ys@c@gA?AUk@Sk@e@kAKY]{@Ui@Ys@GMCIGMKOIIGGOOUSMIk@_@MIKGGGCCAAIKIIEIEGIQEKEIO_@AEK[GOCMGUCSESCQCYC_@E}@AOC_@CUAKCQIc@GUOi@Y}@[}@]cAKYe@yAKYCGGQCEEICCMOCCEEKIQICAOGs@WUKKEWKi@Ue@SWMIGUOAACCIIQOIKMQEEEIKUCGAAM_@AAAGGKGUCIEMGUAIAAKa@AEEMEGEIGIIKIMm@m@UYU]U[Ua@QWKSCECGACGOEUAAESCSOw@Kq@Ie@CIIg@Mm@?AMk@Oo@CMMg@Kc@ACOm@CKIYEWAKAMAQAm@Ac@C[C]AMEk@AWAc@?ICWC_@Ca@E]E]CMCQCGAIEQESGUM[GUKWOc@EKM[Wm@M[EKUm@EMUg@MWGIACCG[i@MUAES]KSYi@_@q@MU]m@M[YaAGQKYMWIOEIUa@Ya@CGIOEIEGGSOe@M]Mc@ACM_@Qk@Ss@Og@IYKc@GWCKGOEOSg@O_@CEKYSm@Kc@Y}@?CQk@W_AK[IWKa@EQI_@G_@Ie@AOGa@CSEYE[GUW}@m@mBAGCKCQE[ESMqAEUGe@CK?EAGAGAEGYIYEQW_ACIAICKAMAGC]AMI{ACu@Cw@Em@Eq@IaCAQA_@?CCo@I}AEgAAECk@A[A_@AUAOCUCWEKEQCIGOEKGO?AKUEMEKGQEMAEGWI_@Qu@EUAICKCMAQAAASC[Aa@AQ?SAs@?AA]AS?SAW?GAG?ICUAOASCQG]W}AG]CSCSAOC[?O?E?W?Q@m@Du@Bc@@E@OBQDYFYFYFSLe@@CTs@X}@FSPk@@AFUDIJ_@Ja@BINu@Da@BS?I@O?CAG?S?K?K?EAIAKCSOsAMcA?CIu@Eo@A_@?OAc@?e@?_@AYEm@Ei@CYCIIm@CSAKCIAIAGKc@I[Mk@o@eCAG]uAYgAQo@EMISEGAGKSYk@]y@EGMYGSGOCOEMAMEUAWAUAS?EAy@Cg@Ce@Ck@Ck@?AA[AO?E@OB]@OBKBU|AcJDUBc@DgB?[Ae@E_AIs@O{@Io@CMSs@ISMQGGEEIGMKGEOKc@[KIECMKQQEGKMAACEEICGM[GWEQE]AICSCYAUAKAOAQEg@?GG}@AGA}@EoA?}@@Q?e@A[?ECgBAK?M?C?Q@Y?G?Q?S?M?OAi@AeACc@?QCm@?]?AAa@@a@?K@OBU@GHe@HYFUFUFWBMHWDUHa@BWBQ@QB{@?{@?o@?m@AU?GGo@C[ASG]AOACAMEOAGACAGGKGKMOKMIGGICCACGGGKCICEW_Aa@oBESIg@Ki@O{@Kg@WkBSgAK{@CM?MAO?OAW?_@?w@?k@AWAQAMEc@CQAKCYEa@M}@Aa@?OB_@De@@M@GHc@BU@UA[EO?AIg@GWCOKw@E[?GGwA?]?YDk@De@TmARaAFa@BY@O@e@?s@Im@AEIk@EQSmAIe@CKIo@WcBCS[qBCKMo@CIKc@g@gB{@_Cc@uA_@mACKS}@M}@Ce@C[AKCQCQCICKCGKQi@}@Ui@MW[y@]_A_@y@IQMSIK_@c@ECKKSO_@WWUc@a@ACQ]M]Oi@Mk@Om@Qw@GWG[UiAKm@AQCUC_@CYC[_@aE]{EWqDGuACuCAUCkAD{AD}A?KR{Af@["
                },
                "start_location": {
                  "lat": 46.21894409999999,
                  "lng": 10.3565657
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "8,0 km",
                  "value": 8049.0
                },
                "duration": {
                  "text": "9 min",
                  "value": 558.0
                },
                "end_location": {
                  "lat": 46.2641207,
                  "lng": 10.5218863
                },
                "html_instructions": "Alla rotonda, prendi la <b>2ª</b> uscita e rimani su <b>SS42</b>",
                "maneuver": "roundabout-right",
                "polyline": {
                  "points": "_bhyG_wz~@B@@@@?@@@?@@@?@?B?@?@A@?@?@A@A@A@?@A@A?A@C@A?A@C?A@A?A?C@C?A?C?C?AAC?C?AAC?AAC?AAAACAAAA?AA?GmAPa@BGBKFQDO@E@CNa@DOF[@A@M@IBOD[DSHq@DUDoA?G@s@DsB?SBgB@sA@i@@kA?G@a@Aw@A]C}@Ei@Io@Oy@Os@Oq@Km@QaAMc@Oi@ACEMGMEKUg@Q_@Yo@?AUe@Si@IWGUCI]qAMo@Om@I_@S{@CKKc@I_@CMKa@CMc@mB_@qBo@}CWyAESG_@Kq@SaBMuAIw@OaAKo@Mm@Mi@AEUaAGUE[CQCUKiAAaAAk@?Y?S?s@A]CYEYEOK[I[AGI]EW?E?S?QBQFUFQN]DKHSH]BU?M?e@?AAUEs@Gq@C]SaBIi@S_BCKAIW}AQu@K]M_@GMISCKGUKi@CW?WAi@CsA?O?MAqA?_ABm@Di@Fc@FU?APk@FOH[Pe@`@sALg@J_@JYLg@BGBIBIDSNy@Hm@@EFm@F_AAe@Aq@A_@KsAMeAKq@y@_FQgAIc@E_@O}@EUMo@_@sBQkAGa@COSoACO?CY}AE[Oq@Y{ACOSyAG[Iq@[}BYwBY_CUcBg@yDAMIi@Iy@C_@Gs@?AAYC{@Cq@?WA_AAYAgAAg@Aq@C_AAgAAQAk@?CAq@CgB?CAm@?E?o@?E?m@?C@k@Bi@@a@HoAJkB?I@KBa@?KBg@@MBm@@WBW@YBY@WBW?KR}D@YBWPuC@[?CPgDHuAH{@Hq@@KJm@DONg@Ne@JURi@n@oAN[Vg@Ve@BGl@kALUDKP[DKR_@BEj@gAVg@FMR]n@mAJQJUVc@n@oAVc@FOFKFK?AP[HUFM@GJYH_@Ni@Ns@Nw@@I@?He@BIH_@P_AVsA`@iB@CLe@La@HSNWDEVa@@At@aA@AJODIBM?A@S?U?SAQESGUIQIOQU_@MGAKAOAI@I@G@GBGBCDA?GHEJCFEFABITGNCFQ`@a@`ASh@Uh@ADELGLUh@Q`@CFMTWd@ABW`@w@jAU`@Wb@?@ILOTIJA@OPeBnBwBbCGFy@z@MFk@ZIBm@Vq@NUDSBOBSBc@FMBUBMBI@IBOBa@He@JGBUJMHED[\\?@A?W\\A@S\\CFUh@Sh@MTc@`AKLIJ[`@Q`@Ut@Q`@UVe@\\_@\\UZUh@Q\\EBEDC@C@GAEACCACCEAE?EAG@E?C@GDGNIPIHEDEDIFOJ[BKP]@Ad@u@`@_Av@aCh@yAZu@JW@E@EBM?G?EAEAA?CAAA??AC?C@A?C?CBCDCBCFABAHCXCLERKVCFOR{A`Cq@dAUR]\\GFUP_@XQNKLOPKL?@Y^A@Y^?@EDOJGFA?UPGD?@_@V]VA@A?]RA?]RA@QHMBA?a@HA?a@@A?E?[CAAUCKCA?UIICA?A?]OAA_@Q_@Qa@QCA]OCAUKECA?AA_@OCA_A]QGq@[CAUIAAEAWMIECAIESGe@QICWOIGAAAEACAA?G?C?A"
                },
                "start_location": {
                  "lat": 46.2494354,
                  "lng": 10.4640036
                },
                "travel_mode": "DRIVING"
              },
              {
                "distance": {
                  "text": "4,9 km",
                  "value": 4902.0
                },
                "duration": {
                  "text": "7 min",
                  "value": 407.0
                },
                "end_location": {
                  "lat": 46.2571169,
                  "lng": 10.5621413
                },
                "html_instructions": "Svolta a <b>destra</b> per rimanere su <b>SS42</b>",
                "maneuver": "turn-right",
                "polyline": {
                  "points": "w}jyGy`f_ABC?ABA@?@?@@B@FDLLTRHDFD@?B@RFHDF@VHf@FVFb@HF@@@F@l@NPDZJB@@?VFF@B?VFB@^Dj@Hj@PPDFB@?NDF@FAB?JCPGDAd@ORGDC@Ab@OTM@?BCNKHKDELUDGBERa@~@iBx@uA@EnA_CPYLYBK@C?A@?@UBO?E@C?C@S@U?O?S@OBa@BQHq@BMDc@@CFm@@C?A@EBe@@G?WASGgAA_@Eq@?C?AEi@?C?C?A?G@O@E@I@C?C@??ABG@CBEDED?B?H?VB@?@?^D@?@?bAJ@?@?R@J?@?`@C@?F?RADA@?LARG@?JCNEDAFEFEDG@E@E?A@ABI@I@G@K@C@CBGBCLIVSJIHE^SNED?@?D@F@D@F?B@@?F@F@N?BADEDEBGBIBEHWDK@IDOPeA@G?ADQDM@C?ABE?A@AFGHEBAD?NCl@CLCJGHK?A?ADE?A@EFMDKDOH_@BGJe@@GJg@N}@\\{B@IJq@LcAN_BLaB@MBY@A?GFi@@O@Q?SAUIm@E]ACCWCO?C?A?E?A@G@GBODG@EVY@AZ[BCX[BA?AVUDEJKJMLQHUHWDS?C?EBW@O@C@QD]@A?EBGDMHUTo@@IBI@K?M?O?K?Q?A?A@Q?KBKDSFQR]@EBE?I?K?KCICGAEMYEKACAGCOAK?I?C?E?OBM@I@G@CBIDGV[DIBI@A?C@EBS?M?C?E?QC[_@cBw@gDYwAKs@Gy@IiAKwAKuBGo@Ge@MOECGAI?G@E@EFCDCBADAH?HAH?J@N@\\@n@?B@r@?~@?VAN?BAHCb@APIdAEl@?H?P@NDV@FBFBFBLBH?BBJBL?B?BAFAD?BA@ABA@A@C@G@C@CACAAACACEAE?AOcAACG_@CKMo@?CKa@Km@[yACMAU@U@K@OFc@Jw@?Y?QCKCEEMEKMQIKMIGKIQGQGQCc@Go@AYB_@@WCUEUGSCIMSIMIMIMEMCSAM?ODWDK@ADKHOFO@I@O?I?G?AMm@AEGk@Iq@Es@ACAm@Cu@CeBAQCa@Em@Go@Ca@Ca@@[F_@H_@Ny@LkA@KDe@?M@i@EoBMyDAu@Gc@AGEKMUY]EGi@g@g@[]OOIKEOOMWIUGUGWMo@I[EQKq@ESE[e@iDE[Kq@M_ACc@OgBEq@Eq@AQAgCB}A?K@e@?cAAk@AEA[Ge@I_@EQa@kBSu@"
                },
                "start_location": {
                  "lat": 46.2641207,
                  "lng": 10.5218863
                },
                "travel_mode": "DRIVING"
              }
            ],
            "traffic_speed_entry": [],
            "via_waypoint": []
          }
        ],
        "overview_polyline": {
          "points": "uxqtGm|_w@cYjM@}GgHaQmQeGgCeQ{IvJq_@yJ_ZiJeDkAuJxH{NbP_MnMyq@|OyPhE{MdRoWf`@mXx_@wJnBmLwG{U_A{Tan@w`@_{ByMsnA{ActAcXslDgSalCsy@gbKwh@kzGop@ouG__BwlNw{B{sJgz@sbDo}AwhD}|Cw{GuNu]uI_`@xCdBiUu@{OoAmC{DpBgm@oBu^eWqw@V}~@lNcd@rq@w}BrF}bAeK}N_TgLw]}RiNkWoJ{r@vNo{AuDeg@{BqTfFkSjHoa@nAcX{Eg[wHis@sIwq@sG}j@`EkdAo@oEiGgB}Tyi@gOa\\sRgIw`@qg@qOqMqFtAwMwAqG}n@{Tmq@_u@yxAwG{KiGga@cWaY_Q{JeKoPyKiGsMHcQxDaM{EgL{DiSaEoMrDyc@mJ__@yZiOsXcW_`@aPaTuR}^oNcLyBaKwRq@uVhEoZu]aAcOgLmUcLiN}GgT{Cyh@dFgOx@c_@EihA~EwjAnG{c@{FiZiEcHaABaPiOuBmIkCsMgMsN_My\\mDeN}QmN}MiRiJck@hE}r@nFgh@{Ree@wu@}gA_]ao@cLwe@aCok@gBkLwNyP}_@}I_ScUcRea@bB}g@tM}f@p@oYeSwbA{FsNy_@{]sz@_eAib@}i@_s@qb@iNqPI}dAsAuWgTkb@el@ix@iKyWw_@wRq_@}d@w]wo@ac@{x@_i@oj@qmAskAwV_UiOme@md@orBmMqo@aM_X{~@ySgPwIgF}KyUao@aT_aAyUew@yd@g_@oYcPiSiQeNoYmRkV_^iK}[gPkY{j@yUiGm`@jFgUsEuPwHuNh@g\\kA{ZqIyY|@y[jQqv@ho@_eApIewAtG}iAx^oRpP{MjLbAxYwF|XsNvQgYhHiWz@aI~D{IoCcMlFiVaGaHfLmGyAca@wa@}NsCivAi{AcM{LosA}v@}f@oFoLuGi@kFmFPqSqJqODw@BuJnDcMlNU^kNfWaZ|g@k]zKcAv@qENuIqGqIqMi[mWaXwPeJTyHkE`ElBsKcD_^yMwPw@iKeLiSh@_MoGaYcJ}BsP{]yf@{Xqm@gVqa@oPer@qXou@gKay@eCaVbEsXeL}v@pBqVyC}LcEmVp@_X}AoSaFwc@G}`@qO_l@sGiKyDsd@rB}Kt@wHeAaj@cMag@wIgbAtEad@iO}cAi@wf@~E}k@`[ow@eDyBsCtGeW`YgOjMiFpGpAuCtDuKmNpP{MWgKkFfK~CtLsArJic@bPkEbFiIdCg\\fGsXyCq`@k@vQ}Fm[eAeYFyVmGeKcEmg@"
        },
        "summary": "SS42",
        "warnings": [],
        "waypoint_order": []
      }
    ]
  },
  "status": "SUCCEEDED"
}

```

## GET

### action-type = placeDetails

This api provides the details of places

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| placeId    | String | x         |

###### Example

```json
{
"actionType": "details",
"placeId": "78b58afc-8421-42e0-802e-7b77ae68cd3f"
}
```

##### Output

```json
{
  "result": {
    "address_components": [
      {
        "long_name": "Milano",
        "short_name": "Milano",
        "types": [
          "locality",
          "political"
        ]
      },
      {
        "long_name": "Milano",
        "short_name": "Milano",
        "types": [
          "administrative_area_level_3",
          "political"
        ]
      },
      {
        "long_name": "Città Metropolitana di Milano",
        "short_name": "MI",
        "types": [
          "administrative_area_level_2",
          "political"
        ]
      },
      {
        "long_name": "Lombardia",
        "short_name": "Lombardia",
        "types": [
          "administrative_area_level_1",
          "political"
        ]
      },
      {
        "long_name": "Italia",
        "short_name": "IT",
        "types": [
          "country",
          "political"
        ]
      }
    ],
    "formatted_address": "Milano MI, Italia",
    "geometry": {
      "location": {
        "lat": 45.4642035,
        "lng": 9.189982
      },
      "viewport": {
        "northeast": {
          "lat": 45.53568898729802,
          "lng": 9.290346273733416
        },
        "southwest": {
          "lat": 45.38977870977718,
          "lng": 8.228119999999999
        }
      }
    },
    "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
    "name": "Milano",
    "place_id": "ChIJ53USP0nBhkcRjQ50xhPN_zw",
    "rating": 0.0,
    "types": [
      "locality",
      "political"
    ],
    "user_ratings_total": 0.0,
    "vicinity": "Milano"
  },
  "status": "SUCCEEDED"
}

```

## GET

### action-type = nearbySearch

This api provides the nearby details of places

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| latitude   | Double | x         |
| longitude  | Double | x         |
| radius     | Int    | x         |
| keyword    | String | x         |

###### Example

```json
{
    "actionType": "nearbySearch",
    "latitude": 48.8,
    "longitude" : 2.3,
    "radius" : 1000,
    "keyword": "parking"
}
```

##### Output

```json
{
  "date": "2023-11-23T12:59:31Z",
  "type": "GET",
  "name": "space.middleware.location",
  "parameters": {
    "actionType": "nearbySearch",
    "latitude": 48.8,
    "longitude": 2.3,
    "radius": 1000,
    "keyword": "parking"
  },
  "result": {
    "places": [
      {
        "geometry": {
          "location": {
            "lat": 48.8049097,
            "lng": 2.2883641
          },
          "viewport": {
            "northeast": {
              "lat": 48.80614282989271,
              "lng": 2.289452429892722
            },
            "southwest": {
              "lat": 48.80344317010727,
              "lng": 2.286752770107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Parking NOMAD",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJkZOfmJFw5kcRzNm3_6Uf75A",
        "rating": 3.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 1.0,
        "vicinity": "12-18 Bd de Vanves, Châtillon"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7948394,
            "lng": 2.3085569
          },
          "viewport": {
            "northeast": {
              "lat": 48.79618922989272,
              "lng": 2.309906729892722
            },
            "southwest": {
              "lat": 48.79348957010728,
              "lng": 2.307207070107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJGWKDdylx5kcRkiDsNsMh1Ps",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "6 Rue François Laurent Gibon, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7998019,
            "lng": 2.3114582
          },
          "viewport": {
            "northeast": {
              "lat": 48.80117852989272,
              "lng": 2.312817429892723
            },
            "southwest": {
              "lat": 48.79847887010727,
              "lng": 2.310117770107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJzSDvpARx5kcR-Xh0i9v2oBU",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "1 All. Georges Brassens, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.800109,
            "lng": 2.29149
          },
          "viewport": {
            "northeast": {
              "lat": 48.80143272989272,
              "lng": 2.292866729892723
            },
            "southwest": {
              "lat": 48.79873307010728,
              "lng": 2.290167070107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJLX_HRxVx5kcR9bzak2PGyds",
        "rating": 4.5,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 4.0,
        "vicinity": "Mairie de, 3 Av. de la République, Châtillon"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.8000473,
            "lng": 2.3062666
          },
          "viewport": {
            "northeast": {
              "lat": 48.80139662989272,
              "lng": 2.307617229892722
            },
            "southwest": {
              "lat": 48.79869697010728,
              "lng": 2.304917570107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJ_15_LxRx5kcRy4fenT2bu20",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "All. Pablo Picasso, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.8043281,
            "lng": 2.3135367
          },
          "viewport": {
            "northeast": {
              "lat": 48.80567792989272,
              "lng": 2.314886529892722
            },
            "southwest": {
              "lat": 48.80297827010727,
              "lng": 2.312186870107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJb5_ISAJx5kcR0e2xrkljrDE",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "6 Rue de Kirovakan, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.807866,
            "lng": 2.2949661
          },
          "viewport": {
            "northeast": {
              "lat": 48.80919822989272,
              "lng": 2.296334429892722
            },
            "southwest": {
              "lat": 48.80649857010727,
              "lng": 2.293634770107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark, location de parking au mois - Arrêt Vauban - Châtillon",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJJUxGWfdx5kcRuGKo4DmujHk",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "83 Av. de Paris, Châtillon"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7949872,
            "lng": 2.2984681
          },
          "viewport": {
            "northeast": {
              "lat": 48.79633697989272,
              "lng": 2.299818079892722
            },
            "southwest": {
              "lat": 48.79363732010728,
              "lng": 2.297118420107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJFcKx9ldx5kcRPpVIp5O6rY4",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "2 Rue du Moulin Blanchard, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7949548,
            "lng": 2.3050123
          },
          "viewport": {
            "northeast": {
              "lat": 48.79627577989272,
              "lng": 2.306416179892722
            },
            "southwest": {
              "lat": 48.79357612010728,
              "lng": 2.303716520107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJ-xl3tgpx5kcRZCfBlFqZ45k",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "10 Rés Etienne Hajdu, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.793893,
            "lng": 2.3086996
          },
          "viewport": {
            "northeast": {
              "lat": 48.79524257989272,
              "lng": 2.310055779892722
            },
            "southwest": {
              "lat": 48.79254292010727,
              "lng": 2.307356120107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJM-Ine9Nx5kcRlRE92ichSPY",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "12 Sent. des Monceaux, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7994071,
            "lng": 2.3043107
          },
          "viewport": {
            "northeast": {
              "lat": 48.80049912989272,
              "lng": 2.305607829892722
            },
            "southwest": {
              "lat": 48.79779947010728,
              "lng": 2.302908170107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Parking de l'Hôtel de Ville",
        "place_id": "ChIJr9aRWuhw5kcR6nQEbka-AbA",
        "rating": 4.7,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 3.0,
        "vicinity": "57 Av. Henri Ravera, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.8039499,
            "lng": 2.3126433
          },
          "viewport": {
            "northeast": {
              "lat": 48.80528087989272,
              "lng": 2.313982279892722
            },
            "southwest": {
              "lat": 48.80258122010728,
              "lng": 2.311282620107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark, location de parking au mois - Avenue Henri Ravera (aérien) - Bagneux",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJTZLAEmJx5kcRkQgWR8DtkDU",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "6-8 All. Anatole France, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7949612,
            "lng": 2.3016908
          },
          "viewport": {
            "northeast": {
              "lat": 48.79632892989271,
              "lng": 2.302942479892722
            },
            "southwest": {
              "lat": 48.79362927010727,
              "lng": 2.300242820107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJ_fLDSORx5kcRAZDsILcl_3g",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "7 Rue Salvador Allende, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.790449,
            "lng": 2.2902841
          },
          "viewport": {
            "northeast": {
              "lat": 48.79178732989272,
              "lng": 2.291629729892722
            },
            "southwest": {
              "lat": 48.78908767010728,
              "lng": 2.288930070107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Yespark",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJZxjIZ6Fx5kcRzYFwvwEcCNQ",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "Centre ville, 26 Rue Boucicaut, Fontenay-aux-Roses"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7911522,
            "lng": 2.2891237
          },
          "viewport": {
            "northeast": {
              "lat": 48.79247637989272,
              "lng": 2.290571779892722
            },
            "southwest": {
              "lat": 48.78977672010728,
              "lng": 2.287872120107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Zenpark - Parking Fontenay aux Roses - Parc Sainte Barbe - Boucicaut",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJT4Zlezxx5kcR8fUkLA-VdmY",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "1 Av. Jeanne et Maurice Dolivet, Fontenay-aux-Roses"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.79448300000001,
            "lng": 2.3013781
          },
          "viewport": {
            "northeast": {
              "lat": 48.79571747989272,
              "lng": 2.302852729892722
            },
            "southwest": {
              "lat": 48.79301782010727,
              "lng": 2.300153070107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "[P] Parking Jean Guimier. Bagneux",
        "place_id": "ChIJBY8HNsJw5kcRFjafhdzijoQ",
        "rating": 3.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 4.0,
        "vicinity": "2 Rue Léon Blum, Bagneux"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.79443920000001,
            "lng": 2.2908851
          },
          "viewport": {
            "northeast": {
              "lat": 48.79573892989272,
              "lng": 2.292430629892722
            },
            "southwest": {
              "lat": 48.79303927010728,
              "lng": 2.289730970107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Zenpark - Parking Fontenay aux Roses - Parc Sainte Barbe - Dolivet",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJY3FPzZ9x5kcRM2ht4VSpU3I",
        "rating": 0.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 0.0,
        "vicinity": "33 Av. Jeanne et Maurice Dolivet, Fontenay-aux-Roses"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.80047099999999,
            "lng": 2.2897126
          },
          "viewport": {
            "northeast": {
              "lat": 48.80180957989273,
              "lng": 2.291075629892723
            },
            "southwest": {
              "lat": 48.79910992010728,
              "lng": 2.288375970107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Parking Mensuel Yespark Mairie de Châtillon",
        "place_id": "ChIJgSqlN5Rw5kcRWMXGzDKmSEo",
        "rating": 2.5,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 8.0,
        "vicinity": "Pl. de l'Église, Châtillon"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7925586,
            "lng": 2.2875162
          },
          "viewport": {
            "northeast": {
              "lat": 48.79391877989272,
              "lng": 2.288904129892722
            },
            "southwest": {
              "lat": 48.79121912010728,
              "lng": 2.286204470107277
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Parking General de Gaulle",
        "place_id": "ChIJM-8gjbtw5kcRJr_ft5jCfvk",
        "rating": 3.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 4.0,
        "vicinity": "Pl. du Général de Gaulle, Fontenay-aux-Roses"
      },
      {
        "geometry": {
          "location": {
            "lat": 48.7904898,
            "lng": 2.290297
          },
          "viewport": {
            "northeast": {
              "lat": 48.79179832989271,
              "lng": 2.291628029892722
            },
            "southwest": {
              "lat": 48.78909867010727,
              "lng": 2.288928370107278
            }
          }
        },
        "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/parking-71.png",
        "name": "Parking mensuel Yespark Centre-ville - Parc Sainte Barbes - Fontenay-aux-Roses",
        "opening_hours": {
          "open_now": true
        },
        "place_id": "ChIJVVUlPrlw5kcREeAtRmi2T5I",
        "rating": 5.0,
        "types": [
          "parking",
          "point_of_interest",
          "establishment"
        ],
        "user_ratings_total": 1.0,
        "vicinity": "26 Rue Boucicaut, Fontenay-aux-Roses"
      }
    ]
  },
  "status": "SUCCEEDED"
}
```
