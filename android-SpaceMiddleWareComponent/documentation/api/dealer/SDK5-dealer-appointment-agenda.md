# space.middleware.dealer

## GET

### action-type = appointment

### action = agenda

This api provides the list of appointment book slot days

##### Input

| Name            | Type                      | Mandatory |
|-----------------|---------------------------|-----------|
| actionType      | String                    | x         |
| action          | String                    | x         |
| bookingId       | String                    | x         |
| bookingLocation | String                    |           |
| startDate       | String                    | x         |
| timeFence       | Enum(WEEK,MONTH,SEMESTER) | x         |
| vin             | String                    | x         |

###### Example

```json
{
  "actionType": "appointment",
  "action": "agenda",
  "bookingId": "0000040179",
  "bookingLocation": "001",
  "startDate": "20230723",
  "timeFence": "month",
  "vin": "TESTOPSP0LPK11565"
}
```

##### Output xPSA

```json
{
    "rrdi": "708224J01F",
    "from": "2023-07-23",
    "to": "2023-08-23",
    "period": 6,
    "type": 3,
    "days": [
      {
        "date": "2023-06-29",
        "slots": [
          {
            "start": "10:30",
            "end": "10:45",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "10:45",
            "end": "11:00",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "11:00",
            "end": "11:15",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "11:15",
            "end": "11:30",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "11:45",
            "end": "12:00",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "13:30",
            "end": "13:45",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "13:45",
            "end": "14:00",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "14:00",
            "end": "14:15",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "14:15",
            "end": "14:30",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "14:30",
            "end": "14:45",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "14:45",
            "end": "15:00",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "15:00",
            "end": "15:15",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          },
          {
            "start": "15:15",
            "end": "15:30",
            "discount": 0,
            "reception_available": 1,
            "reception_total": 1
          }
        ]
      }
    ]
}
```

##### Output xFCA (EMEA)

```json
{
  "days": [
    {
      "date": "2023-06-29",
      "slots": [
        {
          "start": "10:30"
        },
        {
          "start": "10:45"
        },
        {
          "start": "11:00"
        },
        {
          "start": "11:15"
        },
        {
          "start": "11:45"
        },
        {
          "start": "13:30"
        },
        {
          "start": "13:45"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:15"
        },
        {
          "start": "14:30"
        },
        {
          "start": "14:45"
        },
        {
          "start": "15:00"
        },
        {
          "start": "15:15"
        }
      ]
    }
  ]
}
```

##### Output xFCA (LATAM)

```json
{
  "days": [
    {
      "date": "2023-11-01",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-02",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-03",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-04",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        }
      ]
    },
    {
      "date": "2023-11-06",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-07",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-08",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-09",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-10",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-11",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        }
      ]
    },
    {
      "date": "2023-11-13",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-14",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-15",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-16",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-17",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-18",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        }
      ]
    },
    {
      "date": "2023-11-20",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-21",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-22",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-23",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-24",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-25",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        }
      ]
    },
    {
      "date": "2023-11-27",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-28",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-29",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    },
    {
      "date": "2023-11-30",
      "slots": [
        {
          "start": "11:00"
        },
        {
          "start": "11:30"
        },
        {
          "start": "12:00"
        },
        {
          "start": "12:30"
        },
        {
          "start": "13:00"
        },
        {
          "start": "13:30"
        },
        {
          "start": "14:00"
        },
        {
          "start": "14:30"
        },
        {
          "start": "16:00"
        },
        {
          "start": "16:30"
        },
        {
          "start": "17:00"
        },
        {
          "start": "17:30"
        },
        {
          "start": "18:00"
        },
        {
          "start": "18:30"
        },
        {
          "start": "19:00"
        },
        {
          "start": "19:30"
        },
        {
          "start": "20:00"
        },
        {
          "start": "20:30"
        }
      ]
    }
  ]
}
```

##### Output xFCA (NAFTA)

```json
{
  "result": {
      "days": [
          {
              "date": "2023-12-14",
              "slots": [
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  }
              ]
          },
          {
              "date": "2023-12-15",
              "slots": [
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  }
              ]
          },
          {
              "date": "2023-12-16",
              "slots": [
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  }
              ]
          },
          {
              "date": "2023-12-18",
              "slots": [
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  }
              ]
          },
          {
              "date": "2023-12-19",
              "slots": [
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  }
              ]
          },
          {
              "date": "2023-12-20",
              "slots": [
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "15:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "16:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "17:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "18:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "19:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "20:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:30",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "21:45",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:00",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  },
                  {
                      "serviceAdvisors": [
                          {
                              "id": 223217.0,
                              "memberId": 76897.0,
                              "name": "Darin Rivera"
                          }
                      ],
                      "start": "22:15",
                      "transportationOptions": [
                          {
                              "code": "own-ride",
                              "description": "Own ride"
                          }
                      ]
                  }
              ]
          }
      ]
  }
}
```
