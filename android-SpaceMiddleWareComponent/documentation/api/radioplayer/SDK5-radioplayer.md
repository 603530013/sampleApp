# space.middleware.radioPlayer

## For PSA

## GET

### action-type = stations

### action = onAir

This api provides radio player stations according to country with onAir information

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| action     | String | x         |
| id         | String | x         |
| country    | String | x         |

###### Example

```json
{
    "actionType": "stations",
    "action": "onAir",
    "id": "2503",
    "country": "FR"
}
```

##### Output

```json
{
    "result": {
        "stations": [
            {
                "artist": "The Rolling Stones",
                "songName": "Gimme Shelter"
            }
        ]
    },
    "status": "SUCCEEDED"
}
```

### action-type = stations

This api provides radio player stations based on country

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| id         | String | x         |
| country    | String | x         |

###### Example

```json
{
    "actionType": "stations",
    "country": "FR"
}
```

##### Output

```json
{
    "result": {
        "name": "Europe 2 - Avranches",
        "description": "Europe 2 - Le Meilleur son",
        "live_streams": [
            {
                "stream_source": {
                    "url": "https://europe2.lmn.fm/europe2-50300/playlist.m3u8?rp_source=1",
                    "mime_value": "audio/x-mpegurl"
                },
                "bit_rate": {
                    "target": 64000
                }
            }
        ],
        "multimedia": [
            {
                "url": "https://assets.radioplayer.org/250/2502774/1600/1200/lc6e6vl6.png",
                "width": 1600,
                "height": 1200
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/220/48/lc6e6vm5.png",
                "width": 220,
                "height": 48
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/86/48/lc6e6vm7.png",
                "width": 86,
                "height": 48
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/1400/1400/lc6e6vma.png",
                "width": 1400,
                "height": 1400
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/1920/1080/lc6e6vmk.png",
                "width": 1920,
                "height": 1080
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/1024/768/lc6e6vmn.png",
                "width": 1024,
                "height": 768
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/112/32/lc6e6vmp.png",
                "width": 112,
                "height": 32
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/320/240/lc6e6vmr.png",
                "width": 320,
                "height": 240
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/600/600/lc6e6vn5.png",
                "width": 600,
                "height": 600
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/128/128/lc6e6vn7.png",
                "width": 128,
                "height": 128
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/32/32/lc6e6vn9.png",
                "width": 32,
                "height": 32
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/288/162/lc6e6vnb.png",
                "width": 288,
                "height": 162
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/160/90/lc6e6vnd.png",
                "width": 160,
                "height": 90
            },
            {
                "url": "https://assets.radioplayer.org/250/2502774/74/41/lc6e6vne.png",
                "width": 74,
                "height": 41
            }
        ],
        "country": "FR",
        "rpuid": "2502774",
        "relevance_index": 0
    },
    "status": "SUCCEEDED"
}
```

### action-type = recommendations

This api provides the radioPlayer recommendations

##### Input

| Name       | Type   | Mandatory |
|------------|--------|-----------|
| actionType | String | x         |
| factors    | String | x         |
| country    | String | x         |
| id         | String | x         |
| latitude   | Double | x         |
| longitude  | Double | x         |

###### Example

```json
{
    "actionType": "stations",
    "factors": " ",
    "country": "FR",
    "id": "2503",
    "latitude": 48.8566,
    "longitude": 2.3522
}

```

##### Output

```json
{
    "result": {
        "stations": [
            {
                "country": "FR",
                "description": "Fun Radio est LA radio du son électrolatino enFrance",
                "id": 25023,
                "liveStreams": [
                    {
                        "bitRate": 64000,
                        "mime": "audio/x-mpegurl",
                        "url": "https://live.m6radio.quortex.io/webM89Hc99XApzgfhXNX8ASN5/groupfun/national/long/index.m3u8?aw_0_1st.rpfr=%7EREQUEST_PREROLL%7E&rp_source=1"
                    }
                ],
                "multimedia": [
                    {
                        "height": 32,
                        "url": "https://assets.radioplayer.org/250/25023/32/32/kyes3c2l.png",
                        "width": 32
                    },
                    {
                        "height": 41,
                        "url": "https://assets.radioplayer.org/250/25023/74/41/l4o4jbof.png",
                        "width": 74
                    },
                    {
                        "height": 48,
                        "url": "https://assets.radioplayer.org/250/25023/86/48/l4o4jbnz.png",
                        "width": 86
                    },
                    {
                        "height": 32,
                        "url": "https://assets.radioplayer.org/250/25023/112/32/kyes3c2d.png",
                        "width": 112
                    },
                    {
                        "height": 128,
                        "url": "https://assets.radioplayer.org/250/25023/128/128/kyes3c2j.png",
                        "width": 128
                    },
                    {
                        "height": 90,
                        "url": "https://assets.radioplayer.org/250/25023/160/90/l4o4jbnh.png",
                        "width": 160
                    },
                    {
                        "height": 48,
                        "url": "https://assets.radioplayer.org/250/25023/220/48/l4o4jbnx.png",
                        "width": 220
                    },
                    {
                        "height": 162,
                        "url": "https://assets.radioplayer.org/250/25023/288/162/l4o4jbns.png",
                        "width": 288
                    },
                    {
                        "height": 240,
                        "url": "https://assets.radioplayer.org/250/25023/320/240/kyes3c1y.png",
                        "width": 320
                    },
                    {
                        "height": 600,
                        "url": "https://assets.radioplayer.org/250/25023/600/600/kyes3c2h.png",
                        "width": 600
                    },
                    {
                        "height": 768,
                        "url": "https://assets.radioplayer.org/250/25023/1024/768/kyes3c1x.png",
                        "width": 1024
                    },
                    {
                        "height": 1400,
                        "url": "https://assets.radioplayer.org/250/25023/1400/1400/kyes3c2f.png",
                        "width": 1400
                    },
                    {
                        "height": 1200,
                        "url": "https://assets.radioplayer.org/250/25023/1600/1200/l4o4jbm5.png",
                        "width": 1600
                    },
                    {
                        "height": 1080,
                        "url": "https://assets.radioplayer.org/250/25023/1920/1080/l4o4jbnv.png",
                        "width": 1920
                    }
                ],
                "name": "Fun radio",
                "relevanceIndex": 122
            }
        ]
    },
    "status": "SUCCEEDED"
}
```

## For FCA

###NA
